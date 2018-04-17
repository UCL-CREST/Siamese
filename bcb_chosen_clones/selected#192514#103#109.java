    protected void writeInstallerObject(String entryName, Object object) throws IOException {
        primaryJarStream.putNextEntry(new org.apache.tools.zip.ZipEntry(entryName));
        ObjectOutputStream out = new ObjectOutputStream(primaryJarStream);
        out.writeObject(object);
        out.flush();
        primaryJarStream.closeEntry();
    }
