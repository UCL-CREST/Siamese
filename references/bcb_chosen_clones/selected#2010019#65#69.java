    public static void copy(FileInputStream inputStream, FileOutputStream outputStream) throws IOException {
        FileChannel input = inputStream.getChannel();
        FileChannel output = outputStream.getChannel();
        input.transferTo(0, input.size(), output);
    }
