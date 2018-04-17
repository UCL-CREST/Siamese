    private void saveTable() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new BufferedWriter(new FileWriter(saveFile)));
                for (Album album : cdTableModel.getTableData()) {
                    writer.println(album.toString());
                }
                writer.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }
