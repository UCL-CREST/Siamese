    public String getMarketInfo() {
        try {
            URL url = new URL("http://api.eve-central.com/api/evemon");
            BufferedReader s = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            String xml = "";
            while ((line = s.readLine()) != null) {
                xml += line;
            }
            return xml;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
