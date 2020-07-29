    public void loadGames() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) return;
        File gameFile = chooser.getSelectedFile();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(gameFile);
            doc.getDocumentElement().normalize();
            NodeList listOfGames = doc.getElementsByTagName("game");
            for (int g = 0; g < listOfGames.getLength(); g++) {
                Node gameNode = listOfGames.item(g);
                if (gameNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element gameElement = (Element) gameNode;
                    String team1 = gameElement.getElementsByTagName("team1").item(0).getTextContent();
                    String team1Score = gameElement.getElementsByTagName("team1Score").item(0).getTextContent();
                    String team2 = gameElement.getElementsByTagName("team2").item(0).getTextContent();
                    String team2Score = gameElement.getElementsByTagName("team2Score").item(0).getTextContent();
                    String date = gameElement.getElementsByTagName("date").item(0).getTextContent();
                    games.add(new Game(team1, Integer.parseInt(team1Score), team2, Integer.parseInt(team2Score), date));
                }
            }
            for (String team : teams.keySet()) {
                team1Combo.addItem(team);
                team2Combo.addItem(team);
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
    }
