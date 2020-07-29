    public JarGenerator(File outputJarInstaller, String baseZipFileResource) throws IOException {
        outputJarInstaller.delete();
        InputStream jixJarInputStream = JarGenerator.class.getResourceAsStream(baseZipFileResource);
        if (jixJarInputStream == null) throw new IOException(baseZipFileResource + " was not found!");
        ZipInputStream zis = new ZipInputStream(jixJarInputStream);
        this.zos = new ZipOutputStream(new FileOutputStream(outputJarInstaller));
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            zos.putNextEntry(new ZipEntry(zipEntry));
            int length;
            while ((length = zis.read(buffer)) != -1) zos.write(buffer, 0, length);
            zis.closeEntry();
            zos.closeEntry();
        }
        zis.close();
    }
