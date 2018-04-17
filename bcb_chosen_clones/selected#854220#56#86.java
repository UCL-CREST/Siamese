    private boolean copyFile(File inFile, File outFile) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(inFile));
            writer = new BufferedWriter(new FileWriter(outFile));
            while (reader.ready()) {
                writer.write(reader.read());
            }
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    return false;
                }
            }
        }
        return true;
    }
