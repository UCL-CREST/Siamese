    public static boolean copyFile(String sourceFileName, String destFileName) {
        if (sourceFileName == null || destFileName == null) return false;
        if (sourceFileName.equals(destFileName)) return false;
        try {
            java.io.FileInputStream in = new java.io.FileInputStream(sourceFileName);
            java.io.FileOutputStream out = new java.io.FileOutputStream(destFileName);
            try {
                byte[] buf = new byte[31000];
                int read = in.read(buf);
                while (read > -1) {
                    out.write(buf, 0, read);
                    read = in.read(buf);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }
