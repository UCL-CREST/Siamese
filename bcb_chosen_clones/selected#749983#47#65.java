    public String getPloidy(String source) {
        StringBuilder ploidyHtml = new StringBuilder();
        String hyperdiploidyUrl = customParameters.getHyperdiploidyUrl();
        String urlString = hyperdiploidyUrl + "?source=" + source;
        URL url = null;
        try {
            url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                ploidyHtml.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ploidyHtml.toString();
    }
