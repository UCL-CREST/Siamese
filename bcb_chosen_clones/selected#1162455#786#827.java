    public static void fastBackup(File file) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream fin = null;
        FileOutputStream fout = null;
        try {
            in = (fin = new FileInputStream(file)).getChannel();
            out = (fout = new FileOutputStream(file.getAbsolutePath() + ".bak")).getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            Logging.getErrorLog().reportError("Fast backup failure (" + file.getAbsolutePath() + "): " + e.getMessage());
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    Logging.getErrorLog().reportException("Failed to close file input stream", e);
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    Logging.getErrorLog().reportException("Failed to close file output stream", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Logging.getErrorLog().reportException("Failed to close file channel", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Logging.getErrorLog().reportException("Failed to close file channel", e);
                }
            }
        }
    }
