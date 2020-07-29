    static long testArchiveUsingTar(File f) throws IOException {
        Process tarProc = Runtime.getRuntime().exec(new String[] { "tar", "--to-stdout", "-xf", f.getAbsolutePath() });
        try {
            InputStream in = new BufferedXInputStream(tarProc.getInputStream());
            boolean failNotEquals = false;
            char expectedChar = FIRST_EXPECTED_CHAR;
            char expChar = 0;
            char gotChar = 0;
            long readed = 0;
            for (int b; ((b = in.read()) >= 0); ) {
                readed++;
                if (b == '0') {
                    expectedChar = nextExpectedChar('0');
                } else {
                    if (!failNotEquals && (expectedChar != b)) {
                        expChar = expectedChar;
                        gotChar = (char) b;
                        failNotEquals = true;
                    }
                    expectedChar = nextExpectedChar(expectedChar);
                }
            }
            Assert.assertTrue(readed >= 0);
            StringBuilder errStr = new StringBuilder();
            InputStream err = tarProc.getErrorStream();
            for (int b; (b = err.read()) >= 0; ) errStr.append((char) b);
            int tarExit = tarProc.waitFor();
            if (tarExit == 0) {
                if (errStr.length() > 0) Logger.global.warning(errStr.toString());
            } else Assert.fail("tar exited with error " + tarExit + ":\n" + errStr);
            if (failNotEquals) Assert.fail("damaged content, expected '" + expChar + "' but got '" + gotChar + "'");
            return readed;
        } catch (final InterruptedException ex) {
            throw (InterruptedIOException) new InterruptedIOException().initCause(ex);
        } finally {
            tarProc.destroy();
        }
    }
