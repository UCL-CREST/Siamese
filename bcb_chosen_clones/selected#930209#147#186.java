    public static final void copyFile(String srcFilename, String dstFilename) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel ifc = null;
        FileChannel ofc = null;
        Util.copyBuffer.clear();
        try {
            fis = new FileInputStream(srcFilename);
            ifc = fis.getChannel();
            fos = new FileOutputStream(dstFilename);
            ofc = fos.getChannel();
            int sz = (int) ifc.size();
            int n = 0;
            while (n < sz) {
                if (ifc.read(Util.copyBuffer) < 0) {
                    break;
                }
                Util.copyBuffer.flip();
                n += ofc.write(Util.copyBuffer);
                Util.copyBuffer.compact();
            }
        } finally {
            try {
                if (ifc != null) {
                    ifc.close();
                } else if (fis != null) {
                    fis.close();
                }
            } catch (IOException exc) {
            }
            try {
                if (ofc != null) {
                    ofc.close();
                } else if (fos != null) {
                    fos.close();
                }
            } catch (IOException exc) {
            }
        }
    }
