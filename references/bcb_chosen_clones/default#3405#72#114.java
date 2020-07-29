    public void readPrefs(int wide) {
        StringTokenizer parameters;
        String str, parm;
        try {
            BufferedReader in = new BufferedReader(new FileReader("ClntComm.def"));
            while ((str = in.readLine()) != null) {
                parameters = new StringTokenizer(str);
                parm = parameters.nextToken();
                if (parm.equals("Projects")) {
                    String[] temp = new String[parameters.countTokens()];
                    for (int i = 0; parameters.hasMoreTokens(); i++) temp[i] = parameters.nextToken().replace('$', ' ');
                    projects = temp;
                } else if (parm.equals("Times")) {
                    String[] temp1 = new String[parameters.countTokens()];
                    long[] temp2 = new long[parameters.countTokens()];
                    for (int i = 0; parameters.hasMoreTokens(); i++) {
                        temp1[i] = parameters.nextToken();
                        if (temp1[i].equals("0")) temp1[i] = "0:00";
                        temp2[i] = parseTimeString(temp1[i]);
                        totalSeconds += temp2[i];
                    }
                    times = temp1;
                    secondsList = temp2;
                } else if (parm.equals("Billable")) {
                    for (int i = 0; parameters.hasMoreTokens(); i++) {
                        String bitIndex = parameters.nextToken();
                        if (!bitIndex.equals("{}")) {
                            if (bitIndex.charAt(0) == '{') {
                                int index = Integer.parseInt(bitIndex.substring(1, bitIndex.length() - 1));
                                billableFlags.set(index);
                            } else {
                                int index = Integer.parseInt(bitIndex.substring(0, bitIndex.length() - 1));
                                billableFlags.set(index);
                            }
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Error reading prefs file.");
        }
    }
