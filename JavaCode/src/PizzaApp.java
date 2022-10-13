import javax.print.DocFlavor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class PizzaApp {
    Scanner scanner = new Scanner(System.in);
    boolean ordered = false;
    ArrayList <Item> basket;
    PizzaApp(){
        setUp();
        basket = new ArrayList<Item>();
    }
    public static void main(String[] args) {
        PizzaApp APP = new PizzaApp();
    }

    private void setUp() {
        makeConnection();
        getInputs();
    }

    private Connection makeConnection() {
        Connection conn = null;
        String db_URL = "jdbc:mysql://localhost/sakila";
        String user = "root";
        String pass = "liupaddb";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(db_URL,user,pass);
        } catch (SQLException ex) {
// handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    private void getInputs() {

        orderloop:
        while(!ordered){
            System.out.println("Welcome to our pizzeria!");
            System.out.println("UI:\n1 Make an order" +
                                 "\n 2 Menu" +
                                 "\n 3 Cancel order" +
                                 "\n 4 Add item to basket" +
                                 "\n 0 Close app");
            int choise = scanner.nextInt();
            switch (choise){
                case 1:
                    makeOrder();
                    break;
                case 2:
                    printMenu();
                    break;
                case 3:
                    cancelOrder();
                    break;
                case 4:
                    addItemToBasket();
                    break;
                case 0:
                    closeApp();
                    break orderloop;
            }
        }
        System.out.println("See you next time!");
        closeApp();
    }

    private void addItemToBasket() {
        System.out.println("Which item(id) do you want to add to your basket?");
        int itemID = scanner.nextInt();


    }

    private void cancelOrder() {
        //TODO: TAKE OUT THE ORDER FROM DATABASE
    }

    private void printMenu() {
        //TODO: MENU FROM DATABASE
        System.out.println("MENU: ");
    }

    private void makeOrder() {
        getAddress();
        //TODO: SEND ORDER TO DATABASE

    }
    private void getAddress(){

    }

    private void closeApp() {
        System.exit(0);
    }
}
