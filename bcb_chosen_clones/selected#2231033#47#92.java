    public static void saveDigraph(mainFrame parentFrame, DigraphView digraphView, File tobeSaved) {
        DigraphFile digraphFile = new DigraphFile();
        DigraphTextFile digraphTextFile = new DigraphTextFile();
        try {
            if (!DigraphFile.DIGRAPH_FILE_EXTENSION.equals(getExtension(tobeSaved))) {
                tobeSaved = new File(tobeSaved.getPath() + "." + DigraphFile.DIGRAPH_FILE_EXTENSION);
            }
            File dtdFile = new File(tobeSaved.getParent() + "/" + DigraphFile.DTD_FILE);
            if (!dtdFile.exists()) {
                File baseDigraphDtdFile = parentFrame.getDigraphDtdFile();
                if (baseDigraphDtdFile != null && baseDigraphDtdFile.exists()) {
                    try {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dtdFile));
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(baseDigraphDtdFile));
                        while (bis.available() > 1) {
                            bos.write(bis.read());
                        }
                        bis.close();
                        bos.close();
                    } catch (IOException ex) {
                        System.out.println("Unable to Write Digraph DTD File: " + ex.getMessage());
                    }
                } else {
                    System.out.println("Unable to Find Base Digraph DTD File: ");
                }
            }
            Digraph digraph = digraphView.getDigraph();
            digraphFile.saveDigraph(tobeSaved, digraph);
            String fileName = tobeSaved.getName();
            int extensionIndex = fileName.lastIndexOf(".");
            if (extensionIndex > 0) {
                fileName = fileName.substring(0, extensionIndex + 1) + "txt";
            } else {
                fileName = fileName + ".txt";
            }
            File textFile = new File(tobeSaved.getParent() + "/" + fileName);
            digraphTextFile.saveDigraph(textFile, digraph);
            digraphView.setDigraphDirty(false);
            parentFrame.setFilePath(tobeSaved.getPath());
            parentFrame.setSavedOnce(true);
        } catch (DigraphFileException exep) {
            JOptionPane.showMessageDialog(parentFrame, "Error Saving File:\n" + exep.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        } catch (DigraphException exep) {
            JOptionPane.showMessageDialog(parentFrame, "Error Retrieving Digraph from View:\n" + exep.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
