    public String uploadFile(String entryName, String entryDescription, String parent, String filePath) throws Exception {
        checkSession();
        Document doc = XmlUtil.makeDocument();
        Element root = XmlUtil.create(doc, TAG_ENTRIES, null, new String[] {});
        Element entryNode = XmlUtil.create(doc, TAG_ENTRY, root, new String[] {});
        entryNode.setAttribute(ATTR_NAME, entryName);
        Element descNode = XmlUtil.create(doc, TAG_DESCRIPTION, entryNode);
        descNode.appendChild(XmlUtil.makeCDataNode(doc, entryDescription, false));
        entryNode.setAttribute(ATTR_PARENT, parent);
        File file = new File(filePath);
        entryNode.setAttribute(ATTR_FILE, IOUtil.getFileTail(filePath));
        entryNode.setAttribute(ATTR_ADDMETADATA, "true");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);
        String xml = XmlUtil.toString(root);
        zos.putNextEntry(new ZipEntry("entries.xml"));
        byte[] bytes = xml.getBytes();
        zos.write(bytes, 0, bytes.length);
        zos.closeEntry();
        String file2string = file.toString();
        zos.putNextEntry(new ZipEntry(IOUtil.getFileTail(file2string)));
        bytes = IOUtil.readBytes(new FileInputStream(file));
        zos.write(bytes, 0, bytes.length);
        zos.closeEntry();
        zos.close();
        bos.close();
        List<HttpFormEntry> postEntries = new ArrayList<HttpFormEntry>();
        postEntries.add(HttpFormEntry.hidden(ARG_SESSIONID, getSessionId()));
        postEntries.add(HttpFormEntry.hidden(ARG_AUTHTOKEN, RepositoryUtil.hashPassword(getSessionId())));
        postEntries.add(HttpFormEntry.hidden(ARG_RESPONSE, RESPONSE_XML));
        postEntries.add(new HttpFormEntry(ARG_FILE, "entries.zip", bos.toByteArray()));
        RequestUrl URL_ENTRY_XMLCREATE = new RequestUrl(this, "/entry/xmlcreate");
        String[] result = doPost(URL_ENTRY_XMLCREATE, postEntries);
        if (result[0] != null) {
            throw new EntryErrorException(result[0]);
        }
        Element response = XmlUtil.getRoot(result[1]);
        if (!responseOk(response)) {
            String body = XmlUtil.getChildText(response);
            throw new EntryErrorException(body);
        }
        Element newEntryNode = XmlUtil.findChild(response, TAG_ENTRY);
        if (newEntryNode == null) {
            throw new IllegalStateException("No entry node found in:" + XmlUtil.toString(response));
        }
        return XmlUtil.getAttribute(newEntryNode, ATTR_ID);
    }
