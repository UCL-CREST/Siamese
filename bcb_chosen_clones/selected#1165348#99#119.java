    private String doSearch(String query) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("http://boss.yahooapis.com/ysearch/web/v1/").append(query).append("?appid=wGsFV_DV34EwXnC.2Bt_Ql8Kcir_HmrxMzWUF2fv64CA8ha7e4zgudqXFA8K_J4-&format=xml&filter=-porn");
        try {
            URL url = new URL(queryBuilder.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return safeParseXml(buffer.toString());
        } catch (MalformedURLException e) {
            log.error("The used url is not right : " + queryBuilder.toString(), e);
            return "The used url is not right.";
        } catch (IOException e) {
            log.error("Problem obtaining search results, connection maybe?", e);
            return "Problem obtaining search results, connection maybe?";
        }
    }
