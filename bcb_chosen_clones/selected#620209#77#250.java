    public static void reconstruct(final List files, final Map properties, final OutputStream fout, final String base_url, final String producer, final PageSize[] size, final List hf) throws CConvertException {
        OutputStream out = fout;
        OutputStream out2 = fout;
        boolean signed = false;
        OutputStream oldOut = null;
        File tmp = null;
        File tmp2 = null;
        try {
            tmp = File.createTempFile("yahp", "pdf");
            tmp2 = File.createTempFile("yahp", "pdf");
            oldOut = out;
            if ("true".equals(properties.get(IHtmlToPdfTransformer.USE_PDF_SIGNING))) {
                signed = true;
                out2 = new FileOutputStream(tmp2);
            } else {
                out2 = oldOut;
            }
            out = new FileOutputStream(tmp);
            com.lowagie.text.Document document = null;
            PdfCopy writer = null;
            boolean first = true;
            Map mapSizeDoc = new HashMap();
            int totalPage = 0;
            for (int i = 0; i < files.size(); i++) {
                final File fPDF = (File) files.get(i);
                final PdfReader reader = new PdfReader(fPDF.getAbsolutePath());
                reader.consolidateNamedDestinations();
                final int n = reader.getNumberOfPages();
                if (first) {
                    first = false;
                    document = new com.lowagie.text.Document(reader.getPageSizeWithRotation(1));
                    writer = new PdfCopy(document, out);
                    writer.setPdfVersion(PdfWriter.VERSION_1_3);
                    writer.setFullCompression();
                    if ("true".equals(properties.get(IHtmlToPdfTransformer.USE_PDF_ENCRYPTION))) {
                        final String password = (String) properties.get(IHtmlToPdfTransformer.PDF_ENCRYPTION_PASSWORD);
                        final int securityType = CDocumentReconstructor.getSecurityFlags(properties);
                        writer.setEncryption(PdfWriter.STRENGTH128BITS, password, null, securityType);
                    }
                    final String title = (String) properties.get(IHtmlToPdfTransformer.PDF_TITLE);
                    if (title != null) {
                        document.addTitle(title);
                    } else if (base_url != null) {
                        document.addTitle(base_url);
                    }
                    final String creator = (String) properties.get(IHtmlToPdfTransformer.PDF_CREATOR);
                    if (creator != null) {
                        document.addCreator(creator);
                    } else {
                        document.addCreator(IHtmlToPdfTransformer.VERSION);
                    }
                    final String author = (String) properties.get(IHtmlToPdfTransformer.PDF_AUTHOR);
                    if (author != null) {
                        document.addAuthor(author);
                    }
                    final String sproducer = (String) properties.get(IHtmlToPdfTransformer.PDF_PRODUCER);
                    if (sproducer != null) {
                        document.addProducer(sproducer);
                    } else {
                        document.addProducer(IHtmlToPdfTransformer.VERSION + " - http://www.allcolor.org/YaHPConverter/ - " + producer);
                    }
                    document.open();
                }
                PdfImportedPage page;
                for (int j = 0; j < n; ) {
                    ++j;
                    totalPage++;
                    mapSizeDoc.put("" + totalPage, "" + i);
                    page = writer.getImportedPage(reader, j);
                    writer.addPage(page);
                }
            }
            document.close();
            out.flush();
            out.close();
            {
                final PdfReader reader = new PdfReader(tmp.getAbsolutePath());
                ;
                final int n = reader.getNumberOfPages();
                final PdfStamper stp = new PdfStamper(reader, out2);
                int i = 0;
                BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                final CHtmlToPdfFlyingSaucerTransformer trans = new CHtmlToPdfFlyingSaucerTransformer();
                while (i < n) {
                    i++;
                    int indexSize = Integer.parseInt((String) mapSizeDoc.get("" + i));
                    final int[] dsize = size[indexSize].getSize();
                    final int[] dmargin = size[indexSize].getMargin();
                    for (final Iterator it = hf.iterator(); it.hasNext(); ) {
                        final CHeaderFooter chf = (CHeaderFooter) it.next();
                        if (chf.getSfor().equals(CHeaderFooter.ODD_PAGES) && (i % 2 == 0)) {
                            continue;
                        } else if (chf.getSfor().equals(CHeaderFooter.EVEN_PAGES) && (i % 2 != 0)) {
                            continue;
                        }
                        final String text = chf.getContent().replaceAll("<pagenumber>", "" + i).replaceAll("<pagecount>", "" + n);
                        final PdfContentByte over = stp.getOverContent(i);
                        final ByteArrayOutputStream bbout = new ByteArrayOutputStream();
                        if (chf.getType().equals(CHeaderFooter.HEADER)) {
                            trans.transform(new ByteArrayInputStream(text.getBytes("utf-8")), base_url, new PageSize(dsize[0] - (dmargin[0] + dmargin[1]), dmargin[3]), new ArrayList(), properties, bbout);
                        } else if (chf.getType().equals(CHeaderFooter.FOOTER)) {
                            trans.transform(new ByteArrayInputStream(text.getBytes("utf-8")), base_url, new PageSize(dsize[0] - (dmargin[0] + dmargin[1]), dmargin[2]), new ArrayList(), properties, bbout);
                        }
                        final PdfReader readerHF = new PdfReader(bbout.toByteArray());
                        if (chf.getType().equals(CHeaderFooter.HEADER)) {
                            over.addTemplate(stp.getImportedPage(readerHF, 1), dmargin[0], dsize[1] - dmargin[3]);
                        } else if (chf.getType().equals(CHeaderFooter.FOOTER)) {
                            over.addTemplate(stp.getImportedPage(readerHF, 1), dmargin[0], 0);
                        }
                        readerHF.close();
                    }
                }
                stp.close();
            }
            try {
                out2.flush();
            } catch (Exception ignore) {
            } finally {
                try {
                    out2.close();
                } catch (Exception ignore) {
                }
            }
            if (signed) {
                final String keypassword = (String) properties.get(IHtmlToPdfTransformer.PDF_SIGNING_PRIVATE_KEY_PASSWORD);
                final String password = (String) properties.get(IHtmlToPdfTransformer.PDF_ENCRYPTION_PASSWORD);
                final String keyStorepassword = (String) properties.get(IHtmlToPdfTransformer.PDF_SIGNING_KEYSTORE_PASSWORD);
                final String privateKeyFile = (String) properties.get(IHtmlToPdfTransformer.PDF_SIGNING_PRIVATE_KEY_FILE);
                final String reason = (String) properties.get(IHtmlToPdfTransformer.PDF_SIGNING_REASON);
                final String location = (String) properties.get(IHtmlToPdfTransformer.PDF_SIGNING_LOCATION);
                final boolean selfSigned = !"false".equals(properties.get(IHtmlToPdfTransformer.USE_PDF_SELF_SIGNING));
                PdfReader reader = null;
                if (password != null) {
                    reader = new PdfReader(tmp2.getAbsolutePath(), password.getBytes());
                } else {
                    reader = new PdfReader(tmp2.getAbsolutePath());
                }
                final KeyStore ks = selfSigned ? KeyStore.getInstance(KeyStore.getDefaultType()) : KeyStore.getInstance("pkcs12");
                ks.load(new FileInputStream(privateKeyFile), keyStorepassword.toCharArray());
                final String alias = (String) ks.aliases().nextElement();
                final PrivateKey key = (PrivateKey) ks.getKey(alias, keypassword.toCharArray());
                final Certificate chain[] = ks.getCertificateChain(alias);
                final PdfStamper stp = PdfStamper.createSignature(reader, oldOut, '\0');
                if ("true".equals(properties.get(IHtmlToPdfTransformer.USE_PDF_ENCRYPTION))) {
                    stp.setEncryption(PdfWriter.STRENGTH128BITS, password, null, CDocumentReconstructor.getSecurityFlags(properties));
                }
                final PdfSignatureAppearance sap = stp.getSignatureAppearance();
                if (selfSigned) {
                    sap.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);
                } else {
                    sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
                }
                if (reason != null) {
                    sap.setReason(reason);
                }
                if (location != null) {
                    sap.setLocation(location);
                }
                stp.close();
                oldOut.flush();
            }
        } catch (final Exception e) {
            throw new CConvertException("ERROR: An Exception occured while reconstructing the pdf document: " + e.getMessage(), e);
        } finally {
            try {
                tmp.delete();
            } catch (final Exception ignore) {
            }
            try {
                tmp2.delete();
            } catch (final Exception ignore) {
            }
        }
    }
