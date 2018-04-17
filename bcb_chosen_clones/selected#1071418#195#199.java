    private void addAManifestInJarFile(Manifest mf, ZipOutputStream outputFileZip, String fileName) throws IOException {
        outputFileZip.putNextEntry(new ZipEntry(fileName));
        mf.write(outputFileZip);
        outputFileZip.closeEntry();
    }
