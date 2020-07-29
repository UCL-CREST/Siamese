    public static long copy(File src, File dest) throws UtilException {
        FileChannel srcFc = null;
        FileChannel destFc = null;
        try {
            srcFc = new FileInputStream(src).getChannel();
            destFc = new FileOutputStream(dest).getChannel();
            long srcLength = srcFc.size();
            srcFc.transferTo(0, srcLength, destFc);
            return srcLength;
        } catch (IOException e) {
            throw new UtilException(e);
        } finally {
            try {
                if (srcFc != null) srcFc.close();
                srcFc = null;
            } catch (IOException e) {
            }
            try {
                if (destFc != null) destFc.close();
                destFc = null;
            } catch (IOException e) {
            }
        }
    }
