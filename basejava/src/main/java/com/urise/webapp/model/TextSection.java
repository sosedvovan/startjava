package com.urise.webapp.model;

import java.util.Objects;
/** Объекты этого класса будут хранить одну строку для Section (в значении Мапы в Резюме)*/
public class TextSection extends Section {
    private static final long serialVersionUID = 1L;

    public static final TextSection EMPTY = new TextSection("");

    private  String content;

    public TextSection(String content) {
        Objects.requireNonNull(content, "content must not be null");
        this.content = content;
    }

    //пустой конструктор для Джакарты(final в полях убираем тк пустой конструктор)
    public TextSection() {
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextSection that = (TextSection) o;

        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public String toString() {
        return content;
    }
}
