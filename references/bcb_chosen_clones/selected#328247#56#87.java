    public void encode(File fromFile, File toFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            in = new BufferedInputStream(new FileInputStream(fromFile));
            out = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(toFile)), this.cipher);
            byte[] rbuffer = new byte[2056];
            int rcount = in.read(rbuffer);
            while (rcount > 0) {
                out.write(rbuffer, 0, rcount);
                rcount = in.read(rbuffer);
            }
        } catch (InvalidKeyException ke) {
            ke.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                }
            }
        }
    }
