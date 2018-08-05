    private void documentFileChooserActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
            File selectedFile = documentFileChooser.getSelectedFile();
            File collectionCopyFile;
            String newDocumentName = selectedFile.getName();
            Document newDocument = new Document(newDocumentName);
            if (activeCollection.containsDocument(newDocument)) {
                int matchingFilenameDistinguisher = 1;
                StringBuilder distinguisherReplacer = new StringBuilder();
                newDocumentName = newDocumentName.concat("(" + matchingFilenameDistinguisher + ")");
                newDocument.setDocumentName(newDocumentName);
                while (activeCollection.containsDocument(newDocument)) {
                    matchingFilenameDistinguisher++;
                    newDocumentName = distinguisherReplacer.replace(newDocumentName.length() - 2, newDocumentName.length() - 1, new Integer(matchingFilenameDistinguisher).toString()).toString();
                    newDocument.setDocumentName(newDocumentName);
                }
            }
            Scanner tokenizer = null;
            FileChannel fileSource = null;
            FileChannel collectionDestination = null;
            HashMap<String, Integer> termHashMap = new HashMap<String, Integer>();
            Index collectionIndex = activeCollection.getIndex();
            int documentTermMaxFrequency = 0;
            int currentTermFrequency;
            try {
                tokenizer = new Scanner(new BufferedReader(new FileReader(selectedFile)));
                tokenizer.useDelimiter(Pattern.compile("\\p{Space}|\\p{Punct}|\\p{Cntrl}"));
                String nextToken;
                while (tokenizer.hasNext()) {
                    nextToken = tokenizer.next().toLowerCase();
                    if (!nextToken.isEmpty()) if (termHashMap.containsKey(nextToken)) termHashMap.put(nextToken, termHashMap.get(nextToken) + 1); else termHashMap.put(nextToken, 1);
                }
                Term newTerm;
                for (String term : termHashMap.keySet()) {
                    newTerm = new Term(term);
                    if (!collectionIndex.termExists(newTerm)) collectionIndex.addTerm(newTerm);
                    currentTermFrequency = termHashMap.get(term);
                    if (currentTermFrequency > documentTermMaxFrequency) documentTermMaxFrequency = currentTermFrequency;
                    collectionIndex.addOccurence(newTerm, newDocument, currentTermFrequency);
                }
                activeCollection.addDocument(newDocument);
                String userHome = System.getProperty("user.home");
                String fileSeparator = System.getProperty("file.separator");
                collectionCopyFile = new File(userHome + fileSeparator + "Infrared" + fileSeparator + activeCollection.getDocumentCollectionName() + fileSeparator + newDocumentName);
                collectionCopyFile.createNewFile();
                fileSource = new FileInputStream(selectedFile).getChannel();
                collectionDestination = new FileOutputStream(collectionCopyFile).getChannel();
                collectionDestination.transferFrom(fileSource, 0, fileSource.size());
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage() + " This error should never occur! The file was just selected!");
                return;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "An I/O error occured during file transfer!", "File transfer I/O error", JOptionPane.WARNING_MESSAGE);
                return;
            } finally {
                try {
                    if (tokenizer != null) tokenizer.close();
                    if (fileSource != null) fileSource.close();
                    if (collectionDestination != null) collectionDestination.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
            processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (evt.getActionCommand().equalsIgnoreCase(JFileChooser.CANCEL_SELECTION)) processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
