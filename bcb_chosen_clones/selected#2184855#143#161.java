    private String GetResponse(URL url) {
        String content = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) content += line;
            } else {
            }
        } catch (MalformedURLException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return content;
    }
