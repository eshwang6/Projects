# -*- coding: utf-8 -*-

import os
import time
import sys
from queue import PriorityQueue

class Node:
    def __init__(self, value=None, key=None, code=None, left=None, right=None):
        self.value = value
        self.key = key
        self.code = code
        self.left = left
        self.right = right
    
    def __str__(self):
        return 'Node <%s>: value: %s code: %s\n\tLeft: %s \n\tRight: %s\n'%(self.key,self.value,self.code,self.left,self.right)
    
    def __contains__(self,key):
        return key == self.key
    
    def __add__(self,node2):
        self.propagateCode(self,'0')
        node2.propagateCode(node2,'1')
        return Node(self.value+node2.value,self.key+node2.key,None,self,node2)
    
    def propagateCode(self,node,code):
        if node.code == None:
            node.code = code
        else:
            node.code = code + node.code
        if node.left:
            node.propagateCode(node.left,code)
        if node.right:
            node.propagateCode(node.right,code)

    def __gt__(self, other):
        return self.value > other.value

    def __lt__(self, other):
        return self.value < other.value

    def __eq__(self, other):
        if (self is None) and (other is not None): return False
        if (self is not None) and (other is None): return False
        if (self is None) and (other is None): return True
        return self.value == other.value

def compress(inFile,outFile):
    infp = open(inFile,'r')
    text = infp.read()
    outfp = open(outFile,'wb')
    
    nodes = {}
    
    for char in text:
        if char in nodes:
            nodes[char].value += 1
        else:
            nodes[char] = Node(1,char)
            
    EOF = chr(255)+chr(255)
    nodes[EOF] = Node(1,EOF)
            
    pq = PriorityQueue()
    for node in nodes.values():
        pq.put(node)

    if (pq.qsize() < 2):
        sys.exit('Document has less than 2 unique characters')
    min1 = pq.get(False)
    min2 = pq.get(False)
    while True:
        pq.put(min1+min2)
        min1 = pq.get(False)
        if (pq.qsize() == 0):
            break
        min2 = pq.get(False)

    root = min1
    
    output = ''.join([nodes[c].code for c in text])

    output+= nodes[EOF].code
    header = traverseTree(root) + '1111111111111111'

    output = header + output
    
    while len(output) % 8 != 0:
        output +='0'

    output = bytes([int(output[i*8:((i+1)*8)],2) for i in range(int(len(output) / 8))])
    size = outfp.write(output)
    infp.close()
    outfp.close()
    
    return size

def traverseTree(root):
    code = ''
    if not root:
        return code
    if root.left or root.right:
        code += '0'
    else:
        code += '1'
        if len(root.key)==2:
            code += '1111111111111111'
        else:
            ASCII = bin(ord(root.key))[2:]
            while len(ASCII) != 8:
                ASCII = '0'+ASCII
            code += ASCII
    return code + traverseTree(root.left)+traverseTree(root.right)

class decompress:
    def __init__(self,inFile,outFile):
        self.code = ''
        self.EOF = chr(255)+chr(255)
        self.nodes = {}
        self.EOFfound = False
        self.infp = open(inFile,'rb')
        self.outfp = open(outFile,'w')
        self.output = ''
        self.process()
    
    def process(self):
        self.gatherInput()
        self.rebuildTree()
        self.code = self.code[16:]
        self.decode()
        self.writeFile()
        self.cleanUp()

    def gatherInput(self):
        text = self.infp.read()
        self.code += ''.join(['{:08b}'.format(byte) for byte in text])

    def rebuildTree(self,root=None):
        while self.code:
            if self.code[0] == '0':
                if root == None:
                    root = Node()
                    self.code = self.code[1:]
                    self.rebuildTree(root)
                else:
                    if not root.left:
                        if root.code == None:
                            root.left = Node(None,None,'0')
                        else:
                            root.left = Node(None,None,root.code+'0')
                        self.code = self.code[1:]
                        self.rebuildTree(root.left)
                    elif not root.right:
                        if root.code == None:
                            root.right = Node(None,None,'1')
                        else:
                            root.right = Node(None,None,root.code+'1')
                        self.code = self.code[1:]
                        self.rebuildTree(root.right)
                    else:
                        return
            else:
                if self.code[0:16] == '1'*16 and self.EOFfound:
                    return root
                if not root.left:
                    if self.code[1:17] == '1'*16 and not self.EOFfound:
                        leaf = chr(int(self.code[1:9],2))+chr(int(self.code[9:17],2))
                        self.code = self.code[8:]
                        self.EOFfound = True
                    else:
                        leaf = chr(int(self.code[1:9],2))
                    root.left = Node(0,leaf,root.code+'0')
                    self.nodes[root.left.code] = leaf
                    self.code = self.code[9:]
                elif not root.right:
                    if self.code[1:17] == '1'*16 and not self.EOFfound:
                        leaf = chr(int(self.code[1:9],2))+chr(int(self.code[9:17],2))
                        self.code = self.code[8:]
                        self.EOFfound = True
                    else:
                        leaf = chr(int(self.code[1:9],2))
                    root.right = Node(0,leaf,root.code+'1')
                    self.nodes[root.right.code] = leaf
                    self.code = self.code[9:]
                else:
                    return
                self.rebuildTree(root)
    
    def decode(self):
        start = 0
        end = 1
        while end <= len(self.code):
            if self.code[start:end] in self.nodes.keys():
                if self.nodes[self.code[start:end]] == self.EOF:
                    return
                self.output += self.nodes[self.code[start:end]]
                start = end
                end += 1
            else:
                end += 1

    def writeFile(self):
        self.outfp.write(self.output)

    def cleanUp(self):
        self.infp.close()
        self.outfp.close()


origSize = os.stat("sawyr10.txt").st_size
huffStartCompTime = time.time()
huffCompSize = compress("sawyr10.txt", "huffzip-sawyr10.txt")
huffEndCompTime = time.time()
huffElapsedCompTime = huffEndCompTime - huffStartCompTime
print('Uncompressed Size: %s bytes'%origSize)
print('Compressed Size: %s bytes'%huffCompSize)
print('Compression Ratio: %f'%(huffCompSize/origSize))
print('Elapsed Time for Compression: %s s' %huffElapsedCompTime)

huffStartDecompTime = time.time()
decompress("huffzip-sawyr10.txt", "dehuff-sawyr10.txt")
huffEndDecompTime = time.time()
huffElapsedDecompTime = huffEndDecompTime - huffStartDecompTime
huffDecompSize = os.stat('dehuff-sawyr10.txt').st_size
print('Does Decompressed File match Original File? ', origSize == huffDecompSize)
print('Elapsed Time for Decompression: %s sec'%huffElapsedDecompTime)

