    public static boolean zipFiles(File output, File... input) {
        if (output == null || input == null || input.length == 0) {
            return false;
        }
        if (!deleteFile(output)) {
            return false;
        }
        byte[] buf = new byte[1024];
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(output));
            for (int i = 0; i < input.length; i++) {
                FileInputStream in = null;
                try {
                    in = new FileInputStream(input[i]);
                    out.putNextEntry(new ZipEntry(input[i].getName()));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                } finally {
                    FileUtil.close(in);
                }
            }
        } catch (IOException ex) {
            ShowDownLog.getInstance().logError(ex.getLocalizedMessage(), ex);
            return false;
        } finally {
            FileUtil.close(out);
        }
        return true;
    }
