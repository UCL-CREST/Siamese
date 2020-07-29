    public static String getInstanceUserdata() throws IOException {
        int retries = 0;
        while (true) {
            try {
                URL url = new URL("http://169.254.169.254/latest/user-data/");
                InputStreamReader rdr = new InputStreamReader(url.openStream());
                StringWriter wtr = new StringWriter();
                char[] buf = new char[1024];
                int bytes;
                while ((bytes = rdr.read(buf)) > -1) {
                    if (bytes > 0) {
                        wtr.write(buf, 0, bytes);
                    }
                }
                rdr.close();
                return wtr.toString();
            } catch (IOException ex) {
                if (retries == 5) {
                    logger.debug("Problem getting user data, retries exhausted...");
                    return null;
                } else {
                    logger.debug("Problem getting user data, retrying...");
                    try {
                        Thread.sleep((int) Math.pow(2.0, retries) * 1000);
                    } catch (InterruptedException e) {
                    }
                    retries++;
                }
            }
        }
    }
