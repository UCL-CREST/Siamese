    private void convertFile() {
        final File fileToConvert = filePanel.getInputFile();
        final File convertedFile = filePanel.getOutputFile();
        if (fileToConvert == null || convertedFile == null) {
            Main.showMessage("Select valid files for both input and output");
            return;
        }
        if (fileToConvert.getName().equals(convertedFile.getName())) {
            Main.showMessage("Input and Output files are same.. select different files");
            return;
        }
        final int len = (int) fileToConvert.length();
        progressBar.setMinimum(0);
        progressBar.setMaximum(len);
        progressBar.setValue(0);
        try {
            fileCopy(fileToConvert, fileToConvert.getAbsolutePath() + ".bakup");
        } catch (IOException e) {
            Main.showMessage("Unable to Backup input file");
            return;
        }
        final BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileToConvert));
        } catch (FileNotFoundException e) {
            Main.showMessage("Unable to create reader - file not found");
            return;
        }
        final BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(convertedFile));
        } catch (IOException e) {
            Main.showMessage("Unable to create writer for output file");
            return;
        }
        String input;
        try {
            while ((input = bufferedReader.readLine()) != null) {
                if (stopRequested) {
                    break;
                }
                bufferedWriter.write(parseLine(input));
                bufferedWriter.newLine();
                progressBar.setValue(progressBar.getValue() + input.length());
            }
        } catch (IOException e) {
            Main.showMessage("Unable to convert " + e.getMessage());
            return;
        } finally {
            try {
                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException e) {
                Main.showMessage("Unable to close reader/writer " + e.getMessage());
                return;
            }
        }
        if (!stopRequested) {
            filePanel.readOutputFile();
            progressBar.setValue(progressBar.getMaximum());
            Main.setStatus("Transliterate Done.");
        }
        progressBar.setValue(progressBar.getMinimum());
    }
