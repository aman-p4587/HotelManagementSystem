import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;

public class HotelManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Felicity@4587";
    public static void main(String[] args) throws ClassNotFoundException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Statement statement = connection.createStatement();
            Scanner sc = new Scanner(System.in);
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                switch(choice){
                    case 1:
                        reserveRoom(statement,sc);
                        break;
                    case 2:
                        viewReservation(statement);
                        break;
                    case 3:
                        getRoomNumber(statement,sc);
                        break;
                    case 4:
                        updateReservation(statement,sc);
                        break;
                    case 5:
                        deleteReservation(statement,sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice!! Try again.");
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }

    }
    private static void reserveRoom(Statement statement,Scanner sc){
        try{
            System.out.print("Enter Guest Name: ");
            String guestName = sc.next();
            sc.nextLine();
            System.out.print("Enter Room Number: ");
            int roomNumber = sc.nextInt();
            System.out.print("Enter Contact number: ");
            String contactNo = sc.next();

            String query = "INSERT INTO reservations (guest_name,room_number,contact_number) "+"VALUES ('"+guestName+"', "+roomNumber+", '"+contactNo+"')";

            int affectedRows = statement.executeUpdate(query);
            if(affectedRows>0){
                System.out.println("Reservation successful!");
            }else{
                System.out.println("Reservation failed!");
            }

        }catch(SQLException e){
            e.getStackTrace();
        }
    }
    private static void viewReservation(Statement statement){
        String query = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";
        try(ResultSet resultSet = statement.executeQuery(query)){
            System.out.println("Current Reservations: ");
            System.out.println();
           while(resultSet.next()){
               int reservation_id = resultSet.getInt("reservation_id");
               String guestName = resultSet.getString("guest_name");
               int roomNumber = resultSet.getInt("room_number");
               String contactNo = resultSet.getString("contact_number");
               String reservationDate = resultSet.getTimestamp("reservation_date").toString();
               System.out.println("===================================");
               System.out.println("Reservation Id : "+reservation_id);
               System.out.println("Guest Name : "+guestName);
               System.out.println("Guest Room No. : "+roomNumber);
               System.out.println("Guest Contact No. : "+contactNo);
               System.out.println("Guest Booked room at : "+reservationDate);
           }
            System.out.println("==================================");
        }catch(SQLException e){
            e.getStackTrace();
        }
    }
    private static void getRoomNumber(Statement statement,Scanner sc){
        try{
            System.out.print("Enter Reservation ID: ");
            int reservationId = sc.nextInt();
            System.out.print("Enter Guest Name: ");
            String guestName = sc.next();
            sc.nextLine();

            String query = "SELECT room_number FROM reservations WHERE reservation_id="+reservationId+" AND guest_name='"+guestName+"'";
            try(ResultSet resultSet = statement.executeQuery(query)){

                if(resultSet.next()){
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation ID "+reservationId+
                            " and Guest "+guestName+" is : "+roomNumber);
                } else{
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        }catch(SQLException e){
            e.getStackTrace();
        }
    }
    private static void updateReservation(Statement statement, Scanner sc){
        try{
            System.out.print("Enter reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine();

            if(!reservationExists(statement, reservationId)){
                System.out.println("Reservation not found for the given ID.");
                return;
            }
            System.out.print("Enter new guest name: ");
            String newGuestName = sc.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = sc.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = sc.next();

            String query = "UPDATE reservations SET guest_name='"+newGuestName+"', "+
                    "room_number = "+newRoomNumber+", "+
                    "contact_number = '"+newContactNumber+"' "+
                    "WHERE reservation_id = "+reservationId;

            int affectedRows = statement.executeUpdate(query);
            if(affectedRows>0){
                System.out.println("Reservation updated successfully!");
            } else{
                System.out.println("Reservation update failed.");
            }

        }catch(SQLException e){
            e.getStackTrace();
        }
    }
    private static void deleteReservation(Statement statement, Scanner sc){
        try{
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = sc.nextInt();

            if(!reservationExists(statement,reservationId)){
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String query = "DELETE FROM reservations WHERE reservation_id = "+reservationId;

            int affectedRows = statement.executeUpdate(query);
            if(affectedRows>0){
                System.out.println("Reservation deleted successfully!");
            } else{
                System.out.println("Reservation deletion failed.");
            }

        } catch(SQLException e){
            e.getStackTrace();
        }
    }
    private static boolean reservationExists(Statement statement, int reservationId){
        try{
            String query = "SELECT reservation_id FROM reservations WHERE reservation_id = "+reservationId;
            try(ResultSet resultSet = statement.executeQuery(query)){
                return resultSet.next();
            }
        }catch(SQLException e){
            e.getStackTrace();
            return false;
        }
    }

    public static void exit() throws InterruptedException{
        System.out.print("Exiting System");
        int i=5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank you For Using Hotel Reservation System!!");
    }
}
