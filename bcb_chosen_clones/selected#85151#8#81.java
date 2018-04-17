    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        inputLine = in.readLine();
        String dist_metric = in.readLine();
        File outFile = new File("data.txt");
        FileWriter outw = new FileWriter(outFile);
        outw.write(inputLine);
        outw.close();
        File sample_coords = new File("sample_coords.txt");
        sample_coords.delete();
        File sp_coords = new File("sp_coords.txt");
        sp_coords.delete();
        try {
            System.out.println("Running python script...");
            System.out.println("Command: " + "python l19test.py " + "\"" + dist_metric + "\"");
            Process pr = Runtime.getRuntime().exec("python l19test.py " + dist_metric);
            BufferedReader br = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            int exitVal = pr.waitFor();
            System.out.println("Process Exit Value: " + exitVal);
            System.out.println("done.");
        } catch (Exception e) {
            System.out.println("Unable to run python script for PCoA analysis");
        }
        File myFile = new File("sp_coords.txt");
        byte[] mybytearray = new byte[(new Long(myFile.length())).intValue()];
        FileInputStream fis = new FileInputStream(myFile);
        System.out.println(".");
        System.out.println(myFile.length());
        out.writeInt((int) myFile.length());
        for (int i = 0; i < myFile.length(); i++) {
            out.writeByte(fis.read());
        }
        myFile = new File("sample_coords.txt");
        mybytearray = new byte[(int) myFile.length()];
        fis = new FileInputStream(myFile);
        fis.read(mybytearray);
        System.out.println(".");
        System.out.println(myFile.length());
        out.writeInt((int) myFile.length());
        out.write(mybytearray);
        myFile = new File("evals.txt");
        mybytearray = new byte[(new Long(myFile.length())).intValue()];
        fis = new FileInputStream(myFile);
        fis.read(mybytearray);
        System.out.println(".");
        System.out.println(myFile.length());
        out.writeInt((int) myFile.length());
        out.write(mybytearray);
        out.flush();
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
