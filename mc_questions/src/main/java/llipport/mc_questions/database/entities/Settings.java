package llipport.mc_questions.database.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String name;
    private int maxQuestions;
    private int minQuestions;
    private int maxNotQuestions;
    private int maxUsedQuestions;
    private int yearsUntilQuestionsAreFree;
    private int maxAnswers;
    private int minAnswers;
    private int maxCorrectAnswers;

    protected Settings() {
    }

    public Settings(
            String name,
            int maxQuestions,
            int minQuestions,
            int maxNotQuestions,
            int maxUsedQuestions,
            int yearsUntilQuestionsAreFree,
            int maxAnswers,
            int minAnswers,
            int maxCorrectAnswers
    ) {
        this.name = name;
        this.maxQuestions = maxQuestions;
        this.minQuestions = minQuestions;
        this.maxNotQuestions = maxNotQuestions;
        this.maxUsedQuestions = maxUsedQuestions;
        this.yearsUntilQuestionsAreFree = yearsUntilQuestionsAreFree;
        this.maxAnswers = maxAnswers;
        this.minAnswers = minAnswers;
        this.maxCorrectAnswers = maxCorrectAnswers;
    }

    public void copyFrom(Settings source) {
        this.maxQuestions = source.getMaxQuestions();
        this.minQuestions = source.getMinQuestions();
        this.maxNotQuestions = source.getMaxNotQuestions();
        this.maxUsedQuestions = source.getMaxUsedQuestions();
        this.yearsUntilQuestionsAreFree = source.getYearsUntilQuestionsAreFree();
        this.maxAnswers = source.getMaxAnswers();
        this.minAnswers = source.getMinAnswers();
        this.maxCorrectAnswers = source.getMaxCorrectAnswers();
    }

    @Override
    public String toString() {
        return "Settings{" +
                "id=" + uuid +
                ", name='" + name + '\'' +
                ", maxQuestions=" + maxQuestions +
                ", minQuestions=" + minQuestions +
                ", maxNotQuestions=" + maxNotQuestions +
                ", maxUsedQuestions=" + maxUsedQuestions +
                ", yearsUntilQuestionsAreFree=" + yearsUntilQuestionsAreFree +
                ", maxAnswers=" + maxAnswers +
                ", minAnswers=" + minAnswers +
                ", maxCorrectAnswers=" + maxCorrectAnswers +
                '}';
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getMaxQuestions() {
        return maxQuestions;
    }

    public int getMinQuestions() {
        return minQuestions;
    }

    public int getMaxNotQuestions() {
        return maxNotQuestions;
    }

    public int getMaxUsedQuestions() {
        return maxUsedQuestions;
    }

    public int getYearsUntilQuestionsAreFree() {
        return yearsUntilQuestionsAreFree;
    }

    public int getMaxAnswers() {
        return maxAnswers;
    }

    public int getMinAnswers() {
        return minAnswers;
    }

    public int getMaxCorrectAnswers() {
        return maxCorrectAnswers;
    }
}
