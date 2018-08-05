            public Object run() {
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA");
                    digest.update(buf.toString().getBytes());
                    byte[] data = digest.digest();
                    serialNum = new BASE64Encoder().encode(data);
                    return serialNum;
                } catch (NoSuchAlgorithmException exp) {
                    BootSecurityManager.securityLogger.log(Level.SEVERE, exp.getMessage(), exp);
                    return buf.toString();
                }
            }
