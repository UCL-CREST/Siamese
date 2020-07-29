    public static String[] expandMacros(String string) {
        String expression = "\\[([\\d]+)-([\\d]+)\\]";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(string);
        Vector vector = new Vector();
        int startPos = 0;
        while (matcher.find()) {
            try {
                Integer lowVal = new Integer(matcher.group(1));
                Integer highVal = new Integer(matcher.group(2));
                vector.add(new Object[] { string.substring(startPos, matcher.start(0)), lowVal, highVal });
                startPos = matcher.end(0);
            } catch (NumberFormatException exception) {
            }
        }
        String ending = string.substring(startPos);
        int dimSize = vector.size();
        String[] stringParts = new String[dimSize];
        int[] startVals = new int[dimSize];
        int[] endVals = new int[dimSize];
        for (int i = 0; i < vector.size(); i++) {
            stringParts[i] = (String) ((Object[]) vector.get(i))[0];
            startVals[i] = ((Integer) ((Object[]) vector.get(i))[1]).intValue();
            endVals[i] = ((Integer) ((Object[]) vector.get(i))[2]).intValue();
        }
        int count = 1;
        int[] dimensions = new int[dimSize];
        int[] positions = new int[dimSize];
        for (int d = 0; d < dimSize; d++) {
            dimensions[d] = Math.abs(startVals[d] - endVals[d]) + 1;
            count *= dimensions[d];
        }
        if (count > Constants.MAX_NAME_MACRO_EXPANSIONS) {
            return null;
        }
        String[] strings = new String[count];
        int pos = 0;
        for (int s = 0; s < count; s++) {
            string = "";
            pos = s;
            for (int d = dimSize - 1; d >= 0; d--) {
                positions[d] = pos % dimensions[d];
                pos /= dimensions[d];
            }
            for (int d = 0; d < dimSize; d++) {
                string += stringParts[d] + (startVals[d] + positions[d] * (endVals[d] - startVals[d] >= 0 ? 1 : -1));
            }
            strings[s] = string + ending;
        }
        return strings;
    }
