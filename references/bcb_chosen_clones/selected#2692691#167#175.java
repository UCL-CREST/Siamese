        public void readEntry(String name, InputStream input) throws Exception {
            File file = new File(this.directory, name);
            OutputStream output = new BufferedOutputStream(FileUtils.openOutputStream(file));
            try {
                org.apache.commons.io.IOUtils.copy(input, output);
            } finally {
                output.close();
            }
        }
