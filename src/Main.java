import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * Created by Tadas Karmanovas 2019-11-05
 */

public class Main {
    public static final String SAMPLE_CSV = "sample.csv";
    final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        String row;
        List<Product> productList = new ArrayList<>();

        getProductData(productList);

        Collections.sort(productList, new Comparator<Product>() {
            @Override
            public int compare(Product u1, Product u2) {
                return u1.getProductName().compareTo(u2.getProductName());
            }
        });

        while (true) {
            System.out.println("Choose one of the following by typing in the number");
            System.out.println("1. Display products that are under certain quantity");
            System.out.println("2. Display products that have experied or will be expiring under a certain date");
            System.out.println("3. Exit");
            Integer menuInput = parseValue(getInput());
            switch (menuInput) {
                case 1:
                    displayByQuantity(productList);
                    break;
                case 2:
                    displayByExpirationDate(productList);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Wrong input");
                    break;
            }
        }
    }

    private static void getProductData(List<Product> productList) {
        BufferedReader csvReader;
        String row;
        try {
            csvReader = new BufferedReader(new FileReader(SAMPLE_CSV));
            while ((row = csvReader.readLine()) != null) {
                Integer quantity;
                boolean duplicate = false;
                String[] data = row.split(",");
                if (data.length != 4) {
                    System.out.println("Error loading the file");
                    return;
                }
                if(data[0].equals("Item Name")){
                    continue;
                }
                quantity = parseValue(data[2]);
                addProduct(productList, quantity, duplicate, data);
            }
            csvReader.close();
            System.out.println("Product data has been loaded succesfully");
        } catch (IOException  | ParseException e){
            e.printStackTrace();
        }
    }

    private static void addProduct(List<Product> productList, Integer quantity, boolean duplicate, String[] data) throws ParseException {
        if (productList.isEmpty()) {
            productList.add(new Product(data[0], data[1], quantity, data[3]));
            return;
        }
        for (Product product : productList) {
            if (product.getProductName().equals(data[0]) && product.getCode().equals(data[1]) &&
                    product.getExpirationDate().equals(formatter.parse(data[3]))) {
                product.addQuantity(quantity);
                duplicate = true;
                break;
            }
        }
        if (!duplicate)
            productList.add(new Product(data[0], data[1], quantity, data[3]));
        return;
    }

    private static Integer parseValue(String input) {
        Integer result = null;
        try {
            result = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Wrong input");
        }
        return result;
    }

    private static void displayByExpirationDate(List<Product> productList) {
        System.out.println("Type in the expiration date you want in YYYY-MM-DD format");
        Date wantedDate;
        try {
            wantedDate = formatter.parse(getInput());
            for (Product product : productList) {
                if (product.getExpirationDate().before(wantedDate))
                    product.printProductData();
            }
        } catch (ParseException e) {
            System.out.println("Wrong input");
            return;
        }
    }

    private static void displayByQuantity(List<Product> productList) {
        System.out.println("Type in the quantity");
        Integer wantedQuantity = parseValue(getInput());
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (wantedQuantity > productList.get(i).getQuantity())
                indexes.add(i);
        }
        if (indexes.isEmpty()) {
            System.out.println("There are no products below this quantity");
        } else {
            for (Integer counter : indexes) {
                productList.get(counter).printProductData();
            }
        }
        return;
    }

    public static String getInput() {
        String input = null;
        try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            input = bufferRead.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
}
