    public static void copy(String source, String dest) throws java.io.IOException {
        java.io.BufferedInputStream in = null;
        java.io.BufferedOutputStream out = null;
        try {
            in = new java.io.BufferedInputStream(new java.io.FileInputStream(source), 1000);
            out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(dest), 1000);
            while (in.available() != 0) {
                out.write(in.read());
            }
        } catch (java.io.IOException e) {
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (java.io.IOException E) {
            }
        }
    }
