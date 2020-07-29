    public static void gunzip() throws Exception {
        System.out.println("gunzip()");
        GZIPInputStream zipin = new GZIPInputStream(new FileInputStream("/zip/myzip.gz"));
        byte buffer[] = new byte[BLOCKSIZE];
        FileOutputStream out = new FileOutputStream("/zip/covers");
        for (int length; (length = zipin.read(buffer, 0, BLOCKSIZE)) != -1; ) out.write(buffer, 0, length);
        out.close();
        zipin.close();
    }
