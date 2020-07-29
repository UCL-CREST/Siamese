    private void writeJarData() throws IOException {
        jarData.setDefaultFile(defaultPath);
        zipOut.putNextEntry(new ZipEntry(Main.JARDATA_FILENAME.substring(1)));
        ObjectOutputStream objOut = new ObjectOutputStream(zipOut);
        objOut.writeObject(jarData);
        objOut.flush();
        zipOut.closeEntry();
    }
