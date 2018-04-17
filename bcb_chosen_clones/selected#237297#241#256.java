    public String getLatestVersion(String website) {
        String latestVersion = "";
        try {
            URL url = new URL(website + "/version");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                latestVersion = string;
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
        return latestVersion;
    }
