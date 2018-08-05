    public String[][] extract(String file) throws Exception {
        String[][] tabData = new String[0][0];
        try {
            FileReader FR = new FileReader(file);
            BufferedReader buff = new BufferedReader(FR);
            int nbLines = 0;
            while (buff.ready()) {
                buff.readLine();
                nbLines++;
            }
            buff.close();
            FR.close();
            tabData = new String[nbLines][nbData];
            FileReader lec = new FileReader(file);
            BufferedReader BF = new BufferedReader(lec);
            int i = 0;
            while (BF.ready()) {
                String line = BF.readLine();
                StringTokenizer tok = new StringTokenizer(line, "\t");
                for (int j = 0; j < nbData; j++) {
                    if (tok.hasMoreTokens()) tabData[i][j] = tok.nextToken();
                    bb[num_frame][num_sub_sequence].write(tabData[i][j] + " ");
                }
                bb[num_frame][num_sub_sequence].newLine();
                i++;
            }
            BF.close();
            lec.close();
        } catch (IOException e) {
            System.out.println("**********************************ERROR***********************");
            System.out.println("error in extraction of local blast");
            throw e;
        }
        return tabData;
    }
