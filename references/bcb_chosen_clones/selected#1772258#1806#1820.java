    private String fetch(URL url) {
        StringBuilder body = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                body.append(inputLine);
            }
            in.close();
            return body.toString();
        } catch (Exception e) {
            debug("Error: fetch: Exception reading URL: " + e);
        }
        return null;
    }
