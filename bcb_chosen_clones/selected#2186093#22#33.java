    public static void copyFile(File source, File dest) {
        try {
            FileChannel in = new FileInputStream(source).getChannel();
            if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();
            FileChannel out = new FileOutputStream(dest).getChannel();
            in.transferTo(0, in.size(), out);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
