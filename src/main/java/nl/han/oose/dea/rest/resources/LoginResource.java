package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.datasource.data.UserDAO;
import nl.han.oose.dea.rest.services.dto.User.UserDTO;
import nl.han.oose.dea.rest.services.dto.Login.LoginRequestDTO;
import nl.han.oose.dea.rest.services.dto.Login.LoginResponseDTO;

@Path("/login")
public class LoginResource {

    private UserDAO userDAO = new UserDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequest) {
        System.out.println("Login attempt: " + loginRequest.getUser());

        UserDTO user = userDAO.getUserByUsername(loginRequest.getUser());

        if (user == null) {
            System.out.println("User not found in database.");
        } else {
            System.out.println("User found: " + user.getUser());
            System.out.println("Password from DB: " + user.getPassword());
            System.out.println("Password provided: " + loginRequest.getPassword());
        }

        if (user != null && user.getPassword().trim().equals(loginRequest.getPassword().trim())) {
            LoginResponseDTO responseDTO = new LoginResponseDTO(user.getUser());
            return Response.ok(responseDTO).build();

    } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
    }
}
