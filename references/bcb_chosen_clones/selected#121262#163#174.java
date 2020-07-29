    private void addZipFile(String filenameIn, ZipOutputStream out) throws FileNotFoundException, IOException {
        byte[] buf = new byte[1024];
        File fileIn = new File(filenameIn);
        out.putNextEntry(new ZipEntry(fileIn.getName()));
        FileInputStream in = new FileInputStream(fileIn);
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.closeEntry();
        in.close();
    }
