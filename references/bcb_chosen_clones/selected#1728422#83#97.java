    private void loadMe(final ResourceBackedScript e) {
        if (!loadedResources.containsKey(this.resourceLocation)) {
            final InputStream resourceAsStream = this.getClass().getResourceAsStream(this.resourceLocation);
            final StringWriter writer = new StringWriter();
            try {
                IOUtils.copy(resourceAsStream, writer);
            } catch (final IOException ex) {
                throw new IllegalStateException("Resource not read-able", ex);
            }
            final String loadedResource = writer.toString();
            loadedResources.put(this.resourceLocation, loadedResource);
        }
        this.setScriptBody(loadedResources.get(this.resourceLocation));
        this.hasRendered = true;
    }
