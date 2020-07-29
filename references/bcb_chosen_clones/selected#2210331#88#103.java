    public String computeMD5(String string) throws ServiceException {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.reset();
            digest.update(string.getBytes(Invoker.ENCODING));
            return convertToHex(digest.digest());
        } catch (NoSuchAlgorithmException exception) {
            String message = "Could not create properly the MD5 digest";
            log.error(message, exception);
            throw new ServiceException(message, exception);
        } catch (UnsupportedEncodingException exception) {
            String message = "Could not encode properly the MD5 digest";
            log.error(message, exception);
            throw new ServiceException(message, exception);
        }
    }
