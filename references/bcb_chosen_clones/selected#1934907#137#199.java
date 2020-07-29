    private void compressElement(OutputStream os, Element element, boolean useHierarchy, String stripOffset) throws XPathException {
        if (!(element.getNodeName().equals("entry") || element.getNamespaceURI().length() > 0)) throw new XPathException(this, "Item must be type of xs:anyURI or element enry.");
        if (element.getChildNodes().getLength() > 1) throw new XPathException(this, "Entry content is not valid XML fragment.");
        String name = element.getAttribute("name");
        if (name == null) throw new XPathException(this, "Entry must have name attribute.");
        String type = element.getAttribute("type");
        if ("uri".equals(type)) {
            compressFromUri(os, XmldbURI.create(element.getFirstChild().getNodeValue()), useHierarchy, stripOffset, element.getAttribute("method"), name);
            return;
        }
        if (useHierarchy) {
            name = removeLeadingOffset(name, stripOffset);
        } else {
            name = name.substring(name.lastIndexOf("/") + 1);
        }
        if ("collection".equals(type)) name += "/";
        Object entry = null;
        try {
            entry = newEntry(name);
            if (!"collection".equals(type)) {
                byte[] value;
                CRC32 chksum = new CRC32();
                Node content = element.getFirstChild();
                if (content == null) {
                    value = new byte[0];
                } else {
                    if (content.getNodeType() == Node.TEXT_NODE) {
                        String text = content.getNodeValue();
                        Base64Decoder dec = new Base64Decoder();
                        if ("binary".equals(type)) {
                            dec.translate(text);
                            value = dec.getByteArray();
                        } else {
                            value = text.getBytes();
                        }
                    } else {
                        Serializer serializer = context.getBroker().getSerializer();
                        serializer.setUser(context.getUser());
                        serializer.setProperty("omit-xml-declaration", "no");
                        value = serializer.serialize((NodeValue) content).getBytes();
                    }
                }
                if (entry instanceof ZipEntry && "store".equals(element.getAttribute("method"))) {
                    ((ZipEntry) entry).setMethod(ZipOutputStream.STORED);
                    chksum.update(value);
                    ((ZipEntry) entry).setCrc(chksum.getValue());
                    ((ZipEntry) entry).setSize(value.length);
                }
                putEntry(os, entry);
                os.write(value);
            }
        } catch (IOException ioe) {
            throw new XPathException(this, ioe.getMessage(), ioe);
        } catch (SAXException saxe) {
            throw new XPathException(this, saxe.getMessage(), saxe);
        } finally {
            if (entry != null) try {
                closeEntry(os);
            } catch (IOException ioe) {
                throw new XPathException(this, ioe.getMessage(), ioe);
            }
        }
    }
