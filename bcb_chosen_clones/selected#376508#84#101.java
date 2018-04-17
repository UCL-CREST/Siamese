    private void encode(File sourceDir, File destDir, String filename) {
        System.out.println("Encoding file: " + filename);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            FileInputStream in = new FileInputStream(new File(sourceDir, filename));
            FileOutputStream out = new FileOutputStream(new File(destDir, filename));
            byte[] buffer = new byte[4096];
            int count;
            while ((count = in.read(buffer)) >= 0) {
                out.write(cipher.update(buffer, 0, count));
            }
            out.write(cipher.doFinal());
            in.close();
            out.close();
        } catch (Exception e) {
            System.err.println("Exception occured: " + e);
        }
    }
