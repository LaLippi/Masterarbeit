package llipport.mc_questions.database.entities;

import jakarta.persistence.*;
import llipport.mc_questions.requirementValidator.ExamCheckResult;
import llipport.mc_questions.requirementValidator.RequirementValidator;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Entity
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "exam_question",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> questions;

    private Date date;

    protected Exam() {
    }

    public Exam(Date date, Set<Question> questions) {
        this.date = date;
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + uuid +
                ", date=" + date +
                '}';
    }

    public ArrayList<UUID> getQuestionIds() {
        ArrayList<UUID> ids = new ArrayList<>();
        for (Question question : questions) {
            ids.add(question.getUuid());
        }
        return ids;
    }

    public ExamCheckResult check(Settings settings) {
        return RequirementValidator.check(this, settings);
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Date getDate() {
        return date;
    }
}
