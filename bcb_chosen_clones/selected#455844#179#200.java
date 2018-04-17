    private void copyFile(File tempFile, String eName) throws IOException {
        ZipEntry ze;
        FileInputStream fis;
        BufferedInputStream bis;
        boolean theEnd;
        int trans;
        ze = new ZipEntry(eName);
        zos.putNextEntry(ze);
        fis = new FileInputStream(tempFile);
        bis = new BufferedInputStream(fis);
        theEnd = false;
        while (!theEnd) {
            trans = bis.read();
            if (trans == -1) {
                theEnd = true;
            } else {
                bos.write(trans);
            }
        }
        bis.close();
        bos.flush();
    }
