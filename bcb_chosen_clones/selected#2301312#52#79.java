    private Map<String, com.sun.jdi.connect.Connector.Argument> parseConnectorArgs(Connector connector, String argString) {
        Map<String, com.sun.jdi.connect.Connector.Argument> arguments = connector.defaultArguments();
        String regexPattern = "(quote=[^,]+,)|" + "(\\w+=)" + "(((\"[^\"]*\")|" + "('[^']*')|" + "([^,'\"]+))+,)";
        Pattern p = Pattern.compile(regexPattern);
        Matcher m = p.matcher(argString);
        while (m.find()) {
            int startPosition = m.start();
            int endPosition = m.end();
            if (startPosition > 0) {
                throw new IllegalArgumentException(MessageOutput.format("Illegal connector argument", argString));
            }
            String token = argString.substring(startPosition, endPosition);
            int index = token.indexOf('=');
            String name = token.substring(0, index);
            String value = token.substring(index + 1, token.length() - 1);
            Connector.Argument argument = arguments.get(name);
            if (argument == null) {
                throw new IllegalArgumentException(MessageOutput.format("Argument is not defined for connector:", new Object[] { name, connector.name() }));
            }
            argument.setValue(value);
            argString = argString.substring(endPosition);
            m = p.matcher(argString);
        }
        if ((!argString.equals(",")) && (argString.length() > 0)) {
            throw new IllegalArgumentException(MessageOutput.format("Illegal connector argument", argString));
        }
        return arguments;
    }
