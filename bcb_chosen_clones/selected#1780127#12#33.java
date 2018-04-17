    public static void copyFile(File source, File dest) throws Exception {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            in.transferTo(0, in.size(), out);
        } catch (Exception e) {
            throw new Exception("Cannot copy file " + source.getAbsolutePath() + " to " + dest.getAbsolutePath(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                throw new Exception("Cannot close streams.", e);
            }
        }
    }
