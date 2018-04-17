    @Test
    public void testIdentification() {
        try {
            String username = "muchu";
            String password = "123";
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            LogService logServiceMock = EasyMock.createMock(LogService.class);
            DbService dbServiceMock = EasyMock.createMock(DbService.class);
            userServ.setDbServ(dbServiceMock);
            userServ.setLogger(logServiceMock);
            logServiceMock.info(DbUserServiceImpl.class, ">>>identification " + username + "<<<");
            IFeelerUser user = new FeelerUserImpl();
            user.setUsername(username);
            user.setPassword(new String(md5.digest()));
            EasyMock.expect(dbServiceMock.queryFeelerUser(username)).andReturn(user);
            EasyMock.replay(logServiceMock, dbServiceMock);
            Assert.assertTrue(userServ.identification(username, password));
            EasyMock.verify(logServiceMock, dbServiceMock);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
