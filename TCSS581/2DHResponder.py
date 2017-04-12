# 2DH key exchange for Responder
import sys
import socket
import time
import random

start_time = time.time()
# For 2DH Key exchange
q = 99877
G = 99875
y = random.randint(1, q-1)
beta = pow(G, y)%q

res_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
res_socket.connect(("127.0.0.1", 9417))

Alpha = res_socket.recv(128).decode()

# obtain session Key
session_id = res_socket.recv(128)
Beta = str(beta)
res_socket.send(Beta.encode())

Alpha = int(Alpha)

Kb = pow(Alpha, y)%q
# discard y
del(y)

gamma = session_id + Kb.to_bytes(Kb.bit_length(), sys.byteorder)

print ("2DH Key Exchange Protocol Successful")

res_socket.close()
end_time = time.time()
elapsed_time = end_time - start_time
print(elapsed_time)
