    void writeToFile(String dir, InputStream input, String fileName) throws FileNotFoundException, IOException {
        makeDirs(dir);
        FileOutputStream fo = null;
        try {
            System.out.println(Thread.currentThread().getName() + " : " + "Writing file " + fileName + " to path " + dir);
            File file = new File(dir, fileName);
            fo = new FileOutputStream(file);
            IOUtils.copy(input, fo);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to write " + fileName);
        }
    }
