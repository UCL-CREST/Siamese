    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(_socket.getInputStream());
            Object receivedObject;
            String setMsg;
            while ((receivedObject = ois.readObject()) != null) {
                if (receivedObject instanceof iSignRegister) {
                    System.out.println("iSignRegister object received");
                    String loginName = ((iSignRegister) receivedObject).getLoginName();
                    String password = ((iSignRegister) receivedObject).getPassword();
                    java.security.cert.Certificate[] cert = ((iSignRegister) receivedObject).getCert();
                    System.out.println(loginName);
                    System.out.println(password);
                    System.out.println(cert[0]);
                    try {
                        FileInputStream f = new FileInputStream(loginName + ".iks");
                        String msg = "USER ALREADY REGISTERED";
                        (new ObjectOutputStream(_socket.getOutputStream())).writeObject(msg);
                        _socket.close();
                        return;
                    } catch (FileNotFoundException e) {
                    }
                    try {
                        KeyStore outks = KeyStore.getInstance("JKS");
                        outks.load(null, password.toCharArray());
                        KeyPairGenerator kg = KeyPairGenerator.getInstance("DSA");
                        kg.initialize(1024);
                        KeyPair kp = kg.generateKeyPair();
                        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
                        java.security.cert.Certificate[] newCert = new java.security.cert.Certificate[1];
                        certGen.reset();
                        certGen.setSubjectDN(new X509Principal(((java.security.cert.X509Certificate) cert[0]).getSubjectX500Principal().getEncoded()));
                        certGen.setIssuerDN(new X509Principal(((java.security.cert.X509Certificate) cert[0]).getSubjectX500Principal().getEncoded()));
                        GregorianCalendar date = new GregorianCalendar();
                        date.add(Calendar.DATE, -1);
                        certGen.setNotBefore(date.getTime());
                        date.add(Calendar.DATE, 1);
                        date.add(Calendar.HOUR_OF_DAY, 24);
                        certGen.setNotAfter(date.getTime());
                        certGen.setSerialNumber(new BigInteger(128, new SecureRandom()));
                        certGen.setPublicKey(kp.getPublic());
                        System.out.println(((java.security.cert.X509Certificate) cert[0]).getSigAlgName());
                        certGen.setSignatureAlgorithm(((java.security.cert.X509Certificate) cert[0]).getSigAlgName());
                        newCert[0] = certGen.generateX509Certificate(kp.getPrivate());
                        outks.setKeyEntry(loginName, kp.getPrivate(), password.toCharArray(), newCert);
                        outks.store(new FileOutputStream(loginName + ".iks"), password.toCharArray());
                        System.out.println("File write success");
                    } catch (Exception e) {
                        (new ObjectOutputStream(_socket.getOutputStream())).writeObject(e.getMessage());
                        _socket.close();
                        return;
                    }
                    setMsg = SERVER_OK;
                    (new ObjectOutputStream(_socket.getOutputStream())).writeObject(setMsg);
                    _socket.close();
                    System.out.println("iSign registration successful");
                    return;
                } else if (receivedObject instanceof iSignLogin) {
                    String loginName = ((iSignLogin) receivedObject).getLoginName();
                    String password = ((iSignLogin) receivedObject).getPassword();
                    PublicKey pubKey = ((iSignLogin) receivedObject).getPublicKey();
                    KeyStore ks = KeyStore.getInstance("JKS");
                    try {
                        ks.load(new FileInputStream(loginName + ".iks"), password.toCharArray());
                    } catch (Exception e) {
                        (new ObjectOutputStream(_socket.getOutputStream())).writeObject(e.getMessage());
                        _socket.close();
                        return;
                    }
                    X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
                    KeyStore iSignks = _server.getServerKeyStore();
                    PrivateKey priv_key = (PrivateKey) (iSignks.getKey(ISIGN_KEYSTORE, ISIGN_KEY_PASSWORD));
                    java.security.cert.Certificate iSignCert = iSignks.getCertificate(ISIGN_KEYSTORE);
                    java.security.cert.Certificate[] cert = ks.getCertificateChain(loginName);
                    java.security.cert.Certificate[] newCert = new java.security.cert.Certificate[cert.length];
                    for (int i = 0; i < cert.length; i++) {
                        certGen.reset();
                        certGen.setSubjectDN(new X509Principal(((java.security.cert.X509Certificate) cert[i]).getSubjectX500Principal().getEncoded()));
                        certGen.setIssuerDN(new X509Principal(((java.security.cert.X509Certificate) iSignCert).getSubjectX500Principal().getEncoded()));
                        GregorianCalendar date = new GregorianCalendar();
                        date.add(Calendar.DATE, -1);
                        certGen.setNotBefore(date.getTime());
                        date.add(Calendar.DATE, 1);
                        date.add(Calendar.HOUR_OF_DAY, 24);
                        certGen.setNotAfter(date.getTime());
                        certGen.setPublicKey(pubKey);
                        certGen.setSerialNumber(new BigInteger(128, new SecureRandom()));
                        certGen.setSignatureAlgorithm(((java.security.cert.X509Certificate) cert[i]).getSigAlgName());
                        BasicConstraints bc = new BasicConstraints(true, 5);
                        certGen.addExtension(X509Extensions.BasicConstraints, true, bc);
                        newCert[i] = certGen.generateX509Certificate(priv_key);
                    }
                    (new ObjectOutputStream(_socket.getOutputStream())).writeObject(newCert);
                    _socket.close();
                    return;
                } else {
                    setMsg = UNKNOWN_OBJECT_ERROR;
                    (new ObjectOutputStream(_socket.getOutputStream())).writeObject(setMsg);
                    _socket.close();
                    return;
                }
            }
            _socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
