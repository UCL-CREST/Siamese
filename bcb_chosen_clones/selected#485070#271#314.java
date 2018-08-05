    public static void copyFiles(File src, File dest) throws IOException {
        if (!src.exists()) {
            throw new IOException("Can not find source: " + src.getAbsolutePath());
        } else if (!src.canRead()) {
            throw new IOException("Cannot read: " + src.getAbsolutePath() + ". Check file permissions.");
        }
        if (src.isDirectory()) {
            if (!dest.exists()) {
                if (!dest.mkdirs()) {
                    throw new IOException("Could not create direcotry: " + dest.getAbsolutePath());
                }
            }
            String list[] = src.list();
            for (int i = 0; i < list.length; i++) {
                File dest1 = new File(dest, list[i]);
                File src1 = new File(src, list[i]);
                copyFiles(src1, dest1);
            }
        } else {
            FileInputStream fin = null;
            FileOutputStream fout = null;
            byte[] buffer = new byte[4096];
            int bytesRead;
            try {
                fin = new FileInputStream(src);
                fout = new FileOutputStream(dest);
                while ((bytesRead = fin.read(buffer)) >= 0) {
                    fout.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                IOException wrapper = new IOException("Unable to copy file: " + src.getAbsolutePath() + "to" + dest.getAbsolutePath());
                wrapper.initCause(e);
                wrapper.setStackTrace(e.getStackTrace());
                throw wrapper;
            } finally {
                if (fin != null) {
                    fin.close();
                }
                if (fout != null) {
                    fin.close();
                }
            }
        }
    }
