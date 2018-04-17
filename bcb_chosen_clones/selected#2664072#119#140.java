        public static String digest(String password) {
            try {
                byte[] digest;
                synchronized (__md5Lock) {
                    if (__md == null) {
                        try {
                            __md = MessageDigest.getInstance("MD5");
                        } catch (Exception e) {
                            Log.warn(e);
                            return null;
                        }
                    }
                    __md.reset();
                    __md.update(password.getBytes(StringUtil.__ISO_8859_1));
                    digest = __md.digest();
                }
                return __TYPE + TypeUtil.toString(digest, 16);
            } catch (Exception e) {
                Log.warn(e);
                return null;
            }
        }
