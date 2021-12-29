package com.urise.webapp.util;

import com.google.gson.*;//gson это Json, от гугла

import java.lang.reflect.Type;
//Json при десериализации не может создать класс Section(от кот зависит объект Resume) тк он абстрактный, а Json наследование
// не поддерживает(в отличии от Xml) и для этого этот класс-адаптер.
//этот класс в файл-сериализации при сериализации будет добавлять две переменных-CLASSNAME и INSTANCE
//(см внизу пример этого файла- там ищи слова CLASSNAME и INSTANCE и все будет понятно)
//и при десериализации это поможет Json правильно создать объекты наследников класса Section
public class JsonSectionAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {//имплементим и то и это и реализуем их абстракты
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

    //ПРИ ДЕСЕРИАЛИЗАЦИИ:
    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();//берем объект
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);//считываем проперти CLASSNAME(кот сами добавили при сериализ)
        String className = prim.getAsString();

        try {
            Class clazz = Class.forName(className);//и после этого создаем этот класс(для разных типов секций-разные имена соотв)
            return context.deserialize(jsonObject.get(INSTANCE), clazz);//далее десириализуем INSTANCE
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    //ПРИ СЕРИАЛИЗАЦИИ:
    @Override
    public JsonElement serialize(T section, Type type, JsonSerializationContext context) {
        //НА УРОВНЕ ОБЪЕКТА РАБОТАЕМ:
        //создаем служебный объект JsonObject
        JsonObject retValue = new JsonObject();
        //через объект JsonObject в проперти добавляем переменную CLASSNAME содержащую в себе
        // section.getClass().getName() - ЭТО ИЛИ текстсекшен ИЛИ листсекшен ИЛИ организатионсекшин
        retValue.addProperty(CLASSNAME, section.getClass().getName());

        //НА УРОВНЕ ВНУТРЕННЕГО СОДЕРЖАНИЯ ОБЪЕКТА РАБОТАЕМ:
        //служебному элементу JsonElement elem присвоим реализацию JsonSerializationcontext в абстракт serialize() которого будет приходить
        // одна из T section модели Резюме(текстсекшен, листсекшен, организатионсекшин)
        JsonElement elem = context.serialize(section);
        //переменная-проперти INSTANCE будет сопоставляться(брать значение)  определенной реализацией
        // Section(текстсекшен, листсекшен, организатионсекшин) и отправляться на сериализацию
        retValue.add(INSTANCE, elem);

        return retValue;
    }
}

//{"uuid":"uuid1","fullName":"Name1","contacts":{"PHONE":"11111","MAIL":"mail1@ya.ru"},
// "sections":{"PERSONAL":{"CLASSNAME":"com.urise.webapp.model.TextSection","INSTANCE":{"content":"Personal data"}},
// "OBJECTIVE":{"CLASSNAME":"com.urise.webapp.model.TextSection","INSTANCE":{"content":"Objective1"}},
// "ACHIEVEMENT":{"CLASSNAME":"com.urise.webapp.model.ListSection",
// "INSTANCE":{"items":["Achivment11","Achivment12","Achivment13"]}},
// "QUALIFICATIONS":{"CLASSNAME":"com.urise.webapp.model.ListSection","INSTANCE":{"items":["Java","SQL","JavaScript"]}},
// "EXPERIENCE":{"CLASSNAME":"com.urise.webapp.model.OrganizationSection",
// "INSTANCE":{"organizations":[{"homePage":{"name":"Organization2","url":"http://Organization2.ru"},
// "position":[{"startDate":{"year":2015,"month":1,"day":1},"endDate":{"year":3000,"month":1,"day":1},
// "title":"position1","description":"content1"}]}]}},"EDUCATION":{"CLASSNAME":"com.urise.webapp.model.OrganizationSection",
// "INSTANCE":{"organizations":[{"homePage":{"name":"Institute"},"position":[{"startDate":{"year":1996,"month":1,"day":1},
// "endDate":{"year":2000,"month":12,"day":1},"title":"aspirant"},{"startDate":{"year":2001,"month":3,"day":1},
// "endDate":{"year":2005,"month":1,"day":1},"title":"student","description":"IT facultet"}]},
// {"homePage":{"name":"Organization12","url":"http://Organization12.ru"},"position":[]}]}}}}