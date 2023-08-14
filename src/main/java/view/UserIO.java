package view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * UserIO class handles input and output operations related to the user interface.
 * This class provides methods to read various data types from the user and to display messages to the user.
 */
public class UserIO {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads an integer input from the user.
     * @param prompt The message or question to be displayed to the user.
     * @return The captured integer input from the user.
     */
    public int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Reads a string input from the user.
     * @param prompt The message or question to be displayed to the user.
     * @return The captured string input from the user.
     */
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Reads a BigDecimal input from the user.
     * @param prompt The message or question to be displayed to the user.
     * @return The captured BigDecimal input from the user.
     */
    public BigDecimal readBigDecimal(String prompt) {
        System.out.print(prompt);
        return new BigDecimal(scanner.nextLine());
    }

    /**
     * Reads a LocalDate input from the user in the format MM-dd-yyyy.
     * @param prompt The message or question to be displayed to the user.
     * @return The captured LocalDate input from the user.
     */
    public LocalDate readLocalDate(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return LocalDate.parse(input, formatter);
    }

    /**
     * Prints a message to the console.
     * @param message The message to be displayed to the user.
     */
    public void printMessage(String message) {
        System.out.println(message);
    }
}
