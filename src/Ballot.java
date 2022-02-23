/**
 * A Ballot contains the ranking that a single voter gave to the candidates on
 * the ballot.  The position in the ballot identifies the candidate.  The value
 * at a position indicates the rank that this voter has placed this candidate 
 * in.
 * 
 * Once created, we can find out who the top candidate is on the ballot, or
 * eliminate a candidate from the ballot.
 */
public class Ballot {
    // The rank of the candidates on the ballot.
    private final int[] ranks;
    
    /**
     * Creates a ballot.  A correctly formulated ballot will have exactly 1 
     * entry with a rank of 1, exactly one entry with a rank of 2, etc.  If 
     * there are n candidates on the ballot, the values in the rank array 
     * passed to the constructor will be some permutation of the numbers 1 to 
     * n.
     *
     * @param ranks the ranks for the ballot.
     */
    public Ballot (int[] ranks) {
        this.ranks = ranks;
    }
    
    /**
     * @return the position of the candidate who has the top rank
     * on this ballot.
     */
    public int getTopCandidate() {
        int topRank = ranks.length + 1;
        int topIndex = -1;
        
        // Find the candidate with the lowest rank.
        for (int i = 0; i < ranks.length; i++) {
            if (ranks[i] < topRank) {
                topRank = ranks[i];
                topIndex = i;
            }
        }
        return topIndex;
    }

    /**
     * Eliminates a candidate from this ballot.
     * @param candidateId the id of the candidate to eliminate
     */
    public void eliminateCandidate(int candidateId) {
        ranks[candidateId] = ranks.length + 1;
    }
}
