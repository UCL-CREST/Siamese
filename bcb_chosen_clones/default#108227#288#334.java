    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Usage: cmd filename");
            System.exit(1);
        }
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(argv[0]));
            doc.getDocumentElement().normalize();
            String sTmp = doc.getDocumentElement().toString();
            String attr = (String) doc.getDocumentElement().getNodeName();
            System.out.println("Root element of the doc is " + attr);
            FileParsing f = new FileParsing();
            f.setDoc(doc);
            f.changeOrAddAttr("author", "Phoebe");
            f.replaceNode("title", "title", "Kinbe");
            f.insertNewNode("title", "Phoebe", "Yeah!!");
            f.setOrAddDOMAttr("slide", "type", "Good!!");
            f.setOrAddDOMAttr("slide", "Kinbe", "Wahaha!!");
            f.listDOMAttr("slide");
            f.addCommentBefore("title", "I O U");
            f.addCommentBehind("slide", "MeiMei");
            f.removeAll(doc, Node.ELEMENT_NODE, "item");
            f.removeOneNode("Lee");
            f.splitTextNode("Jingle", "orange");
            f.mergeTextNode("drink");
            f.addPI("slide", "Kenbe", "Good");
            f.addCDATA("Angus", "CD");
            File outputFile = new File(argv[0]);
            FileWriter fo = new FileWriter(outputFile);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc.getDocumentElement());
            StreamResult result = new StreamResult(fo);
            transformer.transform(source, result);
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println("   " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(0);
    }
