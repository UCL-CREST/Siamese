    public static KUID createRandomID() {
        MessageDigestInput randomNumbers = new MessageDigestInput() {

            public void update(MessageDigest md) {
                byte[] random = new byte[LENGTH * 2];
                GENERATOR.nextBytes(random);
                md.update(random);
            }
        };
        MessageDigestInput properties = new MessageDigestInput() {

            public void update(MessageDigest md) {
                Properties props = System.getProperties();
                try {
                    for (Entry entry : props.entrySet()) {
                        String key = (String) entry.getKey();
                        String value = (String) entry.getValue();
                        md.update(key.getBytes("UTF-8"));
                        md.update(value.getBytes("UTF-8"));
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        MessageDigestInput millis = new MessageDigestInput() {

            public void update(MessageDigest md) {
                long millis = System.currentTimeMillis();
                md.update((byte) ((millis >> 56L) & 0xFFL));
                md.update((byte) ((millis >> 48L) & 0xFFL));
                md.update((byte) ((millis >> 40L) & 0xFFL));
                md.update((byte) ((millis >> 32L) & 0xFFL));
                md.update((byte) ((millis >> 24L) & 0xFFL));
                md.update((byte) ((millis >> 16L) & 0xFFL));
                md.update((byte) ((millis >> 8L) & 0xFFL));
                md.update((byte) ((millis) & 0xFFL));
            }
        };
        MessageDigestInput nanos = new MessageDigestInput() {

            public void update(MessageDigest md) {
                long nanos = System.nanoTime();
                md.update((byte) ((nanos >> 56L) & 0xFFL));
                md.update((byte) ((nanos >> 48L) & 0xFFL));
                md.update((byte) ((nanos >> 40L) & 0xFFL));
                md.update((byte) ((nanos >> 32L) & 0xFFL));
                md.update((byte) ((nanos >> 24L) & 0xFFL));
                md.update((byte) ((nanos >> 16L) & 0xFFL));
                md.update((byte) ((nanos >> 8L) & 0xFFL));
                md.update((byte) ((nanos) & 0xFFL));
            }
        };
        MessageDigestInput[] input = { properties, randomNumbers, millis, nanos };
        Arrays.sort(input);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            for (MessageDigestInput mdi : input) {
                mdi.update(md);
                int hashCode = System.identityHashCode(mdi);
                md.update((byte) ((hashCode >> 24) & 0xFF));
                md.update((byte) ((hashCode >> 16) & 0xFF));
                md.update((byte) ((hashCode >> 8) & 0xFF));
                md.update((byte) ((hashCode) & 0xFF));
                md.update((byte) ((mdi.rnd >> 24) & 0xFF));
                md.update((byte) ((mdi.rnd >> 16) & 0xFF));
                md.update((byte) ((mdi.rnd >> 8) & 0xFF));
                md.update((byte) ((mdi.rnd) & 0xFF));
            }
            return new KUID(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
