    private Document getOpenLinkResponse(String queryDoc) throws IOException, UnvalidResponseException {
        URL url = new URL(WS_URI);
        URLConnection conn = url.openConnection();
        logger.debug(".conn open");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml");
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(queryDoc);
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        logger.debug(".resp obtained");
        StringBuffer responseBuffer = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            responseBuffer.append(line);
            responseBuffer.append(NEWLINE);
        }
        wr.close();
        rd.close();
        logger.debug(".done");
        try {
            return documentParser.parse(responseBuffer.toString());
        } catch (SAXException e) {
            throw new UnvalidResponseException("Response is not a valid XML file", e);
        }
    }
