    public static Gene[] getGenesFromElement(Configuration a_activeConfiguration, Element a_xmlElement) throws ImproperXMLException, UnsupportedRepresentationException, GeneCreationException {
        if (a_xmlElement == null || !(a_xmlElement.getTagName().equals(GENES_TAG))) {
            throw new ImproperXMLException("Unable to build Chromosome instance from XML Element: " + "given Element is not a 'genes' element.");
        }
        List genes = Collections.synchronizedList(new ArrayList());
        NodeList geneElements = a_xmlElement.getElementsByTagName(GENE_TAG);
        if (geneElements == null) {
            throw new ImproperXMLException("Unable to build Gene instances from XML Element: " + "'" + GENE_TAG + "'" + " sub-elements not found.");
        }
        int numberOfGeneNodes = geneElements.getLength();
        for (int i = 0; i < numberOfGeneNodes; i++) {
            Element thisGeneElement = (Element) geneElements.item(i);
            thisGeneElement.normalize();
            String geneClassName = thisGeneElement.getAttribute(CLASS_ATTRIBUTE);
            Gene thisGeneObject;
            Class geneClass = null;
            try {
                geneClass = Class.forName(geneClassName);
                try {
                    Constructor constr = geneClass.getConstructor(new Class[] { Configuration.class });
                    thisGeneObject = (Gene) constr.newInstance(new Object[] { a_activeConfiguration });
                } catch (NoSuchMethodException nsme) {
                    Constructor constr = geneClass.getConstructor(new Class[] {});
                    thisGeneObject = (Gene) constr.newInstance(new Object[] {});
                    thisGeneObject = (Gene) PrivateAccessor.invoke(thisGeneObject, "newGeneInternal", new Class[] {}, new Object[] {});
                }
            } catch (Throwable e) {
                throw new GeneCreationException(geneClass, e);
            }
            NodeList children = thisGeneElement.getChildNodes();
            int childrenSize = children.getLength();
            String alleleRepresentation = null;
            for (int j = 0; j < childrenSize; j++) {
                Element alleleElem = (Element) children.item(j);
                if (alleleElem.getTagName().equals(ALLELE_TAG)) {
                    alleleRepresentation = alleleElem.getAttribute("value");
                }
                if (children.item(j).getNodeType() == Node.TEXT_NODE) {
                    alleleRepresentation = children.item(j).getNodeValue();
                    break;
                }
            }
            if (alleleRepresentation == null) {
                throw new ImproperXMLException("Unable to build Gene instance from XML Element: " + "value (allele) is missing representation.");
            }
            try {
                thisGeneObject.setValueFromPersistentRepresentation(alleleRepresentation);
            } catch (UnsupportedOperationException e) {
                throw new GeneCreationException("Unable to build Gene because it does not support the " + "setValueFromPersistentRepresentation() method.");
            }
            genes.add(thisGeneObject);
        }
        return (Gene[]) genes.toArray(new Gene[genes.size()]);
    }
