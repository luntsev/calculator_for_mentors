package com.company;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Scanner console_input = new Scanner(System.in);                                   //Создаём объект чтения строки из консоли
        MathExpression user_expression = new MathExpression(console_input.nextLine());    //Создаем объект математическое выражение
        Calculator new_result = new Calculator(user_expression);                          //Создаём объект результата
        if(user_expression.isRoman()) {                             //Проверяем в каком формате были заданы числа в математическом выражении
            Numbers result = new Numbers(new_result.getResult());   //Если в римском, то создаем объект класса Numbers в качетсве аргумента используем
            System.out.println(result.roman_number);                //возвращаемое значение int, и выводим соответствующую ему строку с рисмским числом
        } else {                                                            //Если аргументы в выражении были заданы в формате арабских чисел, то
            System.out.println(Integer.toString(new_result.getResult()));   //использовать класс Numbers для преобразования не нужно, просто выводим результат
        }
    }
}

class MathExpression {
    /* Класс MathExpression содержит всю необходимую информацию о математическом выраджении по которому будет выполняться
    вычисление результата. В класе предусмотрен елинственный конструктор которому в качестве аргумента передаётся строка.
    Коструктор, предусмотренный в классе парсит строку используя в качестве разделителей пробел и выделяет 3 подстроки:
    первый аргумент, арифметический оператор и второй аргумент. В конструкторе предусмотренны все проверки, которые
    указаны в задании: диапазон аргументов от 1 до 10, оба аргумента в одном формате (арабские числи или римские) и,т.д.
     */
    private boolean is_roman_expression;                                      //Переменная соджержит информацию о формате чисел в выражении
    private Numbers arg_1;                                                    //Переменная содержит первый аргумент
    private Numbers arg_2;                                                    //Переменная содержит второй аргумент
    private String operator;                                                  //Переменная содержит символ математического оператора

