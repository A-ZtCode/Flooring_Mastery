package view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UserIO {

    private final Scanner scanner = new Scanner(System.in);

    // Read an integer input from the user
    public int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

    // Read a string input from the user
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Read a BigDecimal input from the user
    public BigDecimal readBigDecimal(String prompt) {
        System.out.print(prompt);
        return new BigDecimal(scanner.nextLine());
    }

    // Read a LocalDate input from the user (format: MM-dd-yyyy)
    public LocalDate readLocalDate(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return LocalDate.parse(input, formatter);
    }

    // Print a message to the console
    public void printMessage(String message) {
        System.out.println(message);
    }
}
