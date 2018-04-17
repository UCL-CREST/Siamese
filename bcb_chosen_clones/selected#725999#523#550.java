    public static String getUrl(String urlString) {
        int retries = 0;
        String result = "";
        while (true) {
            try {
                URL url = new URL(urlString);
                BufferedReader rdr = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = rdr.readLine();
                while (line != null) {
                    result += line;
                    line = rdr.readLine();
                }
                return result;
            } catch (IOException ex) {
                if (retries == 5) {
                    logger.debug("Problem getting url content exhausted");
                    return result;
                } else {
                    logger.debug("Problem getting url content retrying..." + urlString);
                    try {
                        Thread.sleep((int) Math.pow(2.0, retries) * 1000);
                    } catch (InterruptedException e) {
                    }
                    retries++;
                }
            }
        }
    }
