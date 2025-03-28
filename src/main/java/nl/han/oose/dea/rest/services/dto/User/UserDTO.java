package nl.han.oose.dea.rest.services.dto.user;

public class UserDTO {
    private String username;
    private String password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUser() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
