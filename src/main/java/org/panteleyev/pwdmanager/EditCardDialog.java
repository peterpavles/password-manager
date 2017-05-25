/*
 * Copyright (c) 2016, 2017, Petr Panteleyev <petr@panteleyev.org>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.panteleyev.pwdmanager;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.panteleyev.utilities.fx.BaseDialog;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

class EditCardDialog extends BaseDialog<Card> implements Styles {
    private final ResourceBundle rb = PasswordManagerApplication.getBundle();

    private final TableView<Field>          cardContentView = new TableView<>();
    private final TableColumn<Field,String> fieldNameColumn = new TableColumn<>();
    private final TableColumn<Field,String> fieldValueColumn = new TableColumn<>();
    private final TextField                 fieldNameEdit = new TextField();
    private final ComboBox<FieldType>       fieldTypeCombo = new ComboBox<>();
    private final TextField                 cardNameEdit = new TextField();
    private final ComboBox<Picture>         pictureList = new ComboBox<>();
    private final TextArea                  noteEditor = new TextArea();

    private Card card;

    EditCardDialog(Card card) {
        super(MainWindowController.CSS_PATH);
        this.card = card;
        initialize();
    }

    private void initialize() {
        setTitle(rb.getString("editCardDialog.title"));

        MenuItem newFieldMenuItem = new MenuItem(rb.getString("editCardDialog.menu.addField"));
        newFieldMenuItem.setOnAction(a -> onNewField());
        MenuItem deleteFieldMenuItem = new MenuItem(rb.getString("editCardDialog.menu.deleteField"));
        deleteFieldMenuItem.setOnAction(a -> onDeleteField());

        ContextMenu contextMenu = new ContextMenu(newFieldMenuItem, new SeparatorMenuItem(), deleteFieldMenuItem);

        fieldNameColumn.setSortable(false);
        fieldNameColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        fieldValueColumn.setSortable(false);
        fieldValueColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        cardContentView.getColumns().setAll(fieldNameColumn, fieldValueColumn);
        cardContentView.setContextMenu(contextMenu);
        cardContentView.setEditable(true);

        GridPane grid1 = new GridPane();
        grid1.getStyleClass().add(GRID_PANE);
        grid1.addRow(0, new Label(rb.getString("label.FieldName")), fieldNameEdit);
        grid1.addRow(1, new Label(rb.getString("label.FieldType")), fieldTypeCombo);

        BorderPane pane = new BorderPane(cardContentView, null, null, grid1, null);
        BorderPane.setAlignment(grid1, Pos.CENTER);
        BorderPane.setMargin(grid1, new Insets(5, 0, 0, 0));

        Tab tab1 = new Tab(rb.getString("editCardDialog.tab.fields"), pane);
        tab1.setClosable(false);

        Tab tab2 = new Tab(rb.getString("editCardDialog.tab.notes"), noteEditor);
        tab2.setClosable(false);

        GridPane grid3 = new GridPane();
        grid3.getStyleClass().add(GRID_PANE);
        grid3.setPadding(new Insets(5, 5, 5, 5));
        grid3.addRow(0, new Label(rb.getString("label.Name")), cardNameEdit);
        grid3.addRow(1, new Label(rb.getString("label.Icon")), pictureList);
        cardNameEdit.setPrefColumnCount(30);

        Tab tab3 = new Tab(rb.getString("editCardDialog.tab.properties"), grid3);
        tab3.setClosable(false);

        TabPane tabPane = new TabPane(tab1, tab2, tab3);

        getDialogPane().setContent(tabPane);
        createDefaultButtons(rb);

        fieldValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        fieldNameColumn.setCellValueFactory(p -> p.getValue().nameProperty());
        fieldValueColumn.setCellValueFactory(p -> p.getValue().valueProperty());

        fieldNameColumn.prefWidthProperty().bind(cardContentView.widthProperty().divide(2).subtract(1));
        fieldValueColumn.prefWidthProperty().bind(cardContentView.widthProperty().divide(2).subtract(1));

        cardContentView.setItems(FXCollections.observableArrayList(
                card.getFields().stream().map(Field::new).collect(Collectors.toList())
        ));

        cardContentView.getSelectionModel()
                .selectedIndexProperty().addListener(x -> onFieldSelected());

        fieldNameEdit.setOnAction(x -> onFieldNameChanged());

        fieldTypeCombo.setItems(FXCollections.observableArrayList(FieldType.values()));
        fieldTypeCombo.setOnAction(x -> onFieldTypeComboChanged());

        noteEditor.setText(card.getNote());

        Picture.setupComboBox(pictureList);
        cardNameEdit.setText(card.getName());
        pictureList.getSelectionModel().select(card.getPicture());

        setResultConverter((ButtonType b) -> {
            if (b == ButtonType.OK) {
                return new Card(card.getId(), cardNameEdit.getText(),
                        pictureList.getSelectionModel().getSelectedItem(),
                        new ArrayList<>(cardContentView.getItems()), noteEditor.getText());
            } else {
                return null;
            }
        });
    }

    private Optional<Field> getSelectedField() {
        return Optional.ofNullable(cardContentView.getSelectionModel().getSelectedItem());
    }

    private void onFieldSelected() {
        getSelectedField().ifPresent(x -> {
            fieldNameEdit.setText(x.getName());
            fieldTypeCombo.getSelectionModel().select(x.getType());
        });
    }

    private void onNewField() {
        Field f = new Field(FieldType.STRING, "New field", "");
        cardContentView.getItems().add(f);
        cardContentView.getSelectionModel().select(f);
    }

    private void onDeleteField() {
        getSelectedField().ifPresent(sel -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sure?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().filter(x -> x == ButtonType.YES).ifPresent(x -> cardContentView.getItems().remove(sel));
        });
    }

    private void onFieldNameChanged() {
        getSelectedField().ifPresent((Field sel) -> {
            String name = fieldNameEdit.getText();
            if (!name.equals(sel.getName())) {
                sel.nameProperty().set(name);
            }
        });
    }

    private void onFieldTypeComboChanged() {
        getSelectedField().ifPresent((Field sel) -> {
            FieldType type = fieldTypeCombo.getValue();
            if (type != sel.getType()) {
                sel.typeProperty().set(type);
            }
        });
    }
}
