        public void actionPerformed(ActionEvent e) {
            String a = e.getActionCommand();
            if (a.equals("SYNC")) {
                TerraMaster.svn.sync(map.getSelection());
                map.clearSelection();
                repaint();
            } else if (a.equals("DELETE")) {
                TerraMaster.svn.delete(map.getSelection());
                map.clearSelection();
                repaint();
            } else if (a.equals("RESET")) {
                map.toggleProj();
                repaint();
            } else if (a.equals("PREFS")) {
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fc.showOpenDialog(butPrefs) == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    fc.setCurrentDirectory(f);
                    try {
                        setTitle(f.getPath() + " - " + title);
                        TerraMaster.mapScenery = TerraMaster.newScnMap(f.getPath());
                        repaint();
                        TerraMaster.svn.setScnPath(f);
                        TerraMaster.props.setProperty("SceneryPath", f.getPath());
                    } catch (Exception x) {
                    }
                }
            }
        }
