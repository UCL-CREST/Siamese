    public LinkedList<SearchResult> search(String strRequest) {
        LinkedList<SearchResult> ret = new LinkedList<SearchResult>();
        HttpClient h = new HttpClient();
        try {
            String strRequestUrl = "http://www.youporn.com/search";
            if (strRequest.toLowerCase().contains("straight!")) {
                strRequestUrl += "?type=straight";
                strRequest = strRequest.replaceAll("straight!", "");
            }
            if (strRequest.toLowerCase().contains("gay!")) {
                strRequestUrl += "?type=gay";
                strRequest = strRequest.replaceAll("gay!", "");
            }
            if (strRequest.toLowerCase().contains("cocks!")) {
                strRequestUrl += "?type=cocks";
                strRequest = strRequest.replaceAll("cocks!", "");
            }
            if (!strRequestUrl.endsWith("search")) strRequestUrl += "&"; else strRequestUrl += "?";
            strRequestUrl += "query=" + URLEncoder.encode(strRequest, "UTF-8");
            if (NoMuleRuntime.DEBUG) System.out.println(strRequestUrl);
            GetMethod get = new GetMethod(strRequestUrl);
            Date d = new Date((new Date()).getTime() + (1 * 24 * 3600 * 1000));
            h.getState().addCookie(new Cookie(".youporn.com", "age_check", "1", "/", d, false));
            h.executeMethod(get);
            BufferedReader in = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream()));
            String s = "";
            String res = "";
            while ((s = in.readLine()) != null) {
                res += s;
            }
            get.releaseConnection();
            if (NoMuleRuntime.DEBUG) System.out.println(res);
            String regexp = "\\<a href\\=\"\\/watch\\/[^\"]+\"\\>[^\\<]+";
            Pattern p = Pattern.compile(regexp);
            Matcher m = p.matcher(res);
            while (m.find()) {
                int startPos = m.start() + "<a href=\"".length();
                String strUrl = "http://www.youporn.com";
                int pos = 0;
                for (pos = startPos; pos < m.end() && (res.charAt(pos) != '\"'); pos++) {
                    strUrl += res.charAt(pos);
                }
                String strTitle = res.substring(pos + 2, m.end());
                if (strTitle.trim().length() > 0) ret.add(new SearchResult(strTitle + " at YouPorn", strUrl));
            }
            return ret;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
