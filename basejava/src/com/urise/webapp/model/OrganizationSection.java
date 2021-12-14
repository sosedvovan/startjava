package com.urise.webapp.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//класс представляет собой хранилище типа List<Organization> для объектов Organization места работы
public class OrganizationSection extends Section {
    private final List<Organization> organizations;

    //при создании объекта в конструктор передаем элементы списка
    //в этом конструкторе эти элементы кладутся в список
    //и передаются this() в главный конструктор
    public OrganizationSection(Organization... organization) {
        this(Arrays.asList(organization));
    }

    public OrganizationSection(List<Organization> organizations) {
        //зделаем здесь проверку на нуль
        Objects.requireNonNull(organizations, "organizations must not be null");
        this.organizations = organizations;
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
