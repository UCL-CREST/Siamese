    public void addAI(String aiFile) throws Exception {
        Player computerPlayer;
        Player peoplePlayer;
        if (this.computer.equals("blue")) {
            computerPlayer = this.blue;
            peoplePlayer = this.red;
        } else {
            computerPlayer = this.red;
            peoplePlayer = this.blue;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.err.println(pce);
            System.exit(1);
        }
        Document doc = null;
        try {
            doc = db.parse(aiFile);
        } catch (DOMException dom) {
            System.err.println(dom.getMessage());
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(1);
        }
        Element root = doc.getDocumentElement();
        this.resetMax = Integer.parseInt(root.getAttribute("max"));
        this.ai.max = this.resetMax + "";
        this.resetMin = Integer.parseInt(root.getAttribute("min"));
        this.ai.min = this.resetMin + "";
        Chessman temp;
        String chessmanClass;
        Element people = (Element) (root.getElementsByTagName("people").item(0));
        NodeList pchessmen = people.getElementsByTagName("chessman");
        for (int j = 0; j < pchessmen.getLength(); ++j) {
            Element pchessman = (Element) pchessmen.item(j);
            chessmanClass = pchessman.getAttribute("name");
            this.ai.people[j].name = chessmanClass;
            this.ai.people[j].underAttack = pchessman.getAttribute("underAttack");
            this.ai.people[j].self = pchessman.getAttribute("self");
            this.ai.people[j].canGoPosition = pchessman.getAttribute("canGoPosition");
            this.ai.people[j].protectedV = pchessman.getAttribute("protected");
            NodeList cpPosition = pchessman.getElementsByTagName("goodPosition");
            for (int pm = 0; pm < cpPosition.getLength(); ++pm) {
                Element pos = (Element) (cpPosition.item(pm));
                this.ai.people[j].goodPosition.add(new PosInfo(pos.getAttribute("x"), pos.getAttribute("y"), pos.getAttribute("value")));
            }
            for (int k = 0; k < peoplePlayer.getChessmen().size(); ++k) {
                temp = (Chessman) (peoplePlayer.getChessmen().get(k));
                if (temp.getClass().getName().equals(chessmanClass)) {
                    temp.setUnderAttackValue(Integer.parseInt(pchessman.getAttribute("underAttack")));
                    temp.setProtectedValue(Integer.parseInt(pchessman.getAttribute("protected")));
                    temp.setSelfValue(Integer.parseInt(pchessman.getAttribute("self")));
                    temp.setCanGoPositionValue(Integer.parseInt(pchessman.getAttribute("canGoPosition")));
                    NodeList pPosition = pchessman.getElementsByTagName("goodPosition");
                    for (int m = 0; m < pPosition.getLength(); ++m) {
                        Element pos = (Element) (pPosition.item(m));
                        temp.setGoodPositionValue(pos.getAttribute("x"), pos.getAttribute("y"), pos.getAttribute("value"));
                    }
                }
            }
        }
        Element computer = (Element) (root.getElementsByTagName("computer").item(0));
        NodeList cchessmen = people.getElementsByTagName("chessman");
        for (int j = 0; j < cchessmen.getLength(); ++j) {
            Element cchessman = (Element) pchessmen.item(j);
            chessmanClass = cchessman.getAttribute("name");
            this.ai.computer[j].name = chessmanClass;
            this.ai.computer[j].underAttack = cchessman.getAttribute("underAttack");
            this.ai.computer[j].self = cchessman.getAttribute("self");
            this.ai.computer[j].canGoPosition = cchessman.getAttribute("canGoPosition");
            this.ai.computer[j].protectedV = cchessman.getAttribute("protected");
            NodeList cpPosition = cchessman.getElementsByTagName("goodPosition");
            for (int pm = 0; pm < cpPosition.getLength(); ++pm) {
                Element pos = (Element) (cpPosition.item(pm));
                this.ai.computer[j].goodPosition.add(new PosInfo(pos.getAttribute("x"), pos.getAttribute("y"), pos.getAttribute("value")));
            }
            for (int k = 0; k < computerPlayer.getChessmen().size(); ++k) {
                temp = (Chessman) (computerPlayer.getChessmen().get(k));
                if (temp.getClass().getName().equals(chessmanClass)) {
                    temp.setUnderAttackValue(Integer.parseInt(cchessman.getAttribute("underAttack")));
                    temp.setProtectedValue(Integer.parseInt(cchessman.getAttribute("protected")));
                    temp.setSelfValue(Integer.parseInt(cchessman.getAttribute("self")));
                    temp.setCanGoPositionValue(Integer.parseInt(cchessman.getAttribute("canGoPosition")));
                    NodeList cPosition = cchessman.getElementsByTagName("goodPosition");
                    for (int m = 0; m < cPosition.getLength(); ++m) {
                        Element pos = (Element) (cPosition.item(m));
                        temp.setGoodPositionValue(pos.getAttribute("x"), pos.getAttribute("y"), pos.getAttribute("value"));
                    }
                }
            }
        }
    }
