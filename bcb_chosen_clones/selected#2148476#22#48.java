    public String getHtmlCode(String urlString) {
        StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        try {
            URL url = new URL((urlString));
            URLConnection con = url.openConnection();
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "ISO-8859-1"));
            String line = null;
            while ((line = in.readLine()) != null) {
                result.append(line + "\r\n");
            }
            in.close();
        } catch (MalformedURLException e) {
            System.out.println("Unable to connect to URL: " + urlString);
        } catch (IOException e) {
            System.out.println("IOException when connecting to URL: " + urlString);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                    System.out.println("Exception throws at finally close reader when connecting to URL: " + urlString);
                }
            }
        }
        return result.toString();
    }
