    private String getAlbum() {
        String strAlbum = "";
        if (nddFile != null && nddFile.hasAlbum()) strAlbum = nddFile.getAlbum(); else {
            String title = getTitle();
            String url = "";
            try {
                url = "http://musicbrainz.org/ws/1/track/?type=xml&title=" + URLEncoder.encode(title, "UTF-8") + "&artist=" + URLEncoder.encode(getAuthor(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String ret = HTTP.get(url);
            String regexp = "<title>[^<]*</title>";
            Pattern p = Pattern.compile(regexp);
            Matcher m = p.matcher(ret);
            while (m.find()) {
                String s = ret.substring(m.start() + 7, m.end() - 8);
                if (!s.toUpperCase().equals(title.toUpperCase())) {
                    strAlbum = s;
                    break;
                }
            }
        }
        try {
            String strImgUrl = "";
            if (nddFile != null && nddFile.hasAlbumImage()) strImgUrl = nddFile.getAlbumImage(); else {
                String url = "http://albumart.org/index.php?srchkey=" + URLEncoder.encode(strAlbum, "UTF-8") + "&itempage=1&newsearch=1&searchindex=Music";
                String ret = HTTP.get(url);
                String regexp = "\"http:\\/\\/[^\"\']*amazon[^\"\']*\\.jpg\"";
                Pattern p = Pattern.compile(regexp);
                Matcher m = p.matcher(ret);
                m.find();
                strImgUrl = ret.substring(m.start() + 1, m.end() - 1);
            }
            HTTP.download(strImgUrl, strOutputfile + ".jpg", new javax.swing.JLabel());
        } catch (IOException e) {
            System.out.println("Hallo");
        } catch (IllegalStateException e) {
            System.out.println("Couldnt find an Album image");
        }
        return strAlbum;
    }
