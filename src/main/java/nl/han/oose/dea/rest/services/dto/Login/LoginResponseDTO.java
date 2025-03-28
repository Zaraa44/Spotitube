package nl.han.oose.dea.rest.services.dto.login;

public class LoginResponseDTO {
    private String token;
    private String user;

    public LoginResponseDTO(String token, String user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public String getUser() {
        return user;
    }
}
