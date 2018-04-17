    public boolean copy(File src, File dest, byte[] b) {
        if (src.isDirectory()) {
            String[] ss = src.list();
            for (int i = 0; i < ss.length; i++) if (!copy(new File(src, ss[i]), new File(dest, ss[i]), b)) return false;
            return true;
        }
        delete(dest);
        dest.getParentFile().mkdirs();
        try {
            FileInputStream fis = new FileInputStream(src);
            try {
                FileOutputStream fos = new FileOutputStream(dest);
                try {
                    int read;
                    while ((read = fis.read(b)) != -1) fos.write(b, 0, read);
                } finally {
                    try {
                        fos.close();
                    } catch (IOException ignore) {
                    }
                    register(dest);
                }
            } finally {
                fis.close();
            }
            if (log.isDebugEnabled()) log.debug("Success: M-COPY " + src + " -> " + dest);
            return true;
        } catch (IOException e) {
            log.error("Failed: M-COPY " + src + " -> " + dest, e);
            return false;
        }
    }
