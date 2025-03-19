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
    public static final int MAX_CANDIDATES = 6;

    private HashMap<String, ArrayList<String>> table;

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
    public Autocorrect() {
        table = new HashMap<>();
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


        Scanner s = new Scanner(System.in);

        System.out.println("What is your word");
        String word = s.nextLine();

        String[] candidates;
        if(word.length() <= Ngram.N) {
            Autocorrect a = new Autocorrect(loadDictionary("sorted"));
            candidates = a.smallMatches(word);
        } else {
            Autocorrect a = new Autocorrect();
            candidates = a.generateCandidates(word);

        }

        if(candidates == null) {
            return;
        }


        System.out.println("--------------------------------------");
        System.out.println("|" + candidates[0] + "|" + candidates[1] + "|" + candidates[2] + "|");
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println("Do you want more words (Y/N)");
        String answer = s.nextLine();

        if(answer.equalsIgnoreCase("Y")) {
            System.out.println("--------------------------------------");
            System.out.println("|" + candidates[3] + "|" + candidates[4] + "|" + candidates[5] + "|");
            System.out.println("--------------------------------------");
            System.out.println();
        }

        return;

    }
    public String[] generateCandidates(String word) {
        this.initTable();
        int[] ngrams = Ngram.generateNgrams(word);

        ArrayList<Pair> candidates = new ArrayList<>();
        populateCandidates(candidates);

        for (int i = 0; i < ngrams.length; i++) {
            for (int j = 0; j < table.get(ngrams[i]).size(); j++) {
                int editDistance = editDistance(table.get(ngrams[i]).get(j), word);

                if(candidates.get(MAX_CANDIDATES - 1).editDistance > editDistance) {
                    // Iterate through the candidates list and insert the new word by edit distance
                    int counter = MAX_CANDIDATES - 1;
                    while (editDistance < candidates.get(counter).editDistance && counter > 0) {
                        counter--;
                    }

                    candidates.removeLast();
                    candidates.add(counter, new Pair(table.get(ngrams[i]).get(j),editDistance));
                }
            }
        }
        if(candidates.get(0).editDistance == 0) {
            return null;
        }

        // Return the top six candidates as an array
        String[] candidateArr = new String[MAX_CANDIDATES];
        for (int i = 0; i < MAX_CANDIDATES; i++) {
            candidateArr[i] = candidates.get(i).word;
        }
        return candidateArr;
    }
    public void initTable() {


        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("table.txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int length = Integer.parseInt(line);

            String ngram = dictReader.readLine();
            while (ngram != null) {
                String value = dictReader.readLine();
                table.put(ngram, new ArrayList<>());
                while (!value.equals(Ngram.END_OF_LIST)) {
                    table.get(ngram).add(value);
                    value = dictReader.readLine();
                }
                ngram = dictReader.readLine();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void populateCandidates(ArrayList<Pair> candidates) {
        Pair nullCandidate = new Pair(null, 100);
        for (int i = 0; i < MAX_CANDIDATES; i++) {
            candidates.add(nullCandidate);
        }
    }

    public  String[] smallMatches(String word) {

       ArrayList<Pair> candidates = new ArrayList<>();

       populateCandidates(candidates);

        // For all the words that are less than three letters
        for (int i = 0; i < DIVIDOR; i++) {
            int editDistance = editDistance(dictionary[i], word);
            // Check to see if the current word has a shorter edit distance than the word that has lowest edit distance currently being tracked in candidates
            if(candidates.get(MAX_CANDIDATES - 1).editDistance > editDistance) {
                // Iterate through the candidates list and insert the new word by edit distance
                for (int j = MAX_CANDIDATES - 1; j > - 1; j--) {
                    if(editDistance < candidates.get(j).editDistance) {
                        candidates.removeLast();
                        candidates.add(j, new Pair(dictionary[i], editDistance));
                    }
                }
            }
        }
        if(candidates.get(0).editDistance == 0) {
            return null;
        }
        // Return the top six candidates as an array
        String[] candidateArr = new String[MAX_CANDIDATES];
        for (int i = 0; i < MAX_CANDIDATES; i++) {
            candidateArr[i] = candidates.get(i).word;
        }
        return candidateArr;
    }

    public static void sortDictionary() {
        try {
            String line;
            BufferedWriter dictWriter = new BufferedWriter(new FileWriter("/dictionares/sorted.txt"));
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