import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {
    private String productName;
    private String code;
    private int quantity;
    private Date expirationDate;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public Product(String productName, String code, int quantity, String expirationDate) {
        try {
            this.expirationDate = formatter.parse(expirationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.productName = productName;
        this.code = code;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getCode() {
        return code;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void printProductData() {
        System.out.format("%-12s%-19s%8d %15s %n",
                productName, code, quantity, formatter.format(expirationDate));
        return;
    }
}
