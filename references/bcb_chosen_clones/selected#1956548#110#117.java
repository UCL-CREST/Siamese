    static void addFile(String entryName, String file, ZipOutputStream zout) throws IOException {
        ZipEntry mf = new ZipEntry(entryName);
        File mfm = new File(file);
        mf.setSize(mfm.length());
        mf.setTime(mfm.lastModified());
        zout.putNextEntry(mf);
        zout.write(IOUtils.load(mfm));
    }
