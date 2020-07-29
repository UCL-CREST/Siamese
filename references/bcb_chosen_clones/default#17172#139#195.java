    public static ArrayList getNodes(SolarNodeSettings theSettings, String mode) {
        String inputLine = "";
        String[] finalString = new String[2];
        finalString[0] = "ok";
        finalString[1] = "";
        ArrayList nodeSet = new ArrayList();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("http://www.solarnetwork.net/getNodes3.php");
            if (theSettings.debug) {
            }
            doc.getDocumentElement().normalize();
            if (theSettings.debug) {
                System.out.println("Root element of the doc should be nodeInfo: " + doc.getDocumentElement().getNodeName());
            }
            NodeList nodesList = doc.getElementsByTagName("nodes");
            if (theSettings.debug) {
                System.out.println("nodesList getLength:" + nodesList.getLength() + "\n");
            }
            Node nodesNode = nodesList.item(0);
            if (theSettings.debug) {
            }
            if (nodesNode.getNodeType() == Node.ELEMENT_NODE) {
                Element nodesElement = (Element) nodesNode;
                NodeList solarNodeList = nodesElement.getElementsByTagName("node");
                if (theSettings.debug) {
                    System.out.println("solarNodeList getLength:" + solarNodeList.getLength() + "\n");
                }
                int i = 0;
                for (i = 0; i < solarNodeList.getLength(); i++) {
                    SolarNode thisNode = new SolarNode();
                    Element thisNodeElement = (Element) solarNodeList.item(i);
                    NodeList thisNodeIdList = thisNodeElement.getElementsByTagName("id");
                    Element thisNodeIdElement = (Element) thisNodeIdList.item(0);
                    NodeList thisNodeIdTextList = thisNodeIdElement.getChildNodes();
                    thisNode.id = Integer.parseInt(((Node) thisNodeIdTextList.item(0)).getNodeValue().toLowerCase().trim());
                    NodeList thisNodeWcIdentifierList = thisNodeElement.getElementsByTagName("wcIdentifier");
                    Element thisNodeWcIdentifierElement = (Element) thisNodeWcIdentifierList.item(0);
                    NodeList thisNodeWcIdentifierTextList = thisNodeWcIdentifierElement.getChildNodes();
                    thisNode.weatherDotComLocationIdentifier = ((Node) thisNodeWcIdentifierTextList.item(0)).getNodeValue().toLowerCase().trim();
                    nodeSet.add(thisNode);
                }
                if (theSettings.debug) {
                }
            }
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return nodeSet;
    }
