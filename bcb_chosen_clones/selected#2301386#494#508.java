    private String readData(URL url) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer responseBuffer = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                responseBuffer.append(line);
            }
            in.close();
            return new String(responseBuffer);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
