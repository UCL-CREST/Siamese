    private ZipOutputStream copyOOXMLContent(String signatureZipEntryName, OutputStream signedOOXMLOutputStream) throws IOException, ParserConfigurationException, SAXException, TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(signedOOXMLOutputStream);
        ZipInputStream zipInputStream = new ZipInputStream(this.getOfficeOpenXMLDocumentURL().openStream());
        ZipEntry zipEntry;
        boolean hasOriginSigsRels = false;
        while (null != (zipEntry = zipInputStream.getNextEntry())) {
            LOG.debug("copy ZIP entry: " + zipEntry.getName());
            ZipEntry newZipEntry = new ZipEntry(zipEntry.getName());
            zipOutputStream.putNextEntry(newZipEntry);
            if ("[Content_Types].xml".equals(zipEntry.getName())) {
                Document contentTypesDocument = loadDocumentNoClose(zipInputStream);
                Element typesElement = contentTypesDocument.getDocumentElement();
                Element overrideElement = contentTypesDocument.createElementNS("http://schemas.openxmlformats.org/package/2006/content-types", "Override");
                overrideElement.setAttribute("PartName", "/" + signatureZipEntryName);
                overrideElement.setAttribute("ContentType", "application/vnd.openxmlformats-package.digital-signature-xmlsignature+xml");
                typesElement.appendChild(overrideElement);
                Element nsElement = contentTypesDocument.createElement("ns");
                nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:tns", "http://schemas.openxmlformats.org/package/2006/content-types");
                NodeList nodeList = XPathAPI.selectNodeList(contentTypesDocument, "/tns:Types/tns:Default[@Extension='sigs']", nsElement);
                if (0 == nodeList.getLength()) {
                    Element defaultElement = contentTypesDocument.createElementNS("http://schemas.openxmlformats.org/package/2006/content-types", "Default");
                    defaultElement.setAttribute("Extension", "sigs");
                    defaultElement.setAttribute("ContentType", "application/vnd.openxmlformats-package.digital-signature-origin");
                    typesElement.appendChild(defaultElement);
                }
                writeDocumentNoClosing(contentTypesDocument, zipOutputStream, false);
            } else if ("_rels/.rels".equals(zipEntry.getName())) {
                Document relsDocument = loadDocumentNoClose(zipInputStream);
                Element nsElement = relsDocument.createElement("ns");
                nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:tns", "http://schemas.openxmlformats.org/package/2006/relationships");
                NodeList nodeList = XPathAPI.selectNodeList(relsDocument, "/tns:Relationships/tns:Relationship[@Target='_xmlsignatures/origin.sigs']", nsElement);
                if (0 == nodeList.getLength()) {
                    Element relationshipElement = relsDocument.createElementNS("http://schemas.openxmlformats.org/package/2006/relationships", "Relationship");
                    relationshipElement.setAttribute("Id", "rel-id-" + UUID.randomUUID().toString());
                    relationshipElement.setAttribute("Type", "http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/origin");
                    relationshipElement.setAttribute("Target", "_xmlsignatures/origin.sigs");
                    relsDocument.getDocumentElement().appendChild(relationshipElement);
                }
                writeDocumentNoClosing(relsDocument, zipOutputStream, false);
            } else if ("_xmlsignatures/_rels/origin.sigs.rels".equals(zipEntry.getName())) {
                hasOriginSigsRels = true;
                Document originSignRelsDocument = loadDocumentNoClose(zipInputStream);
                Element relationshipElement = originSignRelsDocument.createElementNS("http://schemas.openxmlformats.org/package/2006/relationships", "Relationship");
                String relationshipId = "rel-" + UUID.randomUUID().toString();
                relationshipElement.setAttribute("Id", relationshipId);
                relationshipElement.setAttribute("Type", "http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/signature");
                String target = FilenameUtils.getName(signatureZipEntryName);
                LOG.debug("target: " + target);
                relationshipElement.setAttribute("Target", target);
                originSignRelsDocument.getDocumentElement().appendChild(relationshipElement);
                writeDocumentNoClosing(originSignRelsDocument, zipOutputStream, false);
            } else {
                IOUtils.copy(zipInputStream, zipOutputStream);
            }
        }
        if (false == hasOriginSigsRels) {
            addOriginSigsRels(signatureZipEntryName, zipOutputStream);
            addOriginSigs(zipOutputStream);
        }
        zipInputStream.close();
        return zipOutputStream;
    }
