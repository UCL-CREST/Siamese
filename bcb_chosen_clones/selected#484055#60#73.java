    public void encodeFile(File source, File dest, boolean deleteSourceAfterEncrypt) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException {
        FileInputStream fis = new FileInputStream(source);
        BufferedInputStream bis = new BufferedInputStream(fis);
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        FileOutputStream fos = new FileOutputStream(dest);
        CipherOutputStream cos = new CipherOutputStream(fos, pbeCipher);
        for (int c; (c = bis.read()) != -1; ) {
            cos.write(c);
        }
        cos.close();
        if (deleteSourceAfterEncrypt == true) {
            FileManager.deleteFilesRecursive(source);
        }
    }
