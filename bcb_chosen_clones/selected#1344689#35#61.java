    public void init(ServletContext context) throws ScratchException {
        try {
            log.debug("Attempting to load Controllers from file: " + REGISTRY_FILENAME);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urls = classLoader.getResources(REGISTRY_FILENAME);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                log.debug("Found: " + url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String className = null;
                while ((className = reader.readLine()) != null) {
                    className = className.trim();
                    if (!"".equals(className) && !className.startsWith("#")) {
                        log.debug("Found class: " + className);
                        Class<?> clazz = classLoader.loadClass(className);
                        addClass(clazz);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error(e);
        }
    }
