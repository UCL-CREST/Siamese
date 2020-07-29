    public static boolean fileCopy(String sFileSrc, String sFileDst) {
        boolean ok = true;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File fSrc = new File(sFileSrc);
            int len = 32768;
            byte[] buff = new byte[(int) Math.min(len, fSrc.length())];
            fis = new FileInputStream(fSrc);
            boolean append = false;
            fos = new FileOutputStream(sFileDst, append);
            while (0 < (len = fis.read(buff))) fos.write(buff, 0, len);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            ok = false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JdxLog.logError(ex);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JdxLog.logError(ex);
                }
            }
        }
        return ok;
    }
