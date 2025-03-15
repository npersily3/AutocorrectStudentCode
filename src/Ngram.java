import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Ngram {

    public static final int N = 4;

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
        String[] dictionary = loadDictionary("large");


    }

    private static void initTable(String dictionary) {
        HashMap<String, ArrayList<String>> table = new HashMap<>();



        for (int i = 0; i < dictionary.length(); i++) {

        }


    }
    private static String[] generateNgrams(String word) {

        ArrayList<String> ngrams = new ArrayList<>();



        return ngrams.toArray(new String[0]);
    }
}