    public static void streamCopyFile(File srcFile, File destFile) {
        try {
            FileInputStream fi = new FileInputStream(srcFile);
            FileOutputStream fo = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int readLength = 0;
            while (readLength != -1) {
                readLength = fi.read(buf);
                if (readLength != -1) {
                    fo.write(buf, 0, readLength);
                }
            }
            fo.close();
            fi.close();
        } catch (Exception e) {
        }
    }
