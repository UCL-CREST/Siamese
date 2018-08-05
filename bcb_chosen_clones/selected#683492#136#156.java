    private void copyFile(File source, File destination) throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(destination);
            int bufferLength = 1024;
            byte[] buffer = new byte[bufferLength];
            int readCount = 0;
            while ((readCount = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, readCount);
            }
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
