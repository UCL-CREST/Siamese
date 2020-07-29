    @Override
    public String getData(String blipApiPath, String authHeader) {
        try {
            URL url = new URL(BLIP_API_URL + blipApiPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (authHeader != null) {
                conn.addRequestProperty("Authorization", "Basic " + authHeader);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer content = new StringBuffer();
            System.out.println("Resp code " + conn.getResponseCode());
            while ((line = reader.readLine()) != null) {
                System.out.println(">> " + line);
                content.append(line);
            }
            reader.close();
            return content.toString();
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
