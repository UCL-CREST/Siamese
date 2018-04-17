    protected static boolean copyFile(File src, File dest) {
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            FileInputStream fis = new FileInputStream(src);
            FileOutputStream fos = new FileOutputStream(dest);
            byte[] temp = new byte[1024 * 8];
            int readSize = 0;
            do {
                readSize = fis.read(temp);
                fos.write(temp, 0, readSize);
            } while (readSize == temp.length);
            temp = null;
            fis.close();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
