package org.example.cmd;

public class SampleClass {

    public String sampleMethod() {
        System.out.println("Executing method");
        return "sample value";
    }
}


class NewAnotherClass {

    private String field1;

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField1() {
        return field1;
    }
}