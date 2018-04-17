    void signJar(String jarName, String alias, String[] args) throws Exception {
        boolean aliasUsed = false;
        X509Certificate tsaCert = null;
        if (sigfile == null) {
            sigfile = alias;
            aliasUsed = true;
        }
        if (sigfile.length() > 8) {
            sigfile = sigfile.substring(0, 8).toUpperCase(Locale.ENGLISH);
        } else {
            sigfile = sigfile.toUpperCase(Locale.ENGLISH);
        }
        StringBuilder tmpSigFile = new StringBuilder(sigfile.length());
        for (int j = 0; j < sigfile.length(); j++) {
            char c = sigfile.charAt(j);
            if (!((c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '-') || (c == '_'))) {
                if (aliasUsed) {
                    c = '_';
                } else {
                    throw new RuntimeException(rb.getString("signature filename must consist of the following characters: A-Z, 0-9, _ or -"));
                }
            }
            tmpSigFile.append(c);
        }
        sigfile = tmpSigFile.toString();
        String tmpJarName;
        if (signedjar == null) tmpJarName = jarName + ".sig"; else tmpJarName = signedjar;
        File jarFile = new File(jarName);
        File signedJarFile = new File(tmpJarName);
        try {
            zipFile = new ZipFile(jarName);
        } catch (IOException ioe) {
            error(rb.getString("unable to open jar file: ") + jarName, ioe);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(signedJarFile);
        } catch (IOException ioe) {
            error(rb.getString("unable to create: ") + tmpJarName, ioe);
        }
        PrintStream ps = new PrintStream(fos);
        ZipOutputStream zos = new ZipOutputStream(ps);
        String sfFilename = (META_INF + sigfile + ".SF").toUpperCase(Locale.ENGLISH);
        String bkFilename = (META_INF + sigfile + ".DSA").toUpperCase(Locale.ENGLISH);
        Manifest manifest = new Manifest();
        Map<String, Attributes> mfEntries = manifest.getEntries();
        Attributes oldAttr = null;
        boolean mfModified = false;
        boolean mfCreated = false;
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
                mattr.putValue("Created-By", jdkVersion + " (" + javaVendor + ")");
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
            if (verbose != null) {
                if (mfCreated) {
                    System.out.println(rb.getString("   adding: ") + mfFile.getName());
                } else if (mfModified) {
                    System.out.println(rb.getString(" updating: ") + mfFile.getName());
                }
            }
            zos.putNextEntry(mfFile);
            zos.write(mfRawBytes);
            ManifestDigester manDig = new ManifestDigester(mfRawBytes);
            SignatureFile sf = new SignatureFile(digests, manifest, manDig, sigfile, signManifest);
            if (tsaAlias != null) {
                tsaCert = getTsaCert(tsaAlias);
            }
            SignatureFile.Block block = null;
            try {
                block = sf.generateBlock(privateKey, sigalg, certChain, externalSF, tsaUrl, tsaCert, signingMechanism, args, zipFile);
            } catch (SocketTimeoutException e) {
                error(rb.getString("unable to sign jar: ") + rb.getString("no response from the Timestamping Authority. ") + rb.getString("When connecting from behind a firewall then an HTTP proxy may need to be specified. ") + rb.getString("Supply the following options to jarsigner: ") + "\n  -J-Dhttp.proxyHost=<hostname> " + "\n  -J-Dhttp.proxyPort=<portnumber> ", e);
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
            if (verbose != null) {
                if (zipFile.getEntry(sfFilename) != null) {
                    System.out.println(rb.getString(" updating: ") + sfFilename);
                } else {
                    System.out.println(rb.getString("   adding: ") + sfFilename);
                }
            }
            if (verbose != null) {
                if (tsaUrl != null || tsaCert != null) {
                    System.out.println(rb.getString("requesting a signature timestamp"));
                }
                if (tsaUrl != null) {
                    System.out.println(rb.getString("TSA location: ") + tsaUrl);
                }
                if (tsaCert != null) {
                    String certUrl = TimestampedSigner.getTimestampingUrl(tsaCert);
                    if (certUrl != null) {
                        System.out.println(rb.getString("TSA location: ") + certUrl);
                    }
                    System.out.println(rb.getString("TSA certificate: ") + printCert("", tsaCert, false, 0));
                }
                if (signingMechanism != null) {
                    System.out.println(rb.getString("using an alternative signing mechanism"));
                }
            }
            zos.putNextEntry(bkFile);
            block.write(zos);
            if (verbose != null) {
                if (zipFile.getEntry(bkFilename) != null) {
                    System.out.println(rb.getString(" updating: ") + bkFilename);
                } else {
                    System.out.println(rb.getString("   adding: ") + bkFilename);
                }
            }
            for (int i = 0; i < mfFiles.size(); i++) {
                ZipEntry ze = mfFiles.elementAt(i);
                if (!ze.getName().equalsIgnoreCase(JarFile.MANIFEST_NAME) && !ze.getName().equalsIgnoreCase(sfFilename) && !ze.getName().equalsIgnoreCase(bkFilename)) {
                    writeEntry(zipFile, zos, ze);
                }
            }
            for (Enumeration<? extends ZipEntry> enum_ = zipFile.entries(); enum_.hasMoreElements(); ) {
                ZipEntry ze = enum_.nextElement();
                if (!ze.getName().startsWith(META_INF)) {
                    if (verbose != null) {
                        if (manifest.getAttributes(ze.getName()) != null) System.out.println(rb.getString("  signing: ") + ze.getName()); else System.out.println(rb.getString("   adding: ") + ze.getName());
                    }
                    writeEntry(zipFile, zos, ze);
                }
            }
        } catch (IOException ioe) {
            error(rb.getString("unable to sign jar: ") + ioe, ioe);
        } finally {
            if (zipFile != null) {
                zipFile.close();
                zipFile = null;
            }
            if (zos != null) {
                zos.close();
            }
        }
        if (signedjar == null) {
            if (!signedJarFile.renameTo(jarFile)) {
                File origJar = new File(jarName + ".orig");
                if (jarFile.renameTo(origJar)) {
                    if (signedJarFile.renameTo(jarFile)) {
                        origJar.delete();
                    } else {
                        MessageFormat form = new MessageFormat(rb.getString("attempt to rename signedJarFile to jarFile failed"));
                        Object[] source = { signedJarFile, jarFile };
                        error(form.format(source));
                    }
                } else {
                    MessageFormat form = new MessageFormat(rb.getString("attempt to rename jarFile to origJar failed"));
                    Object[] source = { jarFile, origJar };
                    error(form.format(source));
                }
            }
        }
        if (hasExpiredCert || hasExpiringCert || notYetValidCert || badKeyUsage || badExtendedKeyUsage || badNetscapeCertType || chainNotValidated) {
            System.out.println();
            System.out.println(rb.getString("Warning: "));
            if (badKeyUsage) {
                System.out.println(rb.getString("The signer certificate's KeyUsage extension doesn't allow code signing."));
            }
            if (badExtendedKeyUsage) {
                System.out.println(rb.getString("The signer certificate's ExtendedKeyUsage extension doesn't allow code signing."));
            }
            if (badNetscapeCertType) {
                System.out.println(rb.getString("The signer certificate's NetscapeCertType extension doesn't allow code signing."));
            }
            if (hasExpiredCert) {
                System.out.println(rb.getString("The signer certificate has expired."));
            } else if (hasExpiringCert) {
                System.out.println(rb.getString("The signer certificate will expire within six months."));
            } else if (notYetValidCert) {
                System.out.println(rb.getString("The signer certificate is not yet valid."));
            }
            if (chainNotValidated) {
                System.out.println(rb.getString("The signer's certificate chain is not validated."));
            }
        }
    }
