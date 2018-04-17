    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Specify name of the file, just one entry per line");
            System.exit(0);
        }
        File inFile = new File(args[0]);
        BufferedReader myBR = null;
        File outFile = new File(args[0] + ".xml");
        BufferedWriter myBW = null;
        try {
            myBR = new BufferedReader(new FileReader(inFile));
            myBW = new BufferedWriter(new FileWriter(outFile));
        } catch (Exception ex) {
            System.out.println("IN: " + inFile.getAbsolutePath());
            System.out.println("OUT: " + outFile.getAbsolutePath());
            ex.printStackTrace();
            System.exit(0);
        }
        try {
            String readLine;
            while ((readLine = myBR.readLine()) != null) {
                myBW.write("<dbColumn name=\"" + readLine + "\" display=\"" + readLine + "\" panel=\"CENTER\"  >");
                myBW.write("\n");
                myBW.write("<dbType name=\"text\" maxVal=\"10\" defaultVal=\"\" sizeX=\"5\"/>");
                myBW.write("\n");
                myBW.write("</dbColumn>");
                myBW.write("\n");
            }
            myBW.close();
            myBR.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        System.out.println("OUT: " + outFile.getAbsolutePath());
        System.out.println("erzeugt");
    }
