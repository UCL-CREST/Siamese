    public static LicenseKey parseKey(String key) throws InvalidLicenseKeyException {
        final String f_key = key.trim();
        StringTokenizer st = new StringTokenizer(f_key, FIELD_SEPERATOR);
        int tc = st.countTokens();
        int tc_name = tc - 9;
        try {
            final String product = st.nextToken();
            final String type = st.nextToken();
            final String loadStr = st.nextToken();
            final int load = Integer.parseInt(loadStr);
            final String lowMajorVersionStr = st.nextToken();
            final int lowMajorVersion = Integer.parseInt(lowMajorVersionStr);
            final String lowMinorVersionStr = st.nextToken();
            final double lowMinorVersion = Double.parseDouble("0." + lowMinorVersionStr);
            final String highMajorVersionStr = st.nextToken();
            final int highMajorVersion = Integer.parseInt(highMajorVersionStr);
            final String highMinorVersionStr = st.nextToken();
            final double highMinorVersion = Double.parseDouble("0." + highMinorVersionStr);
            String regName = "";
            for (int i = 0; i < tc_name; i++) regName += (i == 0 ? st.nextToken() : FIELD_SEPERATOR + st.nextToken());
            final String randomHexStr = st.nextToken();
            final String md5Str = st.nextToken();
            String subKey = f_key.substring(0, f_key.indexOf(md5Str) - 1);
            byte[] md5;
            MessageDigest md = null;
            md = MessageDigest.getInstance("MD5");
            md.update(subKey.getBytes());
            md.update(FIELD_SEPERATOR.getBytes());
            md.update(zuonicsPassword.getBytes());
            md5 = md.digest();
            String testKey = subKey + FIELD_SEPERATOR;
            for (int i = 0; i < md5.length; i++) testKey += Integer.toHexString(md5[i]).toUpperCase();
            if (!testKey.equals(f_key)) throw new InvalidLicenseKeyException("doesn't hash");
            final String f_regName = regName;
            return new LicenseKey() {

                public String getProduct() {
                    return product;
                }

                public String getType() {
                    return type;
                }

                public int getLoad() {
                    return load;
                }

                public String getRegName() {
                    return f_regName;
                }

                public double getlowVersion() {
                    return lowMajorVersion + lowMinorVersion;
                }

                public double getHighVersion() {
                    return highMajorVersion + highMinorVersion;
                }

                public String getRandomHexStr() {
                    return randomHexStr;
                }

                public String getMD5HexStr() {
                    return md5Str;
                }

                public String toString() {
                    return f_key;
                }

                public boolean equals(Object obj) {
                    if (obj.toString().equals(toString())) return true;
                    return false;
                }
            };
        } catch (Exception e) {
            throw new InvalidLicenseKeyException(e.getMessage());
        }
    }
