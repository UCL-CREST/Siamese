    public static String getAlbumName(String author, String track) {
        String url = "http://musicbrainz.org/ws/1/track/?type=xml&title=" + track.replace(' ', '+') + "&artist=" + author.replace(' ', '+');
        String ret = HTTP.get(url);
        System.out.println(url);
        String regexp = "<title>[^<]*</title>";
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(ret);
        while (m.find()) {
            String s = ret.substring(m.start() + 7, m.end() - 8);
            if (!s.toUpperCase().equals(track.toUpperCase())) return s;
        }
        return "";
    }
