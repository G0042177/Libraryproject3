package ie.atu.standard;


public class Service {
    private AccountInfo current;

    public void login(AccountInfo info) {
        this.current = info;
    }

    public AccountInfo getCurrent() {
        return current;
    }

    public boolean isStaff() {
        return current != null && "Staff".equalsIgnoreCase(current.getType());
    }
}
