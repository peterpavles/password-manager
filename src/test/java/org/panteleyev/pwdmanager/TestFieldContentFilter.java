/*
 * Copyright (c) 2020, Petr Panteleyev <petr@panteleyev.org>
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

import org.panteleyev.pwdmanager.filters.FieldContentFilter;
import org.panteleyev.pwdmanager.model.Card;
import org.panteleyev.pwdmanager.model.Field;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;
import java.util.UUID;
import static org.testng.Assert.assertEquals;

@Test
public class TestFieldContentFilter {

    private static final Card CARD =
        Card.newCard(UUID.randomUUID().toString(), null, List.of(
            new Field(FieldType.STRING, UUID.randomUUID().toString(), "value1"),
            new Field(FieldType.STRING, UUID.randomUUID().toString(), "Value1"),
            new Field(FieldType.STRING, UUID.randomUUID().toString(), "Value2"))
        );

    @DataProvider
    public Object[][] dataProvider() {
        return new Object[][]{
            {CARD, "value", true},
            {CARD, "value1", true},
            {CARD, "ValUe", true},
            {CARD, "ValUe3", false},
        };
    }

    @Test(dataProvider = "dataProvider")
    public void test(Card card, String value, boolean expected) {
        assertEquals(new FieldContentFilter(value).test(card), expected);
    }
}
