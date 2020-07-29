    public void encrypt(OutputStream out) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, paramSpec);
            out = new CipherOutputStream(out, cipher);
            StringBuffer u = new StringBuffer(1024);
            u.append("Userid:" + userid + " " + password);
            for (int i = u.length(); i < 128; i++) u.append('\0');
            out.write(u.toString().getBytes());
            out.close();
        } catch (java.io.IOException e) {
            System.out.println("Encrypt i/o exception");
        } catch (InvalidKeyException e1) {
            e1.printStackTrace();
        } catch (InvalidAlgorithmParameterException e1) {
            e1.printStackTrace();
        }
    }
