    private boolean write(File file) {
        String filename = file.getPath();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(byteArrayOutputStream);
        try {
            StringBuffer xml = null;
            if (MainFrame.getInstance().getAnimation() != null) {
                MainFrame.getInstance().getAnimation().xml(out, "\t");
            } else {
                xml = MainFrame.getInstance().getModel().xml("\t");
            }
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename + "~"));
                char[] buffer = new char[65536];
                int charsRead = 0;
                while ((charsRead = reader.read(buffer)) > 0) writer.write(buffer, 0, charsRead);
                reader.close();
                writer.close();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<jpatch version=\"" + VersionInfo.ver + "\">\n");
            if (xml != null) writer.write(xml.toString()); else writer.write(byteArrayOutputStream.toString());
            writer.write("</jpatch>\n");
            writer.close();
            MainFrame.getInstance().getUndoManager().setChange(false);
            if (MainFrame.getInstance().getAnimation() != null) MainFrame.getInstance().getAnimation().setFile(file); else MainFrame.getInstance().getModel().setFile(file);
            MainFrame.getInstance().setFilename(file.getName());
            return true;
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Unable to save file \"" + filename + "\"\n" + ioException, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
