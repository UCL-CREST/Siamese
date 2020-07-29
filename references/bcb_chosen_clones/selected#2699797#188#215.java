    protected void setupZipFile(String path, int entrySize, int filePerDir, int count) throws Exception {
        Random rand = new Random(Double.doubleToLongBits(Math.PI));
        if (filePerDir < 1) filePerDir = 10;
        String filePath = getWorkDir() + File.separator + path;
        File f = new File(filePath);
        if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
        ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(filePath));
        int num = 0;
        byte[] dataBuff = new byte[entrySize];
        try {
            for (int i = 0; i < count; i++) {
                StringBuffer entryPath = new StringBuffer();
                num = i;
                do {
                    int a = num % filePerDir;
                    num = (int) (num / filePerDir);
                    if (entryPath.length() > 0) entryPath.append("/");
                    if (0 < num) entryPath.append("entry-dir-" + a); else entryPath.append("entry" + i);
                } while (0 < num);
                zo.putNextEntry(new ZipEntry(new String(entryPath)));
                fillData(rand, dataBuff);
                zo.write(dataBuff);
                zo.closeEntry();
            }
        } finally {
            if (zo != null) zo.close();
        }
    }
