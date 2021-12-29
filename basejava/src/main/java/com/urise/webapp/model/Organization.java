package com.urise.webapp.model;

import com.urise.webapp.util.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.Serializable;
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
//@XmlAccessorType(XmlAccessType.FIELD)//через родителя наследует
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;
    //в модели организация представляет собой хоум-линк(объект класса Link в поле имеет ссылку)
    // и список позиций (список объектов класса Position(внутренний в этом) в полях даты с титрами)
    private  Link homePage;
    private List<Position> positions = new ArrayList<>();//даты старт-энд периодов работы-учебы с комментариями

    //низкоуровневый конструктор с вар-аргами - пробрасывает в следующий чз this
    //получает имя и url для создания объекта линка, и получает перечисление объектов Position для создания Листа с position
    //создается все это внутри этого конструктора и прередается this()- в высокоуровневый конструктор
    public Organization(String name, String url, Position... position){
        this (new Link(name, url), Arrays.asList(position));
    }

    //высокоуровневый конструктор
    public Organization(Link homePage, List<Position> position) {
        this.homePage = homePage;//код, проверяющ Link останется в Link
        this.positions = position;
    }

    //пустой конструктор для Джакарты(final в полях убираем тк пустой конструктор)
    public Organization() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (homePage != null ? !homePage.equals(that.homePage) : that.homePage != null) return false;
        return positions != null ? positions.equals(that.positions) : that.positions == null;
    }

    @Override
    public int hashCode() {
        int result = homePage != null ? homePage.hashCode() : 0;
        result = 31 * result + (positions != null ? positions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Organization(" + homePage + ',' + positions + ')';
    }



    /**ДАЛЕЕ ВНУТРЕННИЙ КЛАСС
    в разное время мы можем работать на одних и тех же
    позициях- организациях. из организайшен переносим сюда некот члены класса
    static- тк ему не нужны методы и члены внешнего класса, те не нужна внутренняя ссылка*/
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable{

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private  LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private  LocalDate endDate;
        private  String title;
        private  String description;

        //сделаем несколько удобных конструкторов

        //конструктор принимает инт старт-года, старт-месяц из Ява-енума, константу NOW(в кот объект конечной LocalDate endDate,...
        //делает из этого объект LocalDate startDate и передает в главный конструктор
        public Position (int startYear, Month startMonth, String title, String description){
            this(of(startYear, startMonth), NOW, title, description);
            //NOW както связанно с паттерном спещелКейс, NOW заменяем константой,
            //кот можем пользоваться для каких-то сравнений
        }

        //конструктор принимает инт старт-года, старт-месяц из Ява-енума, инт конец-года, конец-месяц из Ява-енума,...
        //делает из этого объекты LocalDate и передает в главный конструктор
        public Position (int startYear, Month startMonth, int endYear, Month endMonth, String title, String description){
            this(of(startYear, startMonth), of(endYear, endMonth), title, description);
        }

        //главный конструктор(высокоуровневый)
        public Position(LocalDate startDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(startDate, "startDate must not be null");
            Objects.requireNonNull(endDate, "endDate must not be null");
            Objects.requireNonNull(title, "title must not be null");
            //для description-описания заголовка(title ) оставим возможность быть нулл
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description == null ? "" : description;
        }

        //пустой конструктор для Джакарты(final в полях убираем тк пустой конструктор)
        public Position() {
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
