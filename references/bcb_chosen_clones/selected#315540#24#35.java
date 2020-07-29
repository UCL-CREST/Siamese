    public static void zip() throws Exception {
        System.out.println("zip()");
        ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(new File("/zip/myzip.zip")));
        ZipEntry entry = new ZipEntry("asdf.script");
        zipout.putNextEntry(entry);
        byte buffer[] = new byte[BLOCKSIZE];
        FileInputStream in = new FileInputStream(new File("/zip/asdf.script"));
        for (int length; (length = in.read(buffer, 0, BLOCKSIZE)) != -1; ) zipout.write(buffer, 0, length);
        in.close();
        zipout.closeEntry();
        zipout.close();
    }
