    public boolean marshallRelationshipPart(PackageRelationshipCollection rels, URI relPartURI, ZipOutputStream zos) {
        if (logger.isDebugEnabled()) {
            logger.debug("writing relation:" + relPartURI);
        }
        Document xmlOutDoc = DocumentFactory.getInstance().createDocument();
        Namespace dfNs = Namespace.get("", PackageRelationship.RELATIONSHIPS_NAMESPACE);
        Element root = xmlOutDoc.addElement(new QName(PackageRelationship.RELATIONSHIPS_TAG_NAME, dfNs));
        for (PackageRelationship rel : rels) {
            Element relElem = root.addElement(PackageRelationship.RELATIONSHIP_TAG_NAME);
            relElem.addAttribute(PackageRelationship.ID_ATTRIBUTE_NAME, rel.getId());
            relElem.addAttribute(PackageRelationship.TYPE_ATTRIBUTE_NAME, rel.getRelationshipType());
            String targetValue;
            URI uri = rel.getTargetUri();
            if (rel.getTargetMode() == TargetMode.EXTERNAL) {
                targetValue = uri.getScheme() + "://" + uri.getPath();
                relElem.addAttribute(PackageRelationship.TARGET_MODE_ATTRIBUTE_NAME, "External");
            } else {
                targetValue = uri.getPath();
            }
            relElem.addAttribute(PackageRelationship.TARGET_ATTRIBUTE_NAME, targetValue);
        }
        xmlOutDoc.normalize();
        ZipEntry ctEntry = new ZipEntry(relPartURI.getPath());
        try {
            zos.putNextEntry(ctEntry);
            if (!Package.saveAsXmlInZip(xmlOutDoc, relPartURI.getPath(), zos)) {
                return false;
            }
            zos.closeEntry();
        } catch (IOException e1) {
            logger.error("cannot create file " + relPartURI, e1);
            return false;
        }
        return true;
    }
