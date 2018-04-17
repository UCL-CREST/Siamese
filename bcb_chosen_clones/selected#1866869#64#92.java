    public static void WritePrivateKey(PrivateKey keys, String pwd) {
        try {
            byte key[] = pwd.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            FileOutputStream fos = new FileOutputStream("privateKey.data");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            CipherOutputStream cos = new CipherOutputStream(bos, desCipher);
            ObjectOutputStream oos = new ObjectOutputStream(cos);
            oos.writeObject(keys);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
