package org.example;

public enum LC3Opcode {
    ADD("0001"),
    AND("0101"),
    NOT("1001"),
    LDI("1010"),
    LD("0010"),
    LDR("0110"),
    LEA("1110"),
    ST("0011"),
    STI("1011"),
    STR("0111"),
    BR("0000"),
    JMP("1100"),
    JSR("0100"),
    JSRR("0100"),
    TRAP("1111"),
    RET("1100"),
    RTI("1000");

    private final String binaryString;

    LC3Opcode(String binaryString) {
        this.binaryString = binaryString;
    }

    public String getBinaryString() {
        return binaryString;
    }
}