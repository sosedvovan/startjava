package com.urise.webapp.model;

import com.urise.webapp.util.DateUtil;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.urise.webapp.util.DateUtil.NOW;
import static com.urise.webapp.util.DateUtil.of;

//класс отобажает собой список мест учебы и работы с линками url
//имеется внутренний статический класс
public class Organization {
    //в модели организация представляет собой хоум-линк и список позиций
    private final Link homePage;
    private List<Position> position = new ArrayList<>();

    //низкоуровневый конструктор с вар-аргами - пробрасывает в следующий чз this
    public Organization(String name, String url, Position... position){
        this (new Link(name, url), Arrays.asList(position));
    }

    //высокоуровневый конструктор
    public Organization(Link homePage, List<Position> position) {
        this.homePage = homePage;//код, проверяющ Link останется в Link
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (homePage != null ? !homePage.equals(that.homePage) : that.homePage != null) return false;
        return position != null ? position.equals(that.position) : that.position == null;
    }

    @Override
    public int hashCode() {
        int result = homePage != null ? homePage.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Organization(" + homePage + ',' + position + ')';
    }

    //в разное время мы можем работать на одних и тех же
    //позициях- организациях. из организайшен переносим сюда некот члены класса
    //static- тк ему не нужны методы и члены внешнего класса, те не нужна внутренняя ссылка
    public static class Position {//

        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String title;
        private final String description;

        //сделаем несколько удобных конструкторов

        public Position (int startYear, Month startMonth, String title, String description){
            this(of(startYear, startMonth), NOW, title, description);
            //NOW както связанно с паттерном спещелКейс, NOW заменяем константой,
            //кот можем пользоваться для каких-то сравнений
        }

        public Position (int startYear, Month startMonth, int endYear, Month endMonth, String title, String description){
            this(of(startYear, startMonth), of(endYear, endMonth), title, description);
        }

        public Position(LocalDate startDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(startDate, "startDate must not be null");
            Objects.requireNonNull(endDate, "endDate must not be null");
            Objects.requireNonNull(title, "title must not be null");
            //для description-описания заголовка(title ) оставим возможность быть нулл
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @Override//без проверки на нуль всех полей, кроме description
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position position = (Position) o;

            if (!startDate.equals(position.startDate)) return false;
            if (!endDate.equals(position.endDate)) return false;
            if (!title.equals(position.title)) return false;
            return description != null ? description.equals(position.description) : position.description == null;
        }

        @Override
        public int hashCode() {
            int result = startDate.hashCode();
            result = 31 * result + endDate.hashCode();
            result = 31 * result + title.hashCode();
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
