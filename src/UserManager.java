import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    static final Map<String, User> users = new HashMap<>();


    public boolean containsemail(String input)
    {
        if(users.containsKey(input))
         return true;
          else
           return false;
    }

    public void createUser(String name, String email, List<String> mobileNumbers) {

        User newUser = new User(name, email, mobileNumbers);
        users.put(email, newUser);

    }

    public boolean deleteUser(String email) {
        if(users.containsKey(email))
        {
            users.remove(email);
        return true;
        }
        else {
            return false;
        }

    }

//    public User getUser(String email) throws UserNotFoundException {
//        User user = users.get(email);
//        if (user == null) {
//            throw new UserNotFoundException("User not found with email: " + email);
//        }
//        return user;
//    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

}