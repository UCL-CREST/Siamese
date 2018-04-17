    private boolean authenticateWithServer(String user, String password) {
        Object o;
        String response;
        byte[] dataKey;
        try {
            o = objectIn.readObject();
            if (o instanceof String) {
                response = (String) o;
                Debug.netMsg("Connected to JFritz Server: " + response);
                if (!response.equals("JFRITZ SERVER 1.1")) {
                    Debug.netMsg("Unkown Server version, newer JFritz protocoll version?");
                    Debug.netMsg("Canceling login attempt!");
                }
                objectOut.writeObject(user);
                objectOut.flush();
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());
                DESKeySpec desKeySpec = new DESKeySpec(md.digest());
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
                Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                desCipher.init(Cipher.DECRYPT_MODE, secretKey);
                SealedObject sealedObject = (SealedObject) objectIn.readObject();
                o = sealedObject.getObject(desCipher);
                if (o instanceof byte[]) {
                    dataKey = (byte[]) o;
                    desKeySpec = new DESKeySpec(dataKey);
                    secretKey = keyFactory.generateSecret(desKeySpec);
                    inCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                    outCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                    inCipher.init(Cipher.DECRYPT_MODE, secretKey);
                    outCipher.init(Cipher.ENCRYPT_MODE, secretKey);
                    SealedObject sealed_ok = new SealedObject("OK", outCipher);
                    objectOut.writeObject(sealed_ok);
                    SealedObject sealed_response = (SealedObject) objectIn.readObject();
                    o = sealed_response.getObject(inCipher);
                    if (o instanceof String) {
                        if (o.equals("OK")) {
                            return true;
                        } else {
                            Debug.netMsg("Server sent wrong string as response to authentication challenge!");
                        }
                    } else {
                        Debug.netMsg("Server sent wrong object as response to authentication challenge!");
                    }
                } else {
                    Debug.netMsg("Server sent wrong type for data key!");
                }
            }
        } catch (ClassNotFoundException e) {
            Debug.error("Server authentication response invalid!");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            Debug.netMsg("MD5 Algorithm not present in this JVM!");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            Debug.netMsg("Error generating cipher, problems with key spec?");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            Debug.netMsg("Error genertating cipher, problems with key?");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            Debug.netMsg("Error generating cipher, problems with padding?");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (EOFException e) {
            Debug.error("Server closed Stream unexpectedly!");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            Debug.error("Read timeout while authenticating with server!");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Debug.error("Error reading response during authentication!");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            Debug.error("Illegal block size exception!");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (BadPaddingException e) {
            Debug.error("Bad padding exception!");
            Debug.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }
