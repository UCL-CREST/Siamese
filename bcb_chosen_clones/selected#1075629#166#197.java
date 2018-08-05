    private static Crypto loadClass(String cryptoClassName, Map map, ClassLoader loader) {
        Class cryptogenClass = null;
        Crypto crypto = null;
        try {
            cryptogenClass = Loader.loadClass(loader, cryptoClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(cryptoClassName + " Not Found");
        }
        log.debug("Using Crypto Engine [" + cryptoClassName + "]");
        try {
            Class[] classes = null;
            if (map instanceof Properties) {
                classes = new Class[] { Properties.class, ClassLoader.class };
            } else {
                classes = new Class[] { Map.class, ClassLoader.class };
            }
            Constructor c = cryptogenClass.getConstructor(classes);
            crypto = (Crypto) c.newInstance(new Object[] { map, loader });
            return crypto;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            log.error("Unable to instantiate (1): " + cryptoClassName, e);
        }
        try {
            crypto = (Crypto) cryptogenClass.newInstance();
            return crypto;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            log.error("Unable to instantiate (2): " + cryptoClassName, e);
            throw new RuntimeException(cryptoClassName + " cannot create instance");
        }
    }
