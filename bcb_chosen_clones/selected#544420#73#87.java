    public static ArrayList<String> getFormatImageURL_DNAPath(String orig, String pathImg, String pathHref, String fullDNAPath) {
        ArrayList<String> lstImages = new ArrayList<String>();
        Pattern pattern = Pattern.compile(image_pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(orig);
        String image_pattern_subs = fullDNAPath + "$1";
        String candidate = null;
        while (matcher.find()) {
            candidate = orig.substring(matcher.start(), matcher.end());
            Matcher matcher2 = pattern.matcher(candidate);
            String image_pattern_subs2 = fullDNAPath + "$1";
            String tmp1 = matcher2.replaceAll(image_pattern_subs);
            lstImages.add(tmp1);
        }
        return lstImages;
    }
