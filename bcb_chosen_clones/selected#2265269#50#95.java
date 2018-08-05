    public static byte[] clearPassToUserPassword(String clearpass, HashAlg alg, byte[] salt) {
        if (alg == null) {
            throw new IllegalArgumentException("Invalid hash argorithm.");
        }
        try {
            MessageDigest digester = null;
            StringBuilder resultInText = new StringBuilder();
            switch(alg) {
                case MD5:
                    resultInText.append("{MD5}");
                    digester = MessageDigest.getInstance("MD5");
                    break;
                case SMD5:
                    resultInText.append("{SMD5}");
                    digester = MessageDigest.getInstance("MD5");
                    break;
                case SHA:
                    resultInText.append("{SHA}");
                    digester = MessageDigest.getInstance("SHA");
                    break;
                case SSHA:
                    resultInText.append("{SSHA}");
                    digester = MessageDigest.getInstance("SHA");
                    break;
                default:
                    break;
            }
            digester.reset();
            digester.update(clearpass.getBytes(DEFAULT_ENCODING));
            byte[] hash = null;
            if (salt != null && (alg == HashAlg.SMD5 || alg == HashAlg.SSHA)) {
                digester.update(salt);
                hash = ArrayUtils.addAll(digester.digest(), salt);
            } else {
                hash = digester.digest();
            }
            resultInText.append(new String(Base64.encodeBase64(hash), DEFAULT_ENCODING));
            return resultInText.toString().getBytes(DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException uee) {
            log.warn("Error occurred while hashing password ", uee);
            return new byte[0];
        } catch (java.security.NoSuchAlgorithmException nse) {
            log.warn("Error occurred while hashing password ", nse);
            return new byte[0];
        }
    }
