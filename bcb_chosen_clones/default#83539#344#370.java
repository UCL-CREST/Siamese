    public void openDocument() {
        try {
            File current = new File(".");
            JFileChooser chooser = new JFileChooser(current);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setFileFilter(new HTMLFileFilter());
            int approval = chooser.showSaveDialog(this);
            if (approval == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();
                FileReader fr = new FileReader(currentFile);
                Document oldDoc = textPane.getDocument();
                if (oldDoc != null) oldDoc.removeUndoableEditListener(undoHandler);
                HTMLEditorKit editorKit = new HTMLEditorKit();
                document = (HTMLDocument) editorKit.createDefaultDocument();
                editorKit.read(fr, document, 0);
                document.addUndoableEditListener(undoHandler);
                textPane.setDocument(document);
                resetUndoManager();
            }
        } catch (BadLocationException ble) {
            System.err.println("BadLocationException: " + ble.getMessage());
        } catch (FileNotFoundException fnfe) {
            System.err.println("FileNotFoundException: " + fnfe.getMessage());
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
