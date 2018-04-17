    protected void showRawData() {
        StatDataset dataset = getDataset((ListValue) datasetsList.getSelectedValue());
        if (Desktop.isDesktopSupported() && dataset != null) {
            try {
                Desktop.getDesktop().open(dataset.getDataFile());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parentWindow, e.getMessage(), "File could not be opened", JOptionPane.WARNING_MESSAGE);
            }
        } else onDatasetsCleared();
    }
