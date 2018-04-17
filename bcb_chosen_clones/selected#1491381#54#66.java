    @Test
    public void testUnregisterUsers() {
        CRC32 crc32 = new CRC32();
        crc32.update("mino.togna@gmail.com".getBytes());
        long abs = Math.abs(crc32.getValue());
        String md5Hex = DigestUtils.md5Hex(String.valueOf(abs));
        String emailHash = abs + "_" + md5Hex;
        Session session = getSession();
        Set<String> set = session.unregisterUsers(new String[] { emailHash });
        log.debug(set);
        Assert.assertNotNull(set);
        Assert.assertTrue(set.size() > 0);
    }
