package user;

public class UserUpdateEmail {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public UserUpdateEmail(String email) {
        this.email = email;
    }

    private String email;

}
