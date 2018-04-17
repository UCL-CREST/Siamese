    public static boolean copyFile(File from, File to, byte[] buf) {
        if (buf == null) buf = new byte[BUFFER_SIZE];
        FileInputStream from_s = null;
        FileOutputStream to_s = null;
        try {
            from_s = new FileInputStream(from);
            to_s = new FileOutputStream(to);
            for (int bytesRead = from_s.read(buf); bytesRead != -1; bytesRead = from_s.read(buf)) to_s.write(buf, 0, bytesRead);
            from_s.close();
            from_s = null;
            to_s.getFD().sync();
            to_s.close();
            to_s = null;
        } catch (IOException ioe) {
            return false;
        } finally {
            if (from_s != null) {
                try {
                    from_s.close();
                } catch (IOException ioe) {
                }
            }
            if (to_s != null) {
                try {
                    to_s.close();
                } catch (IOException ioe) {
                }
            }
        }
        return true;
    }