    public MathExpression(String value) {                                     //Конструктор класса в качестве аргумента принимает строку
        try {
            if(value.split(" ").length != 3) {                          //Делим строку на подстроки, в качестве разделителя используем пробел, смотрим длину возвращаемого массива строк,
                throw new CalculatorExeption("Неверный формат ввода данных!");//она должна быть равна 3: два аргумента и символ операции, если не так - бросаем исключение
            }
            arg_1 = new Numbers(value.split(" ")[0].trim());    //Создаём объекты класса Numbers для каждого аргумента (используем trim,
            arg_2 = new Numbers(value.split(" ")[2].trim());    //так как строка может начинаться с пробела, а может им заканчиваться
            operator = value.split(" ")[1].trim();              //в переменную оператора сохраняем значение оператора
            if (!(operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/"))) {  //Проверяем, что оператор принимает допустимое значение,
                throw new CalculatorExeption("Неверный символ оператора!");     //в противном случае кидаем исключение
            }
            if (arg_1.isRoman() && arg_2.isRoman()) {       //Определяем в каком формате заданы аргументы в выражении
                is_roman_expression = true;                 //Если оба в римском, то устанавливаем соответствующее значение в переменной
                if(arg_1.getValue() < arg_2.getValue() && operator == "-") {    //А также проверяем, что в случае вычитания второй аргумент не окажется
                    throw new CalculatorExeption("Результат разности римских чисел - отрицательное число!");    //больше первого, если так - кидаем исключение
                }
            } else if (!arg_1.isRoman() && !arg_2.isRoman()) {              //Если оба аргумента заданы не в римском формате (тоесть в арабском),
                is_roman_expression = false;                                //То устанавливаем соответствующее значение переменной
            } else {                                                        //Если один из аргументов задан в одном формате, а второй в другом,
                throw new CalculatorExeption("Разные форматы аргуметов!");  //то кидаем исключение.
            }
            if (arg_1.getValue() > 10 && arg_2.getValue() > 10 && arg_1.getValue() < 1 && arg_2.getValue() < 10) {  //Если значение одного из аргументов (или обоих) за
                throw new CalculatorExeption("Недупустимое значение аргумента(ов)!");                               //пределами диапазона 1..10 - кидаем исключение
            }
        } catch (RuntimeException e) {
            System.out.println(e);          //Выводим сообщение об ошибке
            System.exit(10);          //и закрываем приложение
        }
    }

    public boolean isRoman() {              //Метод возвращает значение переменной указывающей
        return is_roman_expression;         //на формат аркументов, возвращаемое значение типа boolean
    }

    public Numbers getArg_1() {             //Метод возвращает первый аргумент
        return arg_1;                       //Возвращаемое значение типа Numbers
    }

    public Numbers getArg_2() {             //Метод возвращает второй аргумент
        return arg_2;                       //Возвращаемое значение типа Numbers
    }

    public String getOperator() {           //Метод возвращает строку, содержащую
        return operator;                    //математический оператор
    }
}

class Numbers {
    /*Класс Numbers позволяет хранить значение чисел и приобразовывать их в римские числа или арабские. В классе
    предусмотренно два конструктора (хотя можно было-бы обойтись и одним), одни из которыйх используется в случае если
    при создании экземпляра класса задаётся строковый аргумент (число задаётся в виде строки), второй используется если
    при создании экземпляра класса задается аргумент типа int. Перваый конструктор используется при создании экземпляров
    класса содержащих аргументы математического выражения, второй конструктор используется при создании экземпляра класса,
    содержащего значение рузельтата операции вычисления. В классе предусмотренны методы, возвращающие: значение числа
    типа int, значение числа типа String в арабском формате, значение числа типа String в римском формате, значение типа
    формата числа переданное конструктору (арабское число или римское). В классе также есть внутренний приватный медот
    для преобразования числа типа int в строку, содержащую число в римском формате.
     */
    String roman_number;        //Строкавая переменная, содержащая число в римском формате
    String arabic_number;       //Строковая переменная, содержащая число в арабском формате
    boolean is_roman_number;    //Переменная boolean, указывающая, на формат числа переданного конструктору
    int number_value;           //Переменная int, содержащая значение числа

    private final static TreeMap<Integer, String> roman_map = new TreeMap<Integer, String>(); //Создаём maпу содержащую
    //числовое значение int и соответствующее ему римское обозначение
    static {
        roman_map.put(100, "C");        //заполняем мапу
        roman_map.put(90, "XC");
        roman_map.put(50, "L");
        roman_map.put(40, "XL");
        roman_map.put(10, "X");
        roman_map.put(9, "IX");
        roman_map.put(5, "V");
        roman_map.put(4, "IV");
        roman_map.put(1, "I");
    }

    public Numbers (String number) {        //Первый конструктор класса, в качетсве аргумента принимает строку, которая может содержать
        try {                               //как арабское обозначение числа, так и римское, римские числа обрабатываются только в диапазоне 1..10
            switch (number) {               //Проверяем полученное значение на соответствие одному из 10-ти римских чисел
                case "I":                   //Если имеется соответствие -
                    number_value = 1;       //записываем соответствующее значение в соответствующую переменную типа int,
                    is_roman_number = true; //устанавливаем в переменной содержащей тип переданного числа соответствующий тип
                    break;
                case "II":
                    number_value = 2;
                    is_roman_number = true;
                    break;
                case "III":
                    number_value = 3;
                    is_roman_number = true;
                    break;
                case "IV":
                    number_value = 4;
                    is_roman_number = true;
                    break;
                case "V":
                    number_value = 5;
                    is_roman_number = true;
                    break;
                case "VI":
                    number_value = 6;
                    is_roman_number = true;
                    break;
                case "VII":
                    number_value = 7;
                    is_roman_number = true;
                    break;
                case "VIII":
                    number_value = 8;
                    is_roman_number = true;
                    break;
                case "IX":
                    number_value = 9;
                    is_roman_number = true;
                    break;
                case "X":
                    number_value = 10;
                    is_roman_number = true;
                    break;
                default:                                    //Если переданная строка не соответствует ни одному из 10-ти римских чисел
                    number_value = Integer.parseInt(number);//Пытаемся преобразовать полученную строку в тип int и присвоить её соответствующей переменной
                    is_roman_number = false;                //Также устанавливаем в переменной содержащей тип переданного числа соответствующий тип
                    break;
            }
            if(is_roman_number) {                           //Если число было переданно в конструктор в римском формате
                roman_number = number;                      //записываем соответствующее значение в переменную, хранящую римское представление числа
            } else {                                        //В противном случае вызываем приватный метод преобразования,
                roman_number = toRoman(number_value);       //и преобразуем int в строку римского представления числа
            }
            arabic_number = Integer.toString(number_value); //Также сохраняем в строковую переменную арабское представление числа
        } catch (RuntimeException e) {
            System.out.println("Неверный формат числа!");   //Обрабатываем исключение неверного задания числа
            System.exit(10);                          //и закрываем приложение
        }
    }

