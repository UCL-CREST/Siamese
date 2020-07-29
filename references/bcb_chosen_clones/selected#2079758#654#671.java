    private String fixUnicode(String s) {
        String s2 = "";
        String patron = "(\\\\[U][+])([0-9A-Fa-f]{4})";
        Pattern compiledPatron = Pattern.compile(patron);
        Matcher matcher = compiledPatron.matcher(s);
        int lastEnd = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String code = matcher.group(2);
            String hexa = "0x" + code;
            int caracter = Integer.decode(hexa).intValue();
            s2 = s2 + s.substring(lastEnd, start) + (char) caracter;
            lastEnd = end;
        }
        s2 = s2 + s.substring(lastEnd);
        return s2;
    }
