    public void DVDMainMenu() {
        File ListDir = new File(m_GUI.strOutputDir);
        FilenameFilter filter = new FilenameFilter() {

            boolean ret_val = true;

            public boolean accept(File dir, String name) {
                if (name.endsWith("vob")) {
                    if (!name.equals("menu.vob")) ret_val = true; else ret_val = false;
                } else ret_val = false;
                return ret_val;
            }
        };
        video_files = ListDir.list(filter);
        m_GUI.lblAuthor.setEnabled(true);
        m_GUI.lblAuthorProg.setEnabled(true);
        m_GUI.prgAuthor.setEnabled(true);
        m_GUI.prgAuthor.setIndeterminate(true);
        String menu_options;
        if (strPicPath == null && strAudioPath == null) menu_options = " -c -D -n '" + strTitle + "' -o " + m_GUI.strOutputDir; else if (strPicPath.equals("") && strAudioPath != null) menu_options = " -c -D -n '" + strTitle + "' -o " + m_GUI.strOutputDir + " -a " + strAudioPath; else if (strPicPath != null && strAudioPath.equals("")) menu_options = " -c -D -n '" + strTitle + "' -o " + m_GUI.strOutputDir + " -b " + strPicPath; else menu_options = " -c -D -n '" + strTitle + "' -o " + m_GUI.strOutputDir + " -a " + strAudioPath + " -b " + strPicPath;
        int i = 0;
        if (strTextFilePath.equals("") || strTextFilePath == null) {
            while (i < video_files.length) {
                menu_options += " -f " + m_GUI.strOutputDir + "/" + video_files[i].substring(0, 11) + ".vob -t " + strTitle + "_" + (i + 1);
                i++;
            }
        } else {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(m_GUI.txtTextFile.getText())));
                String line;
                i = 0;
                while ((line = in.readLine()) != null) {
                    titles[i] = line;
                    i++;
                }
                int num_blanks = 0;
                for (i = 0; i < titles.length; i++) {
                    if (titles[i].equals("BLANK")) num_blanks++;
                    if (titles[i].equals("DONE")) break;
                }
                int num_lines = i;
                if ((num_lines - num_blanks) > 10) {
                    m_GUI.MessageBox("Your text file can only have a maximum" + "of 10 titles!  Please edit your text file and rerun" + "dvd-homevideo.", 1);
                    m_Error = true;
                    ;
                } else if (num_lines == video_files.length) {
                    i = 0;
                    while (i < video_files.length) {
                        if (!titles[i].equals("BLANK") && !titles[i].equals("DONE")) {
                            menu_options += " -t " + titles[i] + " -f " + m_GUI.strOutputDir + "/" + video_files[i].substring(0, 11) + ".vob";
                            i++;
                        } else if (titles[i].equals("DONE")) break; else {
                            menu_options += " -f " + m_GUI.strOutputDir + "/" + video_files[i].substring(0, 11) + ".vob";
                            i++;
                        }
                    }
                } else {
                    m_GUI.MessageBox("It appears that your text file is not\n" + "formatted correctly.  You currently have\n" + (video_files.length + 1) + " vob files, \n" + "however your text file is showing\n" + num_lines + " lines.", 1);
                    m_Error = true;
                    ;
                }
            } catch (FileNotFoundException ex) {
                SaveStackTrace.printTrace(m_GUI.strOutputDir, ex);
                m_GUI.MessageBox("Can not find " + m_GUI.txtTextFile + "\n" + ex.toString(), 0);
                ex.printStackTrace();
                m_Error = true;
            } catch (IOException ex) {
                SaveStackTrace.printTrace(m_GUI.strOutputDir, ex);
                m_GUI.MessageBox(baseErr + "IO Error\n" + ex.toString(), 0);
                ex.printStackTrace();
                m_Error = true;
            }
        }
        if (pal_menu == true) menu_options += " -p";
        dvd_menu += (menu_options + " 2>&1");
        m_GUI.txtAreaOutput.append(dvd_menu + "\n");
        try {
            String[] dvd_menu_cmd = { "/bin/sh", "-c", dvd_menu };
            Process p = Runtime.getRuntime().exec(dvd_menu_cmd, null, new File(m_GUI.strOutputDir));
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader err_in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            StringTokenizer st;
            while ((line = in.readLine()) != null) {
                if (line.equals("/bin/sh: dvd-menu: command not found")) {
                    m_GUI.MessageBox("Could not locate dvd-menu in your path." + "\nPlease install all necessary dependencies" + "\nand rerun dvd-homevideo.", 0);
                    in.close();
                    err_in.close();
                    m_Error = true;
                }
                st = new StringTokenizer(line);
                if (st.hasMoreTokens()) {
                    if (!st.nextToken().equals("Frame#")) {
                        m_GUI.txtAreaOutput.append(line + "\n");
                        m_GUI.txtAreaOutput.setCaretPosition(m_GUI.txtAreaOutput.getDocument().getLength());
                    }
                }
            }
            p.waitFor();
            p = Runtime.getRuntime().exec("mv dvd-menu.log log/", null, new File(m_GUI.strOutputDir));
            File CreateOutDir = new File(m_GUI.strOutputDir + "/dvd_fs");
            if (CreateOutDir.exists() && CreateOutDir.isDirectory()) CreateOutDir.delete();
            Thread.sleep(2000);
        } catch (IOException ex) {
            SaveStackTrace.printTrace(m_GUI.strOutputDir, ex);
            m_GUI.MessageBox(baseErr + "IO Error\n" + ex.toString(), 0);
            ex.printStackTrace();
            m_Error = true;
        } catch (InterruptedException ex) {
            SaveStackTrace.printTrace(m_GUI.strOutputDir, ex);
            m_GUI.MessageBox(baseErr + "dvd-homevideo thread was interrupted\n" + ex.toString(), 0);
            ex.printStackTrace();
            m_Error = true;
        }
    }
