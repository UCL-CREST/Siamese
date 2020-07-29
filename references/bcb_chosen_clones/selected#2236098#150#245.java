    byte[] encrypt(byte[] b) {
        try {
            KeyGenerator key_gen = KeyGenerator.getInstance("AES", "BC");
            key_gen.init(128, sec_rand);
            Key aes_key = key_gen.generateKey();
            log.debug("Set up cipher AES/CBC/PKCS7Padding");
            Cipher output_cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            output_cipher.init(Cipher.ENCRYPT_MODE, aes_key, sec_rand);
            byte[] aes_iv = output_cipher.getIV();
            byte[] aes_key_enc = aes_key.getEncoded();
            Mac mac = Mac.getInstance("HMACSHA1", "BC");
            log.debug("Generate key and then set up HMAC (HMACSHA1).");
            byte[] mac_key_bytes = new byte[20];
            sec_rand.nextBytes(mac_key_bytes);
            Key mac_key = new SecretKeySpec(mac_key_bytes, "HMACSHA1");
            mac.init(mac_key);
            log.debug("Set up RSA with OAEPPadding (see PKCS1 V2)");
            Cipher rsa_eng = Cipher.getInstance("RSA/None/OAEPPadding", "BC");
            ByteArrayOutputStream msg_str = new ByteArrayOutputStream();
            MacOutputStream mac_str = new MacOutputStream(msg_str, mac);
            DataOutputStream data_str = new DataOutputStream(mac_str);
            log.debug("writing header");
            data_str.writeShort(FILE_HEADER);
            log.debug("Writing HashCodes of used Certificates");
            data_str.writeShort(CERT_BLOCK);
            data_str.writeInt(certs.length);
            PublicKey pub_key[] = new PublicKey[certs.length];
            for (int i = 0; i < certs.length; i++) {
                pub_key[i] = certs[i].getPublicKey();
                data_str.writeInt(certs[i].hashCode());
            }
            log.debug("Aes key enc with RSA");
            data_str.writeShort(KEY_BLOCK);
            for (int i = 0; i < pub_key.length; i++) {
                rsa_eng.init(Cipher.ENCRYPT_MODE, pub_key[i], sec_rand);
                byte[] tmp = rsa_eng.doFinal(aes_key_enc);
                data_str.writeInt(tmp.length);
                data_str.write(tmp);
                blank(tmp);
            }
            log.debug("Aes IV enc with RSA (See note in source code)");
            data_str.writeShort(IV_BLOCK);
            for (int i = 0; i < pub_key.length; i++) {
                rsa_eng.init(Cipher.ENCRYPT_MODE, pub_key[i], sec_rand);
                byte[] tmp = rsa_eng.doFinal(aes_iv);
                data_str.writeInt(tmp.length);
                data_str.write(tmp);
                blank(tmp);
            }
            log.debug("HMACSHA1 key enc with RSA");
            data_str.writeShort(HMAC_KEY_BLOCK);
            byte[] tmp = output_cipher.doFinal(mac_key.getEncoded());
            data_str.writeInt(tmp.length);
            data_str.write(tmp);
            blank(tmp);
            int l = 0;
            byte[] buf = new byte[8192];
            byte[] out = null;
            ByteArrayInputStream in_str = new ByteArrayInputStream(b);
            while ((l = in_str.read(buf)) > -1) {
                out = output_cipher.update(buf, 0, l);
                if (out != null) {
                    data_str.writeShort(DATA_BLOCK);
                    data_str.writeInt(out.length);
                    data_str.write(out);
                    log.debug("Encrypted " + out.length + " bytes output");
                }
            }
            out = output_cipher.doFinal();
            data_str.writeShort(FINAL_DATA_BLOCK);
            data_str.writeInt(out.length);
            data_str.write(out);
            log.debug("Final Encrypted " + out.length + " bytes output");
            blank(buf);
            buf = null;
            log.debug("Write out HMAC code block.");
            data_str.writeShort(HMAC_BLOCK);
            data_str.flush();
            tmp = mac.doFinal();
            data_str.writeInt(tmp.length);
            data_str.write(tmp);
            blank(tmp);
            data_str.flush();
            data_str.close();
            log.debug("Dispose of key material as best we can in java..");
            blank(aes_key_enc);
            aes_key_enc = null;
            aes_key = null;
            tmp = null;
            log.debug("The End..");
            return msg_str.toByteArray();
        } catch (Exception e) {
            log.fatal(e.toString());
            return null;
        }
    }
