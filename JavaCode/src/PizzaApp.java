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
        String url = "jdbc:mysql:127.0.0.1:3306/pizzeria";
        String user = "root";
        String password = "qGan94+60";
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(url, user, password);
            } catch (SQLException | ClassNotFoundException ex) {

                System.out.println("SQLException: " + ex.getMessage());

            }
            return null;

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
