    public void addEntry(InputStream jis, JarEntry entry) throws IOException, URISyntaxException {
        File target = new File(this.target.getPath() + entry.getName()).getAbsoluteFile();
        if (!target.exists()) {
            target.createNewFile();
        }
        if ((new File(this.source.toURI())).isDirectory()) {
            File sourceEntry = new File(this.source.getPath() + entry.getName());
            FileInputStream fis = new FileInputStream(sourceEntry);
            byte[] classBytes = new byte[fis.available()];
            fis.read(classBytes);
            (new FileOutputStream(target)).write(classBytes);
        } else {
            readwriteStreams(jis, (new FileOutputStream(target)));
        }
    }
