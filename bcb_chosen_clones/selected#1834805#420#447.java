    private void copyValidFile(File file, int cviceni) {
        try {
            String filename = String.format("%s%s%02d%s%s", validovane, File.separator, cviceni, File.separator, file.getName());
            boolean copy = false;
            File newFile = new File(filename);
            if (newFile.exists()) {
                if (file.lastModified() > newFile.lastModified()) copy = true; else copy = false;
            } else {
                newFile.createNewFile();
                copy = true;
            }
            if (copy) {
                String EOL = "" + (char) 0x0D + (char) 0x0A;
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                FileWriter fw = new FileWriter(newFile);
                String line;
                while ((line = br.readLine()) != null) fw.write(line + EOL);
                br.close();
                fw.close();
                newFile.setLastModified(file.lastModified());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
