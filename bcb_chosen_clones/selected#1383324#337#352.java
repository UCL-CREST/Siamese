    public static String getMacAddress(final NetworkInterface ni) throws SocketException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(ni));
        if (null == ni) {
            throw new RuntimeExceptionIsNull("ni");
        }
        final StringBuilder sb = new StringBuilder();
        final byte[] hardwareAddress = ni.getHardwareAddress();
        if (null != hardwareAddress) {
            for (int ii = 0; ii < hardwareAddress.length; ii++) {
                sb.append(String.format((0 == ii ? HelperString.EMPTY_STRING : '-') + "%02X", hardwareAddress[ii]));
            }
        }
        final String result = sb.toString();
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit(result));
        return result;
    }
