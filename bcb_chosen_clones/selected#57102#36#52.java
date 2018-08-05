    public static boolean copyFile(File from, File tu) {
        final int BUFFER_SIZE = 4096;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            FileInputStream in = new FileInputStream(from);
            FileOutputStream out = new FileOutputStream(tu);
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
