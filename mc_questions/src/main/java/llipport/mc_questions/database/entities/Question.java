package llipport.mc_questions.database.entities;

import jakarta.persistence.*;
import llipport.mc_questions.protoClasses.ProtoQuestion;
import llipport.mc_questions.requirementValidator.QuestionCheckResult;
import llipport.mc_questions.requirementValidator.RequirementValidator;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Answer correctAnswer;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "question_incorrectAnswer",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id")
    )
    private Set<Answer> incorrectAnswers;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private AppUser author;

    @ManyToMany(mappedBy = "questions")
    private Set<Exam> inExams;

    // possible new feature: insert topic and subtopic

    @Column(unique = true)
    private String questionText;

    private String questionInfo;

    private Boolean isNotQuestion;

    // possible new feature: insert questionPicture

    protected Question() {
    }

    public Question(
            String questionText,
            String questionInfo,
            Boolean isNotQuestion,
            Answer correctAnswer,
            Set<Answer> incorrectAnswers,
            AppUser author,
            Set<Exam> inExams
    ) {
        this.questionText = questionText;
        this.questionInfo = questionInfo;
        this.isNotQuestion = isNotQuestion;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.author = author;
        this.inExams = inExams;
    }

    public Question(ProtoQuestion protoQuestion, Answer correctAnswer, Set<Answer> incorrectAnswers, AppUser author, Set<Exam> inExams) {
        this.questionText = protoQuestion.getQuestionText();
        this.questionInfo = protoQuestion.getQuestionInfo();
        this.isNotQuestion = protoQuestion.isNotQuestion();
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.author = author;
        this.inExams = inExams;
    }

    @Override
    public String toString() {
        return String.format("Question[id = %s, q = '%s', info = '%s']",
                uuid,
                questionText,
                questionInfo);
    }

    public void setFromProto(ProtoQuestion protoQuestion) {
        this.questionText = (protoQuestion.getQuestionText() != null) ? protoQuestion.getQuestionText() : questionText;
        this.questionInfo = (protoQuestion.getQuestionInfo() != null) ? protoQuestion.getQuestionInfo() : questionInfo;
        this.isNotQuestion = protoQuestion.isNotQuestion();
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setIncorrectAnswers(Set<Answer> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public void setInExams(Set<Exam> inExams) {
        this.inExams = inExams;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public ArrayList<UUID> getIncorrectAnswerIds() {
        ArrayList<UUID> ids = new ArrayList<>();
        for (Answer answer : incorrectAnswers) {
            ids.add(answer.getUuid());
        }
        return ids;
    }

    public QuestionCheckResult check(Settings settings) {
        return RequirementValidator.check(this, settings);
    }

    public Set<Answer> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public AppUser getAuthor() {
        return author;
    }

    public Set<Exam> getInExams() {
        return inExams;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getQuestionInfo() {
        return questionInfo;
    }

    public Boolean isNotQuestion() {
        return isNotQuestion;
    }

    @PreRemove
    private void removeInExamAssociations() {
        for (Exam exam : this.inExams) {
            exam.getQuestions().remove(this);
        }
    }

    public void setAuthorToDeleted() {
        this.author = null;
    }
}
