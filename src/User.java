import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String email;
    private List<String> mobileNumbers;

    public User(String name, String email, List<String> mobileNumbers) {
        this.name = name;
        this.email = email;
        this.mobileNumbers = new ArrayList<>(mobileNumbers);
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public List<String> getMobileNumbers() { return new ArrayList<>(mobileNumbers); }
    public void setMobileNumbers(List<String> mobileNumbers) {
        this.mobileNumbers = new ArrayList<>(mobileNumbers);
    }

    @Override
    public String toString() {
        return String.format("User[name='%s', email='%s', numbers=%s]",
                name, email, mobileNumbers);
    }
}