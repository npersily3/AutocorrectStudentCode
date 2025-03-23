# Time Complexities

## Ngram 

### Main

O(D* L) 
where D is the length of the dictionary and L is the average length of the word in the dictionary

### Generate Ngrams
O(L)
where L is the length of the inputted word

### Hash
O(L)
where L is the length of the inputted word

## Autocorrect

### main 

If the length of the word is less than n in the ngrams class, O(D*L + K)  where D is the amount of two or one-letter words in the dictionary, L is the length of the given word, and K is the number of candidate words. 
If the length is greater than n O(L + L * K * (L * N) + M + M log M + K) where L is the length of the inputted word, 
k is amount of words associated with a given ngram,  n is the length of the word associated with an ngram, 
and m is the number of candidate words generated

### generateCandidates

O(L + L * K * (L * N) + M + M log M) where L is the length of the inputted word, k is amount of words associated with a given ngram, 
n is the length of the word associated with an ngram, and m is the number of candidate words generated.
According to the java website put and contains key are constant time operations,

### initTable


O(1) assuming the file stays the same which it should

### smallMatches 

O(D*L) where D is the amount of two or one-letter words in the dictionary and L is the length of the given word

### sortDictionary 
O(D + D log D) where D is the length of the dictionary


