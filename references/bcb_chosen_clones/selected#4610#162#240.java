    protected gUnitTestResult runLexer(String lexerName, String testRuleName, gUnitTestInput testInput) throws Exception {
        CharStream input;
        Class lexer = null;
        PrintStream ps = null;
        PrintStream ps2 = null;
        try {
            if (testInput.inputIsFile) {
                String filePath = testInput.testInput;
                File testInputFile = new File(filePath);
                if (!testInputFile.exists() && grammarInfo.getHeader() != null) {
                    testInputFile = new File("./" + grammarInfo.getHeader().replace('.', '/'), testInput.testInput);
                    if (testInputFile.exists()) filePath = testInputFile.getCanonicalPath();
                }
                input = new ANTLRFileStream(filePath);
            } else {
                input = new ANTLRStringStream(testInput.testInput);
            }
            lexer = classForName(lexerName);
            Class[] lexArgTypes = new Class[] { CharStream.class };
            Constructor lexConstructor = lexer.getConstructor(lexArgTypes);
            Object[] lexArgs = new Object[] { input };
            Object lexObj = lexConstructor.newInstance(lexArgs);
            Method ruleName = lexer.getMethod("m" + testRuleName, new Class[0]);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            ps = new PrintStream(out);
            ps2 = new PrintStream(err);
            System.setOut(ps);
            System.setErr(ps2);
            ruleName.invoke(lexObj, new Object[0]);
            Method ruleName2 = lexer.getMethod("getCharIndex", new Class[0]);
            int currentIndex = (Integer) ruleName2.invoke(lexObj, new Object[0]);
            if (currentIndex != input.size()) {
                ps2.print("extra text found, '" + input.substring(currentIndex, input.size() - 1) + "'");
            }
            if (err.toString().length() > 0) {
                gUnitTestResult testResult = new gUnitTestResult(false, err.toString(), true);
                testResult.setError(err.toString());
                return testResult;
            }
            String stdout = null;
            if (out.toString().length() > 0) {
                stdout = out.toString();
            }
            return new gUnitTestResult(true, stdout, true);
        } catch (IOException e) {
            return getTestExceptionResult(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SecurityException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InvocationTargetException e) {
            return getTestExceptionResult(e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (ps2 != null) ps2.close();
                System.setOut(console);
                System.setErr(consoleErr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new Exception("This should be unreachable?");
    }
