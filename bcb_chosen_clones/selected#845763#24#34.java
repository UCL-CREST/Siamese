    public ThemeBuilder addResource(String resourceKey, byte[] resourceValue) throws IOException {
        try {
            zip.putNextEntry(new ZipEntry(resourceKey));
            zip.write(resourceValue);
            zip.closeEntry();
        } catch (IOException ex) {
            System.err.println("Error when adding resource to theme: " + ex);
            throw ex;
        }
        return this;
    }
