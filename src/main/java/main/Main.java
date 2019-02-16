package main;

import field.Field;
import field.FieldGenerator;
import field.Numbers;
import field.NumbersSide;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        Field field = FieldGenerator.generate(5, 5);
        System.out.println(field);

        System.out.println("--top--");
        Numbers topNumbers = new Numbers(field, NumbersSide.Top);
        System.out.println(topNumbers);

        System.out.println("--left--");
        Numbers leftNumbers = new Numbers(field, NumbersSide.Left);
        System.out.println(leftNumbers);
    }
}
