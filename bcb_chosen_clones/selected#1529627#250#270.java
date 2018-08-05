    public static Model downloadModel(String url) {
        Model model = ModelFactory.createDefaultModel();
        try {
            URLConnection connection = new URL(url).openConnection();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestProperty("Accept", "application/rdf+xml, */*;q=.1");
                httpConnection.setRequestProperty("Accept-Language", "en");
            }
            InputStream in = connection.getInputStream();
            model.read(in, url);
            in.close();
            return model;
        } catch (MalformedURLException e) {
            logger.debug("Unable to download model from " + url, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.debug("Unable to download model from " + url, e);
            throw new RuntimeException(e);
        }
    }
