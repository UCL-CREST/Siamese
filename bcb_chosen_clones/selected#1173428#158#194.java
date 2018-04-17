    @SuppressWarnings("unchecked")
    public static <T> List<T> getServices(String service) {
        String serviceUri = "META-INF/services/" + service;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources(serviceUri);
            if (urls.hasMoreElements()) {
                List<T> services = new ArrayList<T>(1);
                do {
                    URL url = urls.nextElement();
                    _LOG.finest("Processing:{0}", url);
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                        try {
                            while (true) {
                                String line = in.readLine();
                                if (line == null) break;
                                T instance = (T) _parseLine(loader, line);
                                if (instance != null) services.add(instance);
                            }
                        } finally {
                            in.close();
                        }
                    } catch (Exception e) {
                        _LOG.warning("ERR_PARSING_URL", url);
                        _LOG.warning(e);
                    }
                } while (urls.hasMoreElements());
                if (services.size() == 1) return Collections.singletonList(services.get(0));
                return Collections.unmodifiableList(services);
            }
        } catch (IOException e) {
            _LOG.severe("ERR_LOADING_RESROUCE", serviceUri);
            _LOG.severe(e);
        }
        return Collections.emptyList();
    }
