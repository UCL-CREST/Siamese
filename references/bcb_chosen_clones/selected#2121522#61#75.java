    public void createMd5Hash() {
        try {
            String vcardObject = new ContactToVcard(TimeZone.getTimeZone("UTC"), "UTF-8").convert(this);
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(vcardObject.getBytes());
            this.md5Hash = new BigInteger(m.digest()).toString();
            if (log.isTraceEnabled()) {
                log.trace("Hash is:" + this.md5Hash);
            }
        } catch (ConverterException ex) {
            log.error("Error creating hash:" + ex.getMessage());
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            log.error("Error creating hash:" + noSuchAlgorithmException.getMessage());
        }
    }
