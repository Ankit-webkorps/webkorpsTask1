import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class UserManager
{
    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public boolean createUser(String name, String email, List<String> mobileNumbers) throws IllegalArgumentException {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        for (String number : mobileNumbers) {
            if (!isValidMobileNumber(number)) {
                throw new IllegalArgumentException("Invalid mobile number: " + number);
            }
        }

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
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean updateUserName(String email, String newName) throws UserNotFoundException {
        User user = getUser(email);
        user.setName(newName);
        return true;
    }

    public boolean addUserMobileNumber(String email, String number) throws UserNotFoundException, IllegalArgumentException {
        if (!isValidMobileNumber(number)) {
            throw new IllegalArgumentException("Invalid mobile number");
        }

        User user = getUser(email);
        user.addMobileNumber(number);
        return true;
    }

    public boolean removeUserMobileNumber(String email, String number) throws UserNotFoundException {
        User user = getUser(email);
        return user.removeMobileNumber(number);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidMobileNumber(String number) {
        // Simple validation - 10 digits, optionally starting with + or having dashes/spaces
        String mobileRegex = "^[+]?[0-9\\-\\s]{10,15}$";
        Pattern pattern = Pattern.compile(mobileRegex);
        return pattern.matcher(number).matches();
    }
}