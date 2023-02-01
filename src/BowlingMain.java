/**
 * 
 * BowlingMain Class
 * 
 * This is the main class for the Bowling program.
 * BowlingMain will create a scanner object
 * and a new userScoreCard object,
 * and pass the information to create a new UserInterface object.
 * 
 * The UserInterface will trigger and all user interaction will remain within that class. 
 * When user interaction is complete, BowlingMain will close the scanner object
 * and end the program.
 * 
 * @author Brandon Jenkins
 * @since 10/20/2022
 *
 */
import java.util.Scanner;

public class BowlingMain {

	public static void main(String[] args) {
		
		// Scanner for user input
		Scanner scanner = new Scanner(System.in);
		
		// Create a new scorecard
		ScoreCard userScoreCard = new ScoreCard();
		
		// Create a user interface for the new scorecard & user input
		UserInterface userUI = new UserInterface(scanner, userScoreCard);
		
		// User interaction begins and ends here
		userUI.start();
		
		scanner.close();
	}

}
