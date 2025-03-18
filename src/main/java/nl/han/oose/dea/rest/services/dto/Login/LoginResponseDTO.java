package nl.han.oose.dea.rest.services.dto.Login;

public class LoginResponseDTO {
    private String user;

    public LoginResponseDTO(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
