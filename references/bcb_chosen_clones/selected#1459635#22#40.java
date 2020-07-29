    public static void compress(File src, File dest) throws ZipException, IOException {
        FileOutputStream fout = new FileOutputStream(dest);
        ZipOutputStream zout = new ZipOutputStream(fout);
        ZipEntry ze = new ZipEntry(src.getName());
        zout.putNextEntry(ze);
        FileInputStream in = new FileInputStream(src);
        byte[] tab = new byte[4096];
        int lu = -1;
        do {
            lu = in.read(tab);
            if (lu > 0) zout.write(tab, 0, lu);
        } while (lu > 0);
        zout.finish();
        in.close();
        zout.closeEntry();
        zout.close();
        fout.close();
        in.close();
    }
