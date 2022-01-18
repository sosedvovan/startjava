package com.urise.webapp.model;

////енумы есть Serializable по дефолту
//эти константы будут ключами в мапе Resume
public enum SectionType {
    OBJECTIVE("Позиция"),
    PERSONAL("Личные качества"),
    ACHIEVEMENT("Достижения"),
    QUALIFICATIONS("Квалификация"),
    EXPERIENCE("Опыт работы"),
    EDUCATION("Образование");

    //добавим поле- заголовок к каждой константе
    private String title;

    //конструктор для добавления заголовка:
    SectionType(String title) {
        this.title = title;
    }

    //геттер чтобы смотреть заголовок-title у каждой константы
    public String getTitle() {
        return title;
    }

    //в TestSingleton в main с пом for() выведем заголовки:
       // for (SectionType type : SectionType.values()){
       // System.out.println(type.getTitle());
        //   }
    //получается,что все константы уже в массиве, по кот можно пройтись итератором
    //и делать обработку в цикле используя втч и полиморфизм
}
