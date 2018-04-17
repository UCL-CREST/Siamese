    private String doMessageDigestAndBase64Encoding(String sequence) throws SeguidException {
        if (sequence == null) {
            throw new NullPointerException("You must give a non null sequence");
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            sequence = sequence.trim().toUpperCase();
            messageDigest.update(sequence.getBytes());
            byte[] digest = messageDigest.digest();
            String seguid = Base64.encodeBytes(digest);
            seguid = seguid.replace("=", "");
            if (log.isTraceEnabled()) {
                log.trace("SEGUID " + seguid);
            }
            return seguid;
        } catch (NoSuchAlgorithmException e) {
            throw new SeguidException("Exception thrown when calculating Seguid for " + sequence, e.getCause());
        }
    }
