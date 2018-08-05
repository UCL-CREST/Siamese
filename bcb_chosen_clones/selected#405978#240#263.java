    public void testFind() {
        String testPattern = "(abb)";
        String testString = "cccabbabbabbabbabb";
        Pattern pat = Pattern.compile(testPattern);
        Matcher mat = pat.matcher(testString);
        int start = 3;
        int end = 6;
        while (mat.find()) {
            assertEquals(start, mat.start(1));
            assertEquals(end, mat.end(1));
            start = end;
            end += 3;
        }
        testPattern = "(\\d{1,3})";
        testString = "aaaa123456789045";
        Pattern pat2 = Pattern.compile(testPattern);
        Matcher mat2 = pat2.matcher(testString);
        start = 4;
        int length = 3;
        while (mat2.find()) {
            assertEquals(testString.substring(start, start + length), mat2.group(1));
            start += length;
        }
    }
