    private void copyFile(String path) {
        try {
            File srcfile = new File(srcdir, path);
            File destfile = new File(destdir, path);
            File parent = destfile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileInputStream fis = new FileInputStream(srcfile);
            FileOutputStream fos = new FileOutputStream(destfile);
            int bytes_read = 0;
            byte buffer[] = new byte[512];
            while ((bytes_read = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytes_read);
            }
            fis.close();
            fos.close();
        } catch (IOException e) {
            throw new BuildException("Error while copying file " + path);
        }
    }
