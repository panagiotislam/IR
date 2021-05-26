import re
import sys
import numpy
import numpy.linalg
import os


ins = open( "txdArray.txt", "r" )

data = []
for line in ins:
    number_strings = line.split() # Split the line on runs of whitespace
    numbers = [int(n) for n in number_strings] # Convert to integers
    data.append(numbers) # Add the "row" to your list.
#print(data) # [[1, 3, 4], [5, 5, 6]]


numpy.set_printoptions(formatter={"float": lambda x:("%2.3f" %x)})
A = numpy.array(data)
#B = numpy.array([[1, 1, 0, 1, 0, 0],[1, 0, 1, 1, 0, 0],[1, 1, 1, 2, 1, 1],[0, 0, 0, 1, 1, 1]])
print(A)
print(A.shape)
#print()
#print(B)
print("SVD:")
print()
U, S, V = numpy.linalg.svd(A)
print("U:")
print(U.shape)
print(U)
print()
print("S:")
print(S.shape)
print(S)
print()
print("V:")
print(V.shape)
print(V)
print()
print("S diag:")
S = numpy.diag(S)
print(S.shape)
U = U[:,:1400]
print("Verify that USV = A:")
print(U.dot(S).dot(V))
print()
print("Verify that the columns of U are orthonormal:")
print(U.transpose().dot(U))
print()
print("Verify that the rows of V are orthonormal:")
print(V.dot(V.transpose()))
print()
k=50
print("Rank %d approximation of A:" %k)
Uk = U[:, :k]
#Sk = S[:k, :k]
Sk = numpy.diag(S)
Sk = S[:k, :k]
Vk = V[:k, :]
print(Uk.shape)
print(Sk.shape)
print(Vk.shape)
Ak = Uk.dot(Sk).dot(Vk)
print(Ak.shape)
print(Ak)
with open("output.txt", "w") as txt_file:
    for line in Ak:
        for value in line:
            txt_file.write(str(value)+" ")
        txt_file.write("\n")
        
        
        

ins = open( "tx1.txt", "r" )

data2 = []
for line in ins:
    number_strings = line.split() # Split the line on runs of whitespace
    numbers = [int(n) for n in number_strings] # Convert to integers
    data2.append(numbers)  

B = numpy.array(data2)    

C = numpy.dot(B.T, A)
print(C.shape)
print(C)

with open("q4.txt", "w") as txt_file:
    for line in C:
        for value in line:
            txt_file.write(str(value)+" ")
        txt_file.write("\n")