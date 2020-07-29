    protected String getHashCode(String value) {
        if (log.isDebugEnabled()) log.debug("getHashCode(...) -> begin");
        String retVal = null;
        try {
            MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
            mdAlgorithm.update(value.getBytes());
            byte[] digest = mdAlgorithm.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                sb.append(this.toHexString(digest[i]));
            }
            retVal = sb.toString();
            if (log.isDebugEnabled()) log.debug("getHashCode(...) -> hashcode = " + retVal);
        } catch (Exception e) {
            log.error("getHashCode(...) -> error occured generating hashcode ", e);
        }
        if (log.isDebugEnabled()) log.debug("getHashCode(...) -> end");
        return retVal;
    }
