package org.example;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import static org.example.LC3Instruction.*;

class LC3InstructionTest {

    @Test
    void decodeAdd() {
        // R2 = R1 + R0
        String s = "0001 010 001 000 000".replaceAll("\\s+", "");
        LC3Instruction instruction = decode(s);
        assertEquals(1, instruction.sr1);
        assertEquals(0, instruction.sr2);
        assertEquals(2, instruction.dr);
        assertEquals(LC3Opcode.ADD, instruction.opcode);
    }

    @Test
    void decodeAddImm() {
        // R2 = R1 + 5
        String s = "0001 111 011 1 00101".replaceAll("\\s+", "");
        LC3Instruction instruction = decode(s);
        assertEquals(3, instruction.sr1);
        assertEquals(5, instruction.imm5value);
        assertEquals(7, instruction.dr);
        assertEquals(LC3Opcode.ADD, instruction.opcode);
    }

    @Test
    void decodeAddImmNegative() {
        // R2 = R1 + 5
        String s = "0001 111 011 1 10010".replaceAll("\\s+", "");
        LC3Instruction instruction = decode(s);
        assertEquals(3, instruction.sr1);
        assertEquals(-14, instruction.imm5value);
        assertEquals(7, instruction.dr);
        assertEquals(LC3Opcode.ADD, instruction.opcode);
    }

    @Test
    void andTest() {
        // R2 = R1 & 7
        String s = "0101 110 011 0 00010".replaceAll("\\s+", "");
        LC3Instruction instruction = decode(s);
        assertEquals(3, instruction.sr1);
        assertEquals(2, instruction.sr2);
        assertEquals(6, instruction.dr);
        assertEquals(LC3Opcode.AND, instruction.opcode);
    }

    @Test
    void andImmediateValueTest() {
        // R2 = R1 & 7
        String s = "0101 110 001 1 00111".replaceAll("\\s+", "");
        LC3Instruction instruction = decode(s);
        assertEquals(1, instruction.sr1);
        assertEquals(7, instruction.imm5value);
        assertEquals(6, instruction.dr);
        assertEquals(LC3Opcode.AND, instruction.opcode);
    }

    @Test
    void signExtendTest() {
        assertEquals(0, signExtend(0, 5));
        assertEquals(4, signExtend(4, 5));
        assertEquals(-1, signExtend(31, 5));
        assertEquals(-5, signExtend(27, 5));
    }

    @Test
    void trapVectTest() {
        String s = "1111 0000 0010 0011".replaceAll("\\s+", "");
        LC3Instruction instruction = decode(s);
        assertEquals(35, instruction.trapvect8);
        assertEquals(LC3Opcode.TRAP, instruction.opcode);
    }

    @Test
    void illegalOrUnknownTrapVectTest() {
        String s = "1111 0000 0010 1111".replaceAll("\\s+", "");
        assertThrows(RuntimeException.class, () -> decode(s));
    }

    // Test for jsrr
    @Test
    void jumpSubroutineTest() {
        // jump to address in R1
        String s = "0100 000 001 000 000".replaceAll("\\s+", "");
        LC3Instruction instruction = decode(s);
        assertEquals(1, instruction.baseR);
        assertEquals(LC3Opcode.JSRR, instruction.opcode);

    }


    @Test
    void jumpSubroutinePCOffsetTest() {
        // jump to pc offset
        // offset is 16 + 8 = 24
        String s = "0100 1 000 0001 1000".replaceAll("\\s+", "");
        LC3Instruction instruction = decode(s);
        assertEquals(24, instruction.offset);
        assertEquals(LC3Opcode.JSR, instruction.opcode);

    }

    @Test
    void jumpTest() {
        // jump to address in R1
        String s = "1100 000 110 000 000".replaceAll("\\s+", "");
        LC3Instruction instruction = decode(s);
        assertEquals(6, instruction.baseR);
        assertEquals(LC3Opcode.JMP, instruction.opcode);

    }

    @Test
    void retTest() {
        // jump to address in R1
        String s = "1100 000 111 000 000".replaceAll("\\s+", "");
        LC3Instruction instruction = decode(s);
        assertEquals(7, instruction.baseR);
        assertEquals(LC3Opcode.RET, instruction.opcode);
    }
}