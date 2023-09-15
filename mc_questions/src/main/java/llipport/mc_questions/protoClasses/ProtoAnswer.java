package llipport.mc_questions.protoClasses;

public class ProtoAnswer {
    private String text;

    @Override
    public String toString() {
        return "ProtoAnswer{" +
                "text='" + text + '\'' +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
