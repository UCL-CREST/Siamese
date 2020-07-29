    public void open() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.addChoosableFileFilter(new VESTChartFilter());
        fileChooser.setCurrentDirectory(new File("."));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) return;
        File fileName = fileChooser.getSelectedFile();
        if (fileName == null || fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(this, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                clearGraph();
                input = new ObjectInputStream(new FileInputStream(fileName));
                int length = input.readInt();
                Object[] cells = new Object[length];
                Hashtable<DefaultGraphCell, AttributeMap> attrib = new Hashtable<DefaultGraphCell, AttributeMap>();
                for (int i = 0; i < length; i++) {
                    DefaultGraphCell tmp = (DefaultGraphCell) input.readObject();
                    if (tmp instanceof DefaultEdge) {
                    } else if (tmp instanceof basicCell) {
                        attrib.put(tmp, tmp.getAttributes());
                        cells[i] = tmp;
                    } else if (tmp instanceof SwimLaneCell) {
                        attrib.put(tmp, tmp.getAttributes());
                        cells[i] = tmp;
                    } else if (tmp instanceof AndStateCell) {
                        attrib.put(tmp, tmp.getAttributes());
                        cells[i] = tmp;
                    } else if (tmp instanceof orthogonalCell) {
                        attrib.put(tmp, tmp.getAttributes());
                        cells[i] = tmp;
                    } else if (tmp instanceof circle) {
                        attrib.put(tmp, tmp.getAttributes());
                        cells[i] = tmp;
                    }
                }
                graph.getGraphLayoutCache().insert(cells, attrib, null, null, null);
                int counter = input.readInt();
                for (int j = 0; j < counter; j++) {
                    String name, source, target;
                    name = (String) input.readObject();
                    source = (String) input.readObject();
                    target = (String) input.readObject();
                    remakeConnection(name, source, target);
                }
                input.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error Opening File", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println(e);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "File is not an Evaluation!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
