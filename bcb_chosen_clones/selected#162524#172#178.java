    protected void writeInstallerObject(String entryName, Object object) throws IOException {
        primaryJarStream.putNextEntry(new ZipEntry(entryName));
        ObjectOutputStream out = new ObjectOutputStream(primaryJarStream);
        out.writeObject(object);
        out.flush();
        primaryJarStream.closeEntry();
    }
