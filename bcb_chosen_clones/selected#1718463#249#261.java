        public void writeFile(OutputStream outputStream) throws IOException {
            InputStream inputStream = null;
            if (file != null) {
                try {
                    inputStream = new FileInputStream(file);
                    IOUtils.copy(inputStream, outputStream);
                } finally {
                    if (inputStream != null) {
                        IOUtils.closeQuietly(inputStream);
                    }
                }
            }
        }
