    public static CLocation convertSecondaryStructure(String secondary) {
        CLocation location = new CLocation();
        String regex = "H+|E+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(secondary);
        while (matcher.find()) {
            if (matcher.group().substring(0, 1).equals("H")) location.add("H", matcher.start(), matcher.end()); else if (matcher.group().substring(0, 1).equals("E")) location.add("E", matcher.start(), matcher.end());
        }
        return location;
    }
