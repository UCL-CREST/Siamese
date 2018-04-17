        public MNewProject(JFrame par, String projFileName, int msg) {
            super(par, true);
            m_dialogOK = false;
            setSize(650, 500);
            if (projFileName == null) reset();
            m_message = msg;
            if (projFileName == null) {
                m_srcLang = CommandThread.core.getPreference(OConsts.PREF_SRCLANG);
                m_locLang = CommandThread.core.getPreference(OConsts.PREF_LOCLANG);
                if (m_srcLang.equals("")) m_srcLang = "EN-US";
                if (m_locLang.equals("")) m_locLang = "ES";
            }
            m_browseTarget = 0;
            m_messageLabel = new JLabel();
            Box bMes = Box.createHorizontalBox();
            bMes.add(m_messageLabel);
            bMes.add(Box.createHorizontalGlue());
            m_srcRootLabel = new JLabel();
            Box bSrc = Box.createHorizontalBox();
            bSrc.add(m_srcRootLabel);
            bSrc.add(Box.createHorizontalGlue());
            m_srcBrowse = new JButton();
            bSrc.add(m_srcBrowse);
            m_srcRootField = new JTextField();
            m_srcRootField.setEditable(false);
            m_locRootLabel = new JLabel();
            Box bLoc = Box.createHorizontalBox();
            bLoc.add(m_locRootLabel);
            bLoc.add(Box.createHorizontalGlue());
            m_locBrowse = new JButton();
            bLoc.add(m_locBrowse);
            m_locRootField = new JTextField();
            m_locRootField.setEditable(false);
            m_glosRootLabel = new JLabel();
            Box bGlos = Box.createHorizontalBox();
            bGlos.add(m_glosRootLabel);
            bGlos.add(Box.createHorizontalGlue());
            m_glosBrowse = new JButton();
            bGlos.add(m_glosBrowse);
            m_glosRootField = new JTextField();
            m_glosRootField.setEditable(false);
            m_tmRootLabel = new JLabel();
            Box bTM = Box.createHorizontalBox();
            bTM.add(m_tmRootLabel);
            bTM.add(Box.createHorizontalGlue());
            m_tmBrowse = new JButton();
            bTM.add(m_tmBrowse);
            m_tmRootField = new JTextField();
            m_tmRootField.setEditable(false);
            m_srcLangLabel = new JLabel();
            Box bSL = Box.createHorizontalBox();
            bSL.add(m_srcLangLabel);
            bSL.add(Box.createHorizontalGlue());
            m_srcLangField = new JTextField();
            m_srcLangField.setText(m_srcLang);
            m_locLangLabel = new JLabel();
            Box bLL = Box.createHorizontalBox();
            bLL.add(m_locLangLabel);
            bLL.add(Box.createHorizontalGlue());
            m_locLangField = new JTextField();
            m_locLangField.setText(m_locLang);
            m_okButton = new JButton();
            m_cancelButton = new JButton();
            Box b = Box.createVerticalBox();
            b.add(bMes);
            b.add(bSrc);
            b.add(m_srcRootField);
            b.add(bLoc);
            b.add(m_locRootField);
            b.add(bGlos);
            b.add(m_glosRootField);
            b.add(bTM);
            b.add(m_tmRootField);
            b.add(bSL);
            b.add(m_srcLangField);
            b.add(bLL);
            b.add(m_locLangField);
            getContentPane().add(b, "North");
            Box b2 = Box.createHorizontalBox();
            b2.add(Box.createHorizontalGlue());
            b2.add(m_cancelButton);
            b2.add(Box.createHorizontalStrut(5));
            b2.add(m_okButton);
            getContentPane().add(b2, "South");
            m_okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    doOK();
                }
            });
            m_cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    doCancel();
                }
            });
            m_srcBrowse.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    m_browseTarget = 1;
                    doBrowseDirectoy();
                }
            });
            m_locBrowse.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    m_browseTarget = 2;
                    doBrowseDirectoy();
                }
            });
            m_glosBrowse.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    m_browseTarget = 3;
                    doBrowseDirectoy();
                }
            });
            m_tmBrowse.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    m_browseTarget = 4;
                    doBrowseDirectoy();
                }
            });
            if (projFileName == null) {
                NewDirectoryChooser ndc = new NewDirectoryChooser();
                String label;
                label = OStrings.PP_SAVE_PROJECT_FILE;
                ndc.setDialogTitle(label);
                String curDir = CommandThread.core.getPreference(OConsts.PREF_CUR_DIR);
                if (curDir != null) {
                    File dir = new File(curDir);
                    if (dir.exists() && dir.isDirectory()) {
                        ndc.setCurrentDirectory(dir);
                    }
                }
                int val = ndc.showSaveDialog(this);
                if (val != JFileChooser.APPROVE_OPTION) {
                    m_dialogCancelled = true;
                    return;
                }
                m_projRoot = ndc.getSelectedFile().getAbsolutePath() + File.separator;
                m_projFile = m_projRoot + OConsts.PROJ_FILENAME;
                CommandThread.core.setPreference(OConsts.PREF_CUR_DIR, ndc.getSelectedFile().getParent());
                m_projName = m_projFile.substring(m_projRoot.length());
                m_srcRoot = m_projRoot + OConsts.DEFAULT_SRC + File.separator;
                m_locRoot = m_projRoot + OConsts.DEFAULT_LOC + File.separator;
                m_glosRoot = m_projRoot + OConsts.DEFAULT_GLOS + File.separator;
                m_tmRoot = m_projRoot + OConsts.DEFAULT_TM + File.separator;
            } else {
                m_projFile = projFileName;
                m_projRoot = m_projFile.substring(0, m_projFile.lastIndexOf(File.separator));
            }
            m_projInternal = m_projRoot + OConsts.DEFAULT_INTERNAL + File.separator;
            m_srcRootField.setText(m_srcRoot);
            m_locRootField.setText(m_locRoot);
            m_glosRootField.setText(m_glosRoot);
            m_tmRootField.setText(m_tmRoot);
            m_srcLangField.setText(m_srcLang);
            m_locLangField.setText(m_locLang);
            updateUIText();
        }
