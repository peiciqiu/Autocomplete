
/**
 * @author Harry Smith
 */

public class Node {

    private Term term;
    private int words;
    private int prefixes;
    private Node[] references;

    /**
     * Initialize a Node with an empty string and 0 weight; useful for
     * writing tests.
     */
    public Node() {

        this.term = new Term("", 0);

        Node[] newReferences = new Node[ITerm.ALPHABET_SIZE];
        for (int i = 0; i < ITerm.ALPHABET_SIZE; i++) {
            newReferences[i] = null;
        }
        this.references = newReferences;

        // for empty string
        this.prefixes = 0;
        this.words = 0;
    }

    /**
     * Initialize a Node with the given query string and weight.
     * @throws IllegalArgumentException if query is null or if weight is negative.
     */
    public Node(String query, long weight) {
        if (query == null || weight < 0) {
            throw new IllegalArgumentException();
        }

        this.term = new Term(query, weight);
        Node[] newReferences = new Node[ITerm.ALPHABET_SIZE];
        for (int i = 0; i < ITerm.ALPHABET_SIZE; i++) {
            newReferences[i] = null;
        }
        this.references = newReferences;

        // for empty string
        this.prefixes = 0;
        this.words = 0;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public int getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(int prefixes) {
        this.prefixes = prefixes;
    }

    public Node[] getReferences() {
        return references;
    }


    public void setReferences(Node[] references) {
        this.references = references;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        Node node = (Node) o;
        return this.getTerm().equals(node.getTerm());
    }


}
