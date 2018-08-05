    @Override
    public String encrypt(String did, String username, String password, String input, boolean uniquekey, String algorithm, int keysize) throws SkceWSException {
        String base64key = null;
        String token = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        Document xencdoc = null;
        String algorithmurl = null;
        String transform = null;
        SecretKey sk = null;
        SecretKeyObject sko = null;
        byte[] ivbytes = null;
        IvParameterSpec ivspec = null;
        Cipher cipher = null;
        try {
            if (did == null) {
                did = DEFAULT_DID;
            }
            Common.log(Level.INFO, "SKCE-MSG-4008", did);
            if (username == null) {
                username = DEFAULT_ENC_USERNAME;
            }
            Common.log(Level.INFO, "SKCE-MSG-4001", username);
            if (password == null) {
                password = DEFAULT_ENC_PASSWORD;
            }
            File inpf = new File(input);
            if (!inpf.exists()) {
                Common.log(Level.SEVERE, "SKCE-ERR-4001", input);
                throw new SkceWSException("No such file: " + input);
            } else if (!inpf.isFile()) {
                Common.log(Level.SEVERE, "SKCE-ERR-4002", input);
                throw new SkceWSException("Not a file: " + input);
            } else if (!inpf.canRead()) {
                Common.log(Level.SEVERE, "SKCE-ERR-4003", input);
                throw new SkceWSException("Not a readable file: " + input);
            } else if (inpf.length() <= 0) {
                Common.log(Level.SEVERE, "SKCE-ERR-4004", input);
                throw new SkceWSException("No data in file: " + input);
            }
            String infName = inpf.getName();
            fis = new FileInputStream(input);
            Common.log(Level.INFO, "SKCE-MSG-4002", input);
            SecureRandom securerandom = SecureRandom.getInstance(DEFAULT_PRNG);
            if (!uniquekey) {
                if (DEFAULT_KEY == null) {
                    algorithm = DEFAULT_ALGORITHM;
                    algorithmurl = DEFAULT_ALGORITHM_URL;
                    if (algorithm.equalsIgnoreCase("AES")) {
                        keysize = DEFAULT_AES_KEYSIZE;
                    } else if (algorithm.equalsIgnoreCase("DESEDE")) {
                        keysize = DEFAULT_DES_KEYSIZE;
                    }
                    KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
                    keygen.init(keysize, securerandom);
                    sk = keygen.generateKey();
                    base64key = new String(Base64.encode(sk.getEncoded()), "UTF-8");
                    sko = new SecretKeyObject(sk, algorithm, DEFAULT_TRANSFORM);
                    DEFAULT_KEY = sko;
                    Common.log(Level.FINE, "SKCE-MSG-4021", new String[] { algorithm, Integer.toString(keysize) });
                }
            } else {
                if (algorithm.equalsIgnoreCase("AES")) {
                    if (keysize != 128) {
                        if (keysize != 192) {
                            if (keysize != 256) {
                                keysize = DEFAULT_AES_KEYSIZE;
                                algorithmurl = DEFAULT_ALGORITHM_URL;
                                Common.log(Level.FINE, "SKCE-MSG-4022", keysize);
                            } else {
                                algorithmurl = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
                            }
                        } else {
                            algorithmurl = "http://www.w3.org/2001/04/xmlenc#aes192-cbc";
                        }
                    } else {
                        algorithmurl = "http://www.w3.org/2001/04/xmlenc#aes128-cbc";
                    }
                    ivbytes = new byte[16];
                    securerandom.nextBytes(ivbytes);
                    transform = AES_TRANSFORM;
                    cipher = Cipher.getInstance(transform);
                    ivspec = new IvParameterSpec(ivbytes);
                } else if (algorithm.equalsIgnoreCase(DESEDE)) {
                    if (keysize != 112 || keysize != 168) {
                        keysize = DEFAULT_DES_KEYSIZE;
                        Common.log(Level.FINE, "SKCE-MSG-4023", keysize);
                    }
                    algorithmurl = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
                    transform = DESEDE_TRANSFORM;
                    ivbytes = new byte[8];
                    securerandom.nextBytes(ivbytes);
                    cipher = Cipher.getInstance(transform);
                    ivspec = new IvParameterSpec(ivbytes);
                }
                Common.log(Level.FINE, "SKCE-MSG-4024", keysize);
                Common.log(Level.FINE, "SKCE-MSG-4025", new String[] { new String(Base64.encode(ivbytes), "UTF-8"), Integer.toString(ivbytes.length) });
                KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
                keygen.init(keysize, securerandom);
                sk = keygen.generateKey();
                base64key = new String(Base64.encode(sk.getEncoded()), "UTF-8");
                sko = new SecretKeyObject(sk, algorithm, transform);
                Common.log(Level.FINE, "SKCE-MSG-4026", new String[] { algorithm, Integer.toString(keysize) });
            }
            Common.log(Level.FINE, "SKCE-MSG-4027", algorithmurl);
            String hosturl = DEFAULT_HOSTPORT + ENCRYPTION_SERVICE_WSDL_SUFFIX;
            URL baseUrl = com.strongauth.strongkeylite.web.EncryptionService.class.getResource(".");
            URL url = new URL(baseUrl, hosturl);
            Common.log(Level.INFO, "SKCE-MSG-4028", hosturl);
            EncryptionService cryptosvc = new EncryptionService(url);
            Encryption port = cryptosvc.getEncryptionPort();
            Common.log(Level.FINE, "SKCE-MSG-4013", hosturl);
            token = port.encrypt(Long.parseLong(did), username, password, base64key);
            if (token != null) {
                Common.putSymmetricKey(token, sko);
                Common.log(Level.FINE, "SKCE-MSG-4029", token);
            }
            String s = ManagementFactory.getRuntimeMXBean().getName();
            int atsign = s.indexOf('@');
            int pid = Integer.parseInt(s.substring(0, atsign));
            File of = new File(input + "-" + token + "-" + pid + "-" + ITERATION++ + XML_PLUS_CIPHERTEXT_FILE_EXTENSION);
            Common.log(Level.FINE, "SKCE-MSG-4030", of.getName());
            File parent = of.getParentFile();
            if (parent != null) {
                if (parent.getFreeSpace() < (inpf.length() * 2)) {
                    Common.log(Level.SEVERE, "SKCE-ERR-4008", parent.getFreeSpace());
                    throw new SkceWSException("Insufficient estimated space to create output file: " + parent.getFreeSpace());
                }
            }
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(of)));
            int count;
            int total = 0;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(input), BUFFER_SIZE);
            Common.log(Level.INFO, "SKCE-MSG-4031", input);
            byte[] data = new byte[BUFFER_SIZE];
            MessageDigest digest = null;
            if (DEFAULT_DIGEST.equalsIgnoreCase("http://www.w3.org/2001/04/xmlenc#sha256")) {
                digest = MessageDigest.getInstance("SHA-256");
            } else if (DEFAULT_DIGEST.equalsIgnoreCase("http://www.w3.org/2001/04/xmlenc#sha512")) {
                digest = MessageDigest.getInstance("SHA-512");
            }
            cipher.init(Cipher.ENCRYPT_MODE, sk, ivspec);
            byte[] ciphertext;
            ZipEntry entry = new ZipEntry(infName + CIPHERTEXT_FILE_EXTENSION);
            zos.putNextEntry(entry);
            zos.write(ivbytes);
            while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                digest.update(data, 0, count);
                ciphertext = cipher.update(data, 0, count);
                zos.write(ciphertext);
                total += count;
            }
            Common.log(Level.INFO, "SKCE-MSG-4032", new String[] { Integer.toString(total), input });
            fis.close();
            bis.close();
            ciphertext = cipher.doFinal();
            zos.write(ciphertext);
            Common.log(Level.INFO, "SKCE-MSG-4033", input + CIPHERTEXT_FILE_EXTENSION);
            String hash = new String(Base64.encode(digest.digest()));
            Common.log(Level.INFO, "SKCE-MSG-4034", new String[] { hash, DEFAULT_DIGEST });
            Common.log(Level.FINE, "SKCE-MSG-5000", "new ByteArrayOutputStream()");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Common.log(Level.FINE, "SKCE-MSG-5000", "DocumentBuilderFactory()");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xencdoc = builder.newDocument();
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(xenc:EncryptedData)");
            Element root = (Element) xencdoc.createElement("xenc:EncryptedData");
            root.setAttribute("Id", "ID".concat(token.concat(Long.toString(Common.nowMs()))));
            root.setIdAttribute("Id", Boolean.TRUE);
            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("xsi:schemaLocation", "http://www.w3.org/2001/04/xmlenc# http://www.w3.org/TR/xmlenc-core/xenc-schema.xsd");
            root.setAttribute("xmlns:xenc", "http://www.w3.org/2001/04/xmlenc#");
            root.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
            root.setAttribute("xmlns:skles", "http://strongkeylite.strongauth.com/SKLES201009");
            org.w3c.dom.Node encdatanode = (org.w3c.dom.Node) xencdoc.appendChild(root);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(xenc:EncryptionMethod)");
            Element encmethod = (Element) xencdoc.createElement("xenc:EncryptionMethod");
            encmethod.setAttribute("Algorithm", algorithmurl);
            org.w3c.dom.Node encmethodnode = (org.w3c.dom.Node) encdatanode.appendChild(encmethod);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(ds:KeyInfo)");
            Element keyinfo = (Element) xencdoc.createElement("ds:KeyInfo");
            org.w3c.dom.Node keyinfonode = (org.w3c.dom.Node) encdatanode.appendChild(keyinfo);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(skles:DomainId)");
            Element domid = (Element) xencdoc.createElement("skles:DomainId");
            org.w3c.dom.Node didnode = (org.w3c.dom.Node) keyinfonode.appendChild(domid);
            org.w3c.dom.Text didtext = xencdoc.createTextNode(did);
            didnode.appendChild(didtext);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(ds:KeyName)");
            Element keyname = (Element) xencdoc.createElement("ds:KeyName");
            org.w3c.dom.Node keynamenode = (org.w3c.dom.Node) keyinfonode.appendChild(keyname);
            org.w3c.dom.Text keynametext = xencdoc.createTextNode(token);
            keynamenode.appendChild(keynametext);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(ds:RetrievalMethod)");
            Element retmethod = (Element) xencdoc.createElement("ds:RetrievalMethod");
            retmethod.setAttribute("URI", hosturl);
            org.w3c.dom.Node retmethodnode = (org.w3c.dom.Node) keyinfonode.appendChild(retmethod);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(xenc:CipherData)");
            Element cipherdata = (Element) xencdoc.createElement("xenc:CipherData");
            org.w3c.dom.Node cipherdatanode = (org.w3c.dom.Node) encdatanode.appendChild(cipherdata);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(xenc:CipherReference)");
            Element cipherref = (Element) xencdoc.createElement("xenc:CipherReference");
            cipherref.setAttribute("URI", infName + CIPHERTEXT_FILE_EXTENSION);
            org.w3c.dom.Node cipherrefnode = (org.w3c.dom.Node) cipherdatanode.appendChild(cipherref);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(xenc:EncryptionProperties)");
            Element encproperties = (Element) xencdoc.createElement("xenc:EncryptionProperties");
            org.w3c.dom.Node encpropertiesnode = (org.w3c.dom.Node) encdatanode.appendChild(encproperties);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(xenc:EncryptionProperty)");
            Element encproperty1 = (Element) xencdoc.createElement("xenc:EncryptionProperty");
            encproperty1.setAttribute("Id", "MessageDigest");
            org.w3c.dom.Node encpropertynode1 = (org.w3c.dom.Node) encpropertiesnode.appendChild(encproperty1);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(ds:DigestMethod)");
            Element digestmethod = (Element) xencdoc.createElement("ds:DigestMethod");
            digestmethod.setAttribute("Algorithm", DEFAULT_DIGEST);
            org.w3c.dom.Node digestmethodnode = (org.w3c.dom.Node) encproperty1.appendChild(digestmethod);
            Common.log(Level.FINE, "SKCE-MSG-5000", "createElement(ds:DigestValue)");
            Element digestvalue = (Element) xencdoc.createElement("ds:DigestValue");
            org.w3c.dom.Node digestvaluenode = (org.w3c.dom.Node) encproperty1.appendChild(digestvalue);
            org.w3c.dom.Text digvaltext = xencdoc.createTextNode(hash);
            digestvaluenode.appendChild(digvaltext);
            Common.log(Level.FINE, "SKCE-MSG-5000", "StreamResult(baos)");
            DOMSource source = new DOMSource(xencdoc);
            StreamResult result = new StreamResult(baos);
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            entry = new ZipEntry(infName + XMLENC_FILE_EXTENSION);
            zos.putNextEntry(entry);
            zos.write(baos.toByteArray());
            zos.close();
            Common.log(Level.INFO, "SKCE-MSG-4035", new String[] { Long.toString(of.length()), of.getName() });
            return of.getAbsolutePath();
        } catch (TransformerConfigurationException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (TransformerException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (ParserConfigurationException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (FileNotFoundException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (BadPaddingException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (IllegalBlockSizeException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (InvalidKeyException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (IOException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (NoSuchAlgorithmException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (NoSuchPaddingException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (StrongKeyLiteException_Exception ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } catch (GeneralSecurityException ex) {
            Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
            throw new SkceWSException(ex);
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException ex) {
                Common.log(Level.SEVERE, "SKCE-ERR-1000", ex.getLocalizedMessage());
                throw new SkceWSException(ex);
            }
        }
    }
