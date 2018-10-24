# SAP2 Emulator and Assembler

---

***Requirements***
---
- JDK1.8

***About SAP2***
---
The __SAP2__ is the second CPU outlined in __Albert Paul Malvino__ and __Jerald A. Brown's__ book __***Digital Computer Electronic***__.
The __SAP2__ has 43 operation types in total. 

| Operation Type     | SAP Assembly  | OP Code    |
|:------------------:|:-------------:|:----------:|
| __Add__ value from B to register A | ADD B  | 0x80 |
| __Add__ value from C to register A | ADD C  | 0x81 |
| __AND__ value in register A with register B | ANA B | 0xA0 |
| __AND__ value in register A with register C | ANA C | 0xA1 |
| __AND__ the byte imediatly after this operation with register A | ANI 0xXX | 0xE6XX |
| __Call__ the part of the program at the provided 2-Byte adress and save the current address in the return pointer| CALL 0xXXXX | 0xCDXXXX |
| __Complement__ the A register | CMA | 0x2F |
| __Decrement__ the A register | DCR A | 0x3D |
| __Decrement__ the B register | DCR B | 0x05 |
| __Decrement__ the C register | DCR C | 0x0D |
| __Decrement__ the B register | DCR B | 0x05 |
| __Halt__ the emulator | HLT | 0x76 |
| __Input__ the provided byte into the A register | IN | 0xDB |
| __Increment__ the A register | INR A | 0x3C |
| __Increment__ the B register | INR B | 0x04 |
| __Increment__ the C register | INR C | 0x0C |
| __Jump__ to the provided 2-Byte address if the minus flag is set | JM 0xXXXX | 0xFAXXXX |
| __Jump__ to the provided 2-Byte address | JMP 0xXXXX | 0xC3XXXX |
| __Jump__ to the provided 2-Byte address if the zero flag is not set| JNZ 0xXXXX | 0xC2XXXX |
| __Jump__ to the provided 2-Byte address if the zero flag is set| JZ 0xXXXX | 0xCAXXXX |
| __Load__ register A with the contents of the follsing address| LDA 0xXXXX | 0x3AXXXX |
| __Move__ data in register B to register A | MOV A,B | 0x78 |
| __Move__ data in register C to register A | MOV A,C | 0x79 |
| __Move__ data in register A to register B | MOV B,A | 0x47 |
| __Move__ data in register C to register B | MOV B,C | 0x41 |
| __Move__ data in register A to register C | MOV C,A | 0x4F |
| __Move__ data in register B to register C | MOV C,B | 0x48 |
| __Move__ the following byte into to register A | MVI A, 0xXX | 0x3EXX |
| __Move__ the following byte into to register B | MVI B, 0xXX | 0x06XX |
| __Move__ the following byte into to register C | MVI C, 0xXX | 0x0EXX |
| __Wait__ for one operation period | NOP | 0x00 |
| __OR__ the value in the A register with the B register | ORA B | 0xB0 |
| __OR__ the value in the A register with the C register | ORA C | 0xB1 |
| __OR__ the value in the A register with the following byte | ORI 0xXX | 0xF6XX |
| __Output__ the value in the A register | OUT | 0xD3 |
| __Rotate__ the value in the A register left | RAL | 0x17 |
| __Rotate__ the value in the A register right | RAT | 0x1F |
| __Return__ to the address saved when CALL was run | RET | 0xC9 |
| __Store__ the value in the A register at the specified 2-Byte address | STA 0xXXXX | 0x32XXXX |
| __Subtract__ the value in the B register from the A register | SUB B | 0x90 |
| __Subtract__ the value in the C register from the A register | SUB C | 0x91 |
| __Xor__ the A register with the value of the B register | XRA B | 0xA8 |
| __Xor__ the A register with the value of the C register | XRA C | 0xA9 |
| __Xor__ the A register with the following byte | XRI 0xXX | 0xEEXX |


__***Note***__: 
1. Use '; \<comment>'.

__***Specs***__:
- 65536 bytes of memory
- 3 loadable registers
- hexadecimal output

***Usage***
---
1. __Write__ your assmebly code in a text file.
2. __Save__ the text file as \<INPUT_FILE>.txt
3. __Choose__ an output file name, in general: \<OUTPUT_FILE>.bin
4. __Run__ the following code in CMD, Terminal or any equivalent:
<br/></br>
  ```java Assembler <INPUT_FILE>.txt <OUTPUT_FILE>.bin```
<br/></br>
5. __Run__ the following to run your program:
<br/></br>
  ```java Emulator <OUTPUT_FILE>.bin```

***Example Program***
---
In the <INPUT_FILE>.txt, the folling program subtracts 7 from 3, and outputs the signed version in hexadecimal
  ```
  ;Program
  LDA 0x000A
  MOV B,A
  LDA 0x000B
  SUB B
  OUT
  HLT
  ;Data
  0x07
  0x04
  ```
  
  When run, the following program while output the following,
  ```
Emulator: 0xFD
Emulator: Emulation Complete!
  ```
