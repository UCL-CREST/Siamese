    public void printContained() {
        BufferedWriter outputChart;
        outputChart = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) return;
        File fileName = fileChooser.getSelectedFile();
        if (fileName == null || fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(this, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                outputChart = new BufferedWriter(new FileWriter(fileName));
                System.out.println("I SHOULD work");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error Saving File", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        Object[] cells = graph.getDescendants(graph.getRoots());
        for (int i = 0; i < cells.length; i++) {
            if (isGroup(cells[i])) {
                System.out.println("I found a group Cell, do something please");
            }
        }
        allStates = new Hashtable<String, Object>();
        allTransitions = new Hashtable<String, Object>();
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] instanceof DefaultEdge) {
                allTransitions.put(cells[i].toString(), cells[i]);
            } else if (cells[i] instanceof basicCell) {
                allStates.put(cells[i].toString(), cells[i]);
            } else if (cells[i] instanceof SwimLaneCell) {
                allStates.put(cells[i].toString(), cells[i]);
            } else if (cells[i] instanceof AndStateCell) {
                allStates.put(cells[i].toString(), cells[i]);
            } else if (cells[i] instanceof orthogonalCell) {
                allStates.put(cells[i].toString(), cells[i]);
            } else if (cells[i] instanceof circle) {
                allStates.put(cells[i].toString(), cells[i]);
            }
        }
        stateChart output = new stateChart(allStates, allTransitions, graph, condCount, groupCount);
        if (output.getValid()) {
            output.printChart(outputChart);
        }
        try {
            outputChart.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
