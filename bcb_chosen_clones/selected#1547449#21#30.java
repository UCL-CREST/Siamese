    public static String httpGet(URL url) throws Exception {
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer content = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        return content.toString();
    }
