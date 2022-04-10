package com.jiajia.essayjoke;

/**
 * Created by Numen_fan on 2022/4/10
 * Desc:
 */
public class Person {

    String name;

    int age;

    public Person() {

    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
