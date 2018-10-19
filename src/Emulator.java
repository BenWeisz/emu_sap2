import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Emulator {
    private static final int MAX_MEM_SIZE = 65536;
    private static byte[] MEM;
    private static int PC = 0x00000000, R = 0x00000000;
    private static byte A = 0x00, B = 0x00, C = 0x00, F = 0x00;
    private static boolean HLT = false;

    public static void start(){
        while(!HLT && PC < MAX_MEM_SIZE)
            Emulator.run();
    }
    private static void loadROM(byte[] MEM){
        if (MEM.length <= MAX_MEM_SIZE) {
            Emulator.MEM = new byte[MAX_MEM_SIZE];

            for (int i = 0; i < MAX_MEM_SIZE; i++) {
                if (i < MEM.length)
                    Emulator.MEM[i] = MEM[i];
                else Emulator.MEM[i] = 0x00;
            }
        }
        else {
            System.err.println("Initial MEM size must be less than or equal to 65536B!");
            System.exit(1);
        }
    }
    private static byte[] packageROM(String path) {
        byte[] mem = null;

        try {
            FileInputStream file = new FileInputStream(path);

            mem = new byte[file.available()];
            file.read(mem);
            file.close();
        }catch (IOException e){
            System.err.println("You have specified an incorrect path!");
            System.exit(1);
        }

        return mem;
    }
    private static void run(){
        byte data = Emulator.MEM[Emulator.PC++];

        // ADD B
        if (data == (byte)0x80) {
            A = add(A, B);
            updateFlags();
        }
        // ADD C
        else if (data == (byte)0x81){
            A = add(A, C);
            updateFlags();
        }
        // ANA B
        else if (data == (byte)0xA0){
            A = (byte)(A & B);
            updateFlags();
        }
        // ANA C
        else if (data == (byte)0xA1){
            A = (byte)(A & B);
            updateFlags();
        }
        // ANI $
        else if (data == (byte)0xE6){
            A = (byte)(A & MEM[PC++]);
            updateFlags();
        }
        // CALL $$
        else if (data == (byte)0xCD){
            R = PC;
            PC = MEM[R] | (MEM[R + 1] << 8);
        }
        // CMA
        else if (data == (byte)0x2F){
            A ^= (byte)0xFF;
        }
        // DCR A
        else if (data == (byte)0x3D){
            A = add(A, (byte)0xFF);
            updateFlags();
        }
        // DCR B
        else if (data == (byte)0x05){
            A = B;
            A = add(A, (byte)0xFF);
            B = A;
            updateFlags();
        }
        // DCR C
        else if (data == (byte)0x0D){
            A = C;
            A = add(A, (byte)0xFF);
            C = A;
            updateFlags();
        }
        //HLT
        else if (data == (byte)0x76){
            HLT = true;
        }
        // IN
        else if (data == (byte)0xDB){
            Scanner in = new Scanner(System.in);
            System.out.println("EMU_SAP2: An input byte is required. Enter byte in hex format 0x$$");
            String hex = in.next();
            A = (byte)Integer.parseInt("" + hex.charAt(2) + hex.charAt(3), 16);
        }
        // INR A
        else if (data == (byte)0x3C){
            A = add(A, (byte)0x01);
            updateFlags();
        }
        // INR B
        else if (data == (byte)0x04){
            A = B;
            A = add(A, (byte)0x01);
            B = A;
            updateFlags();
        }
        // INR C
        else if (data == (byte)0x0C){
           A = C;
           A = add(A, (byte)0x01);
           C = A;
           updateFlags();
        }
        // JM
        else if (data == (byte)0xFA){
            if (((F >> 1) & 0x01) == 1){
                PC = MEM[PC] | (MEM[PC + 1] << 8);
            }
            else PC += 2;
        }
        // JMP
        else if (data == (byte)0xC3){
            PC = MEM[PC] | (MEM[PC + 1] << 8);
        }
        // JNZ
        else if (data == (byte)0xC2){
            if ((F & 0x01) == 0){
                PC = MEM[PC] | (MEM[PC + 1] << 8);
            }
            else PC += 2;
        }
        // JZ
        else if (data == (byte)0xCA){
            if ((F & 0x01) == 1){
                PC = MEM[PC] | (MEM[PC + 1] << 8);
            }
            else PC += 2;
        }
        // LDA
        else if (data == (byte)0x3A){
            
        }
    }
    private static byte add(byte a, byte b){
        byte s = a;
        byte c = 0x00;

        for(int i = 0; i < 8; i++){
            byte tA = (byte)((a >> i) & 0x01);
            byte tB = (byte)((b >> i) & 0x01);

            byte t = (byte)(tA + tB + c);
            c = (byte)(t >> 1);

            t = (byte)(t & 1);
            if (t == 0)
                s = (byte)(s & ((1 << i) ^ 0xFF));
            else s = (byte)(s | (1 << i));
        }

        return s;
    }
    private static void updateFlags(){
        //Zero Flag
        if (A == 0x00)
            F = (byte)(F | 0x1);
        else F = (byte)(F & (0x1 ^ 0xFF));

        //Negative Flag
        if (A >> 7 == 1)
            F = (byte)(F | 0x2);
        else F = (byte)(F & (0x2 ^ 0xFF));
    }

    public static void main(String[] args) {
        byte[] memory = Emulator.packageROM(args[0]);
        Emulator.loadROM(memory);

        System.out.println(Integer.parseInt("FF", 16));
    }

}
