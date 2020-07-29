    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse("pcb1.xml");
        Element docEle = doc.getDocumentElement();
        System.out.println("\nProcessing componentlist.");
        NodeList nl = docEle.getElementsByTagName("componentlist");
        Node CompList = nl.item(0);
        System.out.println(CompList.getLocalName());
        NodeList CompNodeList = ((Element) CompList).getElementsByTagName("component");
        System.out.println("number of components = " + CompNodeList.getLength());
        if (CompNodeList == null) {
            System.out.println("Could not find any components in componentlist!");
        } else {
            for (int i = 0; i < CompNodeList.getLength(); i++) {
                Node CompNode = CompNodeList.item(i);
                Node CompnameNode = (((Element) CompNode).getElementsByTagName("componentname")).item(0);
                System.out.println("\t" + CompNode.getLocalName() + " = " + CompnameNode.getTextContent());
                NodeList CompPinList = ((Element) CompNode).getElementsByTagName("pin");
                System.out.println("\tnumber of pins = " + CompPinList.getLength());
                for (int j = 0; j < CompPinList.getLength(); j++) {
                    Node PinNode = CompPinList.item(j);
                    Node PinnameNode = (((Element) PinNode).getElementsByTagName("pinname")).item(0);
                    Node PinnumberNode = (((Element) PinNode).getElementsByTagName("pinnumber")).item(0);
                    System.out.println("\t\t" + PinNode.getLocalName() + " = " + PinnameNode.getTextContent() + "," + PinnumberNode.getTextContent());
                }
            }
        }
        System.out.println("\nProcessing instancelist.");
        nl = docEle.getElementsByTagName("instancelist");
        Node InstList = nl.item(0);
        System.out.println(InstList.getLocalName());
        NodeList InstNodeList = ((Element) InstList).getElementsByTagName("instance");
        System.out.println("number of instances = " + InstNodeList.getLength());
        if (InstNodeList == null) {
            System.out.println("Could not find any instance nodes in 'instancelist'!");
        } else {
            for (int i = 0; i < InstNodeList.getLength(); i++) {
                Node InstNode = InstNodeList.item(i);
                Node InstnameNode = (((Element) InstNode).getElementsByTagName("instancename")).item(0);
                Node InstCompnameNode = (((Element) InstNode).getElementsByTagName("componentname")).item(0);
                Node RefdesNode = (((Element) InstNode).getElementsByTagName("refdes")).item(0);
                System.out.println("\t" + InstNode.getLocalName() + " " + InstnameNode.getTextContent() + " = " + InstCompnameNode.getTextContent() + "," + RefdesNode.getTextContent());
            }
        }
        System.out.println("\nProcessing netlist.");
        nl = docEle.getElementsByTagName("netlist");
        Node NetList = nl.item(0);
        System.out.println(NetList.getLocalName());
        NodeList NetNodeList = ((Element) NetList).getElementsByTagName("net");
        System.out.println("number of nets = " + NetNodeList.getLength());
        if (NetNodeList == null) {
            System.out.println("Could not find any net nodes in 'netlist'!");
        } else {
            for (int i = 0; i < NetNodeList.getLength(); i++) {
                Node NetNode = NetNodeList.item(i);
                Node NetnameNode = (((Element) NetNode).getElementsByTagName("netname")).item(0);
                Node NetconnectionNode = (((Element) NetNode).getElementsByTagName("netconnection")).item(0);
                System.out.println("\t" + NetNode.getLocalName() + " " + NetnameNode.getTextContent() + " = " + NetconnectionNode.getTextContent());
            }
        }
    }
