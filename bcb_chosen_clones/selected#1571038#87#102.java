    private String[] getLines() {
        String text = getText();
        List list = new ArrayList();
        Pattern pat = Pattern.compile("\\r?\\n");
        Matcher mat = pat.matcher(text);
        int start = 0;
        while (mat.find(start)) {
            list.add(text.substring(start, mat.start()));
            start = mat.end();
        }
        list.add(text.substring(start));
        if (list.size() == 0) {
            return new String[] { "" };
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
