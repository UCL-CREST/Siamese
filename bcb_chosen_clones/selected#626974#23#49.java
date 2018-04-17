    @Test
    public void testRegister() {
        try {
            String username = "muchu";
            String password = "123";
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            String passwordMd5 = new String(md5.digest());
            LogService logServiceMock = EasyMock.createMock(LogService.class);
            DbService dbServiceMock = EasyMock.createMock(DbService.class);
            userServ.setDbServ(dbServiceMock);
            userServ.setLogger(logServiceMock);
            IFeelerUser user = new FeelerUserImpl();
            user.setUsername(username);
            user.setPassword(passwordMd5);
            logServiceMock.info(DbUserServiceImpl.class, ">>>rigister " + username + "<<<");
            EasyMock.expect(dbServiceMock.queryFeelerUser(username)).andReturn(null);
            dbServiceMock.addFeelerUser(username, passwordMd5);
            logServiceMock.info(DbUserServiceImpl.class, ">>>identification " + username + "<<<");
            EasyMock.expect(dbServiceMock.queryFeelerUser(username)).andReturn(user);
            EasyMock.replay(dbServiceMock, logServiceMock);
            Assert.assertTrue(userServ.register(username, password));
            EasyMock.verify(dbServiceMock, logServiceMock);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
