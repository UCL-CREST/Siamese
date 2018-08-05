    public static File zipFile(String pathToBeZipped, String pathZippedFile) throws IOException {
        if (pathZippedFile.indexOf(".zip") < 0) {
            pathZippedFile += ".zip";
        }
        File fileASerZipado = new File(pathToBeZipped);
        byte[] buf = new byte[BUFFER_SIZE];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(pathZippedFile));
        FileInputStream in = new FileInputStream(pathToBeZipped);
        out.putNextEntry(new ZipEntry(fileASerZipado.getName()));
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.closeEntry();
        in.close();
        out.close();
        return new File(pathZippedFile);
    }
