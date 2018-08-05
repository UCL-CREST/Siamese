    public void analyzeDocuments(String analysisText) {
        outputFileSelected = outputFileSelector.getSelected();
        File inputDir = new File(inputFileSelector.getSelected());
        if (outputFileSelector.getSelected().length() > 0) outputDirectory = new File(outputFileSelector.getSelected()); else outputDirectory = null;
        String tempFileDir = null;
        if ((analysisText != null) && (outputDirectory != null)) {
            tempFileDir = outputFileSelector.getSelected() + "/interactive_temp";
            inputDir = new File(tempFileDir);
            if (!inputDir.exists()) inputDir.mkdirs();
            outputFileSelected = outputFileSelector.getSelected() + "/interactive_out";
            prefsMed.setOutputDirForInteractiveMode(outputFileSelected, outputFileSelector.getSelected());
            outputDirectory = new File(outputFileSelected);
        } else {
            analysisText = null;
        }
        aeSpecifierFile = new File(xmlFileSelector.getSelected());
        String xmlTag = runParametersField.getText();
        if ("".equals(xmlTag)) {
            xmlTag = null;
        }
        String language = (String) languageComboBox.getSelectedItem();
        String encoding = (String) encodingComboBox.getSelectedItem();
        if (aeSpecifierFile.getName().equals("")) {
            displayError("An Analysis Engine Descriptor is Required");
        } else if (!aeSpecifierFile.exists()) {
            displayError("Analysis Engine Descriptor \"" + xmlFileSelector.getSelected() + "\" does not exist.");
        } else if (aeSpecifierFile.isDirectory()) {
            displayError("The Analysis Engine Descriptor (" + xmlFileSelector.getSelected() + ") must be a file, not a directory.");
        } else if (inputDir.getName().equals("")) {
            displayError("An Input Directory is Required");
        } else if (!inputDir.exists()) {
            displayError("The input directory \"" + inputFileSelector.getSelected() + "\" does not exist.");
        } else if (!inputDir.isDirectory()) {
            displayError("The input directory (" + inputFileSelector.getSelected() + ") must be a directory, not a file.");
        } else if (outputDirectory != null && (!outputDirectory.exists() && !outputDirectory.mkdirs()) || !outputDirectory.isDirectory()) {
            displayError("The output directory \"" + outputFileSelector.getSelected() + "\" does not exist and could not be created.");
        } else if (inputDir.equals(outputDirectory)) {
            displayError("The input directory and the output directory must be different.");
        } else {
            if (analysisText != null) {
                File[] filesInOutDir = inputDir.listFiles();
                for (int i = 0; i < filesInOutDir.length; i++) {
                    if (!filesInOutDir[i].isDirectory()) {
                        filesInOutDir[i].delete();
                    }
                }
                File tempFile = new File(tempFileDir + "/" + interactiveTempFN);
                PrintWriter writer;
                try {
                    writer = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));
                    writer.println(analysisText);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File[] filesInOutDir = outputDirectory.listFiles();
            for (int i = 0; i < filesInOutDir.length; i++) {
                if (!filesInOutDir[i].isDirectory()) {
                    String filename = filesInOutDir[i].getName();
                    if (filename.endsWith(".xmi")) {
                        filename = filename.substring(0, filename.length() - 4);
                    }
                    if (!new File(inputDir, filename).exists()) {
                    }
                }
            }
            for (int i = 0; i < filesInOutDir.length; i++) {
                if (!filesInOutDir[i].isDirectory()) {
                    filesInOutDir[i].delete();
                }
            }
            ProcessingThread thread = new ProcessingThread(inputDir, outputDirectory, aeSpecifierFile, xmlTag, language, encoding);
            thread.start();
        }
    }
