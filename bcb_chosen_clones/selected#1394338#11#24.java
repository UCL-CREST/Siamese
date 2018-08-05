    public static void copy(File file, File dir, boolean overwrite) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        File out = new File(dir, file.getName());
        if (out.exists() && !overwrite) {
            throw new IOException("File: " + out + " already exists.");
        }
        FileOutputStream fos = new FileOutputStream(out, false);
        byte[] block = new byte[4096];
        int read = bis.read(block);
        while (read != -1) {
            fos.write(block, 0, read);
            read = bis.read(block);
        }
    }
