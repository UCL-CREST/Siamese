    @Test
    public void testRegisterUsers() {
        Session session = getSession();
        Account[] accounts = new Account[3];
        CRC32 crc32 = new CRC32();
        crc32.update("mino.togna@gmail.com".getBytes());
        long abs = Math.abs(crc32.getValue());
        String md5Hex = DigestUtils.md5Hex(String.valueOf(abs));
        String emailHash = abs + "_" + md5Hex;
        Account account = new Account(emailHash);
        account.setUser(752141196L);
        accounts[0] = account;
        log.debug("adding+ " + account.getUser() + ", " + account.getEmailHash());
        crc32 = new CRC32();
        crc32.update("gino75@gmail.com".getBytes());
        abs = Math.abs(crc32.getValue());
        md5Hex = DigestUtils.md5Hex(String.valueOf(abs));
        emailHash = abs + "_" + md5Hex;
        account = new Account(emailHash);
        account.setUser(733192756L);
        accounts[1] = account;
        log.debug("adding+ " + account.getUser() + ", " + account.getEmailHash());
        crc32 = new CRC32();
        crc32.update("geebay@belasius.com".getBytes());
        abs = Math.abs(crc32.getValue());
        md5Hex = DigestUtils.md5Hex(String.valueOf(abs));
        emailHash = abs + "_" + md5Hex;
        account = new Account(emailHash);
        account.setUser(591307764L);
        accounts[2] = account;
        log.debug("adding+ " + account.getUser() + ", " + account.getEmailHash());
        Set<String> emails = session.registerUsers(accounts);
        log.debug(emails);
        Assert.assertNotNull(emails);
        Assert.assertTrue(emails.size() > 0);
    }
