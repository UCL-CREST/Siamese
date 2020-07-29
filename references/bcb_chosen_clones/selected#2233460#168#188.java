    public void setPassword(UserType user, String clearPassword) {
        try {
            Random r = new Random();
            String newSalt = Long.toString(Math.abs(r.nextLong()));
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.reset();
            md.update(newSalt.getBytes("UTF-8"));
            md.update(clearPassword.getBytes("UTF-8"));
            String encodedString = new String(Base64.encode(md.digest()));
            user.setPassword(encodedString);
            user.setSalt(newSalt);
            this.markModified(user);
        } catch (UnsupportedEncodingException ex) {
            logger.fatal("Your computer does not have UTF-8 support for Java installed.", ex);
            GlobalUITools.displayFatalExceptionMessage(null, "UTF-8 for Java not installed", ex, true);
        } catch (NoSuchAlgorithmException ex) {
            String errorMessage = "Could not use algorithm " + HASH_ALGORITHM;
            logger.fatal(errorMessage, ex);
            GlobalUITools.displayFatalExceptionMessage(null, errorMessage, ex, true);
        }
    }
