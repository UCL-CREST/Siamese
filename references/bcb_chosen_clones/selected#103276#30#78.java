    protected Configuration() {
        try {
            Enumeration<URL> resources = getClass().getClassLoader().getResources("activejdbc_models.properties");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                LogFilter.log(logger, "Load models from: " + url.toExternalForm());
                InputStream inputStream = null;
                try {
                    inputStream = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = Util.split(line, ':');
                        String modelName = parts[0];
                        String dbName = parts[1];
                        if (modelsMap.get(dbName) == null) {
                            modelsMap.put(dbName, new ArrayList<String>());
                        }
                        modelsMap.get(dbName).add(modelName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) inputStream.close();
                }
            }
        } catch (IOException e) {
            throw new InitException(e);
        }
        if (modelsMap.isEmpty()) {
            LogFilter.log(logger, "ActiveJDBC Warning: Cannot locate any models, assuming project without models.");
            return;
        }
        try {
            InputStream in = getClass().getResourceAsStream("/activejdbc.properties");
            if (in != null) properties.load(in);
        } catch (Exception e) {
            throw new InitException(e);
        }
        String cacheManagerClass = properties.getProperty("cache.manager");
        if (cacheManagerClass != null) {
            try {
                Class cmc = Class.forName(cacheManagerClass);
                cacheManager = (CacheManager) cmc.newInstance();
            } catch (Exception e) {
                throw new InitException("failed to initialize a CacheManager. Please, ensure that the property " + "'cache.manager' points to correct class which extends 'activejdbc.cache.CacheManager' class and provides a default constructor.", e);
            }
        }
    }
