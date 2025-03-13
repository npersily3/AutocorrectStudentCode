import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 *
 * @author Zach Blick
 * @author YOUR NAME HERE
 */
public class Autocorrect {

    /**
     * Constucts an instance of the Autocorrect class.
     *
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */

    private String[] dictionary;
    private int threshold;

    private class Pair {
        String word;
        int threshold;

        private Pair(String word, int threshold) {
            this.word = word;
            this.threshold = threshold;

        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getThreshold() {
            return threshold;
        }

        public void setThreshold(int threshold) {
            this.threshold = threshold;
        }
    }

    public Autocorrect(String[] words, int threshold) {

        dictionary = words;
        this.threshold = threshold;
    }

    public int editDistance(String s1, String s2) {

        int[][] table = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i < table.length; i++) {
            table[i][0] = i;
        }
        for (int i = 0; i < table[0].length; i++) {
            table[0][i] = i;
        }


        for (int i = 1; i < table.length; i++) {
            for (int j = 1; j < table[0].length; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    table[i][j] = table[i - 1][j - 1];
                } else {
                    // Delete
                    table[i][j] = 1 + table[i-1][j];
                    // Addition
                    table[i][j] = Math.min(table[i][j], table[i][j-1] + 1);
                    //Swap
                    table[i][j] = Math.min(table[i][j], table[i - 1][j-1] + 1);
                }
            }
        }
        return table[table.length - 1][table[0].length - 1];
    }

    /**
     * Runs a test from the tester file, AutocorrectTester.
     *
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */


    public String[] runTest(String typed) {

        ArrayList<Pair> valid = new ArrayList<>();

        int length = dictionary.length;
        for (int i = 0; i < length; i++) {

            if(dictionary[i].equals("memento")) {
                System.out.println("menlo");
            }

            int editDistance = editDistance(typed, dictionary[i]);


            if( editDistance <= threshold) {
                valid.add(new Pair(dictionary[i], editDistance) );
            }
        }
        valid.sort(Comparator.comparing(Pair::getWord));
        valid.sort(Comparator.comparing(Pair::getThreshold));

        int validLength = valid.size();
        String[] correct = new String[validLength];

        for (int i = 0; i < validLength; i++) {
            correct[i] = valid.get(i).word;
            System.out.println(correct[i]);
        }
       return correct;
    }


    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     *
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary) {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;

            }
            return words;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {


    }
}