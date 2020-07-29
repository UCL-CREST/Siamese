    public boolean loadResource(String resourcePath) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
            if (url == null) {
                logger.error("Cannot find the resource named: '" + resourcePath + "'. Failed to load the keyword list.");
                return false;
            }
            InputStreamReader isr = new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader(isr);
            String ligne = br.readLine();
            while (ligne != null) {
                if (!contains(ligne.toUpperCase())) addLast(ligne.toUpperCase());
                ligne = br.readLine();
            }
            return true;
        } catch (IOException ioe) {
            logger.log(Level.ERROR, "Cannot load default SQL keywords file.", ioe);
        }
        return false;
    }
