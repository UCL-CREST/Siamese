    public synchronized GSSServerMode getGSSServerMode(final boolean isSMTP, final String clientIp) throws SaslException {
        try {
            return (GSSServerMode) Subject.doAsPrivileged(((isSMTP || subjects.length == 1) ? subjects[0] : subjects[1]), new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    Class c = Class.forName("com.ericdaugherty.mail.server.auth.GSSServerMode", true, cl);
                    Object instance = c.getConstructor(Boolean.class, String.class).newInstance(new Boolean(isSMTP), clientIp);
                    java.lang.reflect.Method mainMethod = c.getMethod("negotiateGSSAuthenticationContext");
                    mainMethod.invoke(instance);
                    return instance;
                }
            }, null);
        } catch (java.security.PrivilegedActionException pae) {
            throw (SaslException) pae.getException();
        }
    }
