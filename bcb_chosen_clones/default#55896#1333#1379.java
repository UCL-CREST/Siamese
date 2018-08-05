    public void ReadProjProperties() {
        File home = new File(System.getProperty("user.home") + "/.dvd-homevideo/properties");
        String line, token;
        StringTokenizer st;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(home));
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line, "<>");
                while (st.hasMoreTokens()) {
                    token = st.nextToken();
                    if (token.equals("menuRdNTSC")) {
                        line = reader.readLine();
                        st = new StringTokenizer(line, "<>");
                        st.nextToken();
                        st.nextToken();
                        token = st.nextToken();
                        menuRdNTSC.setSelected(Boolean.parseBoolean(token));
                        menuRdPAL.setSelected(!Boolean.parseBoolean(token));
                        rdNTSC.setSelected(menuRdNTSC.isSelected());
                        rdPAL.setSelected(menuRdPAL.isSelected());
                    } else if (token.equals("menuRd_4_3")) {
                        line = reader.readLine();
                        st = new StringTokenizer(line, "<>");
                        st.nextToken();
                        st.nextToken();
                        token = st.nextToken();
                        menuRd_4_3.setSelected(Boolean.parseBoolean(token));
                        menuRd_16_9.setSelected(!Boolean.parseBoolean(token));
                        rd4_3.setSelected(menuRd_4_3.isSelected());
                        rd16_9.setSelected(menuRd_16_9.isSelected());
                    } else if (token.equals("menuChkThread")) {
                        line = reader.readLine();
                        st = new StringTokenizer(line, "<>");
                        st.nextToken();
                        st.nextToken();
                        token = st.nextToken();
                        menuChkThread.setSelected(Boolean.parseBoolean(token));
                    }
                }
            }
            reader.close();
        } catch (IOException ex) {
            SaveStackTrace.printTrace(strOutputDir, ex);
            MessageBox("IO Error in ReadProjectProperties in GUI.java\n" + ex.toString(), 0);
            ex.printStackTrace();
        }
    }
