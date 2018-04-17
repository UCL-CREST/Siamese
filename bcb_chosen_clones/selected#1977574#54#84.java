    public LinkedList<SearchResult> search(String strRequest) {
        LinkedList<SearchResult> ret = new LinkedList<SearchResult>();
        try {
            String strSearchUrl = "http://vids.myspace.com/index.cfm?fuseaction=vids.search&SearchBoxID=SplashHeader&q=" + URLEncoder.encode(strRequest, "UTF-8") + "&t=vid";
            String res = HTTP.get(strSearchUrl);
            if (NoMuleRuntime.DEBUG) System.out.println(res);
            String regexp = "<a href=\"\\/index.cfm\\?fuseaction\\=vids\\.individual\\&videoid\\=[0-9]+\">[^<]+<\\/a>";
            Pattern p = Pattern.compile(regexp);
            Matcher m = p.matcher(res);
            String result = "";
            while (m.find()) {
                result = res.substring(m.start(), m.end());
                if (NoMuleRuntime.DEBUG) System.out.println("Searchresult:" + result);
                String urlRegExp = "\\/index.cfm\\?fuseaction\\=vids\\.individual\\&videoid\\=[0-9]+";
                String titleRegExp = "\\>[^\\<]+\\<";
                Pattern pUrl = Pattern.compile(urlRegExp);
                Pattern pTitle = Pattern.compile(titleRegExp);
                Matcher mUrl = pUrl.matcher(result);
                Matcher mTitle = pTitle.matcher(result);
                if (mUrl.find() && mTitle.find()) {
                    String strUrl = "http://vids.myspace.com" + result.substring(mUrl.start(), mUrl.end());
                    String strTitle = result.substring(mTitle.start() + 1, mTitle.end() - 1) + " at MySpace";
                    SearchResult s = new SearchResult(strTitle, strUrl);
                    ret.add(s);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret;
    }
