    private static boolean copyFile(File in, File out) {
        boolean ok = true;
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(in);
            os = new FileOutputStream(out);
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = is.read(buffer)) != -1; ) os.write(buffer, 0, len);
        } catch (IOException e) {
            System.err.println(e);
            ok = false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
        return ok;
    }
