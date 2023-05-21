package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LC3BinaryTranslator {

    public static String translateOpcode(String opcode, int currentAddress) {
        if (opcode.length() != 16) {
            throw new IllegalArgumentException("Invalid opcode length");
        }

        // Split the opcode into its constituent parts
        String op = opcode.substring(0, 4);
        String dr = opcode.substring(4, 7);
        String sr1 = opcode.substring(7, 10);
        String pcOffset9 = opcode.substring(7, 16);
        String bit11 = opcode.substring(10, 11);
        String baseR = opcode.substring(7,10);
        String arg = opcode.substring(11);
        String lastThreeBits = opcode.substring(13, 16);
        String offset6 = opcode.substring(10,16);
        String bit5 = opcode.substring(4,5);

        // Create the string builder
        StringBuilder opcodeTranslation = new StringBuilder();

        // Translating opcodes
        if (op.equals("0001")) {  // ADD
            opcodeTranslation.append("ADD R").append(Integer.parseInt(dr, 2)).append(", R").append(Integer.parseInt(sr1, 2));
            if (bit11.equals("1")) {  // Immediate mode
                opcodeTranslation.append(", #").append(signedBinaryToDecimal(arg, false));
            } else {  // Register mode
                opcodeTranslation.append(", R").append(Integer.parseInt(lastThreeBits, 2));
            }
        }
        else if (op.equals("0101")) {  // AND
            opcodeTranslation.append("AND R").append(Integer.parseInt(dr, 2)).append(", R").append(Integer.parseInt(sr1, 2));
            if (bit11.equals("1")) {  // Immediate mode
                opcodeTranslation.append(", #").append(signedBinaryToDecimal(arg, false));
            } else {  // Register mode
                opcodeTranslation.append(", R").append(Integer.parseInt(lastThreeBits, 2));
            }
        }
        else if (op.equals("0000")) {  // BR
            String n = dr.substring(0, 1);
            String z = dr.substring(1, 2);
            String p = dr.substring(2);
            opcodeTranslation.append("BR");
            if (n.equals("1")) opcodeTranslation.append("n");
            if (z.equals("1")) opcodeTranslation.append("z");
            if (p.equals("1")) opcodeTranslation.append("p");
            opcodeTranslation.append(" ").append(signedBinaryToDecimal(pcOffset9, false));  // PC-relative
        }

        //NOT
        if (op.equals("1001")) {
            opcodeTranslation.append("NOT R").append(Integer.parseInt(dr, 2)).append(", R").append(Integer.parseInt(sr1, 2));
        }

        if (op.equals("1100")) {  // JMP and RET
            if (baseR.equals("111")){
                opcodeTranslation.append("RET");

            }
            else {
            opcodeTranslation.append("JMP R").append(Integer.parseInt(sr1, 2));
            }
        }

        if (op.equals("0010")) {  // LD
            int offset = signedBinaryToDecimal(pcOffset9, false);
            opcodeTranslation.append("LD R").append(Integer.parseInt(dr, 2)).append(", ").append(signedBinaryToDecimal(pcOffset9, false));
            opcodeTranslation.append(" ; R").append(Integer.parseInt(dr, 2)).append(" -> x").append(Integer.toHexString(currentAddress + offset + 1));
        }

        if (op.equals("1010")) {  // LDI
            int offset = signedBinaryToDecimal(pcOffset9, false);
            opcodeTranslation.append("LDI R").append(Integer.parseInt(dr, 2)).append(", ").append(signedBinaryToDecimal(pcOffset9, false));
            opcodeTranslation.append(" ; R").append(Integer.parseInt(dr, 2)).append(" -> x").append(Integer.toHexString(currentAddress + offset + 1));
        }

        if (op.equals("0110")) {  // LDR
            opcodeTranslation.append("LDR R").append(Integer.parseInt(dr, 2)).append(", R").append(Integer.parseInt(sr1, 2)).append(", #").append(Integer.parseInt(bit11 + arg, 2));
        }

        if (op.equals("1110")) {  // LEA
            int offset = signedBinaryToDecimal(pcOffset9, false);
            opcodeTranslation.append("LEA R").append(Integer.parseInt(dr, 2)).append(", ").append(signedBinaryToDecimal(pcOffset9, false));
            opcodeTranslation.append(" ; R").append(Integer.parseInt(dr, 2)).append(" -> x").append(Integer.toHexString(currentAddress + offset + 1));
        }

        if (op.equals("0011")) {  // ST
            int offset = signedBinaryToDecimal(pcOffset9, false);
            opcodeTranslation.append("ST R").append(Integer.parseInt(dr, 2)).append(", ").append(signedBinaryToDecimal(pcOffset9, false));
            opcodeTranslation.append(" ; R").append(Integer.parseInt(dr, 2)).append(" -> x").append(Integer.toHexString(currentAddress + offset + 1));
        }

        if (op.equals("1011")) {  // STI
            int offset = signedBinaryToDecimal(pcOffset9, false);
            opcodeTranslation.append("STI R").append(Integer.parseInt(dr, 2)).append(", ").append(signedBinaryToDecimal(pcOffset9, false));
            opcodeTranslation.append(" ; R").append(Integer.parseInt(dr, 2)).append(" -> x").append(Integer.toHexString(currentAddress + offset + 1));
        }

        if (op.equals("1000")) { // RTI
            opcodeTranslation.append("RTI");
             }

        if (op.equals("0111")) {  // STR
            opcodeTranslation.append("STR R").append(Integer.parseInt(dr, 2)).append(", R").append(Integer.parseInt(sr1, 2)).append(", #").append(signedBinaryToDecimal(offset6, false));
        }
        if (op.equals("0100")) {  // JSR or JSRR
            if (bit5.equals("1")) {  // JSR
                int offset = signedBinaryToDecimal(sr1 + arg, false);
                opcodeTranslation.append("JSR #").append(offset);
                opcodeTranslation.append(" ; Points to -> x").append(Integer.toHexString(currentAddress + offset + 1));
            } else {  // JSRR
                opcodeTranslation.append("JSRR R").append(Integer.parseInt(baseR, 2));
            }
        }

        if (op.equals("1111")) {  // TRAP
            String trapvec = bit11 + arg;  // Combine bit11 and arg to form 8-bit trap vector
            switch (trapvec) {
                case "100000":
                    opcodeTranslation.append("GETC");
                    break;
                case "100001":
                    opcodeTranslation.append("OUT");
                    break;
                case "100010":
                    opcodeTranslation.append("PUTS");
                    break;
                case "100011":
                    opcodeTranslation.append("IN");
                    break;
                case "100100":
                    opcodeTranslation.append("PUTSP");
                    break;
                case "100101":
                    opcodeTranslation.append("HALT");
                    break;
                default:
                    opcodeTranslation.append("TRAP UNKNOWN").append(" ; Unknown Trap - Check input");  // Default case for unsupported trap vectors
            }
        }

        return opcodeTranslation.toString();
    }

    // Helper function to handle sign extension
    private static int signedBinaryToDecimal(String binary, boolean isPCRelative) {
        int offset;
        if (binary.charAt(0) == '1') {
            offset = Integer.parseInt(binary, 2) - (1 << binary.length());
        } else {
            offset = Integer.parseInt(binary, 2);
        }
        if (isPCRelative) {
            offset += 1;
        }
        return offset;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the .ORIG of the program (e.g., 3000):");
        int currentAddress = Integer.parseInt(scanner.nextLine(), 16);  // Parse the input as a hexadecimal number
        System.out.println("Enter your binary strings without anything but the numbers, and an enter between each line - END WITH EMPTY LINE");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> lines = new ArrayList<>();
        String line;

        try {
            while (!(line = reader.readLine()).equalsIgnoreCase("")) {
                lines.add(line);
            }

            System.out.println(".ORIG x"+ Integer.toHexString(currentAddress));

            for (int i = 0; i < lines.size(); i++) {
                line = lines.get(i);
                System.out.println(translateOpcode(line, currentAddress));
                currentAddress++;  // Increment currentAddress after each line of binary code has been translated
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading input: " + e.getMessage());
        } finally {
            if (reader != null){
                reader.close();
            }
            System.out.println(".END");
        }
    }
}
