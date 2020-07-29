    public boolean downloadNextTLE() {
        boolean success = true;
        if (!downloadINI) {
            errorText = "startTLEDownload() must be ran before downloadNextTLE() can begin";
            return false;
        }
        if (!this.hasMoreToDownload()) {
            errorText = "There are no more TLEs to download";
            return false;
        }
        int i = currentTLEindex;
        try {
            URL url = new URL(rootWeb + fileNames[i]);
            URLConnection c = url.openConnection();
            InputStreamReader isr = new InputStreamReader(c.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            File outFile = new File(localPath + fileNames[i]);
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            String currentLine = "";
            while ((currentLine = br.readLine()) != null) {
                writer.write(currentLine);
                writer.newLine();
            }
            br.close();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error Reading/Writing TLE - " + fileNames[i] + "\n" + e.toString());
            success = false;
            errorText = e.toString();
            return false;
        }
        currentTLEindex++;
        return success;
    }
