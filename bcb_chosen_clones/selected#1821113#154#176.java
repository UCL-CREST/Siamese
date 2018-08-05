    private HashSet<String> loadSupportedAnnotationTypes(VannitationType baseVannitationType) {
        Enumeration<URL> urls = null;
        try {
            urls = this.getClass().getClassLoader().getResources("META-INF/" + baseVannitationType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the annotations we support", e);
        }
        supportedAnnotationTypes.put(baseVannitationType, new HashSet<String>());
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    supportedAnnotationTypes.get(baseVannitationType).add(line.trim());
                }
                reader.close();
            } catch (Exception e) {
                throw new RuntimeException("Could not open " + url);
            }
        }
        return supportedAnnotationTypes.get(baseVannitationType);
    }
