    private void compile(String path, String target) {
        String sourceJarFile = path + File.separatorChar + PATH_CODE_MANAGER + File.separatorChar + NAME_CODE_MANAGER;
        String destJarFile = path + File.separatorChar + target;
        String certificateJarPath = PATH_CERTIFICATE + File.separatorChar + NAME_CERTIFICATE;
        String codesPath = path + File.separatorChar + PATH_CODES;
        String codesJarPath = PATH_CODES;
        String itineraryPath = path + File.separatorChar + PATH_ITINERARY + File.separatorChar + NAME_ITINERARY;
        String itineraryJarPath = PATH_ITINERARY + File.separatorChar + NAME_ITINERARY;
        PrivateKey privKey;
        ZipEntry ze;
        byte[] hash, signature, ciphered;
        FileInputStream fis;
        FileOutputStream fos;
        JarOutputStream jos;
        JarInputStream jis;
        try {
            Security.addProvider(new BouncyCastleProvider());
            System.out.println("Creating disposable key pair...");
            KeyPair pair = createKeyPair();
            if (pair != null) {
                privKey = pair.getPrivate();
                X509Certificate cert = createX509Certificate(pair);
                System.out.println("Calculating code manager hash...");
                fis = new FileInputStream(sourceJarFile);
                FileInputStream itIS = new FileInputStream(itineraryPath);
                hash = calculateHash(fis, itIS, cert);
                fis.close();
                itIS.close();
                fos = new FileOutputStream(destJarFile);
                jos = new JarOutputStream(fos);
                System.out.println("Adding code manager to the agent Jar...");
                copyJarEntries(sourceJarFile, jos);
                System.out.println("Adding disposable public key and certificate...");
                ze = new ZipEntry(certificateJarPath);
                jos.putNextEntry(ze);
                ByteArrayInputStream bais = new ByteArrayInputStream(cert.getEncoded());
                copyIStoOS(bais, jos, 4096);
                jos.closeEntry();
                bais.close();
                System.out.println("Adding agent itinerary...");
                ze = new ZipEntry(itineraryJarPath);
                jos.putNextEntry(ze);
                fis = new FileInputStream(itineraryPath);
                copyIStoOS(fis, jos, 4096);
                jos.closeEntry();
                fis.close();
                System.out.println("Processing each agent task...");
                File codeFolder = new File(codesPath);
                File[] files;
                String fileStr, pubKeyPath;
                files = codeFolder.listFiles();
                int pos;
                ByteArrayOutputStream baos;
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");
                System.out.println("Securing tasks...");
                for (int i = 0; i < files.length; i++) {
                    fileStr = files[i].getName();
                    if (files[i].isFile() && fileStr.endsWith(PLAIN_CODE_EXTENSION)) {
                        pos = fileStr.lastIndexOf(PLAIN_CODE_EXTENSION);
                        fileStr = fileStr.substring(0, pos);
                        fis = new FileInputStream(files[i]);
                        baos = new ByteArrayOutputStream();
                        copyIStoOS(fis, baos, 4096);
                        signature = sign(baos.toByteArray(), privKey, cert, true);
                        pubKeyPath = codesPath + File.separatorChar + fileStr + PUBLIC_KEY_EXTENSION;
                        fis = new FileInputStream(pubKeyPath);
                        X509Certificate hostCert = (X509Certificate) certFactory.generateCertificate(fis);
                        ciphered = cipher(signature, hash, hostCert);
                        signature = null;
                        fis.close();
                        ze = new ZipEntry(codesJarPath + File.separatorChar + fileStr + CIPHERED_CODE_EXTENSION);
                        jos.putNextEntry(ze);
                        bais = new ByteArrayInputStream(ciphered);
                        copyIStoOS(bais, jos, 4096);
                        jos.closeEntry();
                        bais.close();
                        bais = null;
                        System.out.println("Task secured: " + files[i].getName());
                    }
                }
                System.out.println("Agent compiled.");
                jos.close();
                fos.close();
            }
        } catch (FileNotFoundException fnfe) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: " + fnfe);
        } catch (CertificateEncodingException e) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: " + e);
        } catch (InvalidKeyException e) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: " + e);
        } catch (IllegalStateException e) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: " + e);
        } catch (NoSuchProviderException e) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: " + e);
        } catch (NoSuchAlgorithmException e) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: " + e);
        } catch (SignatureException e) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: " + e);
        } catch (IOException e) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: " + e);
        } catch (CertificateException e) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: External certificate: " + e);
        } catch (CMSException e) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: Cipher: " + e);
        }
    }
