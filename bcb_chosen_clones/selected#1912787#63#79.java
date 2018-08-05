    @Deprecated
    public static boolean preg_match(String regex, String cadena, Integer[][] matches) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(cadena);
        boolean b = m.matches();
        LinkedList<Integer[]> o = new LinkedList<Integer[]>();
        while (m.find()) {
            Integer i = m.start(0);
            Integer f = m.end(0);
            o.add(new Integer[] { i, f });
        }
        matches = new Integer[o.size()][];
        for (int i = 0; i < o.size(); i++) {
            matches[i] = o.get(i);
        }
        return b;
    }
