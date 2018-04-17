    public String getValidationKey(String transactionId, double transactionAmount) {
        try {
            java.security.MessageDigest d = java.security.MessageDigest.getInstance("MD5");
            d.reset();
            String value = this.getPostingKey() + transactionId + transactionAmount;
            d.update(value.getBytes());
            byte[] buf = d.digest();
            return Base64.encodeBytes(buf);
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
