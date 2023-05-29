################################################################################
# Decrypt an encrypted data using the AES algorithm                            #
################################################################################

import os, sys, traceback
from Cryptodome.Cipher import AES
import ujson
import base64

KEY_FILE_NAME = 'aes-key.txt'
initial_vector = 'FmsRVks2VoUDFPhXEVT0Tw=='

unpad = lambda s: s[:-ord(s[len(s) - 1:])]

def read_key():
    with open(KEY_FILE_NAME, 'r') as file:
        return file.read()

def read_file(file):
    origin = file.read()
    
    key = base64.b64decode(read_key())
    iv = base64.b64decode(initial_vector)
    aes = AES.new(key, AES.MODE_CBC, iv)
    
    plain = aes.decrypt(origin)
    plain = unpad(plain)
    
    decoded = plain.decode('utf-8')    
    return ujson.loads(decoded)

def main():
    with open('encrypted.dat', 'rb') as file:
        json_arr_data = read_file(file)
        print(json_arr_data)

if __name__ == '__main__':
    try:
        main()
    except:
        traceback.print_exc(file=sys.stdout)
