    private String getHTML(String pageURL, String encoding, String dirPath) throws IOException {
        StringBuilder pageHTML = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(pageURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "MSIE 7.0");
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            String line = null;
            while ((line = br.readLine()) != null) {
                pageHTML.append(line);
                pageHTML.append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        if (dirPath != null) {
            File file = new File(dirPath);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(pageHTML.toString());
            bufferedWriter.close();
        }
        return pageHTML.toString();
    }
