package com.urise.webapp.storage;

public class MainUtil {
    public static void main(String[] args) {
        System.out.println(Integer.valueOf(-1) == Integer.valueOf(-1));
        System.out.println(Integer.valueOf(-1) == new Integer(-1));

        int result = getInt();//getInt() возвращает Integer и -> автоматич анбоксинг к int будет
        System.out.println(result);

    }

    private static Integer getInt() {
       // return null;// будет NullPointerException не чекед а рантайм- Идеа не подчеркивает
        return -1; //будет автоматич инбоксинг к Integer
    }
}
