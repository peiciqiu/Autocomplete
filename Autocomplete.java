import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Autocomplete implements IAutocomplete {
    Node root;
    int maxNum;

    public Autocomplete() {
        this.root = new Node();
        this.maxNum = 0;
    }
    @Override
    public void addWord(String word, long weight) {
        word = word.toLowerCase();

        //check if the prefix is valid
        if (!word.matches("[a-z]+") || weight < 0) {
            return;
        }
        Node curr = this.root;
        this.root.setPrefixes(this.root.getPrefixes() + 1);
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (curr.getReferences()[index] == null) {
                if (i == (word.length() - 1)) {
                    curr.getReferences()[index] = new Node(word.substring(0, i + 1), weight);
                    Node newNode = curr.getReferences()[index];
                    newNode.setWords(1);
                    newNode.setPrefixes(1);
                } else {
                    curr.getReferences()[index] = new Node(word.substring(0, i + 1), 0);
                    Node newNode = curr.getReferences()[index];
                    newNode.setPrefixes(1);
                }

            //the prefix of substring(0, i) has existed
            } else {
                Node theNode = curr.getReferences()[index];
                theNode.setPrefixes(theNode.getPrefixes() + 1);
                if (i == (word.length() - 1)) {
                    theNode.setWords(1);
                    theNode.getTerm().setWeight(weight);
                }
            }

            curr = curr.getReferences()[index];
        }

    }

    @Override
    public Node buildTrie(String filename, int k) {
        FileReader fr = null;
        try {
            fr = new FileReader(filename);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(fr);
        String thisLine;
        int count = 0;

        while (true) {
            try {
                thisLine = br.readLine();
                if (thisLine == null) {
                    break;
                }
                thisLine = thisLine.trim();
                if (thisLine.length() == 0 || count == 0) {
//                    if (count == 0) {
//                        int totalNum = Integer.parseInt(thisLine);
//                        this.root.setPrefixes(totalNum);
//                    }
                    count++;
                    continue;
                }
//                thisLine = thisLine.trim();
                String[] split = toSplit(thisLine);
                long weight = Long.parseLong(split[0]);
                String word = split[1];
                addWord(word, weight);
//                int oldPrefixes = this.root.getPrefixes();
//                this.root.setPrefixes(oldPrefixes + 1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.maxNum = k;
        return this.root;
    }

    private String[] toSplit(String line) {
        String[] split = line.split("\t");
        split[0] = split[0].replaceAll(" ", "");
        split[1] = split[1].replaceAll(" ", "");
        return split;
    }

    @Override
    public Node getSubTrie(String prefix) {

        prefix = prefix.toLowerCase();

        if (prefix.equals("")) {
            return this.root;
        }

        if (!prefix.matches("[a-z]+")) {
            return null;
        }

        Node curr = this.root;
        for (int i = 0; i < prefix.length(); i++) {
            int index = prefix.charAt(i) - 'a';
            curr = curr.getReferences()[index];
            if (curr == null) {
                return null;
            }
        }

        return curr;
    }

    @Override
    public int countPrefixes(String prefix) {
        Node subTrie = getSubTrie(prefix);
        if (subTrie == null) {
            return 0;
        }
        return subTrie.getPrefixes();
    }

    @Override
    public List<ITerm> getSuggestions(String prefix) {
        List<ITerm> suggestions = new ArrayList<>();

        if (prefix.equals("")) {
            dfsTraversal(this.root, suggestions);
            return suggestions;
        }

        //handle invalid cases
        prefix = prefix.toLowerCase();
        if (!prefix.matches("[a-z]+")) {
            return suggestions;
        }

        Node subTrie = getSubTrie(prefix);

        // DFS to traverse the tries
        dfsTraversal(subTrie, suggestions);

        return suggestions;
    }

    private void dfsTraversal(Node root, List<ITerm> sugList) {
        if (root == null) {
            return;
        }

        if (root.getWords() == 1) {
            Term copy = new Term(root.getTerm().getTerm(), root.getTerm().getWeight());
            sugList.add(copy);
        }

        for (int i = 0; i < ITerm.ALPHABET_SIZE; i++) {
            if (root.getReferences()[i] != null) {
                dfsTraversal(root.getReferences()[i],  sugList);
            }
        }

    }
}
