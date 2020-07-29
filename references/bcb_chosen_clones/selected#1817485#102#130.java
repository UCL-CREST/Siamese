    public boolean copyDir(File oSource, File oDestination) {
        try {
            if (oSource.exists()) {
                if (oSource.isDirectory()) {
                    if (!oDestination.exists()) {
                        oDestination.mkdir();
                    }
                    String[] children = oSource.list();
                    for (int i = 0; i < children.length; i++) {
                        copyDir(new File(oSource, children[i]), new File(oDestination, children[i]));
                    }
                } else {
                    InputStream in = new FileInputStream(oSource);
                    OutputStream out = new FileOutputStream(oDestination);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                }
                return true;
            }
        } catch (IOException ex) {
            throw new FilesystemException(ex.getMessage() + " " + oSource.getName(), ex);
        }
        return false;
    }
