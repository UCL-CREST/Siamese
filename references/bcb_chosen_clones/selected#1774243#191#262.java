    String getOutputPage(String action, String XML, String xslFileName, InputStream pageS, HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException, TransformerException {
        String sPage = null;
        Transformer transformer = null;
        String dig = null;
        CharArrayWriter page = new CharArrayWriter();
        if (this.nCachedPages > 0) {
            java.security.MessageDigest mess = java.security.MessageDigest.getInstance("SHA1");
            mess.update(XML.getBytes());
            mess.update(Long.toString(new File(basePath + xslFileName).lastModified()).getBytes());
            dig = new String(mess.digest());
            synchronized (pages) {
                if (pages.containsKey(dig)) {
                    sPage = pages.get(dig);
                }
            }
        }
        if (sPage == null && xslFileName.length() > 4) {
            try {
                long modifyTime = new File(basePath + xslFileName).lastModified();
                String path = basePath.replaceAll("\\\\", "/") + xslFileName;
                path = "file:///" + path;
                boolean add2cache = false;
                if (this.nCachedTransformers > 0) {
                    String cacheKey = action + xslFileName + modifyTime;
                    if (this.transformers.containsKey(cacheKey)) {
                        transformer = this.transformers.get(cacheKey);
                        synchronized (transformer) {
                            transformer.transform(new StreamSource(new ByteArrayInputStream(XML.getBytes("UTF-8"))), new StreamResult(page));
                        }
                    } else {
                        add2cache = true;
                    }
                }
                if (transformer == null) {
                    transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(path));
                    transformer.transform(new StreamSource(new ByteArrayInputStream(XML.getBytes("UTF-8"))), new StreamResult(page));
                }
                sPage = page.toString();
                sPage = sPage.replaceAll("&lt;", "<");
                sPage = sPage.replaceAll("&gt;", ">");
                sPage = replaceLinks(sPage, request);
                if (this.nCachedPages > 0) {
                    synchronized (pages) {
                        pages.put(dig, sPage);
                        if (pages.size() > nCachedPages) {
                            Iterator<String> i = pages.values().iterator();
                            i.next();
                            i.remove();
                        }
                    }
                }
                if (add2cache) {
                    synchronized (this.transformers) {
                        this.transformers.put(action + xslFileName + modifyTime, transformer);
                        if (this.transformers.size() > this.nCachedTransformers) {
                            Iterator<Transformer> it = this.transformers.values().iterator();
                            it.next();
                            it.remove();
                        }
                    }
                }
            } catch (TransformerException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "---------------------------------------------");
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ("XSL file: " + xslFileName));
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, XML);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "---------------------------------------------");
                throw ex;
            }
        }
        return sPage;
    }
