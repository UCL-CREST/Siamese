    private void callService() {
        try {
            URL url = new URL(baseUrl + servicePath + attributes);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            StringBuffer buf = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                buf.append(inputLine);
            }
            in.close();
            answer = buf.toString();
        } catch (MalformedURLException e) {
            answer = "Malformed Url:" + e.getMessage();
            return;
        } catch (IOException e) {
            answer = "I/O exception: " + e.getMessage();
            return;
        }
    }
