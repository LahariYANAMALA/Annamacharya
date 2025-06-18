import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.SwingUtilities; // Required for launching the Swing application

public class CombinedApplicationLauncher {

    // Using a static scanner that needs re-initialization because
    // DataStructureCalculator and MySQLTestDBManager close System.in.
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Three-in-One Java Application!");
        System.out.println("---------------------------------------------");
        System.out.println("NOTE: For 'Data Structure Calculator' and 'MySQL CRUD Manager',");
        System.out.println("      please run this application from a command line/terminal");
        System.out.println("      to ensure proper interaction with their console menus.");
        System.out.println("      (The Swing GUI app will open in a separate window.)");
        System.out.println("---------------------------------------------");

        while (true) {
            displayMainMenu();
            int choice = getUserChoice(); // This will consume the newline

            switch (choice) {
                case 1:
                    System.out.println("\n--- Launching Data Structure Calculator ---");
                    // Call the main method of the DataStructureCalculator
                    DataStructureCalculator.main(new String[]{});
                    // After DataStructureCalculator finishes (and closes its scanner),
                    // re-initialize our launcher's scanner for subsequent inputs.
                    scanner = new Scanner(System.in);
                    System.out.println("--- Data Structure Calculator Session Ended ---");
                    break;
                case 2:
                    System.out.println("\n--- Launching MySQL CRUD Manager ---");
                    // Call the main method of the MySQLTestDBManager
                    MySQLTestDBManager.main(new String[]{});
                    // After MySQLTestDBManager finishes (and closes its scanner),
                    // re-initialize our launcher's scanner.
                    scanner = new Scanner(System.in);
                    System.out.println("--- MySQL CRUD Manager Session Ended ---");
                    break;
                case 3:
                    System.out.println("\n--- Launching Java Swing GUI CRUD Application ---");
                    // Launch the Swing application on the Event Dispatch Thread (EDT)
                    SwingUtilities.invokeLater(() -> {
                        DatabaseApp.main(new String[]{});
                    });
                    // The Swing application runs in its own window and thread,
                    // so the console application will immediately return to the main menu.
                    System.out.println("--- Swing GUI Application Launched (check new window) ---");
                    break;
                case 4:
                    System.out.println("Exiting the Three-in-One Application. Goodbye!");
                    scanner.close(); // Close the launcher's scanner before exiting
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
            // Add a pause to allow user to read messages before returning to main menu
            System.out.println("\nPress Enter to return to the Main Menu...");
            scanner.nextLine(); // Wait for user to press Enter
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- Main Application Menu ---");
        System.out.println("1. Data Structure Calculator (Console)");
        System.out.println("2. MySQL CRUD Manager (Console)");
        System.out.println("3. Java Swing GUI CRUD Application");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        while (true) {
            try {
                // Read the integer choice
                int choice = scanner.nextInt();
                // Consume the rest of the line, including the newline character,
                // so that subsequent scanner.nextLine() calls work as expected.
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.next(); // Consume the invalid token to prevent an infinite loop
            }
        }
    }
}