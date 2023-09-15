package llipport.mc_questions.protoClasses;

public class ProtoUser {
    private String name;
    private String password;
    private int roleIndex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleIndex() {
        return roleIndex;
    }

    public void setRoleIndex(int roleIndex) {
        this.roleIndex = roleIndex;
    }
}
