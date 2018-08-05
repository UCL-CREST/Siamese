    public static String injectAfterAnnotation(Message extend, Message by, String extendProto, String byContent) {
        Pattern messageRegexp = Pattern.compile("[\\n\\r]?([ \\t]*)(message\\s+" + extend.getName() + "\\s+\\{)", Pattern.MULTILINE);
        int messageIndex = -1, openBracketIndex = -1;
        Matcher matcher = messageRegexp.matcher(extendProto);
        if (matcher.find()) {
            int is = matcher.start(1), ie = matcher.end(1);
            String indentation = generateIndentation(extendProto.substring(is, ie), 4);
            messageIndex = matcher.start(2);
            openBracketIndex = matcher.end(2);
            extendProto = extendProto.substring(0, openBracketIndex) + LINE_SEPARATOR + indentation + "// " + generateTimestamp(extend, by) + LINE_SEPARATOR + insertIndentation(byContent, indentation) + LINE_SEPARATOR + extendProto.substring(openBracketIndex);
        }
        Pattern annotationRegexp = Pattern.compile("[\\n\\r]?([ \\t]*@Extend\\s*\\([^)]+" + by.getName() + "[^)]*\\))");
        String annotationSpace = extendProto.substring(0, messageIndex);
        matcher = annotationRegexp.matcher(annotationSpace);
        int astart = -1, aend = 0;
        while (matcher.find(aend)) {
            astart = matcher.start(1);
            aend = matcher.end(1);
        }
        if (astart > -1) extendProto = extendProto.substring(0, astart) + "// " + extendProto.substring(astart);
        return extendProto;
    }
