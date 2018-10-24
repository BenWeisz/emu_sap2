# SAP2 Emulator and Assembler

---

***Requirements***
---
- JDK1.8

***About SAP2***
---
The __SAP2__ is the second CPU outlined in __Albert Paul Malvino__ and __Jerald A. Brown's__ book __***Digital Computer Electronic***__.
Simple in its design, the __SAP2__ has 43 operation types. 

| Operation Type     | SAP Assembly  | OP Code    |
|:------------------:|:-------------:|:----------:|
| __Add__ value from B to register A | ADD B  | 0x80 |
| __Add__ value from C to register A | ADD C  | 0x81 |
| __AND__ value in register B with register A | ANA B | 0xA0 |
| __AND__ value in register C with register A | ANA C | 0xA1 |
| __AND__ the byte imediatly after this operation with register A | ANI 0xXX | 0xE6XX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Complement__ the A register | CMA | 0x2F |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |
| __Call__ the part of the program at the provided 2-Byte adress | CALL 0xXXXX | 0xCDXXXX |

__***Note***__: 
1. \<adr> is a 4-bit word with no prefix (either in hexidecimal or binary).
2. Use '; \<comment>'.

__***Specs***__:
- 16 bytes of memory
- 1 loadable register 
- Decimal output

***Usage***
---
1. __Write__ your assmebly code in a text file.
2. __Save__ the text file as \<INPUT_FILE>.txt
3. __Choose__ an output file name, in general: \<OUTPUT_FILE>.txt
4. __Run__ the following code in CMD, Terminal or any equivalent:
<br/></br>
  ```py assembler.py <INPUT_FILE>.txt <OUTPUT_FILE>.txt```
<br/></br>
5. __Run__ the following to run your program:
<br/></br>
  ```py emu.py <OUTPUT_FILE>.txt```
