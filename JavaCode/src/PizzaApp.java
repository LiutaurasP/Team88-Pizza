import javax.print.DocFlavor;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PizzaApp {
    Scanner scanner = new Scanner(System.in);
    boolean ordered = false;
    Connection connection;
    ArrayList basket;
    PizzaApp() throws SQLException {
        basket = new ArrayList<Integer>();
        setUp();
    }
    public static void main(String[] args) throws SQLException {
        new PizzaApp();
    }

    private void setUp() throws SQLException {
        this.connection = makeConnection();
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

    private void getInputs() throws SQLException {

        System.out.println("Welcome to our pizzeria!");
        System.out.println("UI:\n 1 Make an order" +
                              "\n 2 Menu" +
                              "\n 3 Cancel order" +
                              "\n 4 Add item to basket" +
                              "\n 5 Get ingredients / Vegetarian?" +
                              "\n 9 Close app" +
                              "\n -----------------");
        orderloop:
        while(true){
            System.out.println("To remind about possible UI choices press 0.");
            System.out.println("Waiting for input...");
            int choise = scanner.nextInt();
            switch (choise){
                case 0:
                    System.out.println("UI:\n 1 Make an order" +
                            "\n 2 Menu" +
                            "\n 3 Cancel order" +
                            "\n 4 Add item to basket" +
                            "\n 5 Get ingredients / Vegetarian?" +
                            "\n 9 Close app" +
                            "\n -----------------");
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
                    System.out.println("Which item(id's) do you want to add to your basket?");
                    System.out.println("Waiting for input...");
                    addItemToBasket(scanner.nextInt());
                    break;
                case 5:
                    System.out.println("For which dish do you want to check ingredients?");
                    System.out.println("Waiting for input...");
                    printIngredients(scanner.nextInt());
                case 9:
                    closeApp();
                    break orderloop;
            }
        }
        System.out.println("See you next time!");
        closeApp();
    }

    private void addItemToBasket(int item_id) throws SQLException {
        String QUERY =  "SELECT item_id FROM  item " +
                        "WHERE item_id = (?) ";
        PreparedStatement st = connection.prepareStatement(QUERY);
        st.setString(1, String.valueOf(item_id));
        ResultSet itemName = st.executeQuery();
        System.out.println("1 " + itemName.getString(1) + "added.");

        basket.add(item_id);

    }

    private void cancelOrder() {
        //TODO: TAKE OUT THE ORDER FROM DATABASE
    }

    private void printIngredients(int recipe_id) throws SQLException {
        boolean vegetarian = true;
        String QUERY =  "SELECT ingredient_name, vegetarian FROM ingredients a " +
                        "JOIN recipe b " +
                        "ON a.ingredient_id = b.ingredient_id " +
                        "WHERE recipe_id = (?) ";
        PreparedStatement st = connection.prepareStatement(QUERY);
        st.setString(1, String.valueOf(recipe_id));
        ResultSet ingredients = st.executeQuery();
        System.out.println("INGREDIENTS:");
        while (ingredients.next()){
            System.out.println("-"+ingredients.getString(1));
            if(ingredients.getString(2)=="0") vegetarian = false;
        }
        if(vegetarian){
            System.out.println("This dish IS vegetarian.");
            return;
        }
        System.out.println("This dish IS NOT vegetarian.");
    }

    private void printMenu() throws SQLException {
        String QUERY = "SELECT item_id, name FROM item";
        System.out.println("MENU: ");
        Statement st = connection.createStatement();
        ResultSet menu = st.executeQuery(QUERY);
        while (menu.next()){
            System.out.print(menu.getString(1)+" "+menu.getString(2));
        }
        System.out.println("If you would like to know about ingredients of a dish, type 5.");
    }

    private void makeOrder() {
        if (basket.isEmpty()){
            System.out.println("Your basket is empty!");
            return;
        }
        System.out.println("Enter the post code: ");
        int postCode = scanner.nextInt();
        System.out.println("Enter the house number: ");
        int houseNumber = scanner.nextInt();

        //TODO: SEND ORDER TO DATABASE

    }

    private void closeApp() {
        System.exit(0);
    }
}
