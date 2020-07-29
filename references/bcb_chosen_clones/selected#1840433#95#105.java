    private void addEntry(File target, String repoPath, ZipOutputStream out) throws IOException {
        ZipEntry entry = new ZipEntry(repoPath);
        int fileLength = (int) target.length();
        FileInputStream fis = new FileInputStream(target);
        byte[] wholeFile = new byte[fileLength];
        fis.read(wholeFile, 0, fileLength);
        fis.close();
        out.putNextEntry(entry);
        out.write(wholeFile, 0, fileLength);
        out.closeEntry();
    }
