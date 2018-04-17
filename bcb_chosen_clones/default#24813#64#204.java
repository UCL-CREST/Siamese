    public void actionPerformed(ActionEvent ae) {
        String s = (String) ae.getActionCommand();
        if (s.equals("New")) {
            gFrame.toolBar.TBNew.doClick();
        } else if (s.equals("Open")) {
            gFrame.toolBar.TBOpen.doClick();
        } else if (s.equals("Save")) {
            gFrame.toolBar.TBSave.doClick();
        } else if (s.equals("Save As...")) {
            int returnVal = fileChooser.showSaveDialog(gFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    currentFile = fileChooser.getSelectedFile();
                    if (!currentFile.getName().endsWith(".gra")) currentFile = new File(currentFile.getPath().concat(".gra"));
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentFile));
                    oos.writeObject(gFrame.gPanel.getGraph());
                    isFileSaved = true;
                    gFrame.toolBar.TBUndo.setEnabled(false);
                    gFrame.toolBar.TBRedo.setEnabled(false);
                    gFrame.menuBar.menuEditUndo.setEnabled(false);
                    gFrame.menuBar.menuEditRedo.setEnabled(false);
                    gFrame.setTitle("GRASP - [" + currentFile.getName() + "]");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(gFrame, "Unable to save file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (s.equals("Print...")) {
            printJob.setPrintable(gFrame.gPanel);
            if (printJob.printDialog()) {
                try {
                    printJob.print();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (s.equals("Print Preview")) {
            Thread runner = new Thread() {

                public void run() {
                    new PrintPreview(gFrame.gPanel, "Print Preview", pf);
                }
            };
            runner.start();
        } else if (s.equals("Page Setup...")) {
            pf = printJob.pageDialog(pf);
        } else if (s.equals("Exit")) {
            System.exit(0);
        } else if (s.equals("Undo")) {
            gFrame.toolBar.TBUndo.doClick();
        } else if (s.equals("Redo")) {
            gFrame.toolBar.TBRedo.doClick();
        } else if (s.equals("Refresh")) {
            gFrame.gPanel.refresh();
        } else if (s.equals("DFS")) {
            gFrame.gPanel.doDFS();
        } else if (s.equals("BFS")) {
            gFrame.gPanel.doBFS();
        } else if (s.equals("Shortest Path")) {
            gFrame.gPanel.doDijkstraShortestPath();
        } else if (s.equals("Prim's MST")) {
            gFrame.gPanel.doPrimMST();
        } else if (s.equals("Kruskal's MST")) {
            gFrame.gPanel.doKruskalMST();
        } else if (s.equals("Topological Sort")) {
            gFrame.gPanel.doTopologicalSort();
        } else if (s.equals("Euler Tour")) {
            gFrame.gPanel.doEulerTour();
        } else if (s.equals("Longest Path")) {
            gFrame.gPanel.doLongestPath();
        } else if (s.equals("Maximum Flow")) {
            gFrame.gPanel.doMaximumFlow();
        } else if (s.equals("Critical Path Analysis")) {
            gFrame.gPanel.doCriticalPathAnalysis();
        } else if (s.equals("Graph Coloring")) {
            gFrame.gPanel.doGraphColoring();
        } else if (s.equals("Travelling Salesman Problem")) {
            gFrame.gPanel.doTravellingSalesmanProblem();
        } else if (s.equals("Hamilton Tour")) {
            gFrame.gPanel.doHamiltonTour();
        } else if (s.equals("Adjacency Matrix")) {
            new AdjMatrixDialog(gFrame);
        } else if (s.equals("Show Weights")) {
            gFrame.toolBar.TBShowWeights.doClick();
        } else if (s.equals("Graph Animation")) {
            gFrame.toolBar.TBStepByStep.doClick();
        } else if (s.equals("Adjust Animation Speed")) {
            JSlider speed = new JSlider(JSlider.HORIZONTAL, 1, 6, gFrame.gPanel.animationDelay / 500);
            speed.setMajorTickSpacing(1);
            speed.setPaintTicks(true);
            speed.setPaintLabels(true);
            speed.setSnapToTicks(true);
            ImageIcon icon = new ImageIcon("images/speed.png");
            int selection = JOptionPane.showOptionDialog(null, speed, "Adjust Animation Speed", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon, null, null);
            if (selection == 0) {
                int newSpeed = 1;
                int getSpeed = speed.getValue();
                if (getSpeed == 1) newSpeed = 500; else if (getSpeed == 2) newSpeed = 1000; else if (getSpeed == 3) newSpeed = 1500; else if (getSpeed == 4) newSpeed = 2000; else if (getSpeed == 5) newSpeed = 2500; else if (getSpeed == 6) newSpeed = 3000;
                gFrame.gPanel.animationDelay = newSpeed;
            }
        } else if (s.equals("Selection Mode")) {
            gFrame.toolBar.TBSelect.doClick();
        } else if (s.equals("Vertex Mode")) {
            gFrame.toolBar.TBVertex.doClick();
        } else if (s.equals("Edge Mode")) {
            gFrame.toolBar.TBEdge.doClick();
        } else if (s.equals("Splash Screen...")) {
            new SplashScreen(false);
        } else if (s.equals("About GRASP...")) {
            JOptionPane.showMessageDialog(gFrame, "      GRASP Version 1.1 by\nP.Jaya Krishna, K.Vamsi Krishna\n & P.Naga Mohan Kumar.", "About...", JOptionPane.INFORMATION_MESSAGE, null);
        } else if (s.equals("GRASP Help")) {
            JEditorPane jep = new JEditorPane();
            final JDialog dialog = new JDialog(gFrame, "GRASP Help", true);
            try {
                URLClassLoader cl = (URLClassLoader) getClass().getClassLoader();
                URL u = cl.getResource("help/UserDocument.rtf");
                jep.setPage(u);
            } catch (Exception e) {
                System.out.println(e);
            }
            JButton ok = new JButton("Ok");
            JPanel panel1 = new JPanel();
            JPanel panel2 = new JPanel();
            panel2.add(ok);
            jep.setEditable(false);
            JScrollPane jsp = new JScrollPane(jep);
            jsp.setPreferredSize(new Dimension(450, 300));
            panel1.add(jsp);
            ActionListener lst = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            };
            ok.addActionListener(lst);
            dialog.add(panel2, BorderLayout.SOUTH);
            dialog.add(panel1, BorderLayout.CENTER);
            dialog.setSize(500, 400);
            dialog.setLocation(100, 100);
            dialog.setVisible(true);
        }
    }
