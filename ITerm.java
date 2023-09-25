import java.util.Comparator;

/**
 * @author ericfouh
 */
public interface ITerm
    extends Comparable<ITerm> {

    public static final int ALPHABET_SIZE = 26;
    /**
     * Compares the two terms in descending order by weight.
     * 
     * @return comparator Object
     */
    public static Comparator<ITerm> byReverseWeightOrder() {
        return new Comparator<ITerm>() {
            public int compare(ITerm a, ITerm b) {
                return (int)(b.getWeight() - a.getWeight());
            }
        };
    }


    /**
     * Compares the two terms in lexicographic order but using only the first r
     * characters of each query.
     * 
     * @param r
     * @return comparator Object
     */
    public static Comparator<ITerm> byPrefixOrder(int r) {
        return new Comparator<ITerm>() {
            public int compare(ITerm a, ITerm b) {

                if (r < 0) {
                    throw new IllegalArgumentException();
                }

                String termA = a.getTerm();
                String termB = b.getTerm();

                int lenA = termA.length();
                int lenB = termB.length();

                int min = Math.min(Math.min(lenA, lenB), r);

                String subStringA = termA.substring(0, min);
                String subStringB = termB.substring(0, min);

                return subStringA.compareTo(subStringB);
            }
        };
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(ITerm that);


    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString();

    // Required getters.
    public long getWeight();
    public String getTerm();

    // Required setters (mostly for autograding purposes)
    public void setWeight(long weight);
    public String setTerm(String term);

}
