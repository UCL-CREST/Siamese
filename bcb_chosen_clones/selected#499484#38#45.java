                public Object run() throws Exception {
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    Class c = Class.forName("com.ericdaugherty.mail.server.auth.GSSServerMode", true, cl);
                    Object instance = c.getConstructor(Boolean.class, String.class).newInstance(new Boolean(isSMTP), clientIp);
                    java.lang.reflect.Method mainMethod = c.getMethod("negotiateGSSAuthenticationContext");
                    mainMethod.invoke(instance);
                    return instance;
                }
