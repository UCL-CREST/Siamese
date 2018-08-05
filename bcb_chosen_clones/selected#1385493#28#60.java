    public static Map<String, String> getInstanceMetadata() {
        HashMap<String, String> result = new HashMap<String, String>();
        int retries = 0;
        while (true) {
            try {
                URL url = new URL("http://169.254.169.254/latest/meta-data/");
                BufferedReader rdr = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = rdr.readLine();
                while (line != null) {
                    try {
                        String val = getInstanceMetadata(line);
                        result.put(line, val);
                    } catch (IOException ex) {
                        logger.error("Problem fetching piece of instance metadata!", ex);
                    }
                    line = rdr.readLine();
                }
                return result;
            } catch (IOException ex) {
                if (retries == 5) {
                    logger.debug("Problem getting instance data, retries exhausted...");
                    return result;
                } else {
                    logger.debug("Problem getting instance data, retrying...");
                    try {
                        Thread.sleep((int) Math.pow(2.0, retries) * 1000);
                    } catch (InterruptedException e) {
                    }
                    retries++;
                }
            }
        }
    }
