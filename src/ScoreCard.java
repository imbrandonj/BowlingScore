/**
 * The ScoreCard class serves the purpose of scoring a bowling game. 
 * Appropriate information will be obtained through user input in the
 * UserInterface class. The input will be used within ScoreCard to present
 * a display of the ScoreCard object's:
 * running total, score by frame, pins knocked for each roll,
 * and information at the end such as open frames, average first roll, and strikes.
 * 
 * If need be, the class can be enlarged to incorporate multiple games
 * to create game series and to track information about the entire series.
 * 
 * The separation between UserInterface and this class provides this ability.
 * 
 * 
 * @author Brandon Jenkins
 * @since 10/20/2022
 *
 */
public class ScoreCard {
	
	private int[] firstRolls = new int[11];  // array for all the first rolls
	private int[] secondRolls = new int[11];  // array for all the second rolls
	private int[] frameScore = new int[11];  // array containing a score for each frame
	private int[] runningTotal = new int[11];  // array containing the running total for each frame
	private int roll3 = -1;  // frame 10 roll 3 null
	
	// Arrays for tracking strikes and spares
	private boolean[] wasStrike = new boolean[11];
	private boolean[] wasSpare = new boolean[11];
	
	// End game stats to be displayed
	private int total;
	private int openFrames = 0;
	private int strikes = 0;
	
	
	public ScoreCard() {
		
		// The construction of a new scorecard
		// Sets all values to -1
		// -1 represents a frame not bowled.
		for ( int i = 0; i <= 10; i++ ) {
			firstRolls[i] = -1;
			secondRolls[i] = -1;
			frameScore[i] = -1;
			runningTotal[i] = -1;
			
			// Strike & spare counters
			wasStrike[i] = false;
			wasSpare[i] = false;
		}	
	}
	
	/**
	 * Sets the first roll of the provided frame.
	 * If the roll is a strike, increment the strike counter.
	 * @param roll
	 * @param frame
	 */
	public void setFirstRoll( int roll, int frame ) {
		this.firstRolls[frame] = roll;
		if ( roll == 10 )
			this.strikes++;
	}
	
	/**
	 * Sets the second roll of the provided frame.
	 * If the roll is ten pins of the tenth frame, increment the strike counter.
	 * @param roll
	 * @param frame
	 */
	public void setSecondRoll ( int roll, int frame ) {
		this.secondRolls[frame] = roll;
		
		// 10th frame second ball strike
		if ( frame == 10 && firstRolls[10] == 10 && roll == 10 )
			this.strikes++;
	}
	
	/**
	 * The bulk logic of scoring.
	 * For each frame the score card is written.<br><br>
	 * This method provides the setting of multiple arrays: 
	 * frameScore, runningTotal, wasStrike, wasSpare.<br><br>
	 * It is important to note that since a bowling game is dynamically changing,
	 * a score two frames prior can be changed by the result of a current frame,
	 * depending on strikes or spares. Thus, many conditions must be checked.
	 * @param frame
	 */
	public void score( int frame ) {
		
		// Strike result
		if ( firstRolls[frame] == 10 || ( firstRolls[10] == 10 && secondRolls[10] == 10 ) ) {
			
			// If two frames ago and last frame there was a strike
			// (The only way a strike can apply to two frames ago)
			if ( (frame > 2) && (wasStrike[frame - 2]) && (wasStrike[frame - 1]) ) {
				frameScore[frame - 2] += 10;
				runningTotal[frame - 2] += 10;
			}
			
			// If one frame ago there was a strike or spare
			if ( (frame > 1) && ((wasStrike[frame - 1]) || (wasSpare[frame - 1])) ) {
				frameScore[frame - 1] += 10;
				runningTotal[frame - 1] += 10;
			}
			
			// Apply roll two to a strike after frame ten first roll strike result
			if ( wasStrike[frame - 1] && firstRolls[10] == 10 ) {
				frameScore[frame - 1] += secondRolls[10];
				runningTotal[frame - 1] += secondRolls[10];
			}
			
			// Set current frame
			frameScore[frame] = 10;
			wasStrike[frame] = true;
			
			// Frame ten scoring
			if ( frame == 10 )
				frameScore[frame] += secondRolls[10];
		}
		
		// Spare result
		else if ( firstRolls[frame] + secondRolls[frame] == 10 ) {
			
			// If two frames ago and last frame there was a strike
			// (The only way a ball can apply to two frames ago)
			// Apply only the first roll
			if ( (frame > 2) && (wasStrike[frame - 2]) && (wasStrike[frame - 1]) ) {
				frameScore[frame - 2] += firstRolls[frame];
				runningTotal[frame - 2] += firstRolls[frame];
			}
			
			// If one frame ago there was a strike
			if ( (frame > 1) && (wasStrike[frame - 1]) ) {
				frameScore[frame - 1] += 10;
				runningTotal[frame - 1] += 10;
			}
			
			// If one frame ago there was a spare
			// Apply only the first roll
			if ( (frame > 1) && (wasSpare[frame - 1]) ) {
				frameScore[frame - 1] += firstRolls[frame];
				runningTotal[frame - 1] += firstRolls[frame];
			}
			
			// Set current frame
			frameScore[frame] = 10;
			wasSpare[frame] = true;
		}
		
		// Open frame result
		else {
			
			// If two frames ago and last frame there was a strike
			// (The only way a ball can apply to two frames ago)
			// Apply only the first roll
			if ( (frame > 2) && (wasStrike[frame - 2]) && (wasStrike[frame - 1]) ) {
				frameScore[frame - 2] += firstRolls[frame];
				runningTotal[frame - 2] += firstRolls[frame];
			}
			
			// If one frame ago there was a strike
			// Apply both rolls
			if ( (frame > 1) && (wasStrike[frame - 1]) ) {
				frameScore[frame - 1] += firstRolls[frame] + secondRolls[frame];;
				runningTotal[frame - 1] += firstRolls[frame] + secondRolls[frame];;
			}
			
			// If one frame ago there was a spare
			// Apply only the first roll
			if ( (frame > 1) && (wasSpare[frame - 1]) ) {
				frameScore[frame - 1] += firstRolls[frame];
				runningTotal[frame - 1] += firstRolls[frame];
			}
			
			// Set current frame
			frameScore[frame] = firstRolls[frame] + secondRolls[frame];
			openFrames++;
		}
		
		// Set running total per frame, as multiple frame scores can change in an event of strike or spare
		for ( int i = 1; i <= frame; i++ ) {
			
			// Frame 1
			if ( i == 1 )
				runningTotal[i] = frameScore[i];
			
			// Standard frames
			else 
				runningTotal[i] = runningTotal[i-1] + frameScore[i];
		}
		
		this.total = runningTotal[frame];
	}
	
