    public static void main(String[] args) {
        Pattern p = null;
        try {
            p = Pattern.compile("newspageName='[a-z 0-9]+'");
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        String s = "asdf asdfnewspageName='tesst'adsf  sdfABCasdfn newspageName='asdf' asdf asdf ";
        Matcher m = p.matcher(s);
        while (m.find()) {
            System.out.println("Found " + m.group());
            System.out.println("  starting at index " + m.start() + " and ending at index " + m.end());
            System.out.println();
        }
    }
