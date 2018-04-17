    private static boolean prepareQualifyingFile(String completePath, String outputFile) {
        try {
            File inFile = new File(completePath + fSep + "qualifying.txt");
            FileChannel inC = new FileInputStream(inFile).getChannel();
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            File outFile = new File(completePath + fSep + "SmartGRAPE" + fSep + outputFile);
            FileChannel outC = new FileOutputStream(outFile, true).getChannel();
            boolean endOfFile = true;
            short movieName = 0;
            int customer = 0;
            while (endOfFile) {
                String line = br.readLine();
                if (line != null) {
                    if (line.indexOf(":") >= 0) {
                        movieName = new Short(line.substring(0, line.length() - 1)).shortValue();
                    } else {
                        customer = new Integer(line.substring(0, line.indexOf(","))).intValue();
                        ByteBuffer outBuf = ByteBuffer.allocate(6);
                        outBuf.putShort(movieName);
                        outBuf.putInt(customer);
                        outBuf.flip();
                        outC.write(outBuf);
                    }
                } else endOfFile = false;
            }
            br.close();
            outC.close();
            return true;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }
