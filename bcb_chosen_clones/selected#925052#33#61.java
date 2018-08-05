    public static void registerProviders(ResteasyProviderFactory factory) throws Exception {
        Enumeration<URL> en = Thread.currentThread().getContextClassLoader().getResources("META-INF/services/" + Providers.class.getName());
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        while (en.hasMoreElements()) {
            URL url = en.nextElement();
            InputStream is = url.openStream();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.equals("")) continue;
                    set.add(line);
                }
            } finally {
                is.close();
            }
        }
        for (String line : set) {
            try {
                Class clazz = Thread.currentThread().getContextClassLoader().loadClass(line);
                factory.registerProvider(clazz, true);
            } catch (NoClassDefFoundError e) {
                logger.warn("NoClassDefFoundError: Unable to load builtin provider: " + line);
            } catch (ClassNotFoundException e) {
                logger.warn("ClassNotFoundException: Unable to load builtin provider: " + line);
            }
        }
    }
