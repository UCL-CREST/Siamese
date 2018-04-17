    protected void innerProcess(CrawlURI curi) throws InterruptedException {
        if (!curi.isHttpTransaction()) {
            return;
        }
        if (!TextUtils.matches("^text.*$", curi.getContentType())) {
            return;
        }
        long maxsize = DEFAULT_MAX_SIZE_BYTES.longValue();
        try {
            maxsize = ((Long) getAttribute(curi, ATTR_MAX_SIZE_BYTES)).longValue();
        } catch (AttributeNotFoundException e) {
            logger.severe("Missing max-size-bytes attribute when processing " + curi.toString());
        }
        if (maxsize < curi.getContentSize() && maxsize > -1) {
            return;
        }
        String regexpr = "";
        try {
            regexpr = (String) getAttribute(curi, ATTR_STRIP_REG_EXPR);
        } catch (AttributeNotFoundException e2) {
            logger.severe("Missing strip-reg-exp when processing " + curi.toString());
            return;
        }
        ReplayCharSequence cs = null;
        try {
            cs = curi.getHttpRecorder().getReplayCharSequence();
        } catch (Exception e) {
            curi.addLocalizedError(this.getName(), e, "Failed get of replay char sequence " + curi.toString() + " " + e.getMessage());
            logger.warning("Failed get of replay char sequence " + curi.toString() + " " + e.getMessage() + " " + Thread.currentThread().getName());
            return;
        }
        MessageDigest digest = null;
        try {
            try {
                digest = MessageDigest.getInstance(SHA1);
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
                return;
            }
            digest.reset();
            String s = null;
            if (regexpr.length() == 0) {
                s = cs.toString();
            } else {
                Matcher m = TextUtils.getMatcher(regexpr, cs);
                s = m.replaceAll(" ");
                TextUtils.recycleMatcher(m);
            }
            digest.update(s.getBytes());
            byte[] newDigestValue = digest.digest();
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Recalculated content digest for " + curi.toString() + " old: " + Base32.encode((byte[]) curi.getContentDigest()) + ", new: " + Base32.encode(newDigestValue));
            }
            curi.setContentDigest(SHA1, newDigestValue);
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (IOException ioe) {
                    logger.warning(TextUtils.exceptionToString("Failed close of ReplayCharSequence.", ioe));
                }
            }
        }
    }
