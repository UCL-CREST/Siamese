            protected final String H(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(data.getBytes("UTF8"));
                byte[] bytes = digest.digest();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < bytes.length; i++) {
                    int aByte = bytes[i];
                    if (aByte < 0) aByte += 256;
                    if (aByte < 16) sb.append('0');
                    sb.append(Integer.toHexString(aByte));
                }
                return sb.toString();
            }
