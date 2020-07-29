    public void actionPerformed(ActionEvent evt) {
        String name = evt.getActionCommand();
        if (name.equals("Load LSculpture")) {
            JFileChooser chooser = new JFileChooser(System.getProperties().getProperty("user.dir"));
            chooser.setDialogTitle("Select the directory containing the layer layout files");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                directoryPath = chooser.getSelectedFile().getAbsolutePath();
            } else {
                return;
            }
            if (foundLayers("ll") || foundLayers("ll2")) {
                currentLayerNum = firstLayer;
                createLayerList();
                mainPane.totalNumLayersLab.setText("" + (lastLayer + 1 - firstLayer));
                mainPane.currentLayerNumLab.setText("" + 1);
                if (firstLayer == lastLayer) {
                    mainPane.previousLayerBut.setEnabled(false);
                    mainPane.nextLayerBut.setEnabled(false);
                } else {
                    mainPane.previousLayerBut.setEnabled(false);
                    mainPane.nextLayerBut.setEnabled(true);
                }
                if (!loadLayerBricks(currentLayerNum, "CURRENT")) {
                    closeCurrentSculpture();
                }
                mainPane.numBricksLab.setText("" + currentLayer.size());
                view2D = new View2D(this, layoutFileExtension);
                mainPane.setVisible(true);
                showFullSculptureMenuItem.setEnabled(true);
                genInstructMenuItem.setEnabled(true);
                loadItem.setEnabled(false);
                closeItem.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "No layout files where found. (layerLayout?.ll)", "No Layouts Found", JOptionPane.ERROR_MESSAGE);
            }
        } else if (name.equals("Close Current Sculpture")) {
            closeCurrentSculpture();
        } else if (name.equals("Exit")) {
            System.exit(0);
        } else if (evt.getSource().equals(showFullSculptureMenuItem)) {
            if (fullSculptureView == null) {
                fullSculptureView = new JFrame();
                fullSculptureView.setTitle("Complete 3D view of the LEGO sculpture");
                fullSculptureView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fullSculptureView.setSize(400, 400);
                fullSculptureView.add(new View3D(this));
                fullSculptureView.setVisible(true);
            } else {
                if (!fullSculptureView.isActive()) {
                    fullSculptureView = new JFrame();
                    fullSculptureView.setTitle("Complete 3D view of the LEGO sculpture");
                    fullSculptureView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    fullSculptureView.setSize(400, 400);
                    fullSculptureView.add(new View3D(this));
                    fullSculptureView.setVisible(true);
                }
            }
        } else if (evt.getSource().equals(genInstructMenuItem)) {
            saveBuildingInstructionstoPDF(true);
        } else if (name.equals("General Information")) {
            helpGUI helpWindow = new helpGUI();
        } else if (name.equals("About")) {
            JOptionPane.showMessageDialog(null, "LSculpturer: LVisual \nVersion 1.0 \nAuthor : Eugene Smal \nContact: eugene.smal@gmail.com \nStellenbosch University Master's Student" + " \n2008 \n \n", "About LSculpturer: LVisual", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images\\about.gif"));
        } else if (evt.getSource().equals(mainPane.nextLayerBut)) {
            mainPane.previousLayerBut.setEnabled(true);
            displayLayer(currentLayerNum + 1, false, true);
            mainPane.layerList.ensureIndexIsVisible(currentLayerNum);
            if (currentLayerNum == lastLayer) {
                mainPane.nextLayerBut.setEnabled(false);
            }
        } else if (evt.getSource().equals(mainPane.previousLayerBut)) {
            mainPane.nextLayerBut.setEnabled(true);
            if (currentLayerNum - 1 == firstLayer) {
                currentLayer = null;
                displayLayer(currentLayerNum - 1, false, false);
                mainPane.previousLayerBut.setEnabled(false);
                previousLayer = null;
            } else {
                displayLayer(currentLayerNum - 1, true, false);
            }
            mainPane.layerList.ensureIndexIsVisible(currentLayerNum);
        } else if (evt.getSource().equals(mainPane.showPreviousCheck)) {
            mustDrawPrevious = mainPane.showPreviousCheck.isSelected();
            view2D.canvas.repaint();
            view2D.refresh3DLayers();
        } else if (evt.getSource().equals(mainPane.showGridCheck)) {
            mustDrawGrid = mainPane.showGridCheck.isSelected();
            view2D.canvas.repaint();
        }
    }
