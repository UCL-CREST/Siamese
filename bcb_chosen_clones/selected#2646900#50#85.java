    public static boolean copyFile(File dest, File source) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        boolean rv = false;
        byte[] buf = new byte[1000000];
        int bytesRead = 0;
        if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(dest);
            while ((bytesRead = fis.read(buf)) > 0) fos.write(buf, 0, bytesRead);
            fis.close();
            fis = null;
            fos.close();
            fos = null;
            rv = true;
        } catch (Throwable t) {
            throw new ApplicationException("copy error (" + source.getAbsolutePath() + " => " + dest.getAbsolutePath(), t);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
                fis = null;
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
                fos = null;
            }
        }
        return rv;
    }
