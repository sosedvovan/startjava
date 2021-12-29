package com.urise.webapp.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**класс представляет собой хранилище типа List<Organization> organizations для объектов Organization места работы*/
public class OrganizationSection extends Section {
    private static final long serialVersionUID = 1L;

    private  List<Organization> organizations;

    /**при создании объекта в конструктор передаем элементы списка: напр: (new OrganizationSection("F", "G", "M","L").
    в этом первом конструкторе эти элементы кладутся в список
    и передаются this() в главный конструктор*/
    public OrganizationSection(Organization... organization) {
        this(Arrays.asList(organization));
    }

    /** далее основной конструктор, принимающий от первого список, инициализирует им поле объекта, + проверка на нуль*/
    public OrganizationSection(List<Organization> organizations) {
        //зделаем здесь проверку на нуль
        Objects.requireNonNull(organizations, "organizations must not be null");
        this.organizations = organizations;
    }

    //пустой конструктор для Джакарты(final в полях убираем тк пустой конструктор)
    public OrganizationSection() {
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationSection that = (OrganizationSection) o;

        return organizations.equals(that.organizations);
    }

    @Override
    public int hashCode() {
        return organizations.hashCode();
    }

    @Override
    public String toString() {
        return organizations.toString();//делигируемся
    }
}
