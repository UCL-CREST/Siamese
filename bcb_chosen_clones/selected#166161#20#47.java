    public static void copy(File src, File dest) throws IOException {
        log.info("Copying " + src.getAbsolutePath() + " to " + dest.getAbsolutePath());
        if (!src.exists()) throw new IOException("File not found: " + src.getAbsolutePath());
        if (!src.canRead()) throw new IOException("Source not readable: " + src.getAbsolutePath());
        if (src.isDirectory()) {
            if (!dest.exists()) if (!dest.mkdirs()) throw new IOException("Could not create direcotry: " + dest.getAbsolutePath());
            String children[] = src.list();
            for (String child : children) {
                File src1 = new File(src, child);
                File dst1 = new File(dest, child);
                copy(src1, dst1);
            }
        } else {
            FileInputStream fin = null;
            FileOutputStream fout = null;
            byte[] buffer = new byte[4096];
            int bytesRead;
            fin = new FileInputStream(src);
            fout = new FileOutputStream(dest);
            while ((bytesRead = fin.read(buffer)) >= 0) fout.write(buffer, 0, bytesRead);
            if (fin != null) {
                fin.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
