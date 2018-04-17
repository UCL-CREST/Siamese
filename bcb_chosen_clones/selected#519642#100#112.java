    @Override
    public String readFixString(final int len) {
        if (len < 1) {
            return StringUtils.EMPTY;
        }
        final StringWriter sw = new StringWriter();
        try {
            IOUtils.copy(createLimitedInputStream(len), sw, null);
        } catch (IOException e) {
            throw createRuntimeException(e);
        }
        return sw.toString();
    }
