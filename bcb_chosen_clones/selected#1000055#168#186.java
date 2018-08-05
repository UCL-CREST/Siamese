    public static File writeInternalFile(Context cx, URL url, String dir, String filename) {
        FileOutputStream fos = null;
        File fi = null;
        try {
            fi = newInternalFile(cx, dir, filename);
            fos = FileUtils.openOutputStream(fi);
            int length = IOUtils.copy(url.openStream(), fos);
            log(length + " bytes copyed.");
        } catch (IOException e) {
            AIOUtils.log("", e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                AIOUtils.log("", e);
            }
        }
        return fi;
    }
