    public void copyFile2(String src, String dest) throws IOException {
        FileWriter fw = null;
        FileReader fr = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        File source = null;
        try {
            fr = new FileReader(src);
            fw = new FileWriter(dest);
            br = new BufferedReader(fr);
            bw = new BufferedWriter(fw);
            source = new File(src);
            int fileLength = (int) source.length();
            char charBuff[] = new char[fileLength];
            while (br.read(charBuff, 0, fileLength) != -1) bw.write(charBuff, 0, fileLength);
        } catch (FileNotFoundException fnfe) {
            throw new FileCopyException(src + " " + MM.PHRASES.getPhrase("35"));
        } catch (IOException ioe) {
            throw new FileCopyException(MM.PHRASES.getPhrase("36"));
        } finally {
            try {
                if (br != null) br.close();
                if (bw != null) bw.close();
            } catch (IOException ioe) {
            }
        }
    }
