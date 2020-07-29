    private static void cut() {
        File inputFile = new File(inputFileName);
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), inputCharSet));
        } catch (FileNotFoundException e) {
            System.err.print("Invalid File Name!");
            System.err.flush();
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.err.print("Invalid Char Set Name!");
            System.err.flush();
            System.exit(1);
        }
        switch(cutMode) {
            case charMode:
                {
                    int outputFileIndex = 1;
                    char[] readBuf = new char[charPerFile];
                    while (true) {
                        int readCount = 0;
                        try {
                            readCount = in.read(readBuf);
                        } catch (IOException e) {
                            System.err.println("Read IO Error!");
                            System.err.flush();
                            System.exit(1);
                        }
                        if (-1 == readCount) break; else {
                            try {
                                int ppos = inputFileName.lastIndexOf(".");
                                String prefixInputFileName = inputFileName.substring(0, ppos);
                                String postfixInputFileName = "html";
                                DecimalFormat outputFileIndexFormat = new DecimalFormat("0000");
                                File outputFile = new File(prefixInputFileName + "-" + outputFileIndexFormat.format(outputFileIndex) + "." + postfixInputFileName);
                                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), outputCharSet));
                                out.write(readBuf, 0, readCount);
                                out.flush();
                                out.close();
                                outputFileIndex++;
                            } catch (IOException e) {
                                System.err.println("Write IO Error!");
                                System.err.flush();
                                System.exit(1);
                            }
                        }
                    }
                    break;
                }
            case lineMode:
                {
                    boolean isFileEnd = false;
                    int outputFileIndex = 1;
                    while (!isFileEnd) {
                        try {
                            int ppos = inputFileName.lastIndexOf(".");
                            String prefixInputFileName = inputFileName.substring(0, ppos);
                            String postfixInputFileName = inputFileName.substring(ppos + 1);
                            DecimalFormat outputFileIndexFormat = new DecimalFormat("0000");
                            File outputFile = new File(prefixInputFileName + outputFileIndexFormat.format(outputFileIndex) + "." + postfixInputFileName);
                            PrintStream out = new PrintStream(new FileOutputStream(outputFile), false, outputCharSet);
                            int p = 0;
                            while (p < linePerFile) {
                                String line = in.readLine();
                                if (null == line) {
                                    isFileEnd = true;
                                    break;
                                }
                                out.println(line);
                                ++p;
                            }
                            out.flush();
                            out.close();
                        } catch (IOException e) {
                            System.err.println("Write IO Error!");
                            System.err.flush();
                            System.exit(1);
                        }
                        ++outputFileIndex;
                    }
                    break;
                }
            case htmlMode:
                {
                    boolean isFileEnd = false;
                    int outputFileIndex = 1;
                    int ppos = inputFileName.lastIndexOf(".");
                    String prefixInputFileName = inputFileName.substring(0, ppos);
                    String postfixInputFileName = "html";
                    DecimalFormat df = new DecimalFormat("0000");
                    while (!isFileEnd) {
                        try {
                            File outputFile = new File(prefixInputFileName + "-" + df.format(outputFileIndex) + "." + postfixInputFileName);
                            PrintStream out = new PrintStream(new FileOutputStream(outputFile), false, outputCharSet);
                            out.println("<html><head><title>" + prefixInputFileName + "-" + df.format(outputFileIndex) + "</title>" + "<meta http-equiv=\"Content-Type\"" + " content=\"text/html; " + "charset=" + outputCharSet + "\" />" + "<link rel =\"stylesheet\" " + "type=\"text/css\" " + "href=\"stylesheet.css\" />" + "</head><body><div id=\"content\">");
                            int p = 0;
                            while (p < pPerFile) {
                                String line = in.readLine();
                                if (null == line) {
                                    isFileEnd = true;
                                    break;
                                }
                                if (line.length() > 0) out.println("<p>" + line + "</p>");
                                ++p;
                            }
                            out.println("</div><a href=\"" + prefixInputFileName + "-" + df.format(outputFileIndex + 1) + "." + postfixInputFileName + "\">NEXT</a></body></html>");
                            out.flush();
                            out.close();
                        } catch (IOException e) {
                            System.err.println("Write IO Error!");
                            System.err.flush();
                            System.exit(1);
                        }
                        ++outputFileIndex;
                    }
                    try {
                        File indexFile = new File("index.html");
                        PrintStream out = new PrintStream(new FileOutputStream(indexFile), false, outputCharSet);
                        out.println("<html><head><title>" + "Index" + "</title>" + "<meta http-equiv=\"Content-Type\"" + " content=\"text/html; " + "charset=" + outputCharSet + "\" />" + "<link rel =\"stylesheet\" " + "type=\"text/css\" " + "href=\"stylesheet.css\" />" + "</head><body><h2>" + htmlTitle + "</h2><div id=\"content\"><ul>");
                        for (int i = 1; i < outputFileIndex; i++) {
                            out.println("<li><a href=\"" + prefixInputFileName + "-" + df.format(i) + "." + postfixInputFileName + "\">" + df.format(i) + "</a></li>");
                        }
                        out.println("</ul></body></html>");
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        System.err.println("Write IO Error!");
                        System.err.flush();
                        System.exit(1);
                    }
                    break;
                }
        }
    }
