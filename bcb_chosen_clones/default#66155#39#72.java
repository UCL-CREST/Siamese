    public void doMain(String[] args) throws Exception {
        filename = args[0];
        xpath = args[1];
        if ((filename != null) && (filename.length() > 0) && (xpath != null) && (xpath.length() > 0)) {
            System.out.println("Loading classes, parsing " + filename + ", and setting up serializer");
            InputSource in = new InputSource(new FileInputStream(filename));
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
            dfactory.setNamespaceAware(true);
            Document doc = dfactory.newDocumentBuilder().parse(in);
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            System.out.println("Querying DOM using " + xpath);
            XPathEvaluator evaluator = new XPathEvaluatorImpl(doc);
            XPathNSResolver resolver = evaluator.createNSResolver(doc);
            XPathResult result = (XPathResult) evaluator.evaluate(xpath, doc, resolver, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null);
            System.out.println("<output>");
            Node n;
            while ((n = result.iterateNext()) != null) {
                if (isTextNode(n)) {
                    StringBuffer sb = new StringBuffer(n.getNodeValue());
                    for (Node nn = n.getNextSibling(); isTextNode(nn); nn = nn.getNextSibling()) {
                        sb.append(nn.getNodeValue());
                    }
                    System.out.print(sb);
                } else {
                    serializer.transform(new DOMSource(n), new StreamResult(new OutputStreamWriter(System.out)));
                }
                System.out.println();
            }
            System.out.println("</output>");
        } else {
            System.out.println("Bad input args: " + filename + ", " + xpath);
        }
    }
