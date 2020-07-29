    @Deprecated
    public static Collection<SearchKeyResult> searchKey(String iText, String iKeyServer) throws Exception {
        List<SearchKeyResult> outVec = new ArrayList<SearchKeyResult>();
        String uri = iKeyServer + "/pks/lookup?search=" + URLEncoder.encode(iText, UTF8);
        URL url = new URL(uri);
        BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
        Pattern regex = Pattern.compile("pub.*?<a\\s+href\\s*=\"(.*?)\".*?>\\s*(\\w+)\\s*</a>.*?(\\d+-\\d+-\\d+).*?<a\\s+href\\s*=\".*?\".*?>\\s*(.+?)\\s*</a>", Pattern.CANON_EQ);
        String line;
        while ((line = input.readLine()) != null) {
            Matcher regexMatcher = regex.matcher(line);
            while (regexMatcher.find()) {
                String id = regexMatcher.group(2);
                String downUrl = iKeyServer + regexMatcher.group(1);
                String downDate = regexMatcher.group(3);
                String name = decodeHTML(regexMatcher.group(4));
                outVec.add(new SearchKeyResult(id, name, downDate, downUrl));
            }
        }
        IOUtils.closeQuietly(input);
        return outVec;
    }
