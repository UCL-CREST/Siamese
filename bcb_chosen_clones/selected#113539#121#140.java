    static void XLinePaser(String str) {
        int startIndex = 0;
        Pattern pattern = Pattern.compile("[;{}]", 1);
        matcher = pattern.matcher(tail + str);
        while (matcher.find()) {
            object = matcher.toMatchResult();
            if (matcher.group().equalsIgnoreCase("}")) {
                mesh.addString(str.subSequence(startIndex, matcher.start()));
                mesh = mesh.getUp();
            } else if (matcher.group().equalsIgnoreCase("{")) {
                xType = new XType();
                xType.setTypeFromString(str.subSequence(startIndex, matcher.start()));
                mesh = mesh.addNewSubTree(xType);
            } else if (matcher.group().equalsIgnoreCase(";")) {
                mesh.addString(str.subSequence(startIndex, matcher.end()));
            }
            startIndex = matcher.end();
        }
        if (startIndex != matcher.regionEnd()) mesh.addString(str.subSequence(startIndex, matcher.regionEnd()));
    }
