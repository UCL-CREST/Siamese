    public static boolean copy(File source, File target) {
        try {
            if (!source.exists()) return false;
            target.getParentFile().mkdirs();
            InputStream input = new FileInputStream(source);
            OutputStream output = new FileOutputStream(target);
            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) > 0) output.write(buf, 0, len);
            input.close();
            output.close();
            return true;
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
    }
