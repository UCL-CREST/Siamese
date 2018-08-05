    public LinkedList<SearchResult> search(String strRequest) {
        LinkedList<SearchResult> ret = new LinkedList<SearchResult>();
        try {
            String strUrl = "http://gdata.youtube.com/feeds/api/videos?vq=" + URLEncoder.encode(strRequest, "UTF-8") + "&max-results=10&orderby=viewCount&alt=atom";
            String res = HTTP.get(strUrl);
            String strRegExpTitles = "\\<media\\:title type\\=\'plain\'\\>[^\\<]*\\<";
            String strRegExpUrls = "\\<media\\:player url\\=\'[^\']+\'/>";
            Pattern pTitles = Pattern.compile(strRegExpTitles);
            Matcher mTitles = pTitles.matcher(res);
            Pattern pUrls = Pattern.compile(strRegExpUrls);
            Matcher mUrls = pUrls.matcher(res);
            LinkedList<String> lTitles = new LinkedList<String>();
            LinkedList<String> lUrls = new LinkedList<String>();
            while (mTitles.find()) {
                lTitles.add(res.substring(mTitles.start() + "<media:title type=\"plain\">".length(), mTitles.end() - "<".length()));
            }
            while (mUrls.find()) {
                lUrls.add(res.substring(mUrls.start() + "<media:player url=\"".length(), mUrls.end() - "\"/>".length()));
            }
            if (lTitles.size() == lUrls.size()) {
                for (int i = 0; i < lTitles.size(); i++) {
                    ret.add(new SearchResult(lTitles.get(i) + " at Youtube!", lUrls.get(i)));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret;
    }
