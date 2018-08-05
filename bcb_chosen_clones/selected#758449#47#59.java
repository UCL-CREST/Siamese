    private void moveFile(File orig, File target) throws IOException {
        byte buffer[] = new byte[1000];
        int bread = 0;
        FileInputStream fis = new FileInputStream(orig);
        FileOutputStream fos = new FileOutputStream(target);
        while (bread != -1) {
            bread = fis.read(buffer);
            if (bread != -1) fos.write(buffer, 0, bread);
        }
        fis.close();
        fos.close();
        orig.delete();
    }