    public Numbers(int number) {                    //Второй конструктор класса, который в качестве входного аргумента использует int
        number_value = number;
        arabic_number = Integer.toString(number);
        roman_number = toRoman(number);
        is_roman_number = false;
    }

    private String toRoman(int number) {            //Приватный метод преобразующий переменную типа int в строковое римское представление числа
        int key =  roman_map.floorKey(number);      //Определяем ближаешее число из мапы к заданному числу
        if ( number == key ) {                      //Если есть полное совпадение числа из мапы и заданного
            return roman_map.get(key);              //просто возвращаем соответствующее римское числу римское представление и мапы
        }                                           //В противном случае конкатинируем ближайшее представление из мапы и результат рекурсивного вызова
        return roman_map.get(key) + toRoman(number-key);    //метода
    }

    public int getValue() {             //Метод возвращает значение числа
        return number_value;            //возвращаемое значение типа int
    }

    public boolean isRoman() {          //Метод возвращает тип представления числа, переданный в конструктор
        return is_roman_number;         //возвращаемое значение типа boolean
    }

    public String getArabic() {         //Метод возвращает арабское представление числа
        return arabic_number;           //возвращаемое значение типа String
    }

    public String getRoman() {          //Метод возвращает римское представление числа
        return toRoman(number_value);   //возвращаемое значение типа String
    }
}

class Calculator {
    /*Класс Calculator выполняет вычисление по заданному выражению - экземпляру класса MathExpression, которое должно
    передаваться в конструктор класса. Помимо конструктора класс содержит единствунный метод, возвращающих рузультат
    вычисления по заданному выражению, возвращается значение типа int.
     */
    private int result;     //Переменная содержит значение результата вычисления

    public Calculator(MathExpression expression) {      //Конструктор класса, в котором выполняется необходимое вычисление
        switch (expression.getOperator()) {             //определяем тип необходимой математической операции
            case "+":
                result = expression.getArg_1().getValue() + expression.getArg_2().getValue();   //и выполняем соответствующее вычисление.
                break;
            case "-":
                result = expression.getArg_1().getValue() - expression.getArg_2().getValue();
                break;
            case "*":
                result = expression.getArg_1().getValue() * expression.getArg_2().getValue();
                break;
            case"/":
                result = expression.getArg_1().getValue() / expression.getArg_2().getValue();
                break;
            default:                                                    //Добавил default на всякий случай, но такого случая не должно быть
                throw new CalculatorExeption("Неизвестная ошибка!");    //так как ранее выполняется аналогичная проверка.
//                break;                //Если раскоментировать этот break идея почему то ругается
        }
    }

    public int getResult() {        //Метод возвращающий результат вычисления
        return result;              //Возвращаемое значение типа int
    }
}


class CalculatorExeption extends RuntimeException {
    public CalculatorExeption(String message) {
        super(message);
    }
}
