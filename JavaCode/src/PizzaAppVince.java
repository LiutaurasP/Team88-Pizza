import javax.print.DocFlavor;
import java.lang.Math;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PizzaAppVince {
    Scanner scanner = new Scanner(System.in);
    boolean ordered = false;
    Connection connection;
    ArrayList basket;
    PizzaAppVince() throws SQLException {
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
        String db_URL = "jdbc:mysql://localhost/pizzeria";
        String user = "root";
        String pass = "qGan94+60";
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
                    break;
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
        itemName.next();
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


    private void updatePizzaQuantity(int customerID) throws SQLException {
        String QUERY = "SELECT pizzas_ordered FROM customer WHERE customer_id = (?)";
        String CUSTOMERINSERT = "INSERT INTO promocode ('customer_id') VALUES (?)";
        String CODEINSERT = "INSERT INTO code ('code') VALUES (?) WHERE customer_id = (?)";
        PreparedStatement st = connection.prepareStatement(QUERY);
        PreparedStatement insertCustomer = connection.prepareStatement(CUSTOMERINSERT);
        PreparedStatement insertCode = connection.prepareStatement(CODEINSERT);
        ResultSet pizzasOrdered = st.executeQuery(QUERY);
        insertCustomer.setString(1,String.valueOf(customerID));

        int noOfPizzas = ((Number) pizzasOrdered.getObject(1)).intValue();
        if (noOfPizzas >= 10) {
            float promoCode = Math.round(Math.random()*10000);
            insertCode.setString(1,String.valueOf(promoCode));
            insertCode.setString(2,String.valueOf(customerID));
            insertCustomer.executeQuery(CUSTOMERINSERT);
            insertCode.executeQuery(CODEINSERT);
            System.out.println("You get a promo code! Your code is: " + promoCode);
            System.out.println("Your customer_id is: " + customerID);

        }
    }

    private void makeOrder() throws SQLException {
        int customerID;
        String getID = "SELECT customer_id FROM customer ORDER BY customer_id DESC LIMIT 1";
        String INSERTCUSTOMER = "INSERT INTO customer (zipcode, adress, name, phone_number) VALUES (?,?,?,?)";
        String DELETECODE = "DELETE FROM promocode WHERE (customer_id = ?)";
        PreparedStatement insertCustomer = connection.prepareStatement(INSERTCUSTOMER);
        PreparedStatement stId = connection.prepareStatement(getID);
        PreparedStatement deleteCode = connection.prepareStatement(DELETECODE);

        if (basket.isEmpty()){
            System.out.println("Your basket is empty!");
            return;
        }
        System.out.println("Do you have already have a customer ID? 0 for no, 1 for yes");
        if(scanner.nextInt()==1){
            System.out.println("Please enter your id");
            customerID = scanner.nextInt();
        }
        else {
            System.out.println("Enter the post code: ");
            int postCode = scanner.nextInt();
            insertCustomer.setInt(1,postCode);
            System.out.println("Enter the adress: ");
            String adress = scanner.next();
            insertCustomer.setString(2, adress);
            System.out.println("Enter the name: ");
            String name = scanner.next();
            insertCustomer.setString(3, name);
            System.out.println("Enter the phone number: ");
            int phoneNumber = scanner.nextInt();
            insertCustomer.setInt(4, phoneNumber);
            insertCustomer.execute();
            ResultSet topId = stId.executeQuery(getID);
            topId.next();
            customerID = topId.getInt(1);
            System.out.println("Your customer ID is: " + topId.getString(1));
        }

        int calculateBasket = 4; // dont need
        double toPay = calculateBasket;
        updatePizzaQuantity(customerID);

        System.out.println("Do have a promo code? (1 for yes, 0 for no)");
        if(scanner.nextInt()==1){
            System.out.println("Please type in your code");
            String userCode = scanner.nextLine();
            String getCode = "SELECT code FROM promocode WHERE customer_id = (?)";
            PreparedStatement GETCODE = connection.prepareStatement(getCode);
            GETCODE.setString(1,String.valueOf(customerID));
            String code = String.valueOf(GETCODE.executeQuery());
            if(userCode.equals(code)){
                System.out.println("You get 10% off!");
                toPay = calculateBasket * 0.9;
                deleteCode.setString(1,String.valueOf(customerID));
                deleteCode.executeQuery();

            }
            else{
                System.out.println("Your code is invalid");
            }
        }

        System.out.println("Your order will be: " + toPay);

        //INSERT INTO `pizzeria`.`customer` (`name`, `adress`) VALUES ('John Doe', '2484');
        //

        //TODO: SEND ORDER TO DATABASE

    }

    private void closeApp() {
        System.exit(0);
    }
}