# -*- coding: utf-8 -*-
"""
@author: Euisoon Hwang
"""

import numpy as np
import csv
import itertools
from random import randint


# Build a matrix given a comma-separated text file
def buildMatrix(inFile):
    with open(inFile, mode = 'r', newline ='\r\n') as csvFile:
        my_list = [list(map(int,rec)) for rec in csv.reader(csvFile, delimiter=',')]
    my_Mat = np.asarray(my_list, dtype=int)
    return my_Mat


# Generate all possible binary sequence matrix (which is a Syndrome) with given length
def getSyndromes(msglen, cwdlen):
    length = cwdlen - msglen
    Mat = []
    for i in range(2**length): 
        temp = list(map(int, np.binary_repr(i).zfill(length)))
        Mat.append(temp)
    Mat = np.asarray(Mat)
    return Mat


# Build Syndrome : CosetLeader Dictionary
def getCosetLeader(parityCheckMatrix):
    n = parityCheckMatrix.shape[1]
    rank = parityCheckMatrix.shape[0]
    my_dict = { 0: (np.zeros(n, dtype=np.int)) }
    allErrVecs = getBinLength(n)
    H_tr = np.transpose(parityCheckMatrix)

    for errVec in allErrVecs:
        syndrome = int(''.join(map(str,np.squeeze(
                np.expand_dims(errVec, axis = 0).dot(H_tr) % 2))), 2)
        if syndrome not in my_dict:
            my_dict[syndrome] = errVec
        if len(my_dict) == (2**rank):
            break
        
    return my_dict


# Get binary sequence with length n starting from the lowest hamming weight
def getBinLength(n):
    for i in range(1, n + 1):
        for comb in itertools.combinations(range(n), i):
            res = [0] * n
            for one in comb:
                res[one] = 1
            yield np.asarray(res)


# Calculate binary hamming weight
def binHammingWeight(array):
    if array.ndim == 1:
        return ''.join(map(str,array)).count('1')
    else:
        return ''.join(map(str,np.squeeze(array))).count('1')
        

# Introduce errors on codewords to simulate BSC
def makeErrOnCodewords(cwdMatrix, errors):
    if errors == 0:
        return cwdMatrix
    else:
        n = cwdMatrix.shape[1]
        errVec = [0]*n
        for i in range(errors):
            index = randint(1, errors)
            if errVec[index] != 1:
                errVec[index] = 1
        errVec = np.asarray(errVec, dtype=int)
        if cwdMatrix.ndim == 1:
            return np.add(cwdMatrix, errVec) % 2
        else:
            receivedWord = []
            nrow = cwdMatrix.shape[0]
            for i in range(nrow):
                res = np.add(cwdMatrix[i], errVec) % 2
                receivedWord.append(res)
            return np.asarray(receivedWord, dtype=np.int)


# Attempt to correct the received word with Syndrome dictionary
def correctWithSyndrome(rcvedWrdMat, parityCheckMatrix, cosetDict):
    H_tr = np.transpose(parityCheckMatrix)
    if rcvedWrdMat.ndim == 1:
        syndrome = int(''.join(map(str,np.squeeze(
                np.expand_dims(rcvedWrdMat, axis = 0).dot(H_tr) % 2))), 2)
        print(syndrome)
        errVec = cosetDict[syndrome]
        print(errVec)
        correctCwd = np.add(rcvedWrdMat, errVec) % 2
        print(correctCwd)
        return correctCwd
    else:
        nrow = rcvedWrdMat.shape[0]
        res = []
        for i in range(nrow):
            syndrome = int(''.join(map(str,np.squeeze(np.expand_dims(
                    rcvedWrdMat[i], axis = 0).dot(H_tr) % 2))), 2)
            print(syndrome)
            errVec = cosetDict[syndrome]
            print(errVec)
            correctCwd = np.add(rcvedWrdMat[i], errVec) % 2
            print(correctCwd)
            res.append(correctCwd)
        res = np.asarray(res, dtype = int)
        return res


def checkifCorrectedRight(cwdMatrix, correctedMatrix):
    if np.array_equal(cwdMatrix, correctedMatrix):
        print("Correction Complete!")
    else:
        print("Oh no! Cannot Correct for some reason :( ")
        
H = buildMatrix('ParCheckMatH.txt')
k = H.shape[1]-H.shape[0]
n = H.shape[1]
syndDict = getCosetLeader(H)
X_user = buildMatrix('userCodeword.txt')

while True:
    print('Simulating Errors')
    userInputError = input("Enter number of errors (Please keep it small): ")
    if not userInputError.isdigit():
        print("Please type numbers only.")
    elif int(userInputError) > n or int(userInputError) < 0:
        print("# of errors should be between 0 and ", n)
    else:
        userInputError = int(userInputError)
        break

Y_user = makeErrOnCodewords(X_user, userInputError)

print("Attempt to correct user created message...")
X_bar_user = correctWithSyndrome(Y_user, H, syndDict)
checkifCorrectedRight(X_user, X_bar_user)





