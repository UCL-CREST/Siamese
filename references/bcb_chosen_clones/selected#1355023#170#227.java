    private static void createMS2SimMatrix(MSDispatcher dispatcher, ConverterXMLReader reader, PrintStream out) throws ParseException, IOException {
        out.println("Create similarity matrix between runs");
        dispatcher.setCombineFilesFlag(false);
        double precMzTol = reader.getPrecursorMzTol();
        double fragMzTol = reader.getFragmentMzTol();
        ArrayList<MSScan> spectrumBatch_i = new ArrayList<MSScan>();
        ArrayList<MSScan> spectrumBatch_j = new ArrayList<MSScan>();
        ArrayList<File> files = dispatcher.getFiles();
        double[][] simMatrix = new double[files.size()][files.size()];
        for (int i = 0; i < files.size(); i++) {
            simMatrix[i][i] = 1.0;
            dispatcher.getNextSpectrumBatch(spectrumBatch_i, files.get(i));
            HashMap<Integer, ArrayList<MSScan>> sorted_i = sortSpectraByChargeMass(spectrumBatch_i);
            for (int j = i + 1; j < files.size(); j++) {
                out.println("Calculate similarty between : " + files.get(i).getName() + " - " + files.get(j).getName());
                dispatcher.getNextSpectrumBatch(spectrumBatch_j, files.get(j));
                HashMap<Integer, ArrayList<MSScan>> sorted_j = sortSpectraByChargeMass(spectrumBatch_j);
                simMatrix[i][j] = calcMS2Similarity(sorted_i, sorted_j, precMzTol, fragMzTol);
                simMatrix[j][i] = simMatrix[i][j];
            }
        }
        FileWriter siMatFile;
        try {
            siMatFile = new FileWriter(reader.getMGFDir() + reader.getOutputFilePrefix() + "SimilarityMatrix.csv", false);
            siMatFile.write("");
            siMatFile.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        try {
            siMatFile = new FileWriter(reader.getMGFDir() + reader.getOutputFilePrefix() + "SimilarityMatrix.csv", true);
            for (int i = 0; i < files.size(); i++) {
                siMatFile.write(files.get(i).getName());
                if (i < files.size() - 1) {
                    siMatFile.write(",");
                } else {
                    siMatFile.write("\n");
                    siMatFile.flush();
                }
            }
            for (int i = 0; i < files.size(); i++) {
                for (int j = 0; j < files.size(); j++) {
                    siMatFile.write(String.format("%.5f", simMatrix[i][j]));
                    if (j < files.size() - 1) {
                        siMatFile.write(",");
                    } else {
                        siMatFile.write("\n");
                        siMatFile.flush();
                    }
                }
            }
            siMatFile.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }
