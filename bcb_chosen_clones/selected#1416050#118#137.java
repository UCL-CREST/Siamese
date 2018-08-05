    public StringBuffer insertContent(StringBuffer input) {
        StringBuffer out = new StringBuffer();
        Pattern p = Pattern.compile("(?si)(" + patternStart + ")(" + patternEnd + ")");
        Matcher m = p.matcher(input);
        int last = 0;
        int counter = 0;
        while (m.find()) {
            out.append(input.substring(last, m.start()));
            out.append(m.group(1));
            try {
                out.append(content.get(counter));
            } catch (IndexOutOfBoundsException e) {
            }
            out.append(m.group(2));
            counter++;
            last = m.end();
        }
        out.append(input.substring(last));
        return out;
    }
