    public boolean isServerAlive(String pStrURL) {
        boolean isAlive;
        long t1 = System.currentTimeMillis();
        try {
            URL url = new URL(pStrURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                logger.fine(inputLine);
            }
            logger.info("**  Connection successful..  **");
            in.close();
            isAlive = true;
        } catch (Exception e) {
            logger.info("**  Connection failed..  **");
            e.printStackTrace();
            isAlive = false;
        }
        long t2 = System.currentTimeMillis();
        logger.info("Time taken to check connection: " + (t2 - t1) + " ms.");
        return isAlive;
    }
