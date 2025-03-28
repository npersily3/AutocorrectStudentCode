import java.io.*;
import java.util.ArrayList;


public class Ngram {

    public static final int N = 3;

    public static final int END_OF_LIST = -1;
    public static final int RADIX = 27;
    public static final int HASH_MASK = (int) Math.pow(RADIX, N - 1);
    public static final int TABLE_LENGTH = (int) Math.pow(RADIX, N);


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
        String[] dictionary = loadDictionary("sorted");
        ArrayList<Integer>[] table = new ArrayList[TABLE_LENGTH];

        // For every word greater than n
        for (int i = Autocorrect.DIVIDOR; i < dictionary.length; i++) {
            int[] ngrams = generateNgrams(dictionary[i]);
            // For every ngram
            for (int j = 0; j < ngrams.length; j++) {
                // Put the index to the word in the corresponding arraylist
                if (table[ngrams[j]] == null) {
                    table[ngrams[j]] = new ArrayList<>();
                }
                table[ngrams[j]].add(i);
            }
        }

        // Write the table to a file
        try {
            BufferedWriter dictWriter = new BufferedWriter(new FileWriter(N + "gram.txt"));

            // For every ngram
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    // For every corresponding word
                    for (int j = 0; j < table[i].size(); j++) {
                        dictWriter.write(String.valueOf(table[i].get(j)));
                        dictWriter.newLine();
                    }
                }
                dictWriter.write(String.valueOf(END_OF_LIST));
                dictWriter.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Given a word return a list of the hashes of all of its ngrams
    public static int[] generateNgrams(String word) {

        // Compute the hash of the first n letters
        ArrayList<Integer> hashes = new ArrayList<>();
        int hash = hash(word.substring(0, N));
        hashes.add(hash);

        // Use rabin karp fingerprint algorithm to generate the rest of the ngrams' hashes
        for (int i = N; i < word.length(); i++) {
            hash = hash % HASH_MASK;
            hash *= RADIX;
            char letter = word.charAt(i);
            if (letter == "'".charAt(0)) {
                hash += RADIX - 1;
            } else {
                hash += letter - 'a';
            }
            hashes.add(hash);
        }

        // Convert into an array
        int[] hashArr = new int[hashes.size()];

        for (int i = 0; i < hashArr.length; i++) {
            hashArr[i] = hashes.get(i);
        }

        return hashArr;
    }

    // Horner's method hash function with logic to deal with apostrophes
    private static int hash(String word) {
        char letter = word.charAt(0);
        int sum = 0;
        if (letter == "'".charAt(0)) {
            sum += RADIX - 1;
        } else {
            sum += letter - 'a';
        }

        for (int i = 1; i < word.length(); i++) {
            letter = word.charAt(i);
            sum *= RADIX;
            if (letter == "'".charAt(0)) {
                sum += RADIX - 1;
            } else {
                sum += letter - 'a';
            }


        }
        return sum;
    }
}