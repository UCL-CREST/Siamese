    public void testVowelsInner() {
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("H[a-z&&[aeiou]]llo", Pattern.CASE_INSENSITIVE);
        m = p.matcher("My name is berlin hallo what is up");
        boolean _found = false;
        while (m.find()) {
            System.out.println(" : Vowel-Search \"" + m.group() + "\" start :  " + m.start() + " end : " + m.end() + ".");
            _found = true;
        }
        assertTrue(_found);
    }
