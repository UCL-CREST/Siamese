    public ArrayList<String[]> getLinkList() {
        if (!isNice()) {
            return null;
        }
        StringBuilder tempcont = getContents();
        if (tempcont == null) {
            return null;
        }
        long times = System.currentTimeMillis();
        Pattern regexLink = Pattern.compile("([^.<>]*)<\\s*[Aa]\\s+[^>]*[Hh][Rr][Ee][Ff]\\s*=([^>]*)>([^<>]*)<\\s*/\\s*[Aa]\\s*>([^.<>]*)");
        Pattern regexBase = Pattern.compile("<\\s*[Bb][Aa][Ss][Ee]\\s+[^>]*[Hh][Rr][Ee][Ff]\\s*=\\s*[\"']?([^\"' >]+)[\"' >]");
        Matcher matcher;
        Matcher baseMatcher;
        matcher = regexLink.matcher(tempcont);
        ArrayList<String[]> temp_list = new ArrayList<String[]>();
        int lastend = -1;
        int streak = 0;
        int current = -1;
        while (matcher.find()) {
            String linkText = matcher.group(3);
            String linkAddr = getURLfromLink(matcher.group(2));
            String linkPre = matcher.group(1);
            String linkPost = matcher.group(4);
            if (matcher.start() == lastend) {
                linkPre = (temp_list.get(current))[2] + (temp_list.get(current))[1] + (temp_list.get(current))[3];
                streak++;
                for (int i = 0; i < streak; i++) {
                    String[] rewrite = temp_list.get(current - i);
                    rewrite[3] += linkText + linkPost;
                    temp_list.set(current - i, rewrite);
                }
            } else if (streak > 0) {
                String[] rewrite = temp_list.get(current);
                rewrite[3] += linkText + linkPost;
                temp_list.set(current, rewrite);
                streak = 0;
            } else {
                streak = 0;
            }
            current++;
            lastend = matcher.end();
            temp_list.add(new String[] { linkAddr, linkText, linkPre, linkPost });
        }
        String base = "none";
        baseMatcher = regexBase.matcher(tempcont);
        while (baseMatcher.find()) {
            base = getURLfromLink(baseMatcher.group(1));
        }
        if (base.equals("none")) {
            base = getAddress();
        }
        URL baseURL = url;
        try {
            int delim = 0;
            for (delim = base.length() - 1; delim >= 0; delim--) {
                if (base.charAt(delim) == '/') {
                    break;
                }
            }
            base = base.substring(0, delim);
            baseURL = new URL(base);
        } catch (Exception e) {
        }
        URL tester;
        String url2;
        addressList = new ArrayList<String[]>();
        times = System.currentTimeMillis() - times;
        System.out.println("  It took " + times + " millis to get link list from " + tempcont.length() + " characters");
        times = System.currentTimeMillis();
        for (String[] urlInfo : temp_list) {
            if (urlInfo[0].length() >= 10 && urlInfo[0].substring(0, 10).equals("javascript")) {
                continue;
            }
            if (urlInfo[0].length() == 0) {
                continue;
            }
            if (urlInfo[0].charAt(0) == '#') {
                continue;
            }
            if (urlInfo[0].length() > 0 && urlInfo[0].charAt(0) == '/') {
                url2 = noPath(baseURL.getProtocol(), baseURL.getHost(), baseURL.getPort()) + noDirPath(urlInfo[0]);
            } else {
                url2 = urlInfo[0];
            }
            try {
                tester = new URL(url2);
                addressList.add(new String[] { tester.toString(), urlInfo[1], urlInfo[2], urlInfo[3] });
            } catch (MalformedURLException e1) {
                try {
                    while (url2.length() > 0 && url2.charAt(0) == '/') {
                        url2 = url2.substring(1);
                    }
                    tester = new URL(noDirPath(baseURL.toExternalForm()) + "/" + url2);
                    addressList.add(new String[] { tester.toString(), urlInfo[1], urlInfo[2], urlInfo[3] });
                } catch (MalformedURLException e2) {
                }
            }
        }
        times = System.currentTimeMillis() - times;
        if (verbose) System.out.println("It took " + times + " millis to populate link list from " + addressList.size() + " links in the list");
        return addressList;
    }
