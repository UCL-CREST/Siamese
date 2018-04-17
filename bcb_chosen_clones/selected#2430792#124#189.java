    public static void init(String caKeystoreFile, String clientKeystoreFile, char[] caPassphrase, char[] clientPassphrase, String caKeystoreType, String clientKeystoreType) throws NamingException {
        if (default_factory != null) return;
        try {
            checkFileSanity(caKeystoreFile, clientKeystoreFile, clientPassphrase);
            if (caKeystoreFile == null) caKeystoreFile = clientKeystoreFile;
            SSLContext sslctx;
            String protocol = System.getProperty("sslversion", "TLS");
            if (!"TLS".equals(protocol)) System.out.println("SECURITY: Using non-standard ssl version: '" + protocol + "'");
            sslctx = SSLContext.getInstance(protocol);
            KeyManagerFactory clientKeyManagerFactory = null;
            TrustManagerFactory caTrustManagerFactory;
            KeyStore caKeystore;
            if ((clientPassphrase != null) && (clientPassphrase.length > 0)) {
                if (PKI_INTERNAL_TYPE.equals(clientKeystoreType)) {
                    try {
                        Class c = getClassLoader().loadClass("com.ca.commons.security.openssl.ParsePkcs12");
                        if (c == null) {
                            System.out.println("PKI internal error");
                            return;
                        }
                        Constructor constructor = c.getConstructor(new Class[] { String.class, byte[].class });
                        int size = clientPassphrase.length;
                        byte[] password = new byte[size];
                        for (int i = 0; i < size; i++) password[i] = (byte) clientPassphrase[i];
                        Object pkcs12Parser = constructor.newInstance(new Object[] { clientKeystoreFile, password });
                        Method getSunKeyStore = c.getMethod("getSunKeyStore", new Class[] { String.class });
                        clientKeystore = (KeyStore) getSunKeyStore.invoke(pkcs12Parser, new Object[] { "MyFriend" });
                        for (int i = 0; i < size; i++) password[i] = 0;
                    } catch (Exception e) {
                        System.err.println("unable to load pkcs12 parser (not in class path?)");
                        e.printStackTrace();
                        return;
                    }
                } else {
                    if (clientKeystoreType == null) clientKeystoreType = DEFAULT_KEYSTORE_TYPE;
                    clientKeystore = KeyStore.getInstance(clientKeystoreType);
                    if (clientKeystoreFile != null) clientKeystore.load(new FileInputStream(clientKeystoreFile), clientPassphrase);
                }
                clientKeyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                clientKeyManagerFactory.init(clientKeystore, clientPassphrase);
            }
            KeyManager[] keyManagers = null;
            if (clientKeyManagerFactory != null) keyManagers = clientKeyManagerFactory.getKeyManagers();
            if (caKeystoreType == null) caKeystoreType = DEFAULT_KEYSTORE_TYPE;
            caKeystore = KeyStore.getInstance(caKeystoreType);
            if (caKeystoreFile != null) {
                caKeystore.load(new FileInputStream(caKeystoreFile), caPassphrase);
            }
            caTrustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            caTrustManagerFactory.init(caKeystore);
            TrustManager[] trustManagers = caTrustManagerFactory.getTrustManagers();
            sslctx.init(keyManagers, trustManagers, null);
            synchronized (JndiSocketFactory.class) {
                factory = sslctx.getSocketFactory();
                default_factory = new JndiSocketFactory();
            }
        } catch (GeneralSecurityException e) {
            NamingException ne = new NamingException("security error: unable to initialise JndiSocketFactory");
            ne.initCause(e);
            throw ne;
        } catch (IOException e) {
            NamingException ne = new NamingException("file access error: unable to initialise JndiSocketFactory");
            ne.initCause(e);
            throw ne;
        }
    }
