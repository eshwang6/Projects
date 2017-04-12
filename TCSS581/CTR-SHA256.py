# -*- coding: utf-8 -*-
import os
import time
from Crypto.Cipher import AES
from Crypto.Hash import HMAC


master_key = os.urandom(16)
enc_key = master_key + b"EncKEncKEncKEncK"
mac_key = master_key + b"AuthAuthAuthAuth"

secret = os.urandom(16)
#print(secret)

bible = ""
with open("king_james.txt",'r') as fileobject:
    for line in fileobject:
        bible+=line
print("Length of the Input File")
print(len(bible))
#bible

# Encryption

crypt_obj = AES.new(enc_key, AES.MODE_CTR, counter=lambda: secret)

start_time_encrypt = time.time()
cipher_text = crypt_obj.encrypt(bible)
end_time_encrypt = time.time()
encrypt_time = end_time_encrypt - start_time_encrypt

print("Encrypted Length")
print(len(cipher_text))

sig = HMAC.new(mac_key, cipher_text).digest()
#print(sig)

# Decryption

testSig = HMAC.new(mac_key, cipher_text).digest()

start_time_decrypt = time.time()

if testSig != sig:
    print("MAC Tag Error")
else:
    decrypt_plain_text = crypt_obj.decrypt(cipher_text)
    end_time_decrypt = time.time()
    decrypt_time = end_time_decrypt - start_time_decrypt
    print("Decrypted Length")
    print(len(decrypt_plain_text))
#    print(decrypt_plain_text)

end_time_decrypt = time.time()
decrypt_time = end_time_decrypt - start_time_decrypt

print(encrypt_time)
print(decrypt_time)


