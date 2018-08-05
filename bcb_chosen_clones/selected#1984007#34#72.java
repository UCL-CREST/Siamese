    public void encryptFile(String srcFileName, String destFileName) throws Exception {
        OutputStream outputWriter = null;
        InputStream inputReader = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            byte[] buf = new byte[100];
            int bufl;
            cipher.init(Cipher.ENCRYPT_MODE, this.pubKeyObj);
            outputWriter = new FileOutputStream(destFileName);
            inputReader = new FileInputStream(srcFileName);
            while ((bufl = inputReader.read(buf)) != -1) {
                byte[] encText = null;
                byte[] newArr = null;
                if (buf.length == bufl) {
                    newArr = buf;
                } else {
                    newArr = new byte[bufl];
                    for (int i = 0; i < bufl; i++) {
                        newArr[i] = (byte) buf[i];
                    }
                }
                encText = cipher.doFinal(newArr);
                outputWriter.write(encText);
            }
            outputWriter.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (outputWriter != null) {
                    outputWriter.close();
                }
                if (inputReader != null) {
                    inputReader.close();
                }
            } catch (Exception e) {
            }
        }
    }
