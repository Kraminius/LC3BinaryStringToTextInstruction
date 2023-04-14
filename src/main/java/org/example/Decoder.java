package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Decoder {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        boolean processedInput = false;
        while ((line = reader.readLine()) != null) {
            line = line.trim(); // trim the input string
            if (line.isEmpty()) {
                continue; // skip empty lines
            }
            if (line.matches("[01]{16}")) {
                try {
                    LC3Instruction instruction = LC3Instruction.decode(line);
                    System.out.println(instruction);
                    processedInput = true;
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid binary string: " + line);
                }
            } else {
                System.err.println("Invalid binary string: " + line);
            }
        }
        if (!processedInput) {
            System.out.println(); // print an empty line if no input was processed
        }
    }
}