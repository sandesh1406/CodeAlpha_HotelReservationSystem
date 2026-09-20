package src;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AuthService handles user authentication and credential management.
 */
public class AuthService {
    private final Map<String, User> userDatabase = new HashMap<>();

    public AuthService() {
        // Demo Credentials
        addUser(new User("admin", "admin123", "Administrator", User.Role.ADMIN));
        addUser(new User("receptionist", "reception123", "Front Desk Staff", User.Role.RECEPTIONIST));
        addUser(new User("manager", "manager123", "Hotel Manager", User.Role.MANAGER));
    }

    private void addUser(User user) {
        userDatabase.put(user.getUsername(), user);
    }

    /**
     * Authenticates a user based on username and password.
     * @return Optional containing the User if authentication is successful.
     */
    public Optional<User> authenticate(String username, String password) {
        User user = userDatabase.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public User getUser(String username) {
        return userDatabase.get(username);
    }
}
