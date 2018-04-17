    private void init() {
        excludedClasses = new ArrayList();
        if (useDefaultExcludes) {
            excludedClasses.add("java.");
            excludedClasses.add("javax.");
            excludedClasses.add("org.");
            excludedClasses.add("sun.");
        }
        excludedClasses.add("[L");
        excludedClasses.add("[B");
        excludedClasses.add("[I");
        excludedClasses.add("[C");
        excludedClasses.add("void");
        excludedClasses.add("int");
        excludedClasses.add("String");
        excludedClasses.add("boolean");
        excludedClasses.add("double");
        excludedClasses.add("long");
        excludedClasses.add("byte");
        excludedClasses.add("char");
        excludedClasses.add("float");
        excludedClasses.add("short");
        DocumentBuilderFactory dbf;
        DocumentBuilder db = null;
        if (useExcludeFile) {
            try {
                dbf = DocumentBuilderFactory.newInstance();
                db = dbf.newDocumentBuilder();
                xmlExcDoc = db.parse(new File(excludeFileName));
            } catch (IOException fnfe) {
                System.err.println("Cannot open exclude file.");
                System.exit(-1);
            } catch (SAXException se) {
                se.printStackTrace();
                System.exit(-1);
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
                System.exit(-1);
            }
            if (xmlExcDoc != null) {
                Element rootNode = xmlExcDoc.getDocumentElement();
                NodeList nodeList = rootNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node tmpNode = (Node) nodeList.item(i);
                    if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element tmpElement = (Element) tmpNode;
                        String nodeName = tmpElement.getNodeName();
                        if (nodeName.equals(EXCLUDE_NODE)) {
                            String exValue = tmpElement.getAttribute("class");
                            if (exValue.endsWith("*")) exValue = exValue.substring(0, exValue.length() - 1);
                            excludedClasses.add(exValue);
                        }
                    }
                }
            }
            dbf = null;
            db = null;
            xmlExcDoc = null;
        }
        if (useDependenceFile) {
            try {
                dbf = DocumentBuilderFactory.newInstance();
                db = dbf.newDocumentBuilder();
                xmlDepDoc = db.parse(new File(dependenceFileName));
                rootElement = xmlDepDoc.getDocumentElement();
            } catch (FileNotFoundException fnfe) {
                if (!updateDependencyFile) {
                    System.err.println("Cannot locate dependency file.");
                    System.exit(-1);
                } else {
                    xmlDepDoc = db.newDocument();
                    rootElement = xmlDepDoc.createElement("dependency");
                    xmlDepDoc.appendChild(rootElement);
                }
            } catch (IOException ioe) {
                System.err.println("Error openning dependency file.");
                System.exit(-1);
            } catch (SAXException se) {
                se.printStackTrace();
                System.exit(-1);
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
                System.exit(-1);
            }
        }
    }
