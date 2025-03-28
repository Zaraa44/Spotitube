package nl.han.oose.dea.rest.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.datasource.dao.UserDAO;
import nl.han.oose.dea.rest.datasource.dao.PlaylistDAO;
import nl.han.oose.dea.rest.services.dto.user.UserDTO;
import nl.han.oose.dea.rest.services.dto.login.LoginRequestDTO;
import nl.han.oose.dea.rest.services.dto.login.LoginResponseDTO;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

@Path("/login")
public class LoginResource {

    @Inject
    private UserDAO userDAO;

    @Inject
    private PlaylistDAO playlistDAO;

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
