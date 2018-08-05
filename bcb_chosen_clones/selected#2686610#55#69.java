    public void addFile(File file) throws Exception {
        FileInputStream tFINS = new FileInputStream(file);
        final int bufLength = 1024;
        byte[] buffer = new byte[bufLength];
        int readReturn = 0;
        zOut.putNextEntry(new ZipEntry(file.getName()));
        do {
            readReturn = tFINS.read(buffer);
            if (readReturn != -1) {
                zOut.write(buffer, 0, readReturn);
            }
        } while (readReturn != -1);
        zOut.closeEntry();
        fileCount++;
    }
