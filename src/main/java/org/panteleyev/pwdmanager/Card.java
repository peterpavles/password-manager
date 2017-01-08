/*
 * Copyright (c) 2016, Petr Panteleyev <petr@panteleyev.org>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Card extends Record {
    private List<Field> fields;
    private final String note;

    public Card(String name, Picture picture, List<Field> fields) {
        this(UUID.randomUUID().toString(), name, picture, fields, "");
    }

    public Card(String name, Picture picture, List<Field> fields, String note) {
        this(UUID.randomUUID().toString(), name, picture, fields, note);
    }

    public Card(String id, String name, Picture picture, List<Field> fields, String note) {
        this(id, System.currentTimeMillis(), name, picture, fields, note);
    }

    public Card(String id, long modified, String name, Picture picture, List<Field> fields, String note) {
        super(id, modified, name, RecordType.EMPTY, picture);
        this.fields = new ArrayList<>();
        if (fields != null) {
            this.fields.addAll(fields);
        }
        this.note = note == null ? "" : note;
    }

    @Override
    public Card clone() {
        Card clone = (Card)super.clone();

        clone.fields = fields.stream()
            .map(f -> f.clone())
            .collect(Collectors.toList());

        return clone;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getNote() {
        return note;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Card) {
            Card that = (Card)o;

            return super.equals(o)
                && Objects.equals(this.fields, that.fields)
                && Objects.equals(this.note, that.note);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 19 * hash + Objects.hashCode(this.fields);
        hash = 19 * hash + Objects.hashCode(this.note);
        return hash;
    }
}
