    @Override
    protected Object transform(Row inputs) throws FunctionException {
        StringBuffer buffer = new StringBuffer();
        for (IColumn c : inputs.getColumns()) {
            buffer.append(c.getValueAsString() + "|");
        }
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(buffer.toString().getBytes());
            byte[] hash = digest.digest();
            return getHex(hash);
        } catch (Exception e) {
            throw new FunctionException(e);
        }
    }
