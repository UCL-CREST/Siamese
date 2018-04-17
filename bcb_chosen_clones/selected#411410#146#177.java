    public void test_digest() throws UnsupportedEncodingException {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
            assertNotNull(sha);
        } catch (NoSuchAlgorithmException e) {
            fail("getInstance did not find algorithm");
        }
        sha.update(MESSAGE.getBytes("UTF-8"));
        byte[] digest = sha.digest();
        assertTrue("bug in SHA", MessageDigest.isEqual(digest, MESSAGE_DIGEST));
        sha.reset();
        for (int i = 0; i < 63; i++) {
            sha.update((byte) 'a');
        }
        digest = sha.digest();
        assertTrue("bug in SHA", MessageDigest.isEqual(digest, MESSAGE_DIGEST_63_As));
        sha.reset();
        for (int i = 0; i < 64; i++) {
            sha.update((byte) 'a');
        }
        digest = sha.digest();
        assertTrue("bug in SHA", MessageDigest.isEqual(digest, MESSAGE_DIGEST_64_As));
        sha.reset();
        for (int i = 0; i < 65; i++) {
            sha.update((byte) 'a');
        }
        digest = sha.digest();
        assertTrue("bug in SHA", MessageDigest.isEqual(digest, MESSAGE_DIGEST_65_As));
        testSerializationSHA_DATA_1(sha);
        testSerializationSHA_DATA_2(sha);
    }
