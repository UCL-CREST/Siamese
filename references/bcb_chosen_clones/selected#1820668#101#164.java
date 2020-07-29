    private void createNewGameDataFiles(String[] dataFiles, Map map) throws IOException {
        String tmpGameFile = GAME_DATA_FILENAME, tmpTurnFile = TURN_DATA_FILENAME;
        Player[] playerObjects = new Player[dataFiles.length];
        Hashtable fleets = new Hashtable();
        Fleet fleet = null;
        PlayerAndFleetWrapper playerFleet;
        try {
            EleconicsXmlParser xmlParser = null;
            for (int file = 0; file < dataFiles.length; file++) {
                xmlParser = new EleconicsXmlParser(new InputStreamReader(new FileInputStream(dataFiles[file])));
                playerFleet = new PlayerAndFleetWrapper(xmlParser);
                playerObjects[file] = playerFleet.getPlayer();
                playerObjects[file].setFleetNumber(file + 1);
                fleet = playerFleet.getFleet();
                fleet.setFleetNumber(file + 1);
                fleet.shiftLocation(map.getLocation(file + 1));
                fleet.rotate(map.getAxisAngle(file + 1), map.getLocation(file + 1));
                fleets.put(playerFleet.getFleet().fleetName(), playerFleet.getFleet());
                xmlParser.close();
            }
        } catch (MultiplayerException e) {
            Utilities.popUp(e.getMessage());
            e.printStackTrace();
        }
        Players players = new Players(playerObjects);
        world = new World(fleets);
        XmlProcessor xmlProcessor = new XmlProcessor();
        xmlProcessor.startElement(MultiplayerXmlProcessor.GAME_TAG);
        players.ToXml(xmlProcessor);
        world.ToXml(xmlProcessor);
        xmlProcessor.endElement();
        BufferedWriter out = new BufferedWriter(new FileWriter(tmpGameFile, false));
        out.write(xmlProcessor.stream.toString());
        out.close();
        xmlProcessor.resetWriter();
        xmlProcessor.startElement(MultiplayerXmlProcessor.TURN_TAG);
        xmlProcessor.XmlNode(MultiplayerXmlProcessor.VIEWING_PLAYER_NUMBER_TAG, "1");
        xmlProcessor.XmlNode(MultiplayerXmlProcessor.FIRST_EXECUTING_ACTIONS_PLAYER_NUMBER_TAG, "1");
        xmlProcessor.XmlNode(MultiplayerXmlProcessor.TURN_ACTIONS_TAG, "");
        xmlProcessor.XmlNode(MultiplayerXmlProcessor.START_TURN_TAG, "true");
        xmlProcessor.endElement();
        out = new BufferedWriter(new FileWriter(tmpTurnFile, false));
        out.write(xmlProcessor.stream.toString());
        out.close();
        byte b[] = new byte[512];
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipDataFileName));
        InputStream in = new FileInputStream(tmpGameFile);
        ZipEntry e = new ZipEntry(tmpGameFile.replace(File.separatorChar, '/'));
        zout.putNextEntry(e);
        int len = 0;
        while ((len = in.read(b)) != -1) {
            zout.write(b, 0, len);
        }
        zout.closeEntry();
        in = new FileInputStream(tmpTurnFile);
        e = new ZipEntry(tmpTurnFile.replace(File.separatorChar, '/'));
        zout.putNextEntry(e);
        len = 0;
        while ((len = in.read(b)) != -1) {
            zout.write(b, 0, len);
        }
        zout.closeEntry();
        zout.close();
    }
