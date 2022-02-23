import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * The ranked choice voting program implements the ranked choice voting 
 * algorithm.  This class reads election data from a file, constructs an 
 * election using that data, and reports the result of the election.
 * 
 * The input file should be formatted as follows:
 * 
 * 1. The file will begin with a line containing a number.  The number 
 * indicates how many candidates are in the election.
 * 2. This will be followed by a sequence of lines, where each line holds the 
 * name of a single candidate.  The order that the candidates are entered
 * is the same as the order that they appear on the ballots.
 * 3. Following that will be the ballots.  If there are n candidates, each 
 * ballot contains a permutation of the numbers from 1 to n.  The first number
 * on the ballot is the rank of the first candidate, the second number on the
 * ballot is the rank of the second candidate, etc.  A correctly formulated 
 * ballot line will have n integers, where n is the number of candidates.
 * Those integers will a permutation of the numbers 1 to n.  There can be
 * any number of ballots in the file. 
 */
public class RankedChoiceVoting {
    /**
     * Reads the election data from the file, runs the ranked choice voting
     * algorithm and reports the result on standard output.  If there is a
     * winner, that winner is announced.  If there is a tie, it announces
     * who is tied.
     * @param args There should be one argument, which is the name of the file 
     * containing the election data.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println ("Please provide the name of the file " +
                                "containing the election data on the " +
                                "command line.");
            return;
        }
        
        Election election = initializeElection(args[0]);
        if (election != null) {
            announceWinner(election);
        }
    }
    
    /**
     * Creates an election and initializes it based on the data in the election file.
     * If the file does not exist, is not correctly formatted, or any ballot is
     * incorrectly formatted, an error message is displayed and the election is not 
     * created.
     * @param filename the name of the file containing the election data
     * @return the initialized election object, or null if there were any problems
     * while reading the election data.
     */
    private static Election initializeElection(String filename) {
        String ballotLine = "";
        Election election = null;
        try {
            Scanner in = new Scanner (new File (filename));
            
            // Read in # of candidates
            int numCandidates = in.nextInt();
            in.nextLine();
            election = new Election (numCandidates); // Election object
            
            // Read in the candidates
            for (int i = 0; i < numCandidates; i++) {
                String name = in.nextLine();
                election.addCandidate(name); // add candidates to the candidates array
            }
            
            // Read in the ballots and assign to the candidates
            while (in.hasNext()) {
                ballotLine = in.nextLine();
                if (ballotLine.equals("")) {
                    break;
                }
                String[] rankStrings = ballotLine.split(" ");
                int[] ranks = new int[rankStrings.length]; // create ballot array with integer ranks
                for (int i =0; i < rankStrings.length; i++) {
                    ranks[i] = Integer.parseInt(rankStrings[i]);
                }
                election.addBallot(ranks);
            }
            in.close(); // has to close the scanned file after using it?
            
        } catch (InputMismatchException e) {
            System.out.println("The file should start with the number of candidates.");
        } catch (NumberFormatException e) {
            System.out.println ("Could not parse a ballot: " + ballotLine);
            election = null;
        } catch (FileNotFoundException e) {
            System.out.println ("Election file " + filename + " was not found.");
        }
        
        return election;
    }

    /**
     * Apply the ranked choice voting algorithm and display the results on the 
     * screen.
     * @param election an initialized election object
     */
    private static void announceWinner(Election election) {
        List<String> winners = election.selectWinner();
        if (winners.size() == 1) {
            System.out.println ("Winner is " + winners.get(0));
        }
        else {
            System.out.println ("Tie!  Remaining candidates are:");
            for (String name : winners) {
                System.out.println ("   " + name);
            }
        }
    }
}
