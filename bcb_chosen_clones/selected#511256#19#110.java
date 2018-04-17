    public void test(TestHarness harness) {
        harness.checkPoint("TestOfMD4");
        try {
            Security.addProvider(new JarsyncProvider());
            algorithm = MessageDigest.getInstance("BrokenMD4", "JARSYNC");
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("TestOfMD4.provider");
            throw new Error(x);
        }
        try {
            for (int i = 0; i < 64; i++) algorithm.update((byte) 'a');
            byte[] md = algorithm.digest();
            String exp = "755cd64425f260e356f5303ee82a2d5f";
            harness.check(exp.equals(Util.toHexString(md)), "testSixtyFourA");
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("TestOfMD4.provider");
        }
        try {
            harness.verbose("NOTE: This test may take a while.");
            for (int i = 0; i < 536870913; i++) algorithm.update((byte) 'a');
            byte[] md = algorithm.digest();
            String exp = "b6cea9f528a85963f7529a9e3a2153db";
            harness.check(exp.equals(Util.toHexString(md)), "test536870913A");
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("TestOfMD4.provider");
        }
        try {
            byte[] md = algorithm.digest("a".getBytes());
            String exp = "bde52cb31de33e46245e05fbdbd6fb24";
            harness.check(exp.equals(Util.toHexString(md)), "testA");
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("TestOfMD4.testA");
        }
        try {
            byte[] md = algorithm.digest("abc".getBytes());
            String exp = "a448017aaf21d8525fc10ae87aa6729d";
            harness.check(exp.equals(Util.toHexString(md)), "testABC");
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("TestOfMD4.testABC");
        }
        try {
            byte[] md = algorithm.digest("message digest".getBytes());
            String exp = "d9130a8164549fe818874806e1c7014b";
            harness.check(exp.equals(Util.toHexString(md)), "testMessageDigest");
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("TestOfMD4.testMessageDigest");
        }
        try {
            byte[] md = algorithm.digest("abcdefghijklmnopqrstuvwxyz".getBytes());
            String exp = "d79e1c308aa5bbcdeea8ed63df412da9";
            harness.check(exp.equals(Util.toHexString(md)), "testAlphabet");
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("TestOfMD4.testAlphabet");
        }
        try {
            byte[] md = algorithm.digest("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".getBytes());
            String exp = "043f8582f241db351ce627e153e7f0e4";
            harness.check(exp.equals(Util.toHexString(md)), "testAsciiSubset");
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("TestOfMD4.testAsciiSubset");
        }
        try {
            byte[] md = algorithm.digest("12345678901234567890123456789012345678901234567890123456789012345678901234567890".getBytes());
            String exp = "e33b4ddc9c38f2199c3e7b164fcc0536";
            harness.check(exp.equals(Util.toHexString(md)), "testEightyNumerics");
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("TestOfMD4.testEightyNumerics");
        }
        try {
            algorithm.update("a".getBytes(), 0, 1);
            clone = (MessageDigest) algorithm.clone();
            byte[] md = algorithm.digest();
            String exp = "bde52cb31de33e46245e05fbdbd6fb24";
            harness.check(exp.equals(Util.toHexString(md)), "testCloning #1");
            clone.update("bc".getBytes(), 0, 2);
            md = clone.digest();
            exp = "a448017aaf21d8525fc10ae87aa6729d";
            harness.check(exp.equals(Util.toHexString(md)), "testCloning #2");
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("TestOfMD4.testCloning");
        }
    }
