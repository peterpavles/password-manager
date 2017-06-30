/*
 * Copyright (c) 2017, Petr Panteleyev <petr@panteleyev.org>
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

package org.panteleyev.pwdmanager

import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.image.Image

enum class Picture {
    GENERIC,
    PASSWORD,
    INFO,
    IMPORTANT,
    AUTO,
    AIRPLANE,
    TRAIN,
    AMEX,
    MASTERCARD,
    INTERNET,
    INSURANCE,
    GLASSES,
    BANK,
    RAIF,
    EMAIL,
    VISA,
    CREDIT_CARD,
    IPHONE,
    MOBILE,
    WIFI,
    CD,
    COMPUTER,
    FACEBOOK,
    VK,
    SKYPE,
    TWITTER,
    GPLUS,
    MOZILLA,
    PASSPORT,
    SHOP,
    NOTE,
    MEDICINE,
    HOUSE,
    FEMALE,
    MALE,
    EDUCATION,
    STEAM,
    GITHUB,
    BITBUCKET,
    TELEGRAM,
    APPLE,
    YAHOO,
    FOLDER;

    val image: Image
    val bigImage: Image

    init {
        val res = name.toLowerCase() + ".png"
        val bigRes = name.toLowerCase() + "-48.png"

        image = Image(javaClass.getResourceAsStream("/pictures/" + res))
        bigImage = Image(javaClass.getResourceAsStream("/pictures/" + bigRes))
    }

    companion object {
        fun setupComboBox(comboBox: ComboBox<Picture>) {
            comboBox.setCellFactory { PictureListCell() }
            comboBox.buttonCell = PictureListCell()
            comboBox.items = FXCollections.observableArrayList(*Picture.values())
        }
    }
}
