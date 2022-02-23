import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An Election consists of the candidates running for office, the ballots that
 * have been cast, and the total number of voters. This class implements the
 * ranked choice voting algorithm.
 * 
 * Ranked choice voting uses this process:
 * 
 * 1. Rather than vote for a single candidate, a voter ranks all the candidates.
 * For example, if 3 candidates are running on the ballot, a voter identifies
 * their first choice, second choice, and third choice. 2. The first-choice
 * votes are tallied. If any candidate receives > 50% of the votes, that
 * candidate wins. 3. If no candidate wins &gt; 50% of the votes, the
 * candidate(s) with the lowest number of votes is(are) eliminated. For each
 * ballot in which an eliminated candidate is the first choice, the 2nd ranked
 * candidate is now the top choice for that ballot. 4. Steps 2 & 3 are repeated
 * until a candidate wins, or all remaining candidates have exactly the same
 * number of votes. In the case of a tie, there would be a separate election
 * involving just the tied candidates.
 */
public class Election {
	// All candidates that were in the election initially. If a candidate is
	// eliminated, they will still stay in this array.
	private final Candidate[] candidates;

	// The next slot in the candidates array to fill.
	private int nextCandidate;

	/**
	 * Create a new Election object. Initially, there are no candidates or votes.
	 * 
	 * @param numCandidates the number of candidates in the election.
	 */
	public Election(int numCandidates) {
		this.candidates = new Candidate[numCandidates];
	}

	/**
	 * Adds a candidate to the election
	 * 
	 * @param name the candidate's name
	 */
	public void addCandidate(String name) {
		candidates[nextCandidate] = new Candidate(name);
		nextCandidate++;
	}

	/**
	 * Adds a completed ballot to the election.
	 * 
	 * @param ranks A correctly formulated ballot will have exactly 1 entry with a
	 *              rank of 1, exactly one entry with a rank of 2, etc. If there are
	 *              n candidates on the ballot, the values in the rank array passed
	 *              to the constructor will be some permutation of the numbers 1 to
	 *              n.
	 * @throws IllegalArgumentException if the ballot is not valid.
	 */
	public void addBallot(int[] ranks) {
		if (!isBallotValid(ranks)) {
			throw new IllegalArgumentException("Invalid ballot");
		}
		Ballot newBallot = new Ballot(ranks);
		assignBallotToCandidate(newBallot);
	}

	/**
	 * Checks that the ballot is the right length and contains a permutation of the
	 * numbers 1 to n, where n is the number of candidates.
	 * 
	 * @param ranks the ballot to check
	 * @return true if the ballot is valid.
	 */
	private boolean isBallotValid(int[] ranks) {
		if (ranks.length != candidates.length) {
			return false;
		}
		int[] sortedRanks = Arrays.copyOf(ranks, ranks.length);
		Arrays.sort(sortedRanks);
		for (int i = 0; i < sortedRanks.length; i++) {
			if (sortedRanks[i] != i + 1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines which candidate is the top choice on the ballot and gives the
	 * ballot to that candidate.
	 * 
	 * @param newBallot a ballot that is not currently assigned to a candidate
	 */
	private void assignBallotToCandidate(Ballot newBallot) {
		while (true) {
			int candidate = newBallot.getTopCandidate();
			if (candidates[candidate].isEliminated()) {
				newBallot.eliminateCandidate(candidate);
			} else {
				candidates[candidate].addBallot(newBallot);
				return;
			}

		}
	}

	/**
	 * Calculate the total number of ballots in the election
	 * 
	 * @return the total number of ballots
	 */
	public int getTotalBallotCount() {
		int totalBallotsNum = 0;
		for (int i = 0; i < candidates.length; i++) {
			totalBallotsNum += candidates[i].getVotes();
		}

		return totalBallotsNum;
	}

	/**
	 * Apply the ranked choice voting algorithm to identify the winner.
	 * 
	 * @return If there is a winner, this method returns a list containing just the
	 *         winner's name is returned. If there is a tie, this method returns a
	 *         list containing the names of the tied candidates.
	 */
	public List<String> selectWinner() {

		// The name of the winners will be stored in this list
		List<String> winners = new ArrayList<>();

		// Total number of ballots
		int totalBallotCount = getTotalBallotCount();

		while (true) {

			// The boolean value indicating whether there is a tie or not.
			boolean isTie = true;

			// The candidates who are not eliminated will be stored in this arraylist.
			ArrayList<Candidate> nonEliminatedCandidates = new ArrayList<Candidate>();

			// Top-choice ballots array of the candidate with the lowest number of
			// top-choice votes will be stored in this arraylist.
			ArrayList<List<Ballot>> eliminatedCandidateBallots = new ArrayList<List<Ballot>>();

			// Add not-eliminated candidates who have at least one top-choice ballot to the
			// arraylist.
			for (int i = 0; i < candidates.length; i++) {
				if (!candidates[i].isEliminated() && candidates[i].getVotes() > 0) {
					nonEliminatedCandidates.add(candidates[i]);
				}
			}

			/*
			 * Check whether not-eliminated candidates have the same number of top-choice
			 * votes. If they are tie, then maintain the boolean as true, but if not, change
			 * the boolean to false and come out of the while loop.
			 */
			int i = 1;
			while (isTie && i < nonEliminatedCandidates.size()) {
				if (nonEliminatedCandidates.get(i).getVotes() != nonEliminatedCandidates.get(0).getVotes()) {
					isTie = false;
					break;
				}
				i++;
			}

			// If there is a tie, add not-eliminated candidates to the winners list and
			// return the list.
			if (isTie) {
				for (int j = 0; j < nonEliminatedCandidates.size(); j++) {
					winners.add(nonEliminatedCandidates.get(j).getName());
				}
				return winners;
			}
			// If not a tie, find a candidate who has received more than 50% of the
			// top-choice votes.
			else {
				// If there is one, then add the candidate's name to the winners list and return
				// the list.
				for (int j = 0; j < nonEliminatedCandidates.size(); j++) {
					if (nonEliminatedCandidates.get(j).getVotes() > totalBallotCount / 2) {
						winners.add(nonEliminatedCandidates.get(j).getName());
						return winners;
					}
				}

				// If there isn't any, find the candidate(s) with the lowest number of
				// top-choice votes.

				// Find the lowest number of top-choice votes
				int minVotes = totalBallotCount;
				for (int j = 0; j < nonEliminatedCandidates.size(); j++) {
					if (nonEliminatedCandidates.get(j).getVotes() < minVotes) {
						minVotes = nonEliminatedCandidates.get(j).getVotes();
					}
				}

				// Add top-choice ballots array of the candidate with the lowest number of
				// top-choice votes to the arraylist.
				for (int j = 0; j < nonEliminatedCandidates.size(); j++) {
					if (nonEliminatedCandidates.get(j).getVotes() == minVotes) {
						eliminatedCandidateBallots.add(nonEliminatedCandidates.get(j).eliminate());
					}
				}

				// Reassign the eliminated candidates' top-choice ballots to the next top-choice
				// candidate's.
				for (int k = 0; k < eliminatedCandidateBallots.size(); k++) {
					for (int m = 0; m < eliminatedCandidateBallots.get(k).size(); m++) {
						assignBallotToCandidate(eliminatedCandidateBallots.get(k).get(m));
					}
				}
			} // else
		} // while loop
	} // method
} // class