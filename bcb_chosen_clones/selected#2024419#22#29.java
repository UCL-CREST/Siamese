    private static void test_regex() {
        String input = "{{pron-rég|France <!-- précisez svp la ville ou la région -->|bɔ̃.ʒuʁ|audio=Bonjour.ogg}}";
        Pattern p = Pattern.compile("(\\{\\{pron-rég|)([^|]*|)");
        Matcher m = p.matcher(input);
        while (m.find()) {
            System.out.println("Le texte \"" + m.group() + "\" débute à " + m.start() + " et termine à " + m.end());
        }
    }
