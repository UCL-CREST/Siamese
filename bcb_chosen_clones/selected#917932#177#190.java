    public static void zip(File inFile, File outFile) throws IOException {
        FileInputStream fin = new FileInputStream(inFile);
        FileOutputStream fout = new FileOutputStream(outFile);
        ZipOutputStream zout = new ZipOutputStream(fout);
        zout.putNextEntry(new ZipEntry(inFile.getName()));
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fin.read(buffer)) > 0) {
            zout.write(buffer, 0, len);
        }
        zout.closeEntry();
        zout.close();
        fin.close();
    }
