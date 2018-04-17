    public static String fetchURL(final String u) {
        String retStr = "";
        try {
            final URL url = new URL(u);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                retStr += line;
            }
            reader.close();
        } catch (final MalformedURLException e) {
            logger.severe("MalformedURLException calling url" + e.getMessage());
        } catch (final IOException e) {
            logger.severe("IOException calling url" + e.getMessage());
        }
        return retStr;
    }
