# 2DH key exchange for Initiator
import sys
import os
import socket
import time
import random
from Crypto.Hash import SHA256

# Create socket
init_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
init_socket.bind(("", 9417))
init_socket.listen(5)

print ("Ready to start KE")

while 1:
    res_socket, address = init_socket.accept()
    print ("New Connection from ", address[0], ":", address[1])
    start_time = time.time()
    # For 2DH Key exchange
    q = 99877
    G = 99875
    x = random.randint(1, q-1)
    alpha = pow(G, x)%q
    
    Alpha = str(alpha)
    res_socket.send(Alpha.encode())
    
    # introduce session Key (not for security purposes)
    urand = os.urandom(64)
    session_id = SHA256.new(urand)
    session_id = session_id.digest()
    del(urand)
    
    time.sleep(1)
    res_socket.send(session_id)
    Beta = res_socket.recv(128).decode()
    Beta = int(Beta)
    
    Ka = pow(Beta, x)%q
    # discard x
    del(x)
    gamma = session_id + Ka.to_bytes(Ka.bit_length(), sys.byteorder)
    
    end_time = time.time()
    elapsed_time = end_time - start_time
    print(elapsed_time)
    print ("2DH Key Exchange Protocol Successful")
    
    res_socket.close()
    

init_socket.close()

