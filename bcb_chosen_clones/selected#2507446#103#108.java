    public void addStringEntry(String filename, String data) throws Exception {
        zOut.putNextEntry(new ZipEntry(filename));
        zOut.write(data.getBytes());
        zOut.closeEntry();
        fileCount++;
    }
