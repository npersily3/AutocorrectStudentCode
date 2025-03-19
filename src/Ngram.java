import java.io.*;
import java.util.ArrayList;


public class Ngram {

    public static final int N = 3;

    public static final int END_OF_LIST = -1;
    public static final int RADIX = 27;
    public static final int DIVISOR = (int) Math.pow(RADIX, N - 1);
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

        for (int i = Autocorrect.DIVIDOR; i < dictionary.length; i++) {
            int[] ngrams = generateNgrams(dictionary[i]);
            for (int j = 0; j < ngrams.length; j++) {
                if (table[ngrams[j]] == null) {
                    table[ngrams[j]] = new ArrayList<>();
                }
                table[ngrams[j]].add(i);
            }
        }


        try {
            BufferedWriter dictWriter = new BufferedWriter(new FileWriter(N + "gram.txt"));


            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
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


    public static int[] generateNgrams(String word) {


        ArrayList<Integer> hashes = new ArrayList<>();
        int hash = hash(word.substring(0, N));
        hashes.add(hash);
        for (int i = N; i < word.length(); i++) {
            hash = hash % DIVISOR;
            hash *= RADIX;
            char letter = word.charAt(i);
            if (letter == "'".charAt(0)) {
                hash += RADIX - 1;
            } else {
                hash += letter - 'a';
            }
            hashes.add(hash);
        }

        int[] hashArr = new int[hashes.size()];

        for (int i = 0; i < hashArr.length; i++) {
            hashArr[i] = hashes.get(i);
        }

        return hashArr;
    }

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