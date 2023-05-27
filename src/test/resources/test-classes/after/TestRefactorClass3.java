import com.project.example.NewSampleClassName;


public class AnotherClassName {

    private String field1;

    private Integer field2;

    public AnotherClassName(String field1) {
        this.field1 = field1;
        NewSampleClassName sampleClass = new NewSampleClassName("param1");
        this.field2 = sampleClass.getParam();
    }
}