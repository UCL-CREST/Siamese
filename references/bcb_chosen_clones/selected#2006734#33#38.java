    public void testCauseConstructor() throws Exception {
        final Throwable EXPECTED = new Throwable("MESSAGE FROM CAUSE");
        Exception e = getExceptionClass().getConstructor(Throwable.class).newInstance(EXPECTED);
        assertEquals("java.lang.Throwable: MESSAGE FROM CAUSE", e.getMessage());
        assertEquals(EXPECTED, e.getCause());
    }
