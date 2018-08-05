    private String File2String(String directory, String filename) {
        String line;
        InputStream in = null;
        try {
            File f = new File(filename);
            System.out.println("File On:>>>>>>>>>> " + f.getCanonicalPath());
            in = new FileInputStream(f);
        } catch (FileNotFoundException ex) {
            in = null;
        } catch (IOException ex) {
            in = null;
        }
        try {
            if (in == null) {
                filename = directory + "/" + filename;
                java.net.URL urlFile = ClassLoader.getSystemResource(filename);
                if (urlFile == null) {
                    System.out.println("Integrated Chips list file not found: " + filename);
                    System.exit(-1);
                }
                in = urlFile.openStream();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer xmlText = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                xmlText.append(line);
            }
            reader.close();
            return xmlText.toString();
        } catch (FileNotFoundException ex) {
            System.out.println("Integrated Chips list file not found");
            System.exit(-1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
