    public boolean validatePassword(UserType nameMatch, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.reset();
            md.update(nameMatch.getSalt().getBytes("UTF-8"));
            md.update(password.getBytes("UTF-8"));
            String encodedString = new String(Base64.encode(md.digest()));
            return encodedString.equals(nameMatch.getPassword());
        } catch (UnsupportedEncodingException ex) {
            logger.fatal("Your computer does not have UTF-8 support for Java installed.", ex);
            logger.fatal("Shutting down...");
            GlobalUITools.displayFatalExceptionMessage(null, "UTF-8 for Java not installed", ex, true);
            assert false : "This should never happen";
            return false;
        } catch (NoSuchAlgorithmException ex) {
            String errorMessage = "Could not use algorithm " + HASH_ALGORITHM;
            logger.fatal(ex.getMessage());
            logger.fatal(errorMessage);
            GlobalUITools.displayFatalExceptionMessage(null, "Could not use algorithm " + HASH_ALGORITHM, ex, true);
            assert false : "This could should never be reached";
            return false;
        }
    }
