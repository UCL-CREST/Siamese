    public String getSHA256Checksum(String source) {
        String checksum = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(source.getBytes());
            byte[] byteData = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            System.out.println("Hex format : " + sb.toString());
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            checksum = hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CMessageDigestFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checksum;
    }
