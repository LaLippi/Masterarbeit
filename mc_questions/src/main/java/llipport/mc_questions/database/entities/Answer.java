package llipport.mc_questions.database.entities;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToMany(mappedBy = "incorrectAnswers")
    private Set<Question> incorrectInQuestions;

    @OneToMany(mappedBy = "correctAnswer")
    private Set<Question> correctInQuestions;

    private String text;

    protected Answer() {
    }

    public Answer(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format("Answer[id=%s, text='%s']", uuid, text);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Question> getIncorrectInQuestions() {
        return incorrectInQuestions;
    }

    public Set<Question> getCorrectInQuestions() {
        return correctInQuestions;
    }

    @PreRemove
    private void removeIncorrectInQuestionAssociations() {
        for (Question question : this.incorrectInQuestions) {
            question.getIncorrectAnswers().remove(this);
        }
    }
}
