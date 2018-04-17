    private static String executeGet(String urlStr) {
        StringBuffer result = new StringBuffer();
        try {
            Authentication.doIt();
            URL url = new URL(urlStr);
            System.out.println("Host: " + url.getHost());
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
