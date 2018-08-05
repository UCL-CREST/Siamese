    public void generateAndSaveXES() {
        if (baseNode == null) {
            EerrorMsg.text.setText("xes.source not loaded ");
            jtp.setSelectedIndex(TAB_RIGHT_ERRORS);
            return;
        }
        try {
            org.w3c.dom.Document doc = generateDomTree();
            if (doc == null) {
                System.out.println("no program loaded ");
                return;
            }
            String xsltStr = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" + "<xsl:output method=\"xml\" indent=\"yes\"/>" + "<xsl:template match=\"node() | @*\">" + "<xsl:copy>" + "<xsl:apply-templates select=\"@* | node()\"/>" + "</xsl:copy>" + "</xsl:template>" + "</xsl:stylesheet>";
            byte buf[] = xsltStr.getBytes();
            ByteArrayInputStream is = new ByteArrayInputStream(buf);
            TransformerFactory tfactory = TransformerFactory.newInstance();
            Transformer xform = tfactory.newTransformer(new StreamSource(is));
            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(EncoderLoad.directory);
            int retValue = jfc.showSaveDialog(this);
            File outFile;
            if (retValue == JFileChooser.APPROVE_OPTION) {
                ((EncoderLoad) (ELoad)).setBaseDir(jfc.getCurrentDirectory());
                outFile = jfc.getSelectedFile();
                org.w3c.dom.Node root = doc.getFirstChild();
                DOMSource ds = new DOMSource(root);
                xform.transform(ds, new StreamResult(outFile));
            }
        } catch (Exception e) {
            System.out.println("\nxes.encodingMethod " + e);
        }
    }
