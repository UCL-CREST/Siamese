    public void setChecksum() {
        try {
            java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
            String fqn = this.path + this.name;
            md5.update(fqn.getBytes());
            byte[] array = md5.digest();
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < array.length; ++j) {
                int b = array[j] & BYTE_CLEANER_FF;
                if (b < BYTE_CLEANER_10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b));
            }
            this.checksum = sb.toString();
        } catch (java.security.NoSuchAlgorithmException nsae) {
            this.checksum = this.path + this.name;
        }
    }
