    public void copyFile(String source_file_path, String destination_file_path) {
        FileWriter fw = null;
        FileReader fr = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        File source = null;
        try {
            fr = new FileReader(source_file_path);
            fw = new FileWriter(destination_file_path);
            br = new BufferedReader(fr);
            bw = new BufferedWriter(fw);
            source = new File(source_file_path);
            int fileLength = (int) source.length();
            char charBuff[] = new char[fileLength];
            while (br.read(charBuff, 0, fileLength) != -1) bw.write(charBuff, 0, fileLength);
        } catch (FileNotFoundException fnfe) {
            System.out.println(source_file_path + " does not exist!");
        } catch (IOException ioe) {
            System.out.println("Error reading/writing files!");
        } finally {
            try {
                if (br != null) br.close();
                if (bw != null) bw.close();
            } catch (IOException ioe) {
            }
        }
    }
