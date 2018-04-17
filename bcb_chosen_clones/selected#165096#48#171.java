    public static void main(String[] args) {
        System.out.println("Chapter 16: example SignedPdf");
        System.out.println("-> Creates a signed PDF;");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> files generated in /results subdirectory:");
        System.out.println("   Resource needed: .keystore");
        System.out.println("-> Resulting PDFs: unsigned_message.pdf, signed_message.pdf,");
        System.out.println("   corrupted_message.pdf, signed_message_invisible.pdf,");
        System.out.println("   double_signed_message.pdf, revision_1.pdf and revision_2.pdf");
        createPdf();
        PdfReader reader;
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream("resources/in_action/chapter16/.keystore"), "f00b4r".toCharArray());
            PrivateKey key = (PrivateKey) ks.getKey("foobar", "r4b00f".toCharArray());
            Certificate[] chain = ks.getCertificateChain("foobar");
            reader = new PdfReader("results/in_action/chapter16/unsigned_message.pdf");
            FileOutputStream os = new FileOutputStream("results/in_action/chapter16/signed_message.pdf");
            PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);
            appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_FORM_FILLING);
            appearance.setReason("It's personal.");
            appearance.setLocation("Foobar");
            appearance.setVisibleSignature(new Rectangle(30, 750, 500, 565), 1, null);
            stamper.close();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {
            reader = new PdfReader("results/in_action/chapter16/signed_message.pdf");
            Document document = new Document(reader.getPageSizeWithRotation(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream("results/in_action/chapter16/corrupted_message.pdf"));
            document.open();
            copy.addPage(copy.getImportedPage(reader, 1));
            document.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (DocumentException de) {
            de.printStackTrace();
        }
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream("resources/in_action/chapter16/.keystore"), "f00b4r".toCharArray());
            PrivateKey key = (PrivateKey) ks.getKey("foobar", "r4b00f".toCharArray());
            Certificate[] chain = ks.getCertificateChain("foobar");
            reader = new PdfReader("results/in_action/chapter16/unsigned_message.pdf");
            FileOutputStream os = new FileOutputStream("results/in_action/chapter16/signed_message_invisible.pdf");
            PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);
            appearance.setReason("It's personal.");
            appearance.setLocation("Foobar");
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reader = new PdfReader("results/in_action/chapter16/signed_message.pdf");
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream("resources/in_action/chapter16/.keystore"), "f00b4r".toCharArray());
            PrivateKey key = (PrivateKey) ks.getKey("foobar", "r4b00f".toCharArray());
            Certificate[] chain = ks.getCertificateChain("foobar");
            FileOutputStream os = new FileOutputStream("results/in_action/chapter16/double_signed_message.pdf");
            PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);
            appearance.setReason("Double signed.");
            appearance.setLocation("Foobar");
            appearance.setVisibleSignature(new Rectangle(300, 750, 500, 800), 1, "secondsig");
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            Collection col = cf.generateCertificates(new FileInputStream("resources/in_action/chapter16/foobar.cer"));
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            for (Iterator it = col.iterator(); it.hasNext(); ) {
                X509Certificate cert = (X509Certificate) it.next();
                System.out.println(cert.getIssuerDN().getName());
                ks.setCertificateEntry(cert.getSerialNumber().toString(Character.MAX_RADIX), cert);
            }
            reader = new PdfReader("results/in_action/chapter16/double_signed_message.pdf");
            AcroFields af = reader.getAcroFields();
            ArrayList names = af.getSignatureNames();
            String name;
            for (Iterator it = names.iterator(); it.hasNext(); ) {
                name = (String) it.next();
                System.out.println("Signature name: " + name);
                System.out.println("Signature covers whole document: " + af.signatureCoversWholeDocument(name));
                System.out.println("Document revision: " + af.getRevision(name) + " of " + af.getTotalRevisions());
                FileOutputStream os = new FileOutputStream("results/in_action/chapter16/revision_" + af.getRevision(name) + ".pdf");
                byte bb[] = new byte[8192];
                InputStream ip = af.extractRevision(name);
                int n = 0;
                while ((n = ip.read(bb)) > 0) os.write(bb, 0, n);
                os.close();
                ip.close();
                PdfPKCS7 pk = af.verifySignature(name);
                Calendar cal = pk.getSignDate();
                Certificate pkc[] = pk.getCertificates();
                System.out.println("Subject: " + PdfPKCS7.getSubjectFields(pk.getSigningCertificate()));
                System.out.println("Document modified: " + !pk.verify());
                Object fails[] = PdfPKCS7.verifyCertificates(pkc, ks, null, cal);
                if (fails == null) System.out.println("Certificates verified against the KeyStore"); else System.out.println("Certificate failed: " + fails[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
