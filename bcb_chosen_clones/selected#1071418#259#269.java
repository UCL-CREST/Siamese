    private void addManifestInJarFile(File manifestFile, ZipOutputStream outputFileZip) throws IOException {
        InputStream in = new FileInputStream(manifestFile);
        byte[] buf = new byte[1024];
        outputFileZip.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        int len;
        while ((len = in.read(buf)) > 0) {
            outputFileZip.write(buf, 0, len);
        }
        outputFileZip.closeEntry();
        in.close();
    }
