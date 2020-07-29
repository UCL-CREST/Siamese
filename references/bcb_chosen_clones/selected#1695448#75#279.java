    public static void signJar(JSParameters param) throws JarSignerException {
        boolean aliasUsed = false;
        X509Certificate tsaCert = null;
        ZipFile zipFile = null;
        String sigfile = null;
        if (sigfile == null) {
            sigfile = param.getAlias();
            aliasUsed = true;
        }
        if (sigfile.length() > 8) {
            sigfile = sigfile.substring(0, 8).toUpperCase();
        } else {
            sigfile = sigfile.toUpperCase();
        }
        StringBuilder tmpSigFile = new StringBuilder(sigfile.length());
        for (int j = 0; j < sigfile.length(); j++) {
            char c = sigfile.charAt(j);
            if (!((c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '-') || (c == '_'))) {
                if (aliasUsed) {
                    c = '_';
                } else {
                    throw new JarSignerException("signature filename must consist of the following characters: A-Z, 0-9, _ or -");
                }
            }
            tmpSigFile.append(c);
        }
        sigfile = tmpSigFile.toString();
        String tmpJarName;
        if (param.getSignedJARName() == null) tmpJarName = param.getJarName() + ".sig"; else tmpJarName = param.getSignedJARName();
        File jarFile = new File(param.getJarName());
        File signedJarFile = new File(tmpJarName);
        try {
            String nombre = param.getJarName();
            zipFile = new ZipFile(nombre);
        } catch (IOException ioe) {
            throw new JarSignerException("unable to open jar file: " + param.getJarName());
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(signedJarFile);
        } catch (IOException ioe) {
            throw new JarSignerException("unable to create: " + tmpJarName);
        }
        PrintStream ps = new PrintStream(fos);
        ZipOutputStream zos = new ZipOutputStream(ps);
        String sfFilename = (META_INF + sigfile + ".SF").toUpperCase();
        String bkFilename = (META_INF + sigfile + ".DSA").toUpperCase();
        Manifest manifest = new Manifest();
        Map<String, Attributes> mfEntries = manifest.getEntries();
        Attributes oldAttr = null;
        boolean mfCreated = false;
        boolean mfModified = false;
        byte[] mfRawBytes = null;
        try {
            MessageDigest digests[] = { MessageDigest.getInstance(digestalg) };
            ZipEntry mfFile;
            if ((mfFile = getManifestFile(zipFile)) != null) {
                mfRawBytes = getBytes(zipFile, mfFile);
                manifest.read(new ByteArrayInputStream(mfRawBytes));
                oldAttr = (Attributes) (manifest.getMainAttributes().clone());
            } else {
                Attributes mattr = manifest.getMainAttributes();
                mattr.putValue(Attributes.Name.MANIFEST_VERSION.toString(), "1.0");
                String javaVendor = System.getProperty("java.vendor");
                String jdkVersion = System.getProperty("java.version");
                mattr.putValue("Created by", jdkVersion + " (" + javaVendor + ")");
                mfFile = new ZipEntry(JarFile.MANIFEST_NAME);
                mfCreated = true;
            }
            BASE64Encoder encoder = new JarBASE64Encoder();
            Vector<ZipEntry> mfFiles = new Vector<ZipEntry>();
            for (Enumeration<? extends ZipEntry> enum_ = zipFile.entries(); enum_.hasMoreElements(); ) {
                ZipEntry ze = enum_.nextElement();
                if (ze.getName().startsWith(META_INF)) {
                    mfFiles.addElement(ze);
                    if (signatureRelated(ze.getName())) {
                        continue;
                    }
                }
                if (manifest.getAttributes(ze.getName()) != null) {
                    if (updateDigests(ze, zipFile, digests, encoder, manifest) == true) {
                        mfModified = true;
                    }
                } else if (!ze.isDirectory()) {
                    Attributes attrs = getDigestAttributes(ze, zipFile, digests, encoder);
                    mfEntries.put(ze.getName(), attrs);
                    mfModified = true;
                }
            }
            if (mfModified) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                manifest.write(baos);
                byte[] newBytes = baos.toByteArray();
                if (mfRawBytes != null && oldAttr.equals(manifest.getMainAttributes())) {
                    mfRawBytes = newBytes;
                    int newPos = findHeaderEnd(newBytes);
                    int oldPos = findHeaderEnd(mfRawBytes);
                    if (newPos == oldPos) {
                        System.arraycopy(mfRawBytes, 0, newBytes, 0, oldPos);
                    } else {
                        byte[] lastBytes = new byte[oldPos + newBytes.length - newPos];
                        System.arraycopy(mfRawBytes, 0, lastBytes, 0, oldPos);
                        System.arraycopy(newBytes, newPos, lastBytes, oldPos, newBytes.length - newPos);
                        newBytes = lastBytes;
                    }
                }
                mfRawBytes = newBytes;
            }
            if (mfModified) {
                mfFile = new ZipEntry(JarFile.MANIFEST_NAME);
            }
            if (param.isVerbose()) {
                if (mfCreated) {
                    System.out.println((" adding: ") + mfFile.getName());
                } else if (mfModified) {
                    System.out.println((" updating: ") + mfFile.getName());
                }
            }
            zos.putNextEntry(mfFile);
            zos.write(mfRawBytes);
            ManifestDigester manDig = new ManifestDigester(mfRawBytes);
            SignatureFile sf = new SignatureFile(digests, manifest, manDig, sigfile, signManifest);
            SignatureFile.Block block = null;
            try {
                block = sf.generateBlock(null, certChain, true, null, tsaCert, null, param, zipFile);
            } catch (SocketTimeoutException e) {
                throw new JarSignerException("unable to sign jar: " + "no response from the Timestamping Authority. " + "When connecting from behind a firewall then an HTTP proxy may need to be specified. " + "Supply the following options to jarsigner: " + "\n  -J-Dhttp.proxyHost=<hostname> " + "\n  -J-Dhttp.proxyPort=<portnumber> ", e);
            } catch (InvalidKeyException e) {
                throw new JarSignerException("unable to sign jar: " + e.getMessage());
            } catch (UnrecoverableKeyException e) {
                throw new JarSignerException("unable to sign jar: " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                throw new JarSignerException("unable to sign jar: " + e.getMessage());
            } catch (SignatureException e) {
                throw new JarSignerException("unable to sign jar: " + e.getMessage());
            } catch (CertificateException e) {
                throw new JarSignerException("unable to sign jar: " + e.getMessage());
            } catch (KeyStoreException e) {
                throw new JarSignerException("unable to sign jar: " + e.getMessage());
            } catch (JarSignerException e) {
                throw new JarSignerException("unable to sign jar: " + e.getMessage());
            }
            sfFilename = sf.getMetaName();
            bkFilename = block.getMetaName();
            ZipEntry sfFile = new ZipEntry(sfFilename);
            ZipEntry bkFile = new ZipEntry(bkFilename);
            long time = System.currentTimeMillis();
            sfFile.setTime(time);
            bkFile.setTime(time);
            zos.putNextEntry(sfFile);
            sf.write(zos);
            zos.putNextEntry(bkFile);
            block.write(zos);
            for (int i = 0; i < mfFiles.size(); i++) {
                ZipEntry ze = mfFiles.elementAt(i);
                if (!ze.getName().equalsIgnoreCase(JarFile.MANIFEST_NAME) && !ze.getName().equalsIgnoreCase(sfFilename) && !ze.getName().equalsIgnoreCase(bkFilename)) {
                    writeEntry(zipFile, zos, ze);
                }
            }
            for (Enumeration<? extends ZipEntry> enum_ = zipFile.entries(); enum_.hasMoreElements(); ) {
                ZipEntry ze = enum_.nextElement();
                if (!ze.getName().startsWith(META_INF)) {
                    writeEntry(zipFile, zos, ze);
                }
            }
        } catch (IOException ioe) {
            throw new JarSignerException("unable to sign jar: " + ioe, ioe);
        } catch (NoSuchAlgorithmException e) {
            throw new JarSignerException("unable to sign jar: " + e, e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    throw new JarSignerException("Exception with zipFile");
                }
                zipFile = null;
            }
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    throw new JarSignerException("Exception with zipFile");
                }
            }
        }
        if (param.getSignedJARName() == null) {
            if (!signedJarFile.renameTo(jarFile)) {
                File origJar = new File(param.getJarName() + ".orig");
                if (jarFile.renameTo(origJar)) {
                    if (signedJarFile.renameTo(jarFile)) {
                        origJar.delete();
                    } else {
                        MessageFormat form = new MessageFormat("attempt to rename signedJarFile to jarFile failed");
                        Object[] source = { signedJarFile, jarFile };
                        throw new JarSignerException(form.format(source));
                    }
                } else {
                    MessageFormat form = new MessageFormat("attempt to rename jarFile to origJar failed");
                    Object[] source = { jarFile, origJar };
                    throw new JarSignerException(form.format(source));
                }
            }
        }
    }
