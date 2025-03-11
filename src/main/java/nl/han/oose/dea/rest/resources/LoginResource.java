package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.dto.Login.LoginRequestDTO;
import nl.han.oose.dea.rest.services.dto.Login.LoginResponseDTO;

@Path("/login")
public class LoginResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequest) {
        if (yesLogin(loginRequest.getUser(), loginRequest.getPassword())) {
            String token = "1234-1234-1234";
            String fullName = "Pasa";

            LoginResponseDTO responseDTO = new LoginResponseDTO(token, fullName);
            return Response.ok(responseDTO).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("foutje").build();
        }
    }

    private boolean yesLogin(String user, String password) {
        return "Pasa".equals(user) && "Password123".equals(password);
    }
}
