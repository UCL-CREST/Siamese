    public Document retrieveDefinition(String uri) throws IOException, UnvalidResponseException {
        if (!isADbPediaURI(uri)) throw new IllegalArgumentException("Not a DbPedia Resource URI");
        String rawDataUri = fromResourceToRawDataUri(uri);
        URL url = new URL(rawDataUri);
        URLConnection conn = url.openConnection();
        logger.debug(".conn open");
        conn.setDoOutput(true);
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        logger.debug(".resp obtained");
        StringBuffer responseBuffer = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            responseBuffer.append(line);
            responseBuffer.append(NEWLINE);
        }
        rd.close();
        logger.debug(".done");
        try {
            return documentParser.parse(responseBuffer.toString());
        } catch (SAXException e) {
            throw new UnvalidResponseException("Incorrect XML document", e);
        }
    }
