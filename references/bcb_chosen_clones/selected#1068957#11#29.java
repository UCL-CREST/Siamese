    public static void copyFile(File source, File dest) throws IOException {
        if (!dest.exists()) {
            dest.createNewFile();
        }
        FileChannel from = null;
        FileChannel to = null;
        try {
            from = new FileInputStream(source).getChannel();
            to = new FileOutputStream(dest).getChannel();
            to.transferFrom(from, 0, from.size());
        } finally {
            if (from != null) {
                from.close();
            }
            if (to != null) {
                to.close();
            }
        }
    }
