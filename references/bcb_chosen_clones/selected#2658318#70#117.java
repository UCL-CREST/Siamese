    public void encodePassword(File fromFile, File toFile, char[] password) {
        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;
        SecretKey pbeKey;
        Cipher pbeCipher = null;
        byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };
        int count = 20;
        pbeParamSpec = new PBEParameterSpec(salt, count);
        pbeKeySpec = new PBEKeySpec(password);
        try {
            keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            pbeKey = keyFac.generateSecret(pbeKeySpec);
            pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(fromFile));
            out = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(toFile)), pbeCipher);
            byte[] rbuffer = new byte[2056];
            int rcount = in.read(rbuffer);
            while (rcount > 0) {
                out.write(rbuffer, 0, rcount);
                rcount = in.read(rbuffer);
            }
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
        System.out.println("Success: " + toFile.getName() + " generated.");
    }
