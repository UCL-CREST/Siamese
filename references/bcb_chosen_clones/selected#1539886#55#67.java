    public static RGB getRgbFromString(String rgbString) {
        if (rgbString == null) return null;
        Pattern pattern = Pattern.compile("\\b\\d+\\b");
        Matcher matcher = pattern.matcher(rgbString);
        int[] rgb = new int[3];
        int i = 0;
        while (matcher.find()) {
            String extracted = rgbString.substring(matcher.start(), matcher.end());
            rgb[i++] = Integer.parseInt(extracted);
            if (i == 4) return null;
        }
        return new RGB(rgb[0], rgb[1], rgb[2]);
    }
