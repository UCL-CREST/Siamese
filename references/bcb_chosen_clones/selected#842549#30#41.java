    public static String compressFile(String fileName) throws IOException {
        String newFileName = fileName + ".gz";
        FileInputStream fis = new FileInputStream(fileName);
        FileOutputStream fos = new FileOutputStream(newFileName);
        GZIPOutputStream gzos = new GZIPOutputStream(fos);
        byte[] buf = new byte[10000];
        int bytesRead;
        while ((bytesRead = fis.read(buf)) > 0) gzos.write(buf, 0, bytesRead);
        fis.close();
        gzos.close();
        return newFileName;
    }
