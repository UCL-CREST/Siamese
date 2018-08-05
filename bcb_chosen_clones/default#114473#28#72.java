    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Argument required: " + "-Dxml-file=<filename>");
            System.exit(1);
        }
        DOMExample de = new DOMExample();
        document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(args[0]));
        } catch (SAXParseException spe) {
            System.out.println("\n** Parsing error" + ", line " + spe.getLineNumber() + ", uri " + spe.getSystemId());
            System.out.println("   " + spe.getMessage());
            Exception x = spe;
            if (spe.getException() != null) {
                x = spe.getException();
            }
            x.printStackTrace();
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
            }
            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage message = messageFactory.createMessage();
            SOAPHeader header = message.getSOAPHeader();
            header.detachNode();
            SOAPBody body = message.getSOAPBody();
            SOAPBodyElement docElement = body.addDocument(document);
            message.saveChanges();
            Iterator iter1 = body.getChildElements();
            de.getContents(iter1, "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
