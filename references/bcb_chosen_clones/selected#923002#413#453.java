    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "find", args = { int.class })
    public void test_findI() {
        String testPattern = "(abb)";
        String testString = "cccabbabbabbabbabb";
        Pattern pat = Pattern.compile(testPattern);
        Matcher mat = pat.matcher(testString);
        int start = 3;
        int end = 6;
        int j;
        for (j = 0; j < 3; j++) {
            while (mat.find(start + j - 2)) {
                assertEquals(start, mat.start(1));
                assertEquals(end, mat.end(1));
                start = end;
                end += 3;
            }
            start = 6;
            end = 9;
        }
        testPattern = "(\\d{1,3})";
        testString = "aaaa123456789045";
        Pattern pat2 = Pattern.compile(testPattern);
        Matcher mat2 = pat2.matcher(testString);
        start = 4;
        int length = 3;
        for (j = 0; j < length; j++) {
            for (int i = 4 + j; i < testString.length() - length; i += length) {
                mat2.find(i);
                assertEquals(testString.substring(i, i + length), mat2.group(1));
            }
        }
        Pattern pat3 = Pattern.compile("new");
        Matcher mat3 = pat3.matcher("Brave new world");
        assertTrue(mat3.find(-1));
        assertTrue(mat3.find(6));
        assertFalse(mat3.find(7));
        mat3.region(7, 10);
        assertFalse(mat3.find(3));
        assertFalse(mat3.find(6));
        assertFalse(mat3.find(7));
    }
