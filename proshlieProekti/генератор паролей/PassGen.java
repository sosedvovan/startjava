package passGen;

import java.util.ArrayList;
class PassGen{
    public static void main(String[] args){
        System.out.println(Generate.generatePassword(20));
    }
}
class Generate{
    public static String getRandomInt (){
        String result = String.valueOf((int)(Math.random()* 10));
        return result;
    }
    public static String getRandomChar(){
        String[] character = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String result = character[(int)(Math.random()*character.length)];
        if (Math.random() < 0.5){
            return result.toUpperCase();
        }
        return result;
    }
    public static String getRandomSym(){
        String[] symbol = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "-", "=", "+"}; String result = symbol[(int)(Math.random()*symbol.length)];
        return result;
    }
    public static String generatePassword(int passLen){
        ArrayList<String> result = new ArrayList<>();
        String temp = "";
        for (int i = 0; i < passLen; i++){
            int switcher = (int)(Math.random()*3);
            if (switcher == 0){
                temp = getRandomInt();
            } else if (switcher == 1){
                temp = getRandomChar();
            } else {
                temp = getRandomSym();
            }
            result.add(temp);
        }
        StringBuilder sb = new StringBuilder();
        for (String x: result){
            sb.append(x);
        }
        return sb.toString();
    }
}