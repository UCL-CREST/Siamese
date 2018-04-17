    protected void copyFile(File from, File to) throws IOException {
        new File(intSfPath).delete();
        if (to.exists() && (from.length() == to.length()) && (from.lastModified() == to.lastModified())) return;
        if (to.exists()) to.delete();
        to.createNewFile();
        FileChannel inFC = null;
        FileChannel outFC = null;
        try {
            inFC = new FileInputStream(from).getChannel();
            outFC = new FileOutputStream(to).getChannel();
            long cnt = outFC.transferFrom(inFC, 0, inFC.size());
            if (cnt < inFC.size()) throw new IOException("File copy failed");
        } finally {
            if (inFC != null) {
                try {
                    inFC.close();
                } catch (IOException ex) {
                }
            }
            if (outFC != null) {
                try {
                    outFC.close();
                } catch (IOException ex) {
                }
            }
            to.setLastModified(from.lastModified());
        }
    }
