    public final String latestVersion() {
        String latestVersion = "";
        try {
            URL url = new URL(Constants.officialSite + ":80/LatestVersion");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                latestVersion = str;
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return latestVersion;
    }
