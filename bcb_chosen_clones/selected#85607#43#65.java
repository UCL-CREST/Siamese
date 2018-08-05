    private static void doCopyRecursively(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            dest.mkdir();
            File[] entries = src.listFiles();
            if (entries == null) {
                throw new IOException("Could not list files in directory: " + src);
            }
            for (int i = 0; i < entries.length; i++) {
                File file = entries[i];
                doCopyRecursively(file, new File(dest, file.getName()));
            }
        } else if (src.isFile()) {
            try {
                dest.createNewFile();
            } catch (IOException ex) {
                IOException ioex = new IOException("Failed to create file: " + dest);
                ioex.initCause(ex);
                throw ioex;
            }
            FileUtil.copy(src, dest);
        } else {
        }
    }
