    public static void parseFile(File file, int no) throws IOException {
        String line = "", token = "";
        BufferedReader bufferedReader = Helper.getBufferedReader(file);
        StringTokenizer tokenizer;
        int documentNo = 1;
        boolean isDocTagRead = false;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals("<doc>") || isDocTagRead) {
                if (!isDocTagRead) line = bufferedReader.readLine(); else {
                    isDocTagRead = false;
                }
                tokenizer = new StringTokenizer(line, "<>");
                tokenizer.nextToken();
                documentNo = Integer.parseInt(tokenizer.nextToken());
                if (documentNo == 0) documentNo = getDocumentNo();
            }
            line = bufferedReader.readLine();
            FileWriter fstream = new FileWriter(OUTPUTFOLDER + "\\" + documentNo + ".txt");
            BufferedWriter out = new BufferedWriter(fstream);
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("</doc>") || line.equals("<doc>")) break;
                tokenizer = new StringTokenizer(line, "<>");
                if (!tokenizer.hasMoreTokens()) continue;
                tokenizer.nextToken();
                if (!tokenizer.hasMoreTokens()) continue;
                token = tokenizer.nextToken();
                token = token.replaceAll("\\p{Punct}", "");
                token = token.toLowerCase();
                System.out.println(token);
                out.write(token + "\n");
            }
            out.close();
            fstream.close();
            if (line == null) break;
            if (line.equals("<doc>")) isDocTagRead = true;
        }
        bufferedReader.close();
    }
