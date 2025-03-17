import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Ngram {

    public static final int N = 3;

    public static final String END_OF_LIST = "0";
    public static final int RADIX = 27;
    public static final int DIVISOR = (int) Math.pow(RADIX, N - 1);
    public static final int TABLE_LENGTH = (int) Math.pow(RADIX, N);
    public static ArrayList<String>[] TABLE = new ArrayList[TABLE_LENGTH];



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
        HashMap<String, ArrayList<String>> table = new HashMap<>();

        for (int i = Autocorrect.DIVIDOR; i < dictionary.length; i++) {
            String[] ngrams = generateNgrams(dictionary[i], i);
            for (int j = 0; j < ngrams.length; j++) {
                if (table.containsKey(ngrams[j])) {
                    table.get(ngrams[j]).add(dictionary[i]);
                } else {
                    table.put(ngrams[j], new ArrayList<>());
                    table.get(ngrams[j]).add(dictionary[i]);
                }
            }
        }


        try {
            BufferedWriter dictWriter = new BufferedWriter(new FileWriter("table.txt"));

            String[] keys = table.keySet().toArray(new String[0]);

            dictWriter.write(keys.length + "");
            dictWriter.newLine();
            for (int i = 0; i < keys.length; i++) {
                dictWriter.write(keys[i]);
                dictWriter.newLine();
                for (int j = 0; j < table.get(keys[i]).size(); j++) {
                    dictWriter.write(table.get(keys[i]).get(j));
                    dictWriter.newLine();
                }
                dictWriter.write(END_OF_LIST);
                dictWriter.newLine();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static String[] generateNgrams(String word, int index) {


        ArrayList<String> ngrams = new ArrayList<>();
        for (int i = N; i < word.length(); i++) {
            String ngram = "";
            for (int j = i - N; j < i; j++) {
                ngram += word.charAt(j);
            }
            ngrams.add(ngram);
        }
        return ngrams.toArray(new String[0]);
    }

    private int hash(String word) {
        char letter = word.charAt(0);
        int sum = 0;
        if (letter == "'".charAt(0)) {
             sum += RADIX - 1;
        } else {
             sum += letter - 'a';
        }

        for (int i = 0; i < word.length(); i++) {
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