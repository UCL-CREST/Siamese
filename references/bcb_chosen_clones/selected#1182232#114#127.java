    private List<String> getHashesFrom(String webPage) {
        Vector<String> out = new Vector();
        try {
            URL url = new URL(webPage);
            BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = r.readLine()) != null) {
                out.add(line);
            }
        } catch (Exception X) {
            return null;
        }
        return out;
    }
