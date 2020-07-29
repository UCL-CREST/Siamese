    public Function findFunction(String functionName) {
        String code = "";
        UserFunction function = (UserFunction) getCachedFunction(functionName);
        if (function != null) return function;
        ErrorLogger.debugLine("MFileWebLoader: loading >" + functionName + ".m<");
        try {
            URL url = new URL(codeBase, directory + "/" + functionName + ".m");
            InputStream in = url.openStream();
            BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = inReader.readLine()) != null) {
                code += line + "\n";
            }
            inReader.close();
        } catch (Exception e) {
            Errors.throwMathLibException("MFileWebLoader: m-file exception via web");
        }
        ErrorLogger.debugLine("MFileWebLoader: code: begin \n" + code + "\ncode end");
        FunctionParser funcParser = new FunctionParser();
        function = funcParser.parseFunction(code);
        function.setName(functionName);
        cacheFunction(function);
        ErrorLogger.debugLine("MFileWebLoader: finished webloading >" + functionName + ".m<");
        return function;
    }
