# -*- coding: utf-8 -*-

import os
import time

dictionary = {};
position = 1;
buff = "";

inFile = open('sawyr10.txt', 'r')
outFile = open('LZ-sawyr10.txt', mode = 'wb')

def writeFile(index, letter):
    output = bytes([(index >> 16) & 0xFF, (index >> 8) & 0xFF, index & 0xFF, ord(letter) >> 8, ord(letter) & 0xFF])
    outFile.write(output)

# ---------------- Start Compressing ----------------

lzStartCompTime = time.time()

while True:
    c = inFile.read(1)
    buff += c;
    
    if len(buff) > 0 and buff not in dictionary:
        dictionary[buff] = position
        position += 1
        
        if len(buff) == 1:
            writeFile(0, buff)
        else:
            char = buff[:-1]
            pos = dictionary[char]
            writeFile(pos, buff[-1])
        buff = ""

    if c is None or len(c) == 0:
      if buff in dictionary:
        pos = dictionary[buff]
        writeFile(pos, '')
      break

lzEndCompTime = time.time()
# ---------------- End Compressing ----------------
inFile.close()
outFile.close()

origSize = os.stat("sawyr10.txt").st_size
lzCompSize = os.stat('LZ-sawyr10.txt').st_size
lzCompTime = lzEndCompTime - lzStartCompTime
print('Uncompressed Size: %s bytes'%origSize)
print('Compressed Size: %s bytes'%lzCompSize)
print('Compression Ratio: %f'%(lzCompSize/origSize))
print('Elapsed Time for Compression: %s s' %lzCompTime)


# -------------------- Decompression -------------------------

compFile = open('LZ-sawyr10.txt', mode = 'rb')

clist = []

lzStartDecompTime = time.time()

while True:
    index = int.from_bytes(compFile.read(3),byteorder='big',signed=False)
    
    letter = compFile.read(2)
    if (len(letter) < 2):
      letter = ''
    else:
      letter = chr(int.from_bytes(letter,byteorder='big',signed=False))

    if index == 0:
        clist.append(letter)
    else:
        prefix = clist[index-1]
        clist.append(prefix+letter)
        
    if letter == '': break

string = ""
for i in clist:
    string += i;

unzipFile = open('DeLZ-sawyr10.txt', 'w')
unzipFile.write(string)
lzEndDecompTime = time.time()
compFile.close()
unzipFile.close()

lzDecompSize = os.stat('DeLZ-sawyr10.txt').st_size
print('Does Decompressed File match Original File? ', origSize == lzDecompSize)
lzElapsedDecompTime = lzEndDecompTime - lzStartDecompTime
print('Elapsed Time for Decompression: %s sec'%lzElapsedDecompTime)
