    public void copieFichier(String fileIn, String fileOut) {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(fileIn).getChannel();
            out = new FileOutputStream(fileOut).getChannel();
            in.transferTo(0, in.size(), out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
