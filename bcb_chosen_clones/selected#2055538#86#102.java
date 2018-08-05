    private boolean _copyPath(String source, String destination, Object handler) {
        try {
            FileInputStream fis = new FileInputStream(_fullPathForPath(source));
            FileOutputStream fos = new FileOutputStream(_fullPathForPath(destination));
            byte[] buffer = new byte[fis.available()];
            int read;
            for (read = fis.read(buffer); read >= 0; read = fis.read(buffer)) {
                fos.write(buffer, 0, read);
            }
            fis.close();
            fos.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
