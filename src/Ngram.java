import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Ngram {

    public static final int N = 4;

    public static final String END_OF_LIST = "0";

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
            String[] ngrams = generateNgrams(dictionary[i]);
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


    public static String[] generateNgrams(String word) {


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
}