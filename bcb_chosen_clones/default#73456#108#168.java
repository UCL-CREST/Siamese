    public boolean action(Event event, Object what) {
        if (event.target instanceof Button) {
            Button b = (Button) event.target;
            if (b.getLabel() == "Add Row") {
                for (int i = 0; i < 3; i++) {
                    createShape(i, Math.random(), (int) (Math.random() * 3 + 1), (int) (Math.random() * 2 + 1));
                }
                incrementShapes();
            } else if (b.getLabel() == "Get File") {
                records = new Vector();
                try {
                    PrivilegeManager privilegeManager = PrivilegeManager.getPrivilegeManager();
                    privilegeManager.enablePrivilege("UniversalFileAccess");
                    BufferedReader in = new BufferedReader(new FileReader("C:/GraphicalLogAnalyzerInputFile.txt"));
                    String oneRecord;
                    while ((oneRecord = in.readLine()) != null) {
                        records.addElement(new String(oneRecord));
                    }
                } catch (EOFException eofex) {
                    output.append("No more record in file\n");
                } catch (Exception ex) {
                    output.append("Error while reading from file\n");
                    output.append("Error is:\n" + ex);
                }
                output.append("# Records: " + records.size() + "\n");
                for (int r = 0; r < records.size(); r++) {
                    String test = (String) records.elementAt(r);
                    StringTokenizer st = new StringTokenizer(test);
                    for (int i = 0; i < 3; i++) {
                        int tempSizeInt = 0;
                        double tempSize = 0;
                        int tempColor = 0;
                        int tempShape = 0;
                        String tempSizeS = "";
                        String tempColorS = "";
                        String tempShapeS = "";
                        try {
                            if (st.hasMoreTokens()) {
                                tempSizeS = st.nextToken();
                                tempSizeInt = Integer.parseInt(tempSizeS);
                                tempSize = tempSizeInt / (double) 100.0;
                            } else output.append("Token error");
                            if (st.hasMoreTokens()) {
                                tempColorS = st.nextToken();
                                tempColor = Integer.parseInt(tempColorS);
                            } else output.append("Token error");
                            if (st.hasMoreTokens()) {
                                tempShapeS = st.nextToken();
                                tempShape = Integer.parseInt(tempShapeS);
                            } else output.append("Token error");
                        } catch (NumberFormatException nfe) {
                            output.append("Num format exception");
                        }
                        createShape(i, tempSize, tempColor, tempShape);
                    }
                    incrementShapes();
                }
            }
        }
        return true;
    }
