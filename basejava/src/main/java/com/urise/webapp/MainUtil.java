package com.urise.webapp;

public class MainUtil {
    public static void main(String[] args) {
        //разные способы создания переменной класса Integer
        System.out.println(Integer.valueOf(-1) == Integer.valueOf(-1));//true
        System.out.println(Integer.valueOf(-1) == new Integer(-1));//false

        int result = getInt();//getInt() возвращает Integer и -> автоматич анбоксинг к int будет
        System.out.println(result);
    }

    //демонстрация работы инбоксинга:
    private static Integer getInt() {
        //null возвращать нельзя, можно int(будет автоинбоксинг)
       // return null;// будет NullPointerException не чекед а рантайм- Идеа не подчеркивает
        return -1; //будет автоматич инбоксинг к Integer
    }
}
