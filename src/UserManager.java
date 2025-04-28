import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private final Map<String, User> users = new HashMap<>();

    public boolean createUser(String name, String email, List<String> mobileNumbers) {
        if (users.containsKey(email)) {
            return false;
        }
        User newUser = new User(name, email, mobileNumbers);
        users.put(email, newUser);
        return true;
    }

    public boolean deleteUser(String email) {
        return users.remove(email) != null;
    }

    public User getUser(String email) throws UserNotFoundException {
        User user = users.get(email);
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean updateUser(String email, String newName, List<String> newNumbers)
            throws UserNotFoundException {
        User user = getUser(email);
        user.setName(newName);
        user.setMobileNumbers(newNumbers);
        return true;
    }
}