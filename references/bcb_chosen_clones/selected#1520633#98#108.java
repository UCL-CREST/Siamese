    public static void gunzip(File gzippedFile, File destinationFile) throws IOException {
        int buffer = 2048;
        FileInputStream in = new FileInputStream(gzippedFile);
        GZIPInputStream zipin = new GZIPInputStream(in);
        byte[] data = new byte[buffer];
        FileOutputStream out = new FileOutputStream(destinationFile);
        int length;
        while ((length = zipin.read(data, 0, buffer)) != -1) out.write(data, 0, length);
        out.close();
        zipin.close();
    }
