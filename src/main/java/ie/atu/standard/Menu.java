package ie.atu.standard;

public final class Menu {
    private Menu() {}

    public static void showMain(boolean isStaff) {
        System.out.println("\nMenu");
        System.out.println("B=List Book");
        System.out.println("S=Search Book by title");
        System.out.println("R=Rent Book");
        System.out.println("T=Return Book");
        System.out.println("V=View Rentals");
        System.out.println("A=Account");
        if (isStaff) {
            System.out.println("Z=Admin");
        }
        System.out.println("X=Exit");
        System.out.print("Select: ");
    }
}



