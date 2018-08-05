    private boolean copyOldSetupClass(File lastVerPath, File destPath) throws java.io.FileNotFoundException, IOException {
        byte[] buf;
        File oldClass = new File(lastVerPath.getAbsolutePath() + File.separator + installClassName_ + ".class");
        if (oldClass.exists()) {
            FileOutputStream out = new FileOutputStream(destPath.getAbsolutePath() + File.separator + installClassName_ + ".class");
            FileInputStream in = new FileInputStream(oldClass);
            buf = new byte[(new Long(oldClass.length())).intValue()];
            int read = in.read(buf, 0, buf.length);
            out.write(buf, 0, read);
            out.close();
            in.close();
            return true;
        }
        return false;
    }
