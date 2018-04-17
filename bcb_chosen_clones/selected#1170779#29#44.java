    @Override
    public String getFeedFeed(String sUrl) {
        try {
            URL url = new URL(sUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String result = "";
            String line;
            for (; (line = reader.readLine()) != null; result += line) {
            }
            reader.close();
            return result;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return null;
    }
