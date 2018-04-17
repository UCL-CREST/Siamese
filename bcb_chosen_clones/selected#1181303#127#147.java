    private static BufferedImage getImageFromNxtImageCreateString(String string) {
        Pattern statePattern = Pattern.compile(".*\\s*new\\s+Image\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*new\\s+byte\\s*\\[\\s*\\]\\s*" + "\\{\\s*([^}]*)\\s*\\}\\s*\\)\\s*[;]?\\s*", Pattern.MULTILINE);
        Matcher stateMatcher = statePattern.matcher(string);
        if (!stateMatcher.matches()) throw new NumberFormatException("illegal format");
        int w = Integer.parseInt(stateMatcher.group(1));
        int h = Integer.parseInt(stateMatcher.group(2));
        String byteArray = stateMatcher.group(3);
        Pattern byteDigitalPattern = Pattern.compile("\\s*(\\(\\s*byte\\s*\\))?\\s*((0x)?[0-9A-Fa-f]+)\\s*[,]?\\s*");
        Matcher byteDigitalMatcher = byteDigitalPattern.matcher(byteArray);
        int start = 0;
        int end = 0;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while (byteDigitalMatcher.find(end)) {
            start = byteDigitalMatcher.start(2);
            end = byteDigitalMatcher.end(2);
            String str = byteArray.substring(start, end);
            int i = str.startsWith("0x") ? Integer.parseInt(str.substring(2), 16) : str.startsWith("0") ? Integer.parseInt(str, 8) : Integer.parseInt(str);
            os.write(i);
        }
        return nxtImageData2Image(os.toByteArray(), w, h);
    }
