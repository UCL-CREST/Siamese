    public void LZWDistance(JTextArea results) {
        int iNumSampleWorks = sampleWorks.size();
        int iNumTestingWorks = testingWorks.size();
        Document currSampleWork = null;
        Document currTestingWork = null;
        Vector LZWResults = new Vector();
        for (int iCounter2 = 0; iCounter2 < iNumTestingWorks; iCounter2++) {
            currTestingWork = (Document) testingWorks.elementAt(iCounter2);
            for (int iCounter = 0; iCounter < iNumSampleWorks; iCounter++) {
                currSampleWork = (Document) sampleWorks.elementAt(iCounter);
                String tempString = null;
                try {
                    PrintWriter testingText = new PrintWriter(new BufferedWriter(new FileWriter("testingText.txt")));
                    for (int iCounter3 = 0; iCounter3 < currTestingWork.unsortedTop50.size(); iCounter3++) {
                        tempString = (String) currTestingWork.unsortedTop50.elementAt(iCounter3);
                        if (tempString.length() > 1) {
                            testingText.print(tempString + " ");
                        } else {
                            testingText.print(tempString);
                        }
                    }
                    testingText.close();
                } catch (Exception e) {
                }
                try {
                    PrintWriter sampleText = new PrintWriter(new BufferedWriter(new FileWriter("sampleText.txt")));
                    for (int iCounter3 = 0; iCounter3 < currSampleWork.unsortedTop50.size(); iCounter3++) {
                        tempString = (String) currSampleWork.unsortedTop50.elementAt(iCounter3);
                        if (tempString.length() > 1) {
                            sampleText.print(tempString + " ");
                        } else {
                            sampleText.print(tempString);
                        }
                    }
                    sampleText.close();
                } catch (Exception e) {
                }
                try {
                    PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter("tempfile.txt")));
                    Process p = Runtime.getRuntime().exec("cat testingText.txt sampleText.txt");
                    BufferedInputStream buffer = new BufferedInputStream(p.getInputStream());
                    DataInputStream commandResult = new DataInputStream(buffer);
                    String s = null;
                    try {
                        while ((s = commandResult.readLine()) != null) output.print(s);
                        commandResult.close();
                        output.close();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
                try {
                    Process p = Runtime.getRuntime().exec("gzip -v -9 tempfile.txt");
                    BufferedInputStream buffer = new BufferedInputStream(p.getInputStream());
                    DataInputStream commandResult = new DataInputStream(buffer);
                    String s = null;
                    try {
                        while ((s = commandResult.readLine()) != null) System.out.println(s);
                        commandResult.close();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
                try {
                    Process p = Runtime.getRuntime().exec("gzip -l tempfile.txt.gz");
                    BufferedInputStream buffer = new BufferedInputStream(p.getInputStream());
                    DataInputStream commandResult = new DataInputStream(buffer);
                    String s = null;
                    try {
                        while ((s = commandResult.readLine()) != null) {
                            StringTokenizer line = new StringTokenizer(s);
                            while (line.hasMoreTokens()) {
                                LZWResults.addElement(line.nextToken());
                            }
                        }
                        commandResult.close();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
                try {
                    Process p = Runtime.getRuntime().exec("rm tempfile.txt.gz");
                    BufferedInputStream buffer = new BufferedInputStream(p.getInputStream());
                    DataInputStream commandResult = new DataInputStream(buffer);
                    String s = null;
                    try {
                        while ((s = commandResult.readLine()) != null) {
                            StringTokenizer line = new StringTokenizer(s);
                            while (line.hasMoreTokens()) {
                                LZWResults.addElement(line.nextToken());
                            }
                        }
                        commandResult.close();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
                try {
                    Process p = Runtime.getRuntime().exec("rm tempfile.txt");
                    BufferedInputStream buffer = new BufferedInputStream(p.getInputStream());
                    DataInputStream commandResult = new DataInputStream(buffer);
                    String s = null;
                    try {
                        while ((s = commandResult.readLine()) != null) {
                            StringTokenizer line = new StringTokenizer(s);
                            while (line.hasMoreTokens()) {
                                LZWResults.addElement(line.nextToken());
                            }
                        }
                        commandResult.close();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
                try {
                    Process p = Runtime.getRuntime().exec("rm testingText.txt");
                    BufferedInputStream buffer = new BufferedInputStream(p.getInputStream());
                    DataInputStream commandResult = new DataInputStream(buffer);
                    String s = null;
                    try {
                        while ((s = commandResult.readLine()) != null) {
                            StringTokenizer line = new StringTokenizer(s);
                            while (line.hasMoreTokens()) {
                                LZWResults.addElement(line.nextToken());
                            }
                        }
                        commandResult.close();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
                try {
                    Process p = Runtime.getRuntime().exec("rm sampleText.txt");
                    BufferedInputStream buffer = new BufferedInputStream(p.getInputStream());
                    DataInputStream commandResult = new DataInputStream(buffer);
                    String s = null;
                    try {
                        while ((s = commandResult.readLine()) != null) {
                            StringTokenizer line = new StringTokenizer(s);
                            while (line.hasMoreTokens()) {
                                LZWResults.addElement(line.nextToken());
                            }
                        }
                        commandResult.close();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
                String stResult = (String) LZWResults.elementAt(6);
                String stTrimmedResult = stResult.substring(0, (stResult.length() - 1));
                double distance = 0.0;
                currSampleWork.dDistance = Double.valueOf(stTrimmedResult).doubleValue();
                LZWResults.removeAllElements();
            }
            double dHighestPercent = 0.0;
            String stMatchingAuthor = "";
            for (int iCounter4 = 0; iCounter4 < iNumSampleWorks; iCounter4++) {
                Double dTemp = new Double(displayDistance(iCounter4));
                if (dTemp.doubleValue() > dHighestPercent) {
                    dHighestPercent = dTemp.doubleValue();
                    stMatchingAuthor = displayAuthor(iCounter4);
                }
            }
            results.append(stMatchingAuthor + "\n");
        }
    }
