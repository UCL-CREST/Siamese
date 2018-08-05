        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonCompile) {
                Process pCompile = null;
                Runtime rCompile = Runtime.getRuntime();
                Runtime rt = Runtime.getRuntime();
                try {
                    Process proc = rt.exec("javac " + OpenOrSaveFilePath);
                    InputStream stderr = proc.getErrorStream();
                    InputStreamReader isr = new InputStreamReader(stderr);
                    BufferedReader br = new BufferedReader(isr);
                    String line = null;
                    String fullList = "";
                    while ((line = br.readLine()) != null) fullList = fullList + line + "\n";
                    int exitVal = proc.waitFor();
                    System.out.println("Process exitValue: " + exitVal);
                    if (fullList != null) JOptionPane.showMessageDialog(null, fullList, "Compile errors", JOptionPane.ERROR_MESSAGE);
                    System.out.println(fullList);
                } catch (Exception ex) {
                }
            }
            if (e.getSource() == buttonRun) {
                Process pRun = null;
                Runtime rRun = Runtime.getRuntime();
                try {
                    System.out.println("trying to run the program");
                    String runName = nameOfTheFile.substring(0, nameOfTheFile.length() - 5);
                    System.out.println(runName);
                    String arg = "java -classpath" + runPathforJava + "  " + runName;
                    pRun = rRun.exec("java -classpath " + runPathforJava + " " + runName);
                    InputStream stderr = pRun.getInputStream();
                    InputStreamReader isr = new InputStreamReader(stderr);
                    BufferedReader br = new BufferedReader(isr);
                    String line = null;
                    String fullList = "";
                    while ((line = br.readLine()) != null) fullList = fullList + line + "\n";
                    int exitVal = pRun.waitFor();
                    System.out.println("Process exitValue: " + exitVal);
                    if (fullList != null) JOptionPane.showMessageDialog(null, fullList, "Output of the program", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println(fullList);
                } catch (Exception ex) {
                }
            }
        }
