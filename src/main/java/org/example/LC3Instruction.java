package org.example;

public class LC3Instruction {

    LC3Opcode opcode;
    int dr;
    int sr1;
    int sr2;

    int imm5value;
    int offset6;
    int baseR;
    int trapvect8;

    String trapMessage;

    public LC3Instruction(LC3Opcode opcode, int dr, int sr1, int sr2, int imm5value, int offset6, int baseR, int trapvect8, String trapMessage) {
        this.opcode = opcode;
        this.dr = dr;
        this.sr1 = sr1;
        this.sr2 = sr2;
        this.imm5value = imm5value;
        this.offset6 = offset6;
        this.baseR = baseR;
        this.trapvect8 = trapvect8;
        this.trapMessage = trapMessage;
    }

    public static LC3Instruction decode(String binaryString) {
        String opcode = binaryString.substring(0, 4);

        switch (opcode) {
            case "0001":
                // ADD instruction
                int drIndex = Integer.parseInt(binaryString.substring(4, 7), 2);
                int sr1Index = Integer.parseInt(binaryString.substring(7, 10), 2);

                boolean isImmediate = Integer.parseInt(binaryString.substring(10, 11)) == 1;

                if (isImmediate) {
                    // ADD instruction with immediate value
                    int parsedImm = Integer.parseInt(binaryString.substring(11, 16), 2);
                    int immediateValue = signExtend(parsedImm, 5);
                    return new LC3Instruction(LC3Opcode.ADD, drIndex, sr1Index, 40000, immediateValue, 40000, 40000, 40000, null);
                } else {
                    // ADD instruction with two registers
                    int sr2 = Integer.parseInt(binaryString.substring(13, 16), 2);
                    return new LC3Instruction(LC3Opcode.ADD, drIndex, sr1Index, sr2, 40000, 40000, 40000, 40000, null);
                }

            case "0101":
                // AND instruction
                int drIndex8 = Integer.parseInt(binaryString.substring(4, 7), 2);
                int srIndex9 = Integer.parseInt(binaryString.substring(7, 10), 2);
                int srIndex10 = Integer.parseInt(binaryString.substring(13, 16), 2);
                return new LC3Instruction(LC3Opcode.AND, drIndex8, srIndex9, 40000, 40000, 40000, srIndex10, 40000, null);

            case "0000":
                // BR instruction
                boolean n = binaryString.charAt(4) == '1';
                boolean z = binaryString.charAt(5) == '1';
                boolean p = binaryString.charAt(6) == '1';
                int pcOffset = signExtend(Integer.parseInt(binaryString.substring(7), 2), 9);
                return new LC3Instruction(LC3Opcode.BR, 40000, 40000, 40000, 40000, pcOffset, (n ? 4 : 0) + (z ? 2 : 0) + (p ? 1 : 0), 40000, null);

            case "1100":
                // JMP or RET instruction
                if (binaryString.charAt(3) == '0') {
                    // JMP instruction
                    int baseRIndex = Integer.parseInt(binaryString.substring(4, 7), 2);
                    return new LC3Instruction(LC3Opcode.JMP, 40000, baseRIndex, 40000, 40000, 40000, 40000, 40000, null);
                } else {
                    // RET instruction
                    return new LC3Instruction(LC3Opcode.RET, 40000, 40000, 40000, 40000, 40000, 40000, 40000, null);
                }

            case "0100":
                // JSR or JSRR instruction
                if (binaryString.charAt(3) == '0') {
                    // JSRR instruction
                    int baseRIndex = Integer.parseInt(binaryString.substring(4, 7), 2);
                    return new LC3Instruction(LC3Opcode.JSRR, baseRIndex, 40000, 40000, 40000, 40000, 40000, 40000, null);
                } else {
                    // JSR instruction
                    int pcOffset11 = signExtend(Integer.parseInt(binaryString.substring(4), 2), 11);
                    return new LC3Instruction(LC3Opcode.JSR, 40000, 40000, 40000, 40000, pcOffset11, 40000, 40000, null);
                }

            case "0010":
                // LD instruction
                int drIndex1 = Integer.parseInt(binaryString.substring(4, 7), 2);
                int pcOffset9 = signExtend(Integer.parseInt(binaryString.substring(7), 2), 9);
                return new LC3Instruction(LC3Opcode.LD, drIndex1, 40000, 40000, 40000, pcOffset9, 40000, 40000, null);

            case "1010":
                // LDI instruction
                int drIndex2 = Integer.parseInt(binaryString.substring(4, 7), 2);
                int pcOffset9_2 = signExtend(Integer.parseInt(binaryString.substring(7), 2), 9);
                return new LC3Instruction(LC3Opcode.LDI, drIndex2, 40000, 40000, 40000, pcOffset9_2, 40000, 40000, null);

            case "0110":
                // LDR instruction
                int drIndex3 = Integer.parseInt(binaryString.substring(4, 7), 2);
                int baseRIndex6 = Integer.parseInt(binaryString.substring(7, 10), 2);
                int offset6 = signExtend(Integer.parseInt(binaryString.substring(10, 16), 2), 6);
                return new LC3Instruction(LC3Opcode.LDR, drIndex3, 40000, 40000, 40000, offset6, baseRIndex6, 40000, null);

            case "1110":
                // LEA instruction
                int drIndex4 = Integer.parseInt(binaryString.substring(4, 7), 2);
                int pcOffset9_3 = signExtend(Integer.parseInt(binaryString.substring(7), 2), 9);
                return new LC3Instruction(LC3Opcode.LEA, drIndex4, 40000, 40000, 40000, pcOffset9_3, 40000, 40000, null);

            case "1001":
                // NOT instruction
                int drIndex5 = Integer.parseInt(binaryString.substring(4, 7), 2);
                int srIndex = Integer.parseInt(binaryString.substring(7, 10), 2);
                return new LC3Instruction(LC3Opcode.NOT, drIndex5, srIndex, 40000, 40000, 40000, 40000, 40000, null);

            case "1101":
                // RTI instruction
                return new LC3Instruction(LC3Opcode.RTI, 40000, 40000, 40000, 40000, 40000, 40000, 40000, null);

            case "0011":
                // ST instruction
                int srIndex4 = Integer.parseInt(binaryString.substring(4, 7), 2);
                int pcOffset9_4 = signExtend(Integer.parseInt(binaryString.substring(7), 2), 9);
                return new LC3Instruction(LC3Opcode.ST, 40000, srIndex4, 40000, 40000, pcOffset9_4, 40000, 40000, null);

            case "1011":
                // STI instruction
                int srIndex5 = Integer.parseInt(binaryString.substring(4, 7), 2);
                int pcOffset9_5 = signExtend(Integer.parseInt(binaryString.substring(7), 2), 9);
                return new LC3Instruction(LC3Opcode.STI, 40000, srIndex5, 40000, 40000, pcOffset9_5, 40000, 40000, null);

            case "0111":
                // STR instruction
                int srIndex6 = Integer.parseInt(binaryString.substring(4, 7), 2);
                int baseRIndex7 = Integer.parseInt(binaryString.substring(7, 10), 2);
                int offset6_2 = signExtend(Integer.parseInt(binaryString.substring(10, 16), 2), 6);
                return new LC3Instruction(LC3Opcode.STR, 40000, srIndex6, 40000, 40000, offset6_2, baseRIndex7, 40000, null);


            case "1111":
                // TRAP instruction
                int trapvect8 = Integer.parseInt(binaryString.substring(8, 16), 2);
                String trapvect8Hex = "0x" + Integer.toHexString(trapvect8);
                String trapMessage = "";
                switch (trapvect8Hex) {
                    case "0x20":
                        trapMessage = "GETC";
                        break;
                    case "0x21":
                        trapMessage = "OUT";
                        break;
                    case "0x22":
                        trapMessage = "PUTS";
                        break;
                    case "0x23":
                        trapMessage = "IN";
                        break;
                    case "0x25":
                        trapMessage = "HALT";
                        break;
                }
                return new LC3Instruction(LC3Opcode.TRAP, 40000, 40000, 40000, 40000, 40000, 40000, trapvect8, trapMessage);

            default:
                throw new IllegalArgumentException("Invalid binary string: " + binaryString);
        }
    }

    static int signExtend(int value, int bits) {
        if ((value & (1 << (bits - 1))) != 0) {
            // negative number
            int mask = (1 << bits) - 1;
            return (value | ~mask);
        } else {
            // positive number
            return value;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(opcode.name());
        builder.append(" ");

        if (dr != 40000) {
            builder.append("DR=").append(dr);
            builder.append(", ");
        }

        if (sr1 != 40000) {
            builder.append("SR1=").append(sr1);
            builder.append(", ");
        }
        if (sr2 != 40000) {
            builder.append("SR2=").append(sr2);
            builder.append(", ");
        }
        if (imm5value != 40000) {
            builder.append("Imm5value=").append(imm5value);
            builder.append(", ");
        }

        if (baseR != 40000) {
            builder.append("BaseR=").append(baseR);
            builder.append(", ");
        }
        if (offset6 != 40000) {
            builder.append("Offset6=").append(offset6);
            builder.append(", ");
        }
        if (trapvect8 != 40000) {
            builder.append("TrapVect8=").append(trapvect8);
            builder.append(", ");
        }
        if (trapMessage != null) {
            builder.append("TRAP=").append(trapMessage);
        }
        return builder.toString();
    }
}