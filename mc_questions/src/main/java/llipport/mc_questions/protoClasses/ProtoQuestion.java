package llipport.mc_questions.protoClasses;

import java.util.ArrayList;
import java.util.UUID;

public class ProtoQuestion {
    private String questionText;
    private String questionInfo;
    private boolean notQuestion;
    private UUID correctAnswerId;
    private ArrayList<UUID> incorrectAnswerIds;


    @Override
    public String toString() {
        return "ProtoQuestion{" +
                "questionText='" + questionText + '\'' +
                ", questionInfo='" + questionInfo + '\'' +
                ", notQuestion=" + notQuestion +
                ", correctAnswerId=" + correctAnswerId +
                ", incorrectAnswerIds=" + incorrectAnswerIds.toString() +
                '}';
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionInfo() {
        return questionInfo;
    }

    public void setQuestionInfo(String questionInfo) {
        this.questionInfo = questionInfo;
    }

    public boolean isNotQuestion() {
        return notQuestion;
    }

    public void setNotQuestion(boolean notQuestion) {
        this.notQuestion = notQuestion;
    }

    public UUID getCorrectAnswerId() {
        return correctAnswerId;
    }

    public void setCorrectAnswerId(UUID correctAnswerId) {
        this.correctAnswerId = correctAnswerId;
    }

    public ArrayList<UUID> getIncorrectAnswerIds() {
        return incorrectAnswerIds;
    }

    public void setIncorrectAnswerIds(ArrayList<UUID> incorrectAnswerIds) {
        this.incorrectAnswerIds = incorrectAnswerIds;
    }
}
