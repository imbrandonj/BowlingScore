import java.util.Scanner;
/**
 * The UserInterface class provides the interface for
 * user interaction within the BowlingMain program.
 * 
 * The UserInterface's purpose is to provide information to the user
 * for the user to provide the program with appropriate input
 * that will be relayed to the ScoreCard class. 
 * 
 * The ScoreCard class will take this information,
 * perform the appropriate actions, and return the information
 * that should be displayed within the UserInterface class.
 * 
 * @author Brandon Jenkins
 * @since 10/20/2022
 *
 */
public class UserInterface {

	private Scanner input;
	private ScoreCard userScoreCard;
	
	
	public UserInterface(Scanner scanner, ScoreCard scorecard) {
		this.input = scanner;
		this.userScoreCard = scorecard;
	}
	
	/**
	 * UserInterface.start() will trigger user interaction, and therefore,
	 * is the start of the program. All input gathered from the user will occur
	 * within this method. All information passed to and from the ScoreCard class
	 * will also occur within this method. This method uses multiple loops for 
	 * user input, as well as secondary validations for correct user input.
	 * All user interaction begins and ends here.
	 */
	public void start() {
		
		// Program explanation
		System.out.println("Welcome to Bowling Scorecard.\n");
		System.out.println("How it works:");
		System.out.println("This program will score your bowling game, "
				+ "as well as keep track of a few statistics.");
		System.out.println("For each frame, you will be prompted for the score of each ball rolled.");
		System.out.println("You'll receive a running total and at the end you'll get the final scorecard displayed.");
		System.out.println("\nLet's begin.\n");
		
		// Begin user input
		int frame = 1;
		while ( frame <= 10 ) {
			int roll1 = -1;
			int roll2 = -1;
			int roll3 = -1;
			String inputRollOne;
			String inputRollTwo;
			String inputRollThree;
			
			
			// Roll one
			while ( roll1 < 0 ) {
				System.out.println("\nEnter score for frame " + frame + " roll 1: ");
				inputRollOne = input.nextLine();
				
				// Convert X to strike pins
				if ( inputRollOne.equals("X") )
					roll1 = 10;
				
				// Convert user input string to an integer
				else
					roll1 = parseToInt(inputRollOne);
				
				// Invalid input will be parsed to -1
				if ( roll1 < 0 )
					continue;
				
				// Set roll one
				userScoreCard.setFirstRoll(roll1, frame);
			}
			
			// Roll two
			if ( (roll1 != 10)  || (frame == 10 && roll1 == 10) ) {
				
				while ( roll2 < 0 ) {
					System.out.println("Enter score for frame " + frame + " roll 2: ");
					inputRollTwo = input.nextLine();
					
					// Convert "/" to spare pins
					// Invalid input if frame 10 roll one is a strike
					if ( inputRollTwo.equals("/") && !( frame == 10 && roll1 == 10 ) ) 
						roll2 = 10 - roll1;
					
					// Strike is valid input on frame 10 if roll one is a strike
					else if ( inputRollTwo.equals("X") && (frame == 10 && roll1 == 10 ) )
						roll2 = 10;
					
					// Convert user input string to an integer
					else 
						roll2 = parseToInt(inputRollTwo);
					
					// Invalid input will be parsed to -1
					if ( roll2 < 0 ) {
						continue;
					}
					
					// Too many pins selected for roll two
					else if ( roll1 + roll2 > 10 && !( frame == 10 && roll1 == 10 ) ) {
						roll2 = -1;
						System.out.println("\nToo many pins selected. Try again.\n");
					}
					
					// Set roll two
					userScoreCard.setSecondRoll(roll2, frame);
				}
			}
			
			// Calculate scoring
			userScoreCard.score(frame);
			
			// 10th frame, third roll
			if ( frame == 10 && (roll1 + roll2 >= 10) ) {
				
				while ( roll3 < 0 ) {
					System.out.println("Enter score for frame " + frame + " roll 3: ");
					
					inputRollThree = input.nextLine();
					
					// Convert "X" to strike pins only if it is valid input
					if ( ( (roll1 == 10 && roll2 == 10) || (roll1 != 10 && roll1 + roll2 == 10) )
							&& inputRollThree.equals("X") )
						roll3 = 10;
					
					else if ( inputRollThree.equals("/") && roll2 != 10 )
						roll3 = 10 - roll2;
					
					// Convert user input string to an integer
					else
						roll3 = parseToInt(inputRollThree);
					
					// Invalid input will be parsed to -1
					if ( roll3 < 0 )
						continue;
					
					// Too many pins selected
					
					// Roll one strike, roll two open
					else if ( roll1 == 10 && roll2 != 10 && roll2 + roll3 > 10 ) {
						roll3 = -1;
						System.out.println("\nToo many pins selected. Try again.\n");
					}
	
					// Set roll three
					userScoreCard.thirdRollEvent(roll3);
				}
			}
			
			// Display the current score
			userScoreCard.displayScore();
			
			frame++;

		}
		
		userScoreCard.displayEndStats();
		System.out.println("\n     ------------");
		System.out.println("*** END OF PROGRAM ***");
		
	}
	// end start()
	
	
	/**
	 * This method takes a string and converts it to an integer.
	 * There is a secondary validation in which 
	 * the pins to be parsed should be within range 0 and 10. 
	 * Of course, invalid strings will not be appropriately parsed. 
	 * Invalid input will return -1
	 * 
	 * @param userInput
	 * @return intParsed
	 */
	public int parseToInt(String userInput) {
		int intParsed;
		
		try {
			 intParsed = Integer.parseInt(userInput);
			 
			 if ( intParsed > 10 || intParsed < 0 )
					throw new NumberFormatException();
			
			// Pins knocked
			 return intParsed;
		}
		
		catch( NumberFormatException nfe ) {
			System.out.println("\nPlease enter a valid number.\n");
			return -1;
		}
		
		catch ( Exception e ) {
			System.out.println("\nPlease enter a valid number.\n");
			return -1;
		}
	}
}
