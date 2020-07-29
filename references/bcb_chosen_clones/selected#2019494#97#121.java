    void copyFileOnPeer(String path, RServerInfo peerserver, boolean allowoverwrite) throws IOException {
        RFile file = new RFile(path);
        OutputStream out = null;
        FileInputStream in = null;
        try {
            in = fileManager.openFileRead(path);
            out = localClient.openWrite(file, false, WriteMode.TRANSACTED, 1, peerserver, !allowoverwrite);
            IOUtils.copyLarge(in, out);
            out.close();
            out = null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable t) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Throwable t) {
                }
            }
        }
    }
