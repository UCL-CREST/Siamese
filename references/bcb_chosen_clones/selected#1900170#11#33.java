    public String getHashedPhoneId(Context aContext) {
        if (hashedPhoneId == null) {
            final String androidId = BuildInfo.getAndroidID(aContext);
            if (androidId == null) {
                hashedPhoneId = "EMULATOR";
            } else {
                try {
                    final MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                    messageDigest.update(androidId.getBytes());
                    messageDigest.update(aContext.getPackageName().getBytes());
                    final StringBuilder stringBuilder = new StringBuilder();
                    for (byte b : messageDigest.digest()) {
                        stringBuilder.append(String.format("%02X", b));
                    }
                    hashedPhoneId = stringBuilder.toString();
                } catch (Exception e) {
                    Log.e(LoggingExceptionHandler.class.getName(), "Unable to get phone id", e);
                    hashedPhoneId = "Not Available";
                }
            }
        }
        return hashedPhoneId;
    }
