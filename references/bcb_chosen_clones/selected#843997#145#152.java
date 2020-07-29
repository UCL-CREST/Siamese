    static void addFile(String entryName, String fileName, ZipOutputStream zout) throws IOException {
        ZipEntry mf = new ZipEntry(entryName);
        File mfm = new File(fileName);
        mf.setSize(mfm.length());
        mf.setTime(mfm.lastModified());
        zout.putNextEntry(mf);
        zout.write(IOUtils.load(mfm));
    }
