    @SuppressWarnings("unchecked")
    private void doOpenCommand() {
        int returnVal = fileChooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String filename = fileChooser.getSelectedFile().getCanonicalPath();
                FileInputStream fin = new FileInputStream(filename);
                ObjectInputStream oin = new ObjectInputStream(fin);
                ArrayList<Column> openedColumns = (ArrayList<Column>) oin.readObject();
                oin.close();
                doNewCommand();
                for (int i = 0; i < openedColumns.size(); i++) {
                    columns.get(i).setTo(openedColumns.get(i));
                }
                setNumColsShown(openedColumns.get(0).getNumColumnsShown());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "There was an error while reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(frame, "This isn't the right file type.  You can only load files saved by this program.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassCastException e) {
                JOptionPane.showMessageDialog(frame, "This isn't the right file type.  You can only load files saved by this program.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
