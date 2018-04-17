    private void miOpenActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(dir);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ExtensionFileFilter filter = new ExtensionFileFilter("grf", "GRAPH representation files (*.grf)");
        chooser.setFileFilter(filter);
        int res = chooser.showOpenDialog(this);
        String filename = chooser.getSelectedFile().getPath();
        try {
            Document doc = jReport.getDocument();
            doc.remove(0, doc.getLength());
            Graph graf = new Graph(filename);
            dir = chooser.getSelectedFile().getParent();
            drawer.setGraph(graf, dir);
            setTitle(frameName + " " + filename);
        } catch (Graph.GraphConstructorException ex) {
            jReport.append("Error: file <" + filename + "> has illegal format or not exist\n");
        } catch (java.io.EOFException ex) {
            jReport.append("Error: " + ex.getMessage() + " file format not recognized\n");
        } catch (BadLocationException ex) {
            System.out.println(ex);
        }
        invalidate();
        validate();
        repaint();
        System.out.println(filename);
    }
