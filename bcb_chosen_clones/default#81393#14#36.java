    public static void main(String args[]) {
        try {
            int port = 8684;
            DatagramSocket dsocket = new DatagramSocket(port);
            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true) {
                dsocket.receive(packet);
                String msg = new String(buffer, 0, packet.getLength());
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(msg));
                Document doc = db.parse(is);
                NodeList nodes = doc.getElementsByTagName("msg");
                Element element = (Element) nodes.item(0);
                Node node = element.getFirstChild();
                msg = node.getNodeValue();
                System.out.println("Receiver : I got a message ==> " + msg + "\n");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
