        public boolean check(Object credentials) {
            try {
                byte[] digest = null;
                if (credentials instanceof Password || credentials instanceof String) {
                    synchronized (__TYPE) {
                        if (__md == null) __md = MessageDigest.getInstance("MD5");
                        __md.reset();
                        __md.update(credentials.toString().getBytes(StringUtil.__ISO_8859_1));
                        digest = __md.digest();
                    }
                    if (digest == null || digest.length != _digest.length) return false;
                    for (int i = 0; i < digest.length; i++) if (digest[i] != _digest[i]) return false;
                    return true;
                } else if (credentials instanceof MD5) {
                    MD5 md5 = (MD5) credentials;
                    if (_digest.length != md5._digest.length) return false;
                    for (int i = 0; i < _digest.length; i++) if (_digest[i] != md5._digest[i]) return false;
                    return true;
                } else if (credentials instanceof Credential) {
                    return ((Credential) credentials).check(this);
                } else {
                    log.warn("Can't check " + credentials.getClass() + " against MD5");
                    return false;
                }
            } catch (Exception e) {
                log.warn(LogSupport.EXCEPTION, e);
                return false;
            }
        }
