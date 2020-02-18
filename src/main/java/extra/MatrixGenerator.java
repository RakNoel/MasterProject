package extra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class MatrixGenerator {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter size of matrix: ");
        int x = scn.nextInt();
        int y = scn.nextInt();
        scn.nextLine();
        System.out.print("Enter name of new file: ");
        String name = scn.nextLine();
        System.out.printf("Generating new matrix. W:%d H:%d output:%s %n", x, y, name);

        File file = new File(name);
        try {
            FileOutputStream fout = new FileOutputStream(file);
            fout.write((x + " " + y + System.lineSeparator()).getBytes());
            Random rnd = new Random();
            for (int i = 0; i < y; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < x; j++)
                    row.append(rnd.nextInt(2));
                row.append(System.lineSeparator());
                fout.write(row.toString().getBytes());
            }
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
