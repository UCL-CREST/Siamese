    public static void fileDownload(String fAddress, String destinationDir) {
        int slashIndex = fAddress.lastIndexOf('/');
        int periodIndex = fAddress.lastIndexOf('.');
        String fileName = fAddress.substring(slashIndex + 1);
        URL url;
        try {
            url = new URL(fAddress);
            URLConnection uc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            File file = new File(destinationDir + "/download.pdf");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            int inputLine;
            while ((inputLine = in.read()) != -1) out.write(inputLine);
            in.close();
        } catch (Exception ex) {
            Logger.getLogger(UrlDownload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
