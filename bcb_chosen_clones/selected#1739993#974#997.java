    public void copyFileToFileWithPaths(String sourcePath, String destinPath) throws Exception {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        byte dataBuff[] = new byte[bufferSize];
        File file1 = new File(sourcePath);
        if (file1.exists() && (file1.isFile())) {
            File file2 = new File(destinPath);
            if (file2.exists()) {
                file2.delete();
            }
            FileUtils.getInstance().createDirectory(file2.getParent());
            in = new BufferedInputStream(new FileInputStream(sourcePath), bufferSize);
            out = new BufferedOutputStream(new FileOutputStream(destinPath), bufferSize);
            int readLen;
            while ((readLen = in.read(dataBuff)) > 0) {
                out.write(dataBuff, 0, readLen);
            }
            out.flush();
            in.close();
            out.close();
        } else {
            throw new Exception("Source file not exist ! sourcePath = (" + sourcePath + ")");
        }
    }
