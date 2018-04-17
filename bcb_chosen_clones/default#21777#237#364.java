    public void actionPerformed(ActionEvent e) {
        String pretype = null;
        String eventtype = null;
        String stNumWorks = new String();
        String arg = e.getActionCommand();
        if (arg.equals("AddSample")) {
            int iReturnValue = chooseFile.showOpenDialog(Interface.this);
            try {
                Process p = Runtime.getRuntime().exec("uname");
                BufferedInputStream buffer = new BufferedInputStream(p.getInputStream());
                DataInputStream commandResult = new DataInputStream(buffer);
                String s = null;
                s = commandResult.readLine();
                isWindows = false;
                commandResult.close();
            } catch (Exception g) {
            }
            if (iReturnValue == JFileChooser.APPROVE_OPTION) {
                String stAuthor = JOptionPane.showInputDialog("Who is the author of this document?");
                File file = chooseFile.getSelectedFile();
                File path = chooseFile.getCurrentDirectory();
                String fullPath = null;
                if (isWindows) {
                    fullPath = path.toString() + "\\" + file.getName();
                } else {
                    fullPath = path.toString() + "/" + file.getName();
                }
                myProcessor.addSampleWork(fullPath, stAuthor);
                sampFilenames.append(file.getName() + "\n");
                sampAuthors.append(stAuthor + "\n");
            }
        } else if (arg.equals("AddTesting")) {
            int iReturnValue = chooseFile.showOpenDialog(Interface.this);
            try {
                Process p = Runtime.getRuntime().exec("uname");
                BufferedInputStream buffer = new BufferedInputStream(p.getInputStream());
                DataInputStream commandResult = new DataInputStream(buffer);
                String s = null;
                s = commandResult.readLine();
                isWindows = false;
                commandResult.close();
            } catch (Exception g) {
            }
            if (iReturnValue == JFileChooser.APPROVE_OPTION) {
                File file = chooseFile.getSelectedFile();
                File path = chooseFile.getCurrentDirectory();
                String fullPath = null;
                if (isWindows) {
                    fullPath = path.toString() + "\\" + file.getName();
                } else {
                    fullPath = path.toString() + "/" + file.getName();
                }
                myProcessor.addTestingWork(fullPath, "");
                testFilenames.append(file.getName() + "\n");
            }
        } else if (arg.equals("Process")) {
            sampFilenames.setEditable(true);
            sampAuthors.setEditable(true);
            testFilenames.setEditable(true);
            testAuthors.setEditable(true);
            testAuthors.selectAll();
            testAuthors.cut();
            sampFilenames.setEditable(false);
            sampAuthors.setEditable(false);
            testFilenames.setEditable(false);
            testAuthors.setEditable(false);
            if (bWord) {
                eventtype = "Word";
            } else {
                eventtype = "Letter";
            }
            if (bYesPre) {
                pretype = "Yes";
            } else {
                pretype = "No";
            }
            if (bCross) {
                myProcessor.createData(eventtype, pretype);
                myProcessor.crossEntDistance(testAuthors);
            } else if (bLZW) {
                myProcessor.createData(eventtype, pretype);
                myProcessor.LZWDistance(testAuthors);
            }
        } else if (arg.equals("Clear")) {
            myProcessor.sampleWorks.removeAllElements();
            myProcessor.testingWorks.removeAllElements();
            sampFilenames.setEditable(true);
            sampAuthors.setEditable(true);
            testFilenames.setEditable(true);
            testAuthors.setEditable(true);
            sampFilenames.selectAll();
            sampFilenames.cut();
            testFilenames.selectAll();
            testFilenames.cut();
            sampAuthors.selectAll();
            sampAuthors.cut();
            testAuthors.selectAll();
            testAuthors.cut();
            sampFilenames.setEditable(false);
            sampAuthors.setEditable(false);
            testFilenames.setEditable(false);
            testAuthors.setEditable(false);
        } else if (arg.equals("Word")) {
            bWord = true;
            bLetter = false;
        } else if (arg.equals("Letter")) {
            bLetter = true;
            bWord = false;
        } else if (arg.equals("Yes")) {
            bYesPre = true;
            bNoPre = false;
        } else if (arg.equals("No")) {
            bNoPre = true;
            bYesPre = false;
        } else if (arg.equals("Exit")) {
            System.exit(0);
        } else if (arg.equals("Cross")) {
            bCross = true;
            bLZW = false;
        } else if (arg.equals("LZW")) {
            bCross = false;
            bLZW = true;
        } else if (arg.equals("Instructions")) {
            JOptionPane.showMessageDialog(null, "Step 1: Add Sample Documents, Entering Their Author in the Dialog Box\nStep 2: Add Testing Documents\nStep 3: Choose Processing Algorithm From Menu (Default: Cross-Entropy)\nStep 4: Choose Event Type From Menu (Default: Word)\nStep 5: Choose Preprocessing / No Preprocessing (Default: Preprocessing)\nStep 6: Click Process Documents\nStep 7: Results Will Be Displayed", "Instructions", JOptionPane.PLAIN_MESSAGE);
        } else if (arg.equals("About")) {
            JOptionPane.showMessageDialog(null, "Authorship Processor\nCoded By: John Sofko\nCOSC-494-61 Computer Stylometry\nDuquesne University\n17 July, 2004", "About", JOptionPane.PLAIN_MESSAGE);
        }
    }
