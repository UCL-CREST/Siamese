    private void extractByParsingHtml(String refererURL, String requestURL) throws MalformedURLException, IOException {
        URL url = new URL(refererURL);
        InputStream is = url.openStream();
        mRefererURL = refererURL;
        if (requestURL.startsWith("http://www.")) {
            mRequestURLWWW = requestURL;
            mRequestURL = "http://" + mRequestURLWWW.substring(11);
        } else {
            mRequestURL = requestURL;
            mRequestURLWWW = "http://www." + mRequestURL.substring(7);
        }
        Parser parser = (new HTMLEditorKit() {

            public Parser getParser() {
                return super.getParser();
            }
        }).getParser();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        StringReader sr = new StringReader(sb.toString());
        parser.parse(sr, new LinkbackCallback(), true);
        if (mStart != 0 && mEnd != 0 && mEnd > mStart) {
            mExcerpt = sb.toString().substring(mStart, mEnd);
            mExcerpt = Utilities.removeHTML(mExcerpt);
            if (mExcerpt.length() > mMaxExcerpt) {
                mExcerpt = mExcerpt.substring(0, mMaxExcerpt) + "...";
            }
        }
        if (mTitle.startsWith(">") && mTitle.length() > 1) {
            mTitle = mTitle.substring(1);
        }
    }
