package com.urise.webapp;

public class MainString {
    public static void main(String[] args) {
        /**
         * мы создадим массив строк и хотим все его элементы  сконкатенировать в одну строку
         * для этого создадим объект StringBuilder, чтобы на каждом цикле
         * конкатенации не создавался новый-промежуточный объект с результатом
         * (как было бы если бы использовали обычный String).
         * метод append() это конкатенация в StringBuilder.
         */
        String[] strArray = new String[]{"1","2","3","4","5"};
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str).append(", ");
        }
        /**
         * здесь воспользуемся toString() для вывода.
         */
        System.out.println(sb.toString());

        String str1 = "abc";
        String str3 = "c";
        String str2 = ("ab" + str3).intern();
        System.out.println(str1 == str2);
    }

    /**
     *
     Одинаковые строковые литералы всегда ссылаются на один и тот же экземпляр класса String .
     Экземпляры классы String , вычисленные во время выполнения, создаются заново,
     автоматически в пуле не участвуют и потому различны.
     Строковые литералы в константных выражениях вычисляются на этапе компиляции
     и затем расцениваются как обычные литералы.
     С помощью метода intern() можно добавить строку в пул либо получить ссылку на такую строку из пула.

     Дело в том, что строку в пул можно поместить принудительно, с помощью метода String.intern().
     Этот метод возвращает из пула строку, равную той, у которой был вызван этот метод.
     Если же такой строки нет – в пул кладется та, у которой вызван метод, после чего возвращается ссылка на нее же.
     Таким образом, при грамотном использовании пула появляется возможность сравнивать строки не по значению,
     через equals, а по ссылке, что значительно, на порядки, быстрее.

     метод intern() : при конкатенации в рантайме jvm увидит что такая строка уже существует в пулле строк
     и для нового объекта str2 не будет создавать новую такую же строку и их можно сравнить с пом. == и получить true.
     если не в рантайме сконкатенировать: str2 = "ab" + "c"; то можно пользоваться == и без метода intern()

     Всегда когда указывается отрезок в массиве, строке, списке или ещё-где-то,
     начальный индекс включается в отрезок, а конечный исключается.

     методы класса String и StringBuilder:
     https://urvanov.ru/2016/04/20/java-8-%D1%81%D1%82%D1%80%D0%BE%D0%BA%D0%B8/

     про кодировки:
     http://www.skipy.ru/technics/encodings.html

     Ошибки при использовании строк:
     http://www.skipy.ru/technics/strings.html

     Обработка строк в Java:
     https://habr.com/ru/post/260767/

     Руководство по String pool в Java:
     https://topjava.ru/blog/rukovodstvo-po-string-pool-v-java

     англ.:
     https://stackoverflow.com/questions/355089/difference-between-stringbuilder-and-stringbuffer?rq=1
     https://www.journaldev.com/538/string-vs-stringbuffer-vs-stringbuilder
     */
}
