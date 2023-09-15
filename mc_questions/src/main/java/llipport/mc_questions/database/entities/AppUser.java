package llipport.mc_questions.database.entities;

import jakarta.persistence.*;
import llipport.mc_questions.database.util.ROLE;

import java.util.Set;
import java.util.UUID;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @OneToMany(mappedBy = "author")
    private Set<Question> authoredQuestions;

    @Column(nullable = false, unique = true)
    private String name;

    private String password;

    private ROLE role;

    protected AppUser() {
    }

    public AppUser(String name, String password, ROLE role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + uuid +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Set<Question> getAuthoredQuestions() {
        return authoredQuestions;
    }

    public void setAuthoredQuestions(Set<Question> authoredQuestions) {
        this.authoredQuestions = authoredQuestions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ROLE getRole() {
        return role;
    }

    public int getRoleIndex() {
        switch (role) {
            case USER -> {
                return 0;
            }
            case MODERATOR -> {
                return 1;
            }
            case ADMIN -> {
                return 2;
            }
            default -> {
                return -1;
            }
        }
    }

    public String getTranslatedRole() {
        if (role == null) {
            return "keine Rolle zugewiesen";
        }
        switch (role) {
            case USER -> {
                return "Benutzer*in";
            }
            case MODERATOR -> {
                return "Moderator";
            }
            case ADMIN -> {
                return "Admin";
            }
            default -> {
                return "";
            }
        }
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    @PreRemove
    public void removeAuthoredQuestionsAssociations() {
        for (Question question : this.getAuthoredQuestions()) {
            question.setAuthorToDeleted();
        }
    }
}
