package orderfileutility;


import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtility {
    private String generateOrderFileName(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        return "Orders_" + sdf.format(date) + ".txt";
    }

    private final String BASE_PATH = "OrderFiles/";

}