import java.util.Arrays;
import java.util.List;

public class CompilableClass {

    public static void main(String[] args) {
        SampleClass sampleClass = new SampleClass("field1_value", 1);
        AnotherSampleClass anotherSampleClass = new AnotherSampleClass(Arrays.asList("VALUE1", "VALUE2"), 1.21);

        sampleClass.setField1("another_value");
        anotherSampleClass.printHello();
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
}

class AnotherSampleClass {

    private List<String> field1;
    private Double field2;

    private SampleClass sampleClass;

    public AnotherSampleClass(List<String> field1, Double field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public void printHello() {
        System.out.println("Hello from AnotherSampleClass!");
    }

    public List<String> getField1() {
        return field1;
    }

    public void setField1(List<String> field1) {
        this.field1 = field1;
    }

    public Double getField2() {
        return field2;
    }

    public void setField2(Double field2) {
        this.field2 = field2;
    }
}


