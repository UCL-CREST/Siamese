    public static final boolean compressToZip(final String sSource, final String sDest, final boolean bDeleteSourceOnSuccess) {
        ZipOutputStream os = null;
        InputStream is = null;
        try {
            os = new ZipOutputStream(new FileOutputStream(sDest));
            is = new FileInputStream(sSource);
            final byte[] buff = new byte[1024];
            int r;
            String sFileName = sSource;
            if (sFileName.indexOf('/') >= 0) sFileName = sFileName.substring(sFileName.lastIndexOf('/') + 1);
            os.putNextEntry(new ZipEntry(sFileName));
            while ((r = is.read(buff)) > 0) os.write(buff, 0, r);
            is.close();
            os.flush();
            os.closeEntry();
            os.close();
        } catch (Throwable e) {
            Log.log(Log.WARNING, "lazyj.Utils", "compressToZip : cannot compress '" + sSource + "' to '" + sDest + "' because", e);
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ioe) {
                }
            }
        }
        if (bDeleteSourceOnSuccess) try {
            if (!(new File(sSource)).delete()) Log.log(Log.WARNING, "lazyj.Utils", "compressToZip: could not delete original file (" + sSource + ")");
        } catch (SecurityException se) {
            Log.log(Log.ERROR, "lazyj.Utils", "compressToZip: security constraints prevents file deletion");
        }
        return true;
    }
