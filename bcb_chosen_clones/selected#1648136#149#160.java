    public void testVowels() {
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("[a-z&&[aeiou]]", Pattern.CASE_INSENSITIVE);
        m = p.matcher("BerlinBrown");
        boolean _found = false;
        while (m.find()) {
            System.out.println(" : Vowel-Search \"" + m.group() + "\" start :  " + m.start() + " end : " + m.end() + ".");
            _found = true;
        }
        assertTrue(_found);
    }
