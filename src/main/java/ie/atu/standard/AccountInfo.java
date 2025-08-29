package ie.atu.standard;

public class AccountInfo {
    private final int memberId;
    private final String name;
    private final String email;
    private final String type;

    public AccountInfo(int memberId, String name, String email, String type) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public int getMemberId() { return memberId; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public String getType()  { return type; }

    @Override
    public String toString() {
        return memberId + " | " + name + " | " + email + " | " + type;
    }
}
