    private void folderFileChooserActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
            File selectedFolder = folderFileChooser.getSelectedFile();
            File collectionCopyFile;
            String newDocumentName;
            Document newDocument;
            StringBuilder distinguisherReplacer = new StringBuilder();
            int matchingFilenameDistinguisher;
            String nextToken;
            Term newTerm;
            String userHome;
            String fileSeparator;
            int userOption;
            ArrayList<File> folderTextFiles = new ArrayList<File>();
            ArrayList<File> folderRejectedFiles = new ArrayList<File>();
            HashSet<File> ignoredFiles = new HashSet<File>();
            FileNameExtensionFilter textFileFilter = new FileNameExtensionFilter("Text Files", "txt");
            Scanner tokenizer = null;
            FileChannel fileSource = null;
            FileChannel collectionDestination = null;
            HashMap<String, Integer> termHashMap = null;
            Index collectionIndex = activeCollection.getIndex();
            int documentTermMaxFrequency;
            int currentTermFrequency;
            for (File folderFile : selectedFolder.listFiles()) if (textFileFilter.accept(folderFile)) folderTextFiles.add(folderFile); else folderRejectedFiles.add(folderFile);
            for (File selectedFile : folderTextFiles) {
                newDocumentName = selectedFile.getName();
                newDocument = new Document(newDocumentName);
                if (activeCollection.containsDocument(newDocument)) {
                    matchingFilenameDistinguisher = 1;
                    newDocumentName = newDocumentName.concat("(" + matchingFilenameDistinguisher + ")");
                    newDocument.setDocumentName(newDocumentName);
                    while (activeCollection.containsDocument(newDocument)) {
                        matchingFilenameDistinguisher++;
                        newDocumentName = distinguisherReplacer.replace(newDocumentName.length() - 2, newDocumentName.length() - 1, new Integer(matchingFilenameDistinguisher).toString()).toString();
                        newDocument.setDocumentName(newDocumentName);
                    }
                }
                termHashMap = new HashMap<String, Integer>();
                try {
                    tokenizer = new Scanner(new BufferedReader(new FileReader(selectedFile)));
                    tokenizer.useDelimiter(Pattern.compile("\\p{Space}|\\p{Punct}|\\p{Cntrl}"));
                    while (tokenizer.hasNext()) {
                        nextToken = tokenizer.next().toLowerCase();
                        if (!nextToken.isEmpty()) if (termHashMap.containsKey(nextToken)) termHashMap.put(nextToken, termHashMap.get(nextToken) + 1); else termHashMap.put(nextToken, 1);
                    }
                    documentTermMaxFrequency = 0;
                    for (String term : termHashMap.keySet()) {
                        newTerm = new Term(term);
                        if (!collectionIndex.termExists(newTerm)) collectionIndex.addTerm(newTerm);
                        currentTermFrequency = termHashMap.get(term);
                        if (currentTermFrequency > documentTermMaxFrequency) documentTermMaxFrequency = currentTermFrequency;
                        collectionIndex.addOccurence(newTerm, newDocument, currentTermFrequency);
                    }
                    newDocument.setTermMaxFrequency(documentTermMaxFrequency);
                    activeCollection.addDocument(newDocument);
                    userHome = System.getProperty("user.home");
                    fileSeparator = System.getProperty("file.separator");
                    collectionCopyFile = new File(userHome + fileSeparator + "Infrared" + fileSeparator + activeCollection.getDocumentCollectionName() + fileSeparator + newDocumentName);
                    collectionCopyFile.createNewFile();
                    fileSource = new FileInputStream(selectedFile).getChannel();
                    collectionDestination = new FileOutputStream(collectionCopyFile).getChannel();
                    collectionDestination.transferFrom(fileSource, 0, fileSource.size());
                } catch (FileNotFoundException e) {
                    System.err.println(e.getMessage() + " This error should never occur! The file was just selected!");
                    return;
                } catch (IOException e) {
                    userOption = JOptionPane.showConfirmDialog(this, "A file insertion has failed. If you want it to ignore this" + "file, press YES, else press NO to repeat the insertion", "FileInsertionFailure", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (userOption == JOptionPane.NO_OPTION) {
                        activeCollection.removeDocument(newDocument);
                        for (String term : termHashMap.keySet()) {
                            collectionIndex.removeAllOccurences(new Term(term), newDocument);
                        }
                        folderTextFiles.add(selectedFile);
                    } else ignoredFiles.add(selectedFile);
                } finally {
                    try {
                        if (tokenizer != null) tokenizer.close();
                        if (fileSource != null) fileSource.close();
                        if (collectionDestination != null) collectionDestination.close();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
            if (ignoredFiles.size() > 0) {
                IgnoredFilesDialog ignoredFilesDialog = new IgnoredFilesDialog(ignoredFiles, this);
                ignoredFilesDialog.setVisible(true);
            }
            processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (evt.getActionCommand().equalsIgnoreCase(JFileChooser.CANCEL_SELECTION)) processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
