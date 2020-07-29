    public static void main(String[] a) {
        ArrayList<String> allFilesToBeCopied = new ArrayList<String>();
        new File(outputDir).mkdirs();
        try {
            FileReader fis = new FileReader(completeFileWithDirToCathFileList);
            BufferedReader bis = new BufferedReader(fis);
            String line = "";
            String currentCombo = "";
            while ((line = bis.readLine()) != null) {
                String[] allEntries = line.split("\\s+");
                String fileName = allEntries[0];
                String thisCombo = allEntries[1] + allEntries[2] + allEntries[3] + allEntries[4];
                if (currentCombo.equals(thisCombo)) {
                } else {
                    System.out.println("merke: " + fileName);
                    allFilesToBeCopied.add(fileName);
                    currentCombo = thisCombo;
                }
            }
            System.out.println(allFilesToBeCopied.size());
            for (String file : allFilesToBeCopied) {
                try {
                    FileChannel srcChannel = new FileInputStream(CathDir + file).getChannel();
                    FileChannel dstChannel = new FileOutputStream(outputDir + file).getChannel();
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                    srcChannel.close();
                    dstChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
