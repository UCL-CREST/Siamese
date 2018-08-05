    static void addFile(String entryName, String fileName, ZipOutputStream zout) throws IOException {
        ZipEntry mf = new ZipEntry(entryName);
        File mfm = new File(fileName);
        if (mfm.canRead()) {
            mf.setSize(mfm.length());
            mf.setTime(mfm.lastModified());
            zout.putNextEntry(mf);
            zout.write(IOUtils.load(mfm));
        } else {
            throw new RuntimeException("File not found: " + mfm.toString());
        }
    }
