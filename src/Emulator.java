import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Emulator {
    private static final int MAX_MEM_SIZE = 65536;
    private static byte[] MEM;
    private static int PC = 0x00000000, R = 0x00000000;
    private static byte A = 0x00, B = 0x00, C = 0x00, F = 0x00;
    private static boolean HLT = false;

    private static void start(){
        while(!HLT && PC < MAX_MEM_SIZE)
            Emulator.run();

        System.out.println("Emulator: Emulation Complete!");
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
            A = MEM[MEM[PC] | (MEM[PC + 1] << 8)];
            PC += 2;
        }
        // MOV A,B
        else if (data == (byte)0x78){
            A = B;
        }
        // MOV A,C
        else if (data == (byte)0x79){
            A = C;
        }
        // MOV B,A
        else if (data == (byte)0x47){
            B = A;
        }
        // MOV B,C
        else if (data == (byte)0x41){
            B = C;
        }
        // MOV C,A
        else if (data == (byte)0x4F){
            C = A;
        }
        // MOV C, B
        else if (data == (byte)0x48){
            C = B;
        }
        // MVI A, $
        else if (data == (byte)0x3E){
            A = MEM[PC++];
        }
        // MVI B, $
        else if (data == (byte)0x06){
            B = MEM[PC++];
        }
        // MVI C, $
        else if (data == (byte)0x0E){
            C = MEM[PC++];
        }
        // NOP
        else if (data == (byte)0x00){

        }
        // ORA B
        else if (data == (byte)0xB0){
            A = (byte)(A | B);
            updateFlags();
        }
        // ORA C
        else if (data == (byte)0xB1){
            A = (byte)(A | C);
            updateFlags();
        }
        // ORI $
        else if (data == (byte)0xF6){
            A = (byte)(A | MEM[PC++]);
        }
        // OUT
        else if (data == (byte)0xD3){
            System.out.println("Emulator: 0x" + Integer.toHexString(Byte.toUnsignedInt(A)).toUpperCase());
        }
        // RAL
        else if (data == (byte)0x17){
             int t = (A >> 7) & 0x01;
             A = (byte)(((A << 1) & 0xFF) | t);
        }
        // RAR
        else if (data == (byte)0x1F){
            int t = A & 0x01;
            A = (byte)((A >> 1) | (t << 7));
        }
        // RET
        else if (data == (byte)0xC9){
            PC = R;
        }
        // STA $$
        else if (data == (byte)0x32){
            MEM[MEM[PC] | (MEM[PC + 1] << 8)] = A;
            PC += 2;
        }
        // SUB B
        else if (data == (byte)0x90){
            byte BTC = add((byte)(B ^ 0xFF), (byte)1);
            A = add(A, BTC);
            updateFlags();
        }
        // SUB C
        else if (data == (byte)0x91){
            byte CTC = add((byte)(C ^ 0xFF), (byte)1);
            A = add(A, CTC);
            updateFlags();
        }
        // XRA B
        else if (data == (byte)0xA8){
            A = (byte)(A ^ B);
            updateFlags();
        }
        // XRA C
        else if (data == (byte)0xA9){
            A = (byte)(A ^ C);
            updateFlags();
        }
        // XRI
        else if (data == (byte)0xEE){
            A = (byte)(A ^ MEM[PC++]);
            updateFlags();
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
        if (((A >> 7) & 0x01) == 1)
            F = (byte)(F | 0x2);
        else F = (byte)(F & (0x2 ^ 0xFF));
    }

    public static void main(String[] args) {
        byte[] memory = Emulator.packageROM(args[0]);
        Emulator.loadROM(memory);
        Emulator.start();
    }

}
