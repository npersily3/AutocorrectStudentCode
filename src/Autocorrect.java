import java.io.*;

import java.util.*;

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
    public static final int DIVIDOR = 163;

    private ArrayList<Integer>[] table;

    // Class of a word and edit distance pair
    private class Pair {
        String word;
        int editDistance;

        private Pair(String word, int editDistance) {
            this.word = word;
            this.editDistance = editDistance;

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


    // Return the edit Distance between two words
    public int editDistance(String s1, String s2) {

        int[][] table = new int[s1.length() + 1][s2.length() + 1];
        // Fill the initial rows and columns of the table
        for (int i = 0; i < table.length; i++) {
            table[i][0] = i;
        }
        for (int i = 0; i < table[0].length; i++) {
            table[0][i] = i;
        }

        // Row major traversal through the table
        for (int i = 1; i < table.length; i++) {
            for (int j = 1; j < table[0].length; j++) {
                // If corresponding letters equal each other, the current box equals the diagonal box
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    table[i][j] = table[i - 1][j - 1];
                } else {
                    //Otherwise add the min + 1 of the diagonal, up and left
                    // Delete
                    table[i][j] = 1 + table[i - 1][j];
                    // Addition
                    table[i][j] = Math.min(table[i][j], table[i][j - 1] + 1);
                    //Swap
                    table[i][j] = Math.min(table[i][j], table[i - 1][j - 1] + 1);
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
        // For the length of the dictionary
        for (int i = 0; i < length; i++) {

            int editDistance = editDistance(typed, dictionary[i]);

            // If a word is less than the threshold add it the dict
            if (editDistance <= threshold) {
                valid.add(new Pair(dictionary[i], editDistance));
            }
        }
        // Sort the dictionary and return it as an array
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

    public static void main(String[] args) {


        Scanner s = new Scanner(System.in);

        System.out.println("What is your word");
        String word = s.nextLine();

        Pair[] candidates;
        Autocorrect a = new Autocorrect(loadDictionary("sorted"));

        // Determines if inputted word is greater than n and sets threshold accordingly
        if (word.length() <= Ngram.N) {
            a.threshold = 1;
            candidates = a.smallMatches(word);
        } else {
            a.threshold = word.length() / Ngram.N + 1;
            candidates = a.generateCandidates(word);

        }

        // If candidates equals null, the word is spelled correctly
        if (candidates == null) {
            return;
        }

        // Print out the candidates in pairs of N
        for (int i = 0; i < candidates.length; i++) {
            if (i % Ngram.N == 0) {
                System.out.println("|");
                System.out.println("--------------------------------------");
            }
            System.out.print("|" + candidates[i].word);
        }
        System.out.println("|");
        System.out.println("--------------------------------------");

    }

    // Generate a list of a candidate words that are close to an inputted word. Return a list of those words and corresponding edit distance
    public Pair[] generateCandidates(String word) {
        this.initTable();
        int[] ngrams = Ngram.generateNgrams(word);

        HashMap<String, Integer> candidateHash = new HashMap<>();

        // For every ngram in the given word
        for (int i = 0; i < ngrams.length; i++) {
            // For every word containing that ngram
            for (int j = 0; j < table[ngrams[i]].size(); j++) {

                String candidate = dictionary[table[ngrams[i]].get(j)];
                int editDistance = editDistance(word, candidate);

                // If the word is close enough and has not been seen add it to the hashmap
                if (editDistance <= threshold) {
                    if (!candidateHash.containsKey(candidate))
                        candidateHash.put(candidate, editDistance);
                }
            }
        }
        // Convert the hashmap to an arraylist to easily sort it by the edit distance
        ArrayList<Pair> candidates = new ArrayList<>();


        String[] values = candidateHash.keySet().toArray(new String[0]);
        for (int i = 0; i < values.length; i++) {
            candidates.add(new Pair(values[i], candidateHash.get(values[i])));
        }

        candidates.sort(Comparator.comparing(Pair::getEditDistance));

        // If the inputted word is spelled correctly return null
        if (candidates.getFirst().editDistance == 0) {
            return null;
        }

        return candidates.toArray(new Pair[0]);
    }

    // Reads in from the table of N-grams and word indices into the table
    public void initTable() {


        try {
            // Instantiate reader and table objects
            table = new ArrayList[Ngram.TABLE_LENGTH];

            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader(Ngram.N + "gram.txt"));


            line = dictReader.readLine();

            int counter = 0;
            // While not at the end of the file
            while (line != null && counter < Ngram.TABLE_LENGTH) {
                int dictIndex = Integer.parseInt(line);

                // While not at the end of an ngram list
                while (dictIndex != Ngram.END_OF_LIST) {
                    // Add an entry to the table
                    if (table[counter] == null) table[counter] = new ArrayList<>();
                    table[counter].add(dictIndex);

                    line = dictReader.readLine();
                    // If at the end, break
                    if (line == null) {
                        break;
                    }

                    dictIndex = Integer.parseInt(line);
                }
                counter++;
                line = dictReader.readLine();
            }
            dictReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // Item that returns a list of candidates  if the word has a length of less than n
    public Pair[] smallMatches(String word) {

        HashMap<String, Integer> candidateHash = new HashMap<>();

        // For all the words that are less than three letters
        for (int i = 0; i < DIVIDOR; i++) {
            int editDistance = editDistance(dictionary[i], word);
            // If within threshold and not found already put into hashMap
            if (editDistance <= threshold) {
                if (!candidateHash.containsKey(dictionary[i])) {
                    candidateHash.put(dictionary[i], editDistance);
                }
            }


        }

        ArrayList<Pair> candidates = new ArrayList<>();

        // Convert Hashmap into arraylist and sort by edit distance
        String[] values = candidateHash.keySet().toArray(new String[0]);
        for (int i = 0; i < values.length; i++) {
            candidates.add(new Pair(values[i], candidateHash.get(values[i])));
        }

        candidates.sort(Comparator.comparing(Pair::getEditDistance));

        // If the word is found return null
        if (candidates.getFirst().editDistance == 0) {
            return null;
        }

        return candidates.toArray(new Pair[0]);
    }

    // One time method used to write a sorted dictionary
    public static void sortDictionary() {
        try {

            BufferedWriter dictWriter = new BufferedWriter(new FileWriter("/dictionaries/sorted.txt"));
            // Read in entrie dictionary and sort it
            String[] dictionary = loadDictionary("large");
            Arrays.sort(dictionary, Comparator.comparing(String::length));

            int length = dictionary.length;

            // Write dictionary to file
            dictWriter.write(length + "");
            dictWriter.newLine();
            for (String s : dictionary) {
                dictWriter.write(s);
                dictWriter.newLine();
            }
            dictWriter.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}