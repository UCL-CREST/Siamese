    @SmallTest
    public void testSha1() throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        int numTests = mTestData.length;
        for (int i = 0; i < numTests; i++) {
            digest.update(mTestData[i].input.getBytes());
            byte[] hash = digest.digest();
            String encodedHash = encodeHex(hash);
            assertEquals(encodedHash, mTestData[i].result);
        }
    }
