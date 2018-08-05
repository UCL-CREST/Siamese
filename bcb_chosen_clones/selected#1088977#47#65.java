    public int[][] get_spectrum(String spectrum) {
        if (spectrum == null) {
            return null;
        }
        int[][] num = new int[spectrum.split(",").length][];
        for (int i = 0; i < num.length; i++) {
            num[i] = new int[2];
        }
        Pattern sp = Pattern.compile("\\d\\d?\\d?\\d?");
        Matcher matcher = sp.matcher(spectrum);
        int i = 0;
        while (matcher.find()) {
            num[i][0] = Integer.parseInt(spectrum.substring(matcher.start(), matcher.end()));
            matcher.find();
            num[i][1] = Integer.parseInt(spectrum.substring(matcher.start(), matcher.end()));
            i++;
        }
        return num;
    }
