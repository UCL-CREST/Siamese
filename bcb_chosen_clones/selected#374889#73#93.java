    public String getWebPage(String url) {
        String content = "";
        URL urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException urlEx) {
            urlEx.printStackTrace();
            throw new Error("URL creation failed.");
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlObj.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error("Page retrieval failed.");
        }
        return content;
    }
