    public void transform(File inputMatrixFile, MatrixIO.Format inputFormat, File outputMatrixFile) throws IOException {
        FileChannel original = new FileInputStream(inputMatrixFile).getChannel();
        FileChannel copy = new FileOutputStream(outputMatrixFile).getChannel();
        copy.transferFrom(original, 0, original.size());
        original.close();
        copy.close();
    }
