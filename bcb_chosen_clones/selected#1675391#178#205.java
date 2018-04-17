    public static void concatenateToDestFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            if (!destFile.createNewFile()) {
                throw new IllegalArgumentException("Could not create destination file:" + destFile.getName());
            }
        }
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        byte[] buffer = new byte[1024];
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destFile, true));
            bufferedInputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            while (true) {
                int readByte = bufferedInputStream.read(buffer, 0, buffer.length);
                if (readByte == -1) {
                    break;
                }
                bufferedOutputStream.write(buffer, 0, readByte);
            }
        } finally {
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }
    }
