
class TestClass {

    private void sampleMethod() {
        System.out.println("Doubled method name");
    }
    public void method1{
        SampleClass sampleClass = new SampleClass("field1", 1);
        sampleClass.sampleMethod();
    }
}

class SampleClass {

    private String field1;
    private Integer field2;

    public SampleClass(String field1, Integer field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public void printHello() {
        System.out.println("Hello from SampleClass!");
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public Integer getField2() {
        return field2;
    }

    public void setField2(Integer field2) {
        this.field2 = field2;
    }

    public void sampleMethod() {
        this.field1 = "modifiedValue";
    }

    public void anotherMethod() {
        this.sampleMethod();
    }
}

