import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

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
    public static final int DIVIDOR = 1412;
    public static final int MAX_CANDIDATES = 6;


    private class Pair {
        String word;
        int editDistance;

        private Pair(String word, int threshold) {
            this.word = word;
            this.editDistance = threshold;

        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getEditDistance() {
            return editDistance;
        }

        public void setThreshold(int threshold) {
            this.editDistance = threshold;
        }
    }

    public Autocorrect(String[] words, int threshold) {

        dictionary = words;
        this.threshold = threshold;
    }
    public Autocorrect(String[] words) {

        dictionary = words;
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


            int editDistance = editDistance(typed, dictionary[i]);


            if( editDistance <= threshold) {
                valid.add(new Pair(dictionary[i], editDistance) );
            }
        }
        valid.sort(Comparator.comparing(Pair::getWord));
        valid.sort(Comparator.comparing(Pair::getEditDistance));

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

    public static void main(String[] args)
    {
         Autocorrect a = new Autocorrect(loadDictionary("sorted"));

        Scanner s = new Scanner(System.in);

        System.out.println("What is your word");
        String word = s.nextLine();

        String[] candidates;
        if(word.length() <= Ngram.N) {
            candidates = a.smallMatches(word);
        } else {

        }


        System.out.println("--------------------------------------");

    }
    public  String[] smallMatches(String word) {

       ArrayList<Pair> candidates = new ArrayList<>();

       // Create the initial candidates then sort them by edit distance
        for (int i = 0; i < MAX_CANDIDATES; i++) {
            candidates.add(new Pair(dictionary[i], editDistance(dictionary[i], word)));
        }

        candidates.sort(Comparator.comparing(Pair::getEditDistance));


        // For all the words that are less than three letters
        for (int i = MAX_CANDIDATES; i < DIVIDOR; i++) {
            int editDistance = editDistance(dictionary[i], word);
            // Check to see if the current word has a shorter edit distance than the word that has lowest edit distance currently being tracked in candidates
            if(candidates.get(MAX_CANDIDATES - 1).editDistance > editDistance) {
                // Iterate through the candidates list and insert the new word by edit distance
                for (int j = MAX_CANDIDATES - 1; j > - 1; j--) {
                    if(editDistance > candidates.get(j).editDistance) {
                        candidates.removeLast();
                        candidates.add(j, new Pair(dictionary[i], editDistance));
                    }
                }
            }
        }
        // Return the top six candidates as an array
        String[] candidateArr = new String[6];
        for (int i = 0; i < 6; i++) {
            candidateArr[i] = candidates.get(i).word;
        }
        return candidateArr;
    }
    public static void main2() {
        try {
            String line;
            BufferedWriter dictWriter = new BufferedWriter(new FileWriter("sorted.txt"));
            String[] dictionary = loadDictionary("large");
            Arrays.sort(dictionary, Comparator.comparing(String::length));

            int length = dictionary.length;

            dictWriter.write(length + "");
            dictWriter.newLine();
            for (String s: dictionary) {
                dictWriter.write(s);
                dictWriter.newLine();
            }
            dictWriter.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}