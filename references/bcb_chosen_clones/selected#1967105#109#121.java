    public void add(final String name, final String content) {
        forBundle(new BundleManipulator() {

            public boolean includeEntry(String entryName) {
                return !name.equals(entryName);
            }

            public void finish(Bundle bundle, ZipOutputStream zout) throws IOException {
                zout.putNextEntry(new ZipEntry(name));
                IOUtils.copy(new StringReader(content), zout, "UTF-8");
            }
        });
    }
