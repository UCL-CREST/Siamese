    @Test
    public void testRegisterOwnJceProvider() throws Exception {
        MyTestProvider provider = new MyTestProvider();
        assertTrue(-1 != Security.addProvider(provider));
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1", MyTestProvider.NAME);
        assertEquals(MyTestProvider.NAME, messageDigest.getProvider().getName());
        messageDigest.update("hello world".getBytes());
        byte[] result = messageDigest.digest();
        Assert.assertArrayEquals("hello world".getBytes(), result);
        Security.removeProvider(MyTestProvider.NAME);
    }
