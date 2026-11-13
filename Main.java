
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nHOSTEL ROOM ALLOCATION SYSTEM");
            System.out.println("--------------------------------");
            System.out.println("1. View Students");
            System.out.println("2. Add Student");
            System.out.println("3. Room Management");
            System.out.println("4. Add Room");
            System.out.println("5. Room Allocation Details");
            System.out.println("6. Add Room Allocation");
            System.out.println("7. Fee Management");
            System.out.println("8. Add Fee");
            System.out.println("9. Room Checkout");
            System.out.println("10. Exit");
            

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    StudentManagement.displayStudents();
                    break;

                case 2:
                    StudentManagement.addStudent(sc);
                    break;

                case 3:
                    RoomManagement.displayRooms();
                    break;

                case 4:
                    sc.nextLine();
                    RoomManagement.addRoom(sc);
                    break;

                case 5:
                    RoomAllocation.displayAllocations();
                    break;

                case 6:
                    RoomAllocation.addAllocation(sc);
                    break;

                case 7:
                    FeeManagement.displayFees();
                    break;
                case 8:
                    FeeManagement.addFee(sc);
                    break;
                case 9:
                    RoomAllocation.checkoutStudent(sc);
                    break;

                case 10:
                    System.out.println("Thank you!");
                    break;

                
                default:
                    System.out.println("Invalid choice!");
            }
            } while (choice != 10);

        

        sc.close();
    }
}
