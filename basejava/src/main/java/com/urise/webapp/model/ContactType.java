package com.urise.webapp.model;

//енумы есть Serializable по дефолту
//эти константы будут ключами в мапе Resume
public enum ContactType {
    PHONE("Тел."),
    MOBILE("Мобильный"),
    HOME_PHONE("Домашний тел."),

    //тк енумы это инстансы этого класса и у них могут быть члены класса, методы. и общий метод toHtml0 мы можем в каждом инстансе переопределить
    SKYPE("Skype") {
        @Override
        public String toHtml0(String value) {
            return getTitle() + ": " + toLink("skype:" + value, value);//отображение контакта SKYPE в виде ссылки skype
        }
    },
    MAIL("Почта") {
        @Override
        public String toHtml0(String value) {
            return getTitle() + ": " + toLink("mailto:" + value, value);//отображение контакта MAIL в виде ссылки mailto
        }
    },
    LINKEDIN("Профиль LinkedIn") {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    },
    GITHUB("Профиль GitHub") {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    },
    STATCKOVERFLOW("Профиль Stackoverflow") {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    },
    HOME_PAGE("Домашняя страница") {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    };

    private final String title;

    //конструктор
    ContactType(String title) {
        this.title = title;
    }
    //геттер
    public String getTitle() {
        return title;
    }

    protected String toHtml0(String value) {
        return title + ": " + value;//тело этого метода переопределенно для всех енумов кроме первых трех
    }

    //метод для красивого отображения контактов в виде view.jsp. выше переопределяем toHtml0() для каждого контакта-енума
    public String toHtml(String value) {//String value это уже конкретное значение придет- напр для енума MAIL придет стринга: "mail1@google.com"
        return (value == null) ? "" : toHtml0(value);// в toHtml0 попадают уже не нулевые значения
    }//если value == null, то маскируем этот null с помощью пустой строки: ""

    public String toLink(String href) {
        return toLink(href, title);
    }

    public static String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }
}
