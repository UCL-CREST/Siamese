    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        String s = "";
        String lineFile = "";
        ;
        if (getNArgIn(operands) != 1) throwMathLibException("urlread: number of arguments < 1");
        if (!(operands[0] instanceof CharToken)) throwMathLibException("urlread: argument must be String");
        String urlString = ((CharToken) operands[0]).toString();
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (Exception e) {
            throwMathLibException("urlread: malformed url");
        }
        try {
            BufferedReader inReader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((lineFile = inReader.readLine()) != null) {
                s += lineFile + "\n";
            }
            inReader.close();
        } catch (Exception e) {
            throwMathLibException("urlread: error input stream");
        }
        return new CharToken(s);
    }
