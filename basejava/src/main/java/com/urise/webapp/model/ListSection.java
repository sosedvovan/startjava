package com.urise.webapp.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/** Объекты этого класса будут хранить список List<String> items строк для Section (в значении Мапы в Резюме)*/
public class ListSection extends Section{
    private static final long serialVersionUID = 1L;

    public static final ListSection EMPTY = new ListSection("");

    private  List<String> items;

    /**при создании объекта в первый конструктор передаем элементы списка-варарги: напр: (new ListSection("F", "G", "M","L").
    в этом конструкторе эти элементы кладутся в список
    и передаются this() в главный конструктор*/
    //принимает последовательность стринг(варарги) а Arrays.asList() обычный массив преобразует  в список List . Иногда в ArrayList???
    public ListSection(String ... items){
        this(Arrays.asList(items));
    }

    //высокоуровневый конструктор с проверкой на нулл:
    public ListSection(List<String> items) {
        Objects.requireNonNull(items, "items must not be null");
        this.items = items;
    }

    //пустой конструктор для Джакарты(final в полях убираем тк пустой конструктор)
    public ListSection() {
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    @Override
    public String toString() {
        return ""
                + items;
    }
}
