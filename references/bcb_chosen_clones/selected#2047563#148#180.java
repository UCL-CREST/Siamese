    public void loadExistingAntlibs(ClassLoader classLoader) {
        URL antlibUrl;
        URI antlibUri;
        try {
            Enumeration<URL> resources = classLoader == null ? ClassLoader.getSystemResources(antLibsResource) : classLoader.getResources(antLibsResource);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                InputStream stream = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    String pkg = line.trim();
                    URI uri = URI.create("antlib:" + pkg);
                    URI resource2antlib = URI.create(antLibsResource2root + pkg.replace('.', '/') + (pkg.isEmpty() ? "" : "/") + "antlib.xml");
                    antlibUri = NetUtils.resolve(url.toURI(), resource2antlib);
                    try {
                        antlibUrl = antlibUri.toURL();
                    } catch (IllegalArgumentException e) {
                        System.err.println("base uri: " + url);
                        System.err.println("relativepath: " + resource2antlib);
                        System.err.println("target uri: " + antlibUri);
                        throw new RuntimeException(antlibUri.toString(), e);
                    }
                    loadAntLib(antlibUrl, uri);
                }
                reader.close();
                stream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
