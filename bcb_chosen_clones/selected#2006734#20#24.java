    public void testStringConstructor() throws Exception {
        Exception e = getExceptionClass().getConstructor(String.class).newInstance("HI MOM");
        assertEquals("HI MOM", e.getMessage());
        assertNull(e.getCause());
    }
