package llipport.mc_questions.protoClasses;

import java.sql.Date;
import java.util.ArrayList;
import java.util.UUID;

public class ProtoExam {
    private Date date;
    private ArrayList<UUID> questionIds;

    @Override
    public String toString() {
        return "ProtoExam{" +
                "date=" + date +
                ", questionIds=" + questionIds.toString() +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<UUID> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(ArrayList<UUID> questionIds) {
        this.questionIds = questionIds;
    }
}
