    private void loadTable() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File loadFile = null;
            BufferedReader in = null;
            try {
                loadFile = fileChooser.getSelectedFile();
                in = new BufferedReader(new FileReader(loadFile));
                ArrayList<Album> newAlbumList = new ArrayList<Album>();
                String nextLine = in.readLine();
                while (nextLine != null) {
                    Album album = Album.makeAlbum(nextLine);
                    if (album != null) {
                        newAlbumList.add(album);
                    }
                    nextLine = in.readLine();
                }
                cdTableModel.setTableData(newAlbumList);
                cdTableModel.fireTableDataChanged();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
