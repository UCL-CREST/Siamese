    public void invoke(WorkflowContext arg0, ProgressMonitor arg1, Issues arg2) {
        File inputFile = new File(getInputFile());
        File outputFile = new File(getOutputFile());
        if (!getFileExtension(getInputFile()).equalsIgnoreCase(getFileExtension(getOutputFile())) || !getFileExtension(getInputFile()).equalsIgnoreCase(OO_CALC_EXTENSION)) {
            OpenOfficeConnection connection = new SocketOpenOfficeConnection();
            OpenOfficeDocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(inputFile, outputFile);
            connection.disconnect();
        } else {
            FileChannel inputChannel = null;
            FileChannel outputChannel = null;
            try {
                inputChannel = new FileInputStream(inputFile).getChannel();
                outputChannel = new FileOutputStream(outputFile).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (FileNotFoundException e) {
                arg2.addError("File not found: " + e.getMessage());
            } catch (IOException e) {
                arg2.addError("Could not copy file: " + e.getMessage());
            } finally {
                if (inputChannel != null) {
                    try {
                        inputChannel.close();
                    } catch (IOException e) {
                        arg2.addError("Could not close input channel: " + e.getMessage());
                    }
                }
                if (outputChannel != null) {
                    try {
                        outputChannel.close();
                    } catch (IOException e) {
                        arg2.addError("Could not close input channel: " + e.getMessage());
                    }
                }
            }
        }
    }
