package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.datasource.data.UserDAO;
import nl.han.oose.dea.rest.datasource.data.PlaylistDAO;
import nl.han.oose.dea.rest.services.dto.User.UserDTO;
import nl.han.oose.dea.rest.services.dto.Login.LoginRequestDTO;
import nl.han.oose.dea.rest.services.dto.Login.LoginResponseDTO;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

@Path("/login")
public class LoginResource {

    private UserDAO userDAO = new UserDAO();
    private PlaylistDAO playlistDAO = new PlaylistDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequest) {
        UserDTO userFromDb = userDAO.getUserByUsername(loginRequest.getUser());

        if (userFromDb == null || !BCrypt.checkpw(loginRequest.getPassword(), userFromDb.getPassword().trim())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }


        String newToken = UUID.randomUUID().toString();
        userDAO.updateUserToken(userFromDb.getUser(), newToken);


        playlistDAO.updateOwnerColumnForUser(userFromDb.getUser());

        return Response.ok(new LoginResponseDTO(newToken, userFromDb.getUser())).build();
    }
}
