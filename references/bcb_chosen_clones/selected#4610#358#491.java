    protected gUnitTestResult runTreeParser(String parserName, String lexerName, String testRuleName, String testTreeRuleName, gUnitTestInput testInput) throws Exception {
        CharStream input;
        String treeParserPath;
        Class lexer = null;
        Class parser = null;
        Class treeParser = null;
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
            if (grammarInfo.getHeader() != null) {
                treeParserPath = grammarInfo.getHeader() + "." + grammarInfo.getTreeGrammarName();
            } else {
                treeParserPath = grammarInfo.getTreeGrammarName();
            }
            lexer = classForName(lexerName);
            Class[] lexArgTypes = new Class[] { CharStream.class };
            Constructor lexConstructor = lexer.getConstructor(lexArgTypes);
            Object[] lexArgs = new Object[] { input };
            Object lexObj = lexConstructor.newInstance(lexArgs);
            CommonTokenStream tokens = new CommonTokenStream((Lexer) lexObj);
            parser = classForName(parserName);
            Class[] parArgTypes = new Class[] { TokenStream.class };
            Constructor parConstructor = parser.getConstructor(parArgTypes);
            Object[] parArgs = new Object[] { tokens };
            Object parObj = parConstructor.newInstance(parArgs);
            Method ruleName = parser.getMethod(testRuleName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            ps = new PrintStream(out);
            ps2 = new PrintStream(err);
            System.setOut(ps);
            System.setErr(ps2);
            Object ruleReturn = ruleName.invoke(parObj);
            Class _return = classForName(parserName + "$" + testRuleName + "_return");
            Method returnName = _return.getMethod("getTree");
            CommonTree tree = (CommonTree) returnName.invoke(ruleReturn);
            CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
            nodes.setTokenStream(tokens);
            treeParser = classForName(treeParserPath);
            Class[] treeParArgTypes = new Class[] { TreeNodeStream.class };
            Constructor treeParConstructor = treeParser.getConstructor(treeParArgTypes);
            Object[] treeParArgs = new Object[] { nodes };
            Object treeParObj = treeParConstructor.newInstance(treeParArgs);
            Method treeRuleName = treeParser.getMethod(testTreeRuleName);
            Object treeRuleReturn = treeRuleName.invoke(treeParObj);
            String astString = null;
            String stString = null;
            if (treeRuleReturn != null) {
                if (treeRuleReturn.getClass().toString().indexOf(testTreeRuleName + "_return") > 0) {
                    try {
                        Class _treeReturn = classForName(treeParserPath + "$" + testTreeRuleName + "_return");
                        Method[] methods = _treeReturn.getDeclaredMethods();
                        for (Method method : methods) {
                            if (method.getName().equals("getTree")) {
                                Method treeReturnName = _treeReturn.getMethod("getTree");
                                CommonTree returnTree = (CommonTree) treeReturnName.invoke(treeRuleReturn);
                                astString = returnTree.toStringTree();
                            } else if (method.getName().equals("getTemplate")) {
                                Method treeReturnName = _return.getMethod("getTemplate");
                                StringTemplate st = (StringTemplate) treeReturnName.invoke(treeRuleReturn);
                                stString = st.toString();
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            }
            if (tokens.index() != tokens.size()) {
                throw new InvalidInputException();
            }
            if (err.toString().length() > 0) {
                gUnitTestResult testResult = new gUnitTestResult(false, err.toString());
                testResult.setError(err.toString());
                return testResult;
            }
            String stdout = null;
            if (out.toString().length() > 0) {
                stdout = out.toString();
            }
            if (astString != null) {
                return new gUnitTestResult(true, stdout, astString);
            } else if (stString != null) {
                return new gUnitTestResult(true, stdout, stString);
            }
            if (treeRuleReturn != null) {
                return new gUnitTestResult(true, stdout, String.valueOf(treeRuleReturn));
            }
            return new gUnitTestResult(true, stdout, stdout);
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
        throw new Exception("Should not be reachable?");
    }
