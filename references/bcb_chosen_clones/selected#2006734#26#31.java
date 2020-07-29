    public void testStringAndCauseConstructor() throws Exception {
        final Throwable EXPECTED = new Throwable();
        Exception e = getExceptionClass().getConstructor(String.class, Throwable.class).newInstance("HI MOM", EXPECTED);
        assertEquals("HI MOM", e.getMessage());
        assertEquals(EXPECTED, e.getCause());
    }
