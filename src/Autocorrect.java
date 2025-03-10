import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author YOUR NAME HERE
 */
public class Autocorrect {

    /**
     * Constucts an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */
    public Autocorrect(String[] words, int threshold) {

    }
    public int editDistance(String s1, String s2) {

        int[][] table = new int[s1.length()][s2.length()];

        boolean seen = false;
        if(s1.charAt(0) == s2.charAt(0)) {
            table[0][0] = 0;
        } else {
            table[0][0] = 1;
        }

        for (int i = 1; i < table.length; i++) {
            if(s1.charAt(i) == s2.charAt(0)) {
                if(seen) {
                    table[i][0] = table[i-1][0] + 1;
                } else {
                    seen = true;
                    table[i][0] = table[i-1][0];
                }
            } else {
                table[i][0] = table[i-1][0] + 1;
            }
        }
        seen = false;
        for (int i = 1; i < table[0].length; i++) {
            if(s1.charAt(0) == s2.charAt(i)) {
                if(seen) {
                    table[0][i] = table[0][i-1] + 1;
                } else {
                    seen = true;
                    table[0][i] = table[0][i - 1];
                }
            } else {
                table[0][i] = table[0][i-1] + 1;
            }
        }
        return 0;
    }

    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {

        return new String[0];
    }


    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary)  {
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
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}