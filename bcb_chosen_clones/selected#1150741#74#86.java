    private static S3ServerException _exceptionForS3Error(String code, String errorMessage, String requestId, String hostId) {
        try {
            Constructor<? extends S3ServerException> construct;
            Class<? extends S3ServerException> cls;
            final Class<?> loadedClass;
            loadedClass = Class.forName("com.threerings.s3.client.S3ServerException$" + code + "Exception");
            cls = loadedClass.asSubclass(S3ServerException.class);
            construct = cls.getConstructor(String.class, String.class, String.class);
            return construct.newInstance(errorMessage, requestId, hostId);
        } catch (Exception e) {
            return new S3ServerException("An unhandled S3 error code was returned: " + code, requestId, hostId);
        }
    }
