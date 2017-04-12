# -*- coding: utf-8 -*-
"""
@author: Euisoon Hwang
"""

import numpy as np

def inCharset(string):
    flag = 0
    for i in string:
        if ord(i)<48 or ord(i)>122:
            flag = 1
    return flag

def createMatrix(string):
    stringMat = []
    for i in string:
        temp = list(map(int, np.binary_repr(ord(i)).zfill(k)))
        stringMat.append(temp)
    stringMat = np.asarray(stringMat)
    return stringMat


# Initialize k and n to 20,30
k, n = 20, 30

# Get Random matrix A with Bernoulli distribution with p = 0.5
randA = np.random.binomial(1, 0.5, (k, n-k))

# Construct a generator matrix G in a systematic form
G = np.hstack((np.identity(k, dtype= int), randA))
print("Saving generator matrix G in systematic form as a text file")
np.savetxt('GenMatG.txt', G, fmt='%s', delimiter=',', newline='\r\n')

# Construct a parity check matrix H in a systematic form with A
H = np.hstack((np.transpose(randA), np.identity((n-k), dtype= int)))
print("Saving parity check matrix H in systematic form as a text file")
np.savetxt('ParCheckMatH.txt', H, fmt='%s', delimiter=',', newline='\r\n')


# For a user created message, accept short input with alphabets & numbers only
while True:
    userMsg = input("Type your message you want to encode.(up to 20 letters): ")
    if len(userMsg) > 20:
        print("Too long. Try again")
    elif inCharset(userMsg) != 0:
        print("Please type Alphabets and Numbers Only.")
    else:
        break

userU = createMatrix(userMsg)
print("User Created Message Binary Matrix:")
print(userU)


# Generate codeword from User Created Message
userX = userU.dot(G) % 2
print("Codeword Matrix for User Created Message : ")
print(userX)
np.savetxt('userCodeword.txt', userX, fmt='%s', delimiter=',', newline='\r\n')


# Test case on one binary message u_test and getting x_test
#u_test = np.expand_dims(np.asarray([1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0]), axis=0)
#x_test = u_test.dot(G) % 2

