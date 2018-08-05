    private void getRdfResponse(StringBuilder sb, String url) {
        try {
            String inputLine = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            while ((inputLine = reader.readLine()) != null) {
                sb.append(inputLine);
            }
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
