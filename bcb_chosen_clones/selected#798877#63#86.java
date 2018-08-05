    @Override
    public void exec() {
        BufferedReader in = null;
        try {
            URL url = new URL(getUrl());
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer result = new StringBuffer();
            String str;
            while ((str = in.readLine()) != null) {
                result.append(str);
            }
            logger.info("received message: " + result);
        } catch (Exception e) {
            logger.error("HttpGetEvent could not execute", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("BufferedReader could not be closed", e);
                }
            }
        }
    }
