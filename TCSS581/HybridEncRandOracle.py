import sys
import os
import time
from math import gcd
from Crypto.Cipher import AES
from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA
from Crypto.Random import random

key = RSA.generate(1024) 
public_key = key.publickey().exportKey("PEM") 
private_key = key.exportKey("PEM")
N = getattr(key.key, 'n')
e = getattr(key.key, 'e')
d = getattr(key.key, 'd')

def select_r(Number):
    while 1:
        randnum = random.randrange(1,Number)
        if gcd(Number, randnum) == 1:
            break
    return randnum

r = select_r(N)

cipher1 = pow(r,e,N)

bible = ""
with open("king_james.txt",'r') as fileobject:
    for line in fileobject:
        bible+=line


# Encryption
enc_key = SHA256.new(r.to_bytes(r.bit_length(),byteorder=sys.byteorder))
enc_key = enc_key.digest()
#print(enc_key)

secret = os.urandom(16)
crypt_obj = AES.new(enc_key, AES.MODE_CTR, counter=lambda: secret)

start_time_encrypt = time.time()
cipher_text = crypt_obj.encrypt(bible)
end_time_encrypt = time.time()
encrypt_time = end_time_encrypt - start_time_encrypt
print(encrypt_time)


# Decryption

if pow(cipher1,d,N) != r:
    print("Error")
else:
    start_time_decrypt = time.time()
    decrypt_plain_text = crypt_obj.decrypt(cipher_text)
    end_time_decrypt = time.time()
    decrypt_time = end_time_decrypt - start_time_decrypt
    print(decrypt_time)

