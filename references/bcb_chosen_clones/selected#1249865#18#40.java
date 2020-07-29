    public static void main(String[] args) {
        String logFileName = args[0];
        int extractLineEvery = new Integer(args[1]).intValue();
        String filterToken = "P0";
        if (args.length > 2) {
            filterToken = args[2];
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(logFileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(logFileName + ".trim")));
            String readLine;
            int x = 0;
            while ((readLine = br.readLine()) != null) {
                if ((x++ % extractLineEvery == 0) && readLine.startsWith(filterToken)) {
                    bw.write(readLine + "\n");
                }
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
