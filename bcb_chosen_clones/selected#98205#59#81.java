    private String getJSONScoreStringFromNet(URL urladdress) {
        InputStream instream = null;
        BufferedReader read = null;
        try {
            instream = urladdress.openStream();
            read = new BufferedReader(new InputStreamReader(instream));
            String s = new String("");
            String line = null;
            while ((line = read.readLine()) != null) {
                s = s + line;
            }
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                read.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
