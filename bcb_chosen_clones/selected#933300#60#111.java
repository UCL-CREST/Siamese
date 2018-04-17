    public String sendXml(URL url, String xmlMessage, boolean isResponseExpected) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        if (xmlMessage == null) {
            throw new IllegalArgumentException("xmlMessage == null");
        }
        LOGGER.finer("url = " + url);
        LOGGER.finer("xmlMessage = :" + xmlMessage + ":");
        LOGGER.finer("isResponseExpected = " + isResponseExpected);
        String answer = null;
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Content-type", "text/xml");
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            Writer writer = null;
            try {
                writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(xmlMessage);
                writer.flush();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            LOGGER.finer("message written");
            StringBuilder sb = new StringBuilder();
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                if (isResponseExpected) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine).append("\n");
                    }
                    answer = sb.toString();
                    LOGGER.finer("response read");
                }
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "No response", e);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (ConnectException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        LOGGER.finer("answer = :" + answer + ":");
        return answer;
    }
