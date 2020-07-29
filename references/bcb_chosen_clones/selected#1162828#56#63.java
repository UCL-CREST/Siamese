    private void writeClassFile(String filename) throws IOException {
        InputStream in = JarArchiveWriter.class.getResourceAsStream(filename);
        if (in == null) throw new FileNotFoundException(filename);
        zipOut.putNextEntry(new ZipEntry(filename.substring(1)));
        int b;
        while ((b = in.read()) != -1) zipOut.write(b);
        zipOut.closeEntry();
    }
