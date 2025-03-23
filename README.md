# Persily Autocorrect
#### A word-suggestion project created by Zach Blick for Adventures in Algorithms at Menlo School in Atherton, CA

## N-Gram

The Ngram class' sole is used to write a file that can be read in as an array of arrayList of integers. The index of an array represents the hash of a ngram and the corresponding list contains the index of corresponding words in a dictionary. 

## Autocorrect

This class receives an inputted word via the terminal. If the word is small enough iterate through the dictionary and if the edit distance is one add it to a hashmap. Once the iteration is over convert the hashmap into a list of pairs, sort it and return it. If the word is long, only try to find candidates using the table of ngrams. Then repeat the same process using edit distance, hashmaps, and arrays of pairs.

## Your goal
Create an autocorrect tool that runs continuously in the IntelliJ terminal. Once the program begins, it waits for the user to type a word into the console. If the word matches a dictionary word, nothing happens. But if the word is misspelled, the program will return the closest matching words. If no matching candidates can be found, the program prints "No matches found."

A tester file has been provided for you. It utilizes junit tests.
To use this test file, run either the entire thing or individual tests (one at a time).
There are five test cases, each of which will load data from [test number].txt, which is in the
test_files directory.