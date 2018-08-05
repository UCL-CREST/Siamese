    public void loadUtilities(String fileOpenWith) {
        boolean openWith = false;
        int returnVal = 21212;
        if (((fileOpenWith.trim()).substring((fileOpenWith.trim()).length() - 4, (fileOpenWith.trim()).length())).equals(".sat")) {
            openWith = true;
        } else {
            fc = new JFileChooser();
            returnVal = fc.showOpenDialog(this);
        }
        if ((returnVal == JFileChooser.APPROVE_OPTION) || (openWith == true)) {
            try {
                FileReader fr;
                if (!openWith) {
                    File file = fc.getSelectedFile();
                    fr = new FileReader(file.getPath());
                } else {
                    File file = new File(fileOpenWith);
                    fr = new FileReader(file);
                    System.out.println(fileOpenWith);
                }
                BufferedReader br = new BufferedReader(fr);
                String line = null;
                line = br.readLine();
                int numberOfSurrogates = Integer.parseInt(line);
                surrogateList.clear();
                for (int i = 0; i < numberOfSurrogates; i++) {
                    String ttid = br.readLine();
                    String ttlabel = br.readLine();
                    String ttsort = br.readLine();
                    String tttype = br.readLine();
                    String ttantecedent1 = br.readLine();
                    boolean ttdot;
                    if (br.readLine().equals("false")) {
                        ttdot = false;
                    } else {
                        ttdot = true;
                    }
                    String ttantecedent2 = br.readLine();
                    String ttauthority1 = br.readLine();
                    String ttauthority2 = br.readLine();
                    String ttactiontime1 = br.readLine();
                    String ttactiontime2 = br.readLine();
                    String ttrecordtime1 = br.readLine();
                    String ttrecordtime2 = br.readLine();
                    int ttx = Integer.parseInt(br.readLine());
                    int tty = Integer.parseInt(br.readLine());
                    surrogate newsurrogate = new surrogate(ttid, ttlabel, ttx, tty, c.getGraphics());
                    newsurrogate.sort = ttsort;
                    newsurrogate.type = tttype;
                    newsurrogate.antecedent1 = ttantecedent1;
                    newsurrogate.dot = ttdot;
                    newsurrogate.antecedent2 = ttantecedent2;
                    newsurrogate.authority1 = ttauthority1;
                    newsurrogate.authority2 = ttauthority2;
                    newsurrogate.actiontime1 = ttactiontime1;
                    newsurrogate.actiontime2 = ttactiontime2;
                    newsurrogate.recordtime1 = ttrecordtime1;
                    newsurrogate.recordtime2 = ttrecordtime2;
                    surrogateList.add(newsurrogate);
                }
            } catch (Exception exception) {
                System.err.println("Error while reading from file");
            }
            for (int i = 0; i < surrogateList.size(); i++) {
                if (autonumber <= Integer.parseInt(((surrogate) surrogateList.get(i)).id)) {
                    autonumber = Integer.parseInt(((surrogate) surrogateList.get(i)).id) + 1;
                }
            }
        }
    }
