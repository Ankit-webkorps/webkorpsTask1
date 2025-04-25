import java.util.ArrayList;
import java.util.List;

class User {
    private String name;
    private String email;
    private List<String> mobileNumbers;

    public User(String name, String email, List<String> mobileNumbers) {
        this.name = name;
        this.email = email;
        this.mobileNumbers = new ArrayList<>(mobileNumbers);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getMobileNumbers() {
        return new ArrayList<>(mobileNumbers);
    }

    public void setMobileNumbers(List<String> mobileNumbers) {
        this.mobileNumbers = new ArrayList<>(mobileNumbers);
    }

    public void addMobileNumber(String number) {
        if (!mobileNumbers.contains(number)) {
            mobileNumbers.add(number);
        }
    }

    public boolean removeMobileNumber(String number) {
        return mobileNumbers.remove(number);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumbers=" + mobileNumbers +
                '}';
    }
}