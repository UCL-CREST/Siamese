    public void run(Preprocessor pp) throws SijappException {
        for (int i = 0; i < this.filenames.length; i++) {
            File srcFile = new File(this.srcDir, this.filenames[i]);
            BufferedReader reader;
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(srcFile), "CP1251");
                reader = new BufferedReader(isr);
            } catch (Exception e) {
                throw (new SijappException("File " + srcFile.getPath() + " could not be read"));
            }
            File destFile = new File(this.destDir, this.filenames[i]);
            BufferedWriter writer;
            try {
                (new File(destFile.getParent())).mkdirs();
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(destFile), "CP1251");
                writer = new BufferedWriter(osw);
            } catch (Exception e) {
                throw (new SijappException("File " + destFile.getPath() + " could not be written"));
            }
            try {
                pp.run(reader, writer);
            } catch (SijappException e) {
                try {
                    reader.close();
                } catch (IOException f) {
                }
                try {
                    writer.close();
                } catch (IOException f) {
                }
                try {
                    destFile.delete();
                } catch (SecurityException f) {
                }
                throw (new SijappException(srcFile.getPath() + ":" + e.getMessage()));
            }
            try {
                reader.close();
            } catch (IOException e) {
            }
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }
