    public static void main(String[] args) {
        String host;
        int port;
        char[] passphrase;
        System.out.println("InstallCert - Install CA certificate to Java Keystore");
        System.out.println("=====================================================");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            if ((args.length == 1) || (args.length == 2)) {
                String[] c = args[0].split(":");
                host = c[0];
                port = (c.length == 1) ? 443 : Integer.parseInt(c[1]);
                String p = (args.length == 1) ? "changeit" : args[1];
                passphrase = p.toCharArray();
            } else {
                String tmpStr;
                do {
                    System.out.print("Enter hostname or IP address: ");
                    tmpStr = StringUtils.defaultIfEmpty(reader.readLine(), null);
                } while (tmpStr == null);
                host = tmpStr;
                System.out.print("Enter port number [443]: ");
                tmpStr = StringUtils.defaultIfEmpty(reader.readLine(), null);
                port = tmpStr == null ? 443 : Integer.parseInt(tmpStr);
                System.out.print("Enter keystore password [changeit]: ");
                tmpStr = reader.readLine();
                String p = "".equals(tmpStr) ? "changeit" : tmpStr;
                passphrase = p.toCharArray();
            }
            char SEP = File.separatorChar;
            final File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
            final File file = new File(dir, "cacerts");
            System.out.println("Loading KeyStore " + file + "...");
            InputStream in = new FileInputStream(file);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(in, passphrase);
            in.close();
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
            SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
            context.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory factory = context.getSocketFactory();
            System.out.println("Opening connection to " + host + ":" + port + "...");
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
            socket.setSoTimeout(10000);
            try {
                System.out.println("Starting SSL handshake...");
                socket.startHandshake();
                socket.close();
                System.out.println();
                System.out.println("No errors, certificate is already trusted");
            } catch (SSLException e) {
                System.out.println();
                e.printStackTrace(System.out);
            }
            X509Certificate[] chain = tm.chain;
            if (chain == null) {
                System.out.println("Could not obtain server certificate chain");
                return;
            }
            System.out.println();
            System.out.println("Server sent " + chain.length + " certificate(s):");
            System.out.println();
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            for (int i = 0; i < chain.length; i++) {
                X509Certificate cert = chain[i];
                System.out.println(" " + (i + 1) + " Subject " + cert.getSubjectDN());
                System.out.println("   Issuer  " + cert.getIssuerDN());
                sha1.update(cert.getEncoded());
                System.out.println("   sha1    " + toHexString(sha1.digest()));
                md5.update(cert.getEncoded());
                System.out.println("   md5     " + toHexString(md5.digest()));
                System.out.println();
            }
            System.out.print("Enter certificate to add to trusted keystore or 'q' to quit [1]: ");
            String line = reader.readLine().trim();
            int k = -1;
            try {
                k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;
            } catch (NumberFormatException e) {
            }
            if (k < 0 || k >= chain.length) {
                System.out.println("KeyStore not changed");
            } else {
                try {
                    System.out.println("Creating keystore backup");
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    final File backupFile = new File(dir, CACERTS_KEYSTORE + "." + dateFormat.format(new java.util.Date()));
                    final FileInputStream fis = new FileInputStream(file);
                    final FileOutputStream fos = new FileOutputStream(backupFile);
                    IOUtils.copy(fis, fos);
                    fis.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Installing certificate...");
                X509Certificate cert = chain[k];
                String alias = host + "-" + (k + 1);
                ks.setCertificateEntry(alias, cert);
                OutputStream out = new FileOutputStream(file);
                ks.store(out, passphrase);
                out.close();
                System.out.println();
                System.out.println(cert);
                System.out.println();
                System.out.println("Added certificate to keystore '" + file + "' using alias '" + alias + "'");
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println("Problem occured during installing certificate:");
            e.printStackTrace();
            System.out.println("----------------------------------------------");
        }
        System.out.println("Press Enter to finish...");
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
