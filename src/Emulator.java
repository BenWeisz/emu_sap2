import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class Emulator {
    private static final int MAX_MEM_SIZE = 65536;
    private static byte[] MEM;
    private static int PC = 0x00000000;
    private static byte A = 0x00, B = 0x00, C = 0x00, F = 0x00;

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
    public static void run(){
        byte data = Emulator.MEM[Emulator.PC];

        PC++;
        if (PC == MAX_MEM_SIZE)
            PC = 0;

        // ADD B
        if (data == 0x80) {
            A = add(A, B);
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
            F = (byte)(F | 1);
        else F = (byte)(F & (1 ^ 0xFF));

        //Negative Flag
        if (A >> 7 == 1){

        }
    }

    public static void main(String[] args) {
        byte[] memory = Emulator.packageROM(args[0]);
        Emulator.loadROM(memory);
    }
}
