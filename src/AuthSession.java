package src;

import java.util.Optional;

/**
 * AuthSession maintains the state of the currently authenticated user.
 * Implemented as a Singleton.
 */
public class AuthSession {
    private static AuthSession instance;
    private User currentUser;

    private AuthSession() {}

    public static synchronized AuthSession getInstance() {
        if (instance == null) {
            instance = new AuthSession();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public boolean hasRole(User.Role role) {
        return currentUser != null && currentUser.getRole() == role;
    }
}
