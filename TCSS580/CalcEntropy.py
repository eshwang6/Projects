# -*- coding: utf-8 -*-

import math

f = open("sawyr10.txt", "r")
tomsawyr = f.read()

# Obtain only letters and change to lower case
new_tom = ""
for ch in tomsawyr:
    if ch.isalpha():
        new_tom += ch.lower()
# print(new_tom[0:39])

# N-gram Counting (frequency of occurrence)
def ngramCount(n, input_string):
    txt_length = len(input_string)
    freq = {}
    for i in range(txt_length-n+1):
        if (input_string[i:i+n]) not in freq:
            freq[input_string[i:i+n]] = 1
        else:
            freq[input_string[i:i+n]] += 1
    
    total = sum(freq.values())
    for key in freq:
      freq[key] = freq[key]/total
    
    return freq

# Calculation of the Entropy based on the frequency of occurrence
def calcEntropy(freqDict):
    entropy = 0
    for key in freqDict:
        v = freqDict[key]
        entropy += (-1)*v*(math.log2(v))
    return entropy

# Uni-, bi-, trigram counting & calculate each entropy
uniFreq = ngramCount(1, new_tom)
biFreq = ngramCount(2, new_tom)
triFreq = ngramCount(3, new_tom)

uni_entropy = calcEntropy(uniFreq)
bi_entropy = calcEntropy(biFreq)
tri_entropy = calcEntropy(triFreq)
print("Unigram Entropy = %g" % uni_entropy)
print("Bigram Entropy =  %g" % bi_entropy)
print("Trigram Entropy =  %g" % tri_entropy)
