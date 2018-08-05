    @Override
    protected svm_model loadModel(InputStream inputStream) throws IOException {
        File tmpFile = File.createTempFile("tmp", ".mdl");
        FileOutputStream output = new FileOutputStream(tmpFile);
        try {
            IOUtils.copy(inputStream, output);
            return libsvm.svm.svm_load_model(tmpFile.getPath());
        } finally {
            output.close();
            tmpFile.delete();
        }
    }
