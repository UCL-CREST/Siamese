    public static long copyFile(File source, File target) throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            FileChannel in = fileInputStream.getChannel();
            FileChannel out = fileOutputStream.getChannel();
            return out.transferFrom(in, 0, source.length());
        } finally {
            if (fileInputStream != null) fileInputStream.close();
            if (fileOutputStream != null) fileOutputStream.close();
        }
    }
