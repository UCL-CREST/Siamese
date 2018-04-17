    public void openSequence() {
        try {
            Debugger.appendAction("Starting openSequence");
            JFileChooser pickFile = new JFileChooser();
            pickFile.setCurrentDirectory(new File(Settings.lastOpenedFolder));
            pickFile.setFileFilter(new SequenceFilter());
            pickFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int retVal = pickFile.showOpenDialog(main);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                ArrayList<Flame> sequenceFlames = new Flame(docBuilder.parse(pickFile.getSelectedFile()), renderQueue).split();
                flames.clear();
                flames.addAll(sequenceFlames);
                Settings.lastOpenedFolder = pickFile.getCurrentDirectory().toString();
                Settings.saveSettings();
                Debugger.appendLog("Succesfull: openSequence");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(main, "Invalid .seq file", "Error opening file", JOptionPane.ERROR_MESSAGE);
            Debugger.appendLog("Failed: openSequence");
            Debugger.storeException(ex);
        } catch (SAXException ex) {
            JOptionPane.showMessageDialog(main, "Invalid .seq file", "Error opening file", JOptionPane.ERROR_MESSAGE);
            Debugger.appendLog("Failed: openSequence");
            Debugger.storeException(ex);
        } catch (ParserConfigurationException ex) {
            Debugger.appendLog("Failed: openSequence");
            Debugger.storeException(ex);
        } catch (NullPointerException ex) {
            Debugger.appendLog("Failed: openSequence");
            Debugger.storeException(ex);
        }
        main.refreshThumbs();
        Debugger.appendAction("Ending openSequence");
    }
