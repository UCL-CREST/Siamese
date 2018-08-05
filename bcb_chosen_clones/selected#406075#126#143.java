    public Parser getParser(Token token) {
        String tokenName = token.getClass().getName();
        Object parserClassName = token2parser.get(tokenName);
        if (parserClassName != null) {
            try {
                Class parserClass = Class.forName((String) parserClassName);
                Class parameterType[] = new Class[] { this.manager.getClass() };
                Object args[] = new Object[] { this.manager };
                Constructor constructor = parserClass.getConstructor(parameterType);
                return (Parser) constructor.newInstance(args);
            } catch (Exception e) {
                System.err.println("Error while creating Parser for token " + tokenName);
            }
        } else {
            System.err.println("No Parser assigned for token " + tokenName);
        }
        return null;
    }
