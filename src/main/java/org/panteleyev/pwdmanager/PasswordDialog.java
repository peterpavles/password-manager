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

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.panteleyev.utilities.fx.BaseDialog;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PasswordDialog extends BaseDialog<String> implements Initializable {
    private static final String FXML_PATH = "/org/panteleyev/pwdmanager/PasswordDialog.fxml";

    @FXML private Label         fileNameLabel;
    @FXML private PasswordField passwordEdit;

    private final File file;

    PasswordDialog(File file) {
        super(FXML_PATH, MainWindowController.UI_BUNDLE_PATH);
        this.file = file;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTitle(resources.getString("passwordDialog.title"));

        createDefaultButtons();

        fileNameLabel.setText(file.getAbsolutePath());

        setResultConverter(b -> b == ButtonType.OK ? passwordEdit.getText() : null);

        Platform.runLater(() -> passwordEdit.requestFocus());
    }
}
