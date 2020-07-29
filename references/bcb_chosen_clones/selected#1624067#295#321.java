    public static List<String> getLevelIndex(URL fetchUrl) {
        List<String> levelNames = new ArrayList<String>();
        BufferedReader bufferedreader;
        try {
            URLConnection urlconnection = fetchUrl.openConnection();
            urlconnection.setConnectTimeout(30000);
            if (urlconnection.getContentEncoding() != null) {
                bufferedreader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), urlconnection.getContentEncoding()));
            } else {
                bufferedreader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "utf-8"));
            }
        } catch (IOException _ex) {
            System.err.println("HexTD::readFile:: Can't read from " + fetchUrl);
            return levelNames;
        }
        String sLine1;
        try {
            while ((sLine1 = bufferedreader.readLine()) != null) {
                if (sLine1.trim().length() != 0) {
                    levelNames.add(sLine1);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MapLoaderClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return levelNames;
    }
