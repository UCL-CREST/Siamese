    public static String prettyPrint(String input) {
        int indent = 0;
        String regex = "(\\s*\n\\s*)+|(\\<\\%\\@.*\\%\\>)|(\\<\\%\\=.*\\%\\>)|(\\%\\>)|(\\<\\%)|(\\<[^\\>]*\\>)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        int previous = 0;
        List<String> result = new Vector<String>();
        while (m.find()) {
            if (previous != m.start()) {
                String inBetween = input.substring(previous, m.start()).trim();
                if (!inBetween.isEmpty()) result.add(inBetween);
            }
            result.add(input.substring(m.start(), m.end()));
            previous = m.end();
        }
        if (input.length() != previous) result.add(input.substring(previous, input.length()));
        String pretty = "";
        boolean noindent = false;
        for (String out : result) {
            if ("<pre>".equals(out)) noindent = true;
            if ("</pre>".equals(out)) noindent = false;
            if (noindent) pretty += out; else if (out.matches("(\\s*\n\\s*)+")) {
                pretty += "\n";
            } else if (out.matches("\\<\\%\\=.*\\%\\>") || out.matches("\\<.*/\\>")) {
                pretty += out;
            } else {
                if (out.matches("^(\\%\\>)$")) indent = indent - 2;
                for (int rep = 0; rep < indent; rep++) {
                    pretty += " ";
                }
                if ("%>".equals(out)) pretty += "\n" + out; else if ("<%".equals(out)) {
                    indent = indent + 2;
                    pretty += out + "\n";
                } else pretty += out;
            }
        }
        return pretty;
    }
