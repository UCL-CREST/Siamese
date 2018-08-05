    char[] DigestCalcResponse(char[] HA1, String serverNonce, String nonceCount, String clientNonce, String qop, String method, String digestUri, boolean clientResponseFlag) throws SaslException {
        byte[] HA2;
        byte[] respHash;
        char[] HA2Hex;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (clientResponseFlag) md.update(method.getBytes("UTF-8"));
            md.update(":".getBytes("UTF-8"));
            md.update(digestUri.getBytes("UTF-8"));
            if ("auth-int".equals(qop)) {
                md.update(":".getBytes("UTF-8"));
                md.update("00000000000000000000000000000000".getBytes("UTF-8"));
            }
            HA2 = md.digest();
            HA2Hex = convertToHex(HA2);
            md.update(new String(HA1).getBytes("UTF-8"));
            md.update(":".getBytes("UTF-8"));
            md.update(serverNonce.getBytes("UTF-8"));
            md.update(":".getBytes("UTF-8"));
            if (qop.length() > 0) {
                md.update(nonceCount.getBytes("UTF-8"));
                md.update(":".getBytes("UTF-8"));
                md.update(clientNonce.getBytes("UTF-8"));
                md.update(":".getBytes("UTF-8"));
                md.update(qop.getBytes("UTF-8"));
                md.update(":".getBytes("UTF-8"));
            }
            md.update(new String(HA2Hex).getBytes("UTF-8"));
            respHash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new SaslException("No provider found for MD5 hash", e);
        } catch (UnsupportedEncodingException e) {
            throw new SaslException("UTF-8 encoding not supported by platform.", e);
        }
        return convertToHex(respHash);
    }
