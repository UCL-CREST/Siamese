    public static void main(String[] args) {
        File base = new File("C:/Users/Sam/Convert");
        File output = new File("C:/Users/Sam/Convert/Output");
        base.mkdirs();
        output.mkdirs();
        for (File file : base.listFiles()) {
            if (file.isDirectory() || file.isHidden() || file.getName().charAt(0) == '.') continue;
            File outputFile = new File(output.getAbsolutePath() + "/" + file.getName());
            if (outputFile.exists()) outputFile.delete();
            convertSVG(file, outputFile);
        }
        System.out.println("Done set");
    }
