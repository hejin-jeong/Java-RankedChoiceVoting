import java.util.ArrayList;
import java.util.List;

/**
 * A Candidate represents a person who is running for office.  A Candidate has 
 * a name, and can be eliminated from the election following the rules of 
 * ranked choice voting.
 */
public class Candidate {
    // The candidate's name
    private final String name;
    
    // Whether the candidate is still in the election
    private boolean eliminated = false;

    // The ballots on which this candidate has the highest rank.
    // If a candidate is eliminated, this list should be empty.
    private List<Ballot> votes = new ArrayList<>(); // Array that contains Ballot objects that has this candidate
    // as the first choice

    /**
     * Create a new candidate
     * @param name the candidate's name
     */
    public Candidate(String name) {
        this.name = name;
    }

    /**
     * @return the candidate's name
     */
    public String getName() {
        return name;
    }

    /**
     * Add a vote for this candidate. Add first choice ballot to the ballot object array.
     * @param newBallot a ballot that has this candidate as its top choice
     */
    public void addBallot(Ballot newBallot) {
        votes.add(newBallot);
    }

    /**
     * @return the number of ballots for which this candidate is the top choice
     */
    public int getVotes() {
        return votes.size();
    }
    
    /**
     * Eliminate this candidate from the election.
     * @return the ballots for which this candidate was the top choice.
     */
    public List<Ballot> eliminate() {
        List<Ballot> returnValue = votes;
        votes = new ArrayList<>(); // What does this code do? Get rids of all the ballots in the array.
        eliminated = true;
        return returnValue;
    }
    
    /**
     * @return true if this candidate has been eliminated from the election.
     */
    public boolean isEliminated() {
        return eliminated;
    }
}
