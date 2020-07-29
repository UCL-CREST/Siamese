    protected void initSslContext() throws Exception {
        logger.info("generating self-signed certificate with " + KEY_SIZE + " bit key (this could take a few moments...)");
        Class<?> keypairClass = Class.forName("sun.security.x509.CertAndKeyGen");
        Constructor<?> keypairConstructor = keypairClass.getConstructor(String.class, String.class, String.class);
        Object keypair = keypairConstructor.newInstance(KEY_ALGORITHM, SIGNATURE_ALGORITHM, getProvider());
        keypairClass.getMethod("generate", Integer.TYPE).invoke(keypair, KEY_SIZE);
        Class<?> x500NameClass = Class.forName("sun.security.x509.X500Name");
        Constructor<?> x500NameConstructor = x500NameClass.getConstructor(String.class, String.class, String.class, String.class, String.class, String.class);
        Object x500Name = x500NameConstructor.newInstance(cn, OU, O, L, ST, C);
        Method getSelfCertificateMethod = keypairClass.getMethod("getSelfCertificate", x500NameClass, Date.class, Long.TYPE);
        X509Certificate cert = (X509Certificate) getSelfCertificateMethod.invoke(keypair, x500Name, new Date(), VALIDITY_DAYS * 24L * 60L * 60L);
        logger.info("generated self-signed certificate with subject: " + cert.getSubjectX500Principal());
        PrivateKey privateKey = (PrivateKey) keypairClass.getMethod("getPrivateKey").invoke(keypair);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(null, null);
        keystore.setKeyEntry(ALIAS, privateKey, PASSWORD, new X509Certificate[] { cert });
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(getSslKeyManagerFactoryAlgorithm());
        keyManagerFactory.init(keystore, PASSWORD);
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(getSslTrustManagerFactoryAlgorithm());
        trustManagerFactory.init(keystore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        SecureRandom secureRandom = null;
        if (getSecureRandomAlgorithm() != null) {
            secureRandom = SecureRandom.getInstance(getSecureRandomAlgorithm());
        }
        if (getProvider() != null) {
            sslContext = SSLContext.getInstance(getProtocol(), getProvider());
        } else {
            sslContext = SSLContext.getInstance(getProtocol());
        }
        sslContext.init(keyManagers, trustManagers, secureRandom);
    }
