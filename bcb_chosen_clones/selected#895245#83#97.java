    private String callPage(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } finally {
            if (reader != null) reader.close();
        }
        return result.toString();
    }