	/**
	 * Event for a third roll if triggered in the tenth frame.
	 * The third roll is added to the tenth frame runningTotal and frameScore.
	 * If roll three knocks all ten pins, increment strikes counter.
	 * @param roll3
	 */
	public void thirdRollEvent( int roll3 ) {
		
		this.roll3 = roll3;
		
		// Add roll three to the already set frame ten variables
		frameScore[10] += this.roll3;
		runningTotal[10] = runningTotal[9] + frameScore[10];
		this.total = runningTotal[10];
		
		if ( secondRolls[10] == 10 && this.roll3 == 10 )
			this.strikes++;
		
	}
	
	/**
	 * Simply tallies the sum of first balls and returns the average
	 * @return average
	 */
	public double averageFirstBall() {
		double sum = 0;
		
		// for loop must skip index 0 (there is no frame 0)
		for ( int i = 1; i <= 10; i++ ) 
			sum += firstRolls[i];
		
		// Count frame ten roll three as a first ball
		if ( this.roll3 > -1 ) {
			sum += this.roll3;
			sum /= 11;
			return Math.round(sum * 100.0) / 100.0;
		}
		
		sum /= 10;
		return Math.round(sum * 100.0) / 100.0;
	}
	
	/**
	 * The displayScore method is the print display.<br><br>
	 * 
	 * For each score iteration the displayScore method will be shown.
	 * This differs from the program's minimum requirements, in which 
	 * the sum of frame input is collected and displayed at end of program.
	 * Of course, this method can be used once or as many times as needed,
	 * thus, the display is dynamic in which it displays a changing score card,
	 * in whatever frame or phase of the bowling game.
	 */
	public void displayScore() {
		System.out.println("\n");
		
		// Frame
		System.out.println("Frame: \t\t \t1 \t2 \t3 \t4 \t5 \t6 \t7 \t8 \t9 \t10");
			
		// Frame Result
		System.out.print("\nResult: \t\t");
		for ( int i = 1; i <= 10; i++ ) {
			
			// Frames not yet bowled are not displayed
			if ( firstRolls[i] == -1 )
				continue;
			
			// Double strike in the tenth display
			if ( firstRolls[i] == 10 && secondRolls[i] == 10 )
				System.out.print("XX");
			
			// Standard strike display
			else if ( firstRolls[i] == 10)
				System.out.print("X");
			
			// Standard spare display
			else if ( firstRolls[i] + secondRolls[i] == 10 )
				System.out.print(firstRolls[i] + "/");
			
			// Standard open frame display
			else
				System.out.print(firstRolls[i] + "," + secondRolls[i]);
			
			// Tenth frame display and roll three
			if ( i == 10 && roll3 > -1 ) {
				
				// X displayed if followed by a tenth frame spare or two strikes
				if ( (firstRolls[i] != 10 && firstRolls[i] + secondRolls[i] == 10 ||
						firstRolls[i] == 10 && secondRolls[i] == 10) && roll3 == 10 )
					System.out.print("X");
				
				else if ( firstRolls[i] == 10 && secondRolls[i] != 10 && secondRolls[i] + roll3 == 10 )
					System.out.print("/");
				
				// Tenth frame, first roll strike, second roll open
				else if ( firstRolls[10] == 10 && secondRolls[10] != 10 )
					System.out.print(secondRolls[10] + roll3);
				
				else
					System.out.print(roll3);
			}
				
			System.out.print("\t");
		}
			
		// Frame Score
		System.out.println("\n");
		System.out.print("Frame Score: \t\t");
		for ( int frame: frameScore ) {
			if ( frame == -1 )
				continue;
			System.out.print(frame + "\t");
		}
			
		// Running Total
		System.out.println("\n");
		System.out.print("Running Total: \t\t");
		for ( int i = 1; i <= 10; i++ ) {
			if ( runningTotal[i] == -1 )
				continue;
			System.out.print(runningTotal[i] + "\t");
		}
		
		System.out.println("\n");
	}
	
	/**
	 * The end game stats to be shown at the end of program.
	 * Any enlargement of the ScoreCard class can incorporate 
	 * new member variables to be displayed here.
	 */
	public void displayEndStats() {
		System.out.println("\n\t==End Stats==");
		System.out.println("\tFinal Score: " + total);
		System.out.println("\tOpen frames: " + openFrames);
		System.out.println("\tStrikes: " + strikes);
		System.out.println("\tAverage first ball: " + averageFirstBall());
	}
}
