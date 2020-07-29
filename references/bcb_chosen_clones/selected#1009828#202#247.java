    public static List<UserUrlCat> parseBookmark(File bookmarkFile) throws Exception {
        List<UserUrlCat> userUrlCatList = new ArrayList<UserUrlCat>(10);
        UserUrlCat userUrlCat, defaultUserUrlCat = new UserUrlCat();
        defaultUserUrlCat.setCatName("收藏夹");
        defaultUserUrlCat.setCatCreateType(UserUrlCat.CAT_CREATE_TYPE_UNLINIT);
        String src = FileIO.readAsString(bookmarkFile, FileUtil.getCharset(bookmarkFile));
        src = formatBookmark(src);
        List<Object[]> tempCatList = new ArrayList<Object[]>(10);
        Pattern p = Pattern.compile("<(h\\d)[^>]*>([^<]*)</\\1>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(src);
        while (m.find()) {
            tempCatList.add(new Object[] { m.group(2), m.start(), m.end() });
        }
        String noCatUrls = null;
        if (tempCatList.size() == 0) {
            noCatUrls = src;
        } else {
            noCatUrls = src.substring(0, (Integer) tempCatList.get(0)[1]);
        }
        if (null != noCatUrls && !noCatUrls.trim().equals("")) {
            parseUserUrl(noCatUrls, defaultUserUrlCat.getUserUrlList());
        }
        String catUrls = null, catName;
        for (int i = 0; i < tempCatList.size(); i++) {
            if (i == tempCatList.size() - 1) {
                catUrls = src.substring((Integer) tempCatList.get(i)[2]);
            } else {
                catUrls = src.substring((Integer) tempCatList.get(i)[2], (Integer) tempCatList.get(i + 1)[1]);
            }
            catName = tempCatList.get(i)[0].toString().trim();
            if (catName.equals("")) {
                parseUserUrl(catUrls, defaultUserUrlCat.getUserUrlList());
            } else {
                userUrlCat = new UserUrlCat().setCatName(catName);
                userUrlCat.setCatCreateType(UserUrlCat.CAT_CREATE_TYPE_UNLINIT);
                parseUserUrl(catUrls, userUrlCat.getUserUrlList());
                if (userUrlCat.getUserUrlList().size() > 0) {
                    userUrlCatList.add(userUrlCat);
                }
            }
        }
        if (defaultUserUrlCat.getUserUrlList().size() > 0) {
            userUrlCatList.add(defaultUserUrlCat);
        }
        return userUrlCatList;
    }
