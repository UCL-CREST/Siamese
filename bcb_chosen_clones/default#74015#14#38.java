    public static void main(String args[]) {
        int port = 9000;
        String msg = null;
        try {
            DatagramSocket dsocket = new DatagramSocket(port);
            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            dsocket.receive(packet);
            msg = new String(buffer, 0, packet.getLength());
            dsocket.close();
        } catch (Exception e) {
        }
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(msg));
            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("msg");
            Element element = (Element) nodes.item(0);
            Node node = element.getFirstChild();
            msg = node.getNodeValue();
        } catch (Exception e) {
        }
        System.out.println("Receiver : I got a message ==> " + msg + "\n");
    }
