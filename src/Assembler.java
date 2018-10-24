import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

class Assembler {
    private static final String[] opCodeNames = new String[]{
            "ADD B", "ADD C", "ANA B", "ANA C", "ANI", "CALL",
            "CMA", "DCR A", "DCR B", "DCR C", "HLT", "IN",
            "INR A", "INR B", "INR C", "JM", "JMP", "JNZ",
            "JZ", "LDA", "MOV A,B", "MOV A,C", "MOV B,A", "MOV B,C",
            "MOV C,A", "MOV C,B", "MVI A,", "MVI B,", "MVI C,", "NOP",
            "ORA B", "ORA C", "ORI", "OUT", "RAL", "RAR",
            "RET", "STA", "SUB B", "SUB C", "XRA B", "XRA C",
            "XRI"
    };
    private static final byte[] opCodes = new byte[]{
            (byte)0x80, (byte)0x81, (byte)0xA0, (byte)0xA1,
            (byte)0xE6, (byte)0xCD, (byte)0x2F, (byte)0x3D,
            (byte)0x05, (byte)0x0D, (byte)0x76, (byte)0xDB,
            (byte)0x3C, (byte)0x04, (byte)0x0C, (byte)0xFA,
            (byte)0xC3, (byte)0xC2, (byte)0xCA, (byte)0x3A,
            (byte)0x78, (byte)0x79, (byte)0x47, (byte)0x41,
            (byte)0x4F, (byte)0x48, (byte)0x3E, (byte)0x06,
            (byte)0x0E, (byte)0x00, (byte)0xB0, (byte)0xB1,
            (byte)0xF6, (byte)0xD3, (byte)0x17, (byte)0x1F,
            (byte)0xC9, (byte)0x32, (byte)0x90, (byte)0x91,
            (byte)0xA8, (byte)0xA9, (byte)0xEE
    };

    private static HashMap<String, Byte> createMapping(){
        HashMap<String, Byte> mapping = new HashMap<>();
        for (int i = 0; i < opCodeNames.length; i++)
            mapping.put(opCodeNames[i], opCodes[i]);

        return mapping;
    }

    static void assemble(String inPath, String outPath){
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileInputStream fstream = new FileInputStream(inPath);
            BufferedReader in = new BufferedReader(new InputStreamReader(fstream));

            String line;
            while ((line = in.readLine()) != null)   {
                lines.add(line);
            }

            in.close();
        } catch (IOException ioe){
            System.err.println("Error Opening File: " + inPath);
        }

        ArrayList<Byte> bytes = new ArrayList<>();
        HashMap<String, Byte> map = createMapping();

        for (String l: lines) {
            int commentPosition = l.indexOf(';');
            if (commentPosition == 0)
                continue;

            String codeLine;

            if (commentPosition >= 0)
                codeLine = l.substring(0, commentPosition);
            else codeLine = l;

            String[] tokens = codeLine.trim().split(" ");

            if (tokens.length == 1 && tokens[0].length() == 4){
                if(tokens[0].substring(0, 2).equals("0x"))
                    bytes.add((byte)Integer.parseInt(tokens[0].substring(2), 16));
            }
            else if(tokens.length == 1){
                Byte opCode = map.get(tokens[0]);
                if (opCode != null)
                    bytes.add(opCode);
                else {
                    System.err.println("The Following Line Is Invalid: " + codeLine);
                    System.exit(1);
                }
            }
            else if (tokens.length == 2){
                if (tokens[1].startsWith("0x")){
                    Byte opCode = map.get(tokens[0]);
                    if (opCode != null){
                        bytes.add(opCode);
                        if (tokens[1].length() == 4)
                            bytes.add((byte)Integer.parseInt(tokens[1].substring(2), 16));
                        else if (tokens[1].length() == 6){
                            bytes.add((byte)Integer.parseInt(tokens[1].substring(4, 6), 16));
                            bytes.add((byte)Integer.parseInt(tokens[1].substring(2, 4), 16));
                        }
                    }
                    else {
                        System.err.println("The Following Line Is Invalid: " + codeLine);
                        System.exit(1);
                    }
                }
                else {
                    Byte opCode = map.get(tokens[0] + " " + tokens[1]);
                    if (opCode != null)
                        bytes.add(opCode);
                    else {
                        System.err.println("The Following Line Is Invalid: " + codeLine);
                        System.exit(1);
                    }
                }
            }
            else if (tokens.length == 3){
                Byte opCode = map.get(tokens[0] + " " + tokens[1]);
                if (opCode != null){
                    bytes.add(opCode);
                    bytes.add((byte)Integer.parseInt(tokens[2].substring(2), 16));
                }
                else {
                    System.err.println("The Following Line Is Invalid: " + codeLine);
                    System.exit(1);
                }
            }
        }

        byte[] byteData = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++){
            byteData[i] = bytes.get(i);
        }

        try {
            FileOutputStream fos = new FileOutputStream(outPath);
            fos.write(byteData);
            fos.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
