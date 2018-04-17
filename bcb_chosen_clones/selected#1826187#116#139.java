    public void copy(File from, String to) throws SystemException {
        assert from != null;
        File dst = new File(folder, to);
        dst.getParentFile().mkdirs();
        FileChannel in = null;
        FileChannel out = null;
        try {
            if (!dst.exists()) dst.createNewFile();
            in = new FileInputStream(from).getChannel();
            out = new FileOutputStream(dst).getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            throw new SystemException(e);
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e1) {
            }
            try {
                if (out != null) out.close();
            } catch (Exception e1) {
            }
        }
    }
