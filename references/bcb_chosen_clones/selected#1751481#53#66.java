    public static String createStringFromHtml(MyUrl url) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.getUrl().openStream(), "UTF-8"));
            String line;
            String xmlAsString = "";
            while ((line = reader.readLine()) != null) {
                xmlAsString += line;
            }
            reader.close();
            return xmlAsString;
        } catch (Exception e) {
            return null;
        }
    }
