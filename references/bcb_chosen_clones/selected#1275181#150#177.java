    static HashSet<ScriptEngineFactory> lookup(ClassLoader loader, String name) {
        HashSet<ScriptEngineFactory> factories = new HashSet<ScriptEngineFactory>();
        try {
            Enumeration<URL> urls = loader.getResources(name);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    if ((line = trim(line)) != null) {
                        try {
                            Class<ScriptEngineFactory> clazz = (Class<ScriptEngineFactory>) Class.forName(line, true, loader);
                            ScriptEngineFactory factory = clazz.newInstance();
                            factories.add(factory);
                        } catch (java.lang.UnsupportedClassVersionError error) {
                            if (DEBUG) {
                                System.err.println(line + ": version mismatch - ignore");
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new ScriptException(ex);
        } finally {
            return factories;
        }
    }
