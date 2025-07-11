package org.rihelo;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TripleDesTest {
    @Test
    void testSafeEncryption() {
        assertDoesNotThrow(() -> {
            org.rihelo.TripleDes testthis= new org.rihelo.TripleDes();
            String[] args = new String[6];
            args[0] = "e";
            args[1] = new File("/Users/rihelo/gh/3des/3des/src/test/resources/TEST.txt").getAbsolutePath();
            args[2] = new File("/Users/rihelo/gh/3des/3des/src/test/resources/OUTPUT.txt").getAbsolutePath();
            args[3] = new File("/Users/rihelo/gh/3des/3des/src/test/resources/K1").getAbsolutePath();
            args[4] = new File("/Users/rihelo/gh/3des/3des/src/test/resources/K2").getAbsolutePath();
            args[5] = new File("/Users/rihelo/gh/3des/3des/src/test/resources/K3").getAbsolutePath();
            org.rihelo.TripleDes.main(args);
        }, "Expected no exception to be thrown");
    }
    @Test
    void testSafeDecryption() {
        assertDoesNotThrow(() -> {
            org.rihelo.TripleDes testthis= new org.rihelo.TripleDes();
            String[] args = new String[6];
            args[0] = "d";
            args[1] = new File("/Users/rihelo/gh/3des/3des/src/test/resources/OUTPUT.txt").getAbsolutePath();
            args[2] = new File("/Users/rihelo/gh/3des/3des/src/test/resources/DECRYPT.txt").getAbsolutePath();
            args[3] = new File("/Users/rihelo/gh/3des/3des/src/test/resources/K1").getAbsolutePath();
            args[4] = new File("/Users/rihelo/gh/3des/3des/src/test/resources/K2").getAbsolutePath();
            args[5] = new File("/Users/rihelo/gh/3des/3des/src/test/resources/K3").getAbsolutePath();
            org.rihelo.TripleDes.main(args);
        }, "Expected no exception to be thrown");
    }
}
