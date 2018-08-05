    public MapInfo loadLocalMapData(String fileName) {
        MapInfo info = mapCacheLocal.get(fileName);
        if (info != null && info.getContent() == null) {
            try {
                BufferedReader bufferedreader;
                URL fetchUrl = new URL(localMapContextUrl, fileName);
                URLConnection urlconnection = fetchUrl.openConnection();
                if (urlconnection.getContentEncoding() != null) {
                    bufferedreader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), urlconnection.getContentEncoding()));
                } else {
                    bufferedreader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "utf-8"));
                }
                String line;
                StringBuilder mapContent = new StringBuilder();
                while ((line = bufferedreader.readLine()) != null) {
                    mapContent.append(line);
                    mapContent.append("\n");
                }
                info.setContent(mapContent.toString());
                GameMapImplementation gameMap = GameMapImplementation.createFromMapInfo(info);
            } catch (IOException _ex) {
                System.err.println("HexTD::readFile:: Can't read from " + fileName);
            }
        }
        return info;
    }
