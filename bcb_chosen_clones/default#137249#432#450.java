    public void showSavePlaylistDialog() {
        fileChooser.setCurrentDirectory(new File(songDirectory));
        fileChooser.rescanCurrentDirectory();
        fileChooser.setFileFilter(playlistFileFilter);
        int returnVal = fileChooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            PrintWriter playlistWriter = null;
            try {
                playlistWriter = new PrintWriter(new FileWriter(fileChooser.getSelectedFile().getAbsolutePath()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Can't write to playlist file " + fileChooser.getSelectedFile().getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Enumeration savePlaylistEnumeration = playlist.elements(); savePlaylistEnumeration.hasMoreElements(); ) {
                playlistWriter.println(savePlaylistEnumeration.nextElement());
            }
            playlistWriter.close();
        }
    }
