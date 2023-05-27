import com.project.example.SampleClass;


public class AnotherClassName {

    private String field1;

    private Integer field2;

    public AnotherClassName(String field1) {
        this.field1 = field1;
        SampleClass sampleClass = new SampleClass("param1");
        this.field2 = sampleClass.getParam();
    }
}