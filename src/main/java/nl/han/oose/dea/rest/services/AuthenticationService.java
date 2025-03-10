package nl.han.oose.dea.rest.services;

import nl.han.oose.dea.rest.resources.user;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {
    private List<user> users = new ArrayList<>();

    public AuthenticationService() {
        users.add(new user("admin", "password123"));
    }

    public boolean authenticate(String username, String password) {
        return users.stream()
                .anyMatch(user -> user.getUsername().equals(username) &&
                        user.getPassword().equals(password));
    }
}
