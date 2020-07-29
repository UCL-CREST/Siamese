    public UserFunction loadMFileViaWeb(URL codeBase, String directoryAndFile, String mFileName) {
        String code = "";
        UserFunction function = null;
        ErrorLogger.debugLine("MFileLoader: loading >" + mFileName + ".m<");
        try {
            URL url = new URL(codeBase, directoryAndFile);
            InputStream in = url.openStream();
            BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = inReader.readLine()) != null) {
                code += line + "\n";
            }
            inReader.close();
        } catch (Exception e) {
            Errors.throwMathLibException("MFileLoader: m-file exception via web");
        }
        ErrorLogger.debugLine("MFileLoader: code: begin \n" + code + "\ncode end");
        FunctionParser funcParser = new FunctionParser();
        function = funcParser.parseFunction(code);
        function.setName(mFileName);
        ErrorLogger.debugLine("MFileLoader: finished webloading >" + mFileName + ".m<");
        return function;
    }
