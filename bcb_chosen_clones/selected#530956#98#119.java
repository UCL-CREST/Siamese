    static File saveHashResult(String baseDir, Document doc, StringWriter sw) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        File outputFile = null;
        outputFile = new File(baseDir + File.separator + AppDigest.DIGEST_FILENAME);
        sw.append("<hr>Saving hashresult to: " + outputFile.getAbsolutePath() + "<br>");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        outputFile.createNewFile();
        KeySpec keySpec = new PBEKeySpec(AppDigest.ENCRYPTION_KEY.toCharArray(), AppDigest.ENCRYPTION_SALT, AppDigest.ENCRYPTION_COUNT);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(AppDigest.ENCRYPTION_SALT, AppDigest.ENCRYPTION_COUNT);
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
        CipherOutputStream cos = new CipherOutputStream(bos, cipher);
        ObjectOutputStream oos = new ObjectOutputStream(cos);
        oos.writeObject(doc);
        oos.flush();
        oos.close();
        sw.append("Hashresult successfully saved<br>");
        return outputFile;
    }
