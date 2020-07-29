	public static Document parse(File xmlFile) throws ParserConfigurationException, SAXException, IOException {	
		// Create DocumentBuilderFactory
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		// Configure Factory (this step will vary largely)
		docFactory.setValidating(false);
		docFactory.setNamespaceAware(true);
		docFactory.setExpandEntityReferences(false);
		
		// Use Factory to Build Document Builder (i.e., configured XML parser)
		DocumentBuilder parser = docFactory.newDocumentBuilder();
		
		//Parse the Document
		Document document = parser.parse(xmlFile);
		
		//Return the Document
		return document;
	}
