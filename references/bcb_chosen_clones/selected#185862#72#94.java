    private static void zip(String sourceDirectory, OutputStream dest) throws FileNotFoundException, IOException {
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        StringBuffer fileList = new StringBuffer();
        getFileList(sourceDirectory, fileList);
        String[] files = fileList.toString().split("\n");
        System.out.println("Filecount:" + (files.length - 1));
        byte[] tmpBuf = new byte[BUFFER];
        for (int i = 1; i < files.length; i++) {
            ZipEntry entry = new ZipEntry(files[i].substring(new File(sourceDirectory).getAbsolutePath().length()));
            out.putNextEntry(entry);
            if (new File(files[i]).isFile()) {
                FileInputStream in = new FileInputStream(files[i]);
                int len;
                while ((len = in.read(tmpBuf)) > 0) {
                    out.write(tmpBuf, 0, len);
                }
                in.close();
            }
            out.closeEntry();
        }
        out.close();
        System.out.println("Compression complete.");
    }
