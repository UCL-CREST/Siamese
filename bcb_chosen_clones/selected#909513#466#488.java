    public static Vector getVectorForm(String u, String usr, String pwd) {
        Vector response = new Vector();
        logger.debug("Attempting to call: " + u);
        logger.debug("Creating Authenticator: usr=" + usr + ", pwd=" + pwd);
        Authenticator.setDefault(new CustomAuthenticator(usr, pwd));
        try {
            URL url = new URL(u);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                response.add(str);
            }
            in.close();
            logger.debug("Response: " + response.toString());
        } catch (MalformedURLException e) {
            logger.error(e);
            logger.trace(e, e);
        } catch (IOException e) {
            logger.error(e);
            logger.trace(e, e);
        }
        return response;
    }
