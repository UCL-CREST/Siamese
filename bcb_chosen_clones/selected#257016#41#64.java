    private String readDataFromUrl(URL url) throws IOException {
        InputStream inputStream = null;
        InputStreamReader streamReader = null;
        BufferedReader in = null;
        StringBuffer data = new StringBuffer();
        try {
            inputStream = url.openStream();
            streamReader = new InputStreamReader(inputStream);
            in = new BufferedReader(streamReader);
            String inputLine;
            while ((inputLine = in.readLine()) != null) data.append(inputLine);
        } finally {
            if (in != null) {
                in.close();
            }
            if (streamReader != null) {
                streamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data.toString();
    }
