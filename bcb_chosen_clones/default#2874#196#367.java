        public MNewProject(JFrame par, String projFileName, int msg) {
            super(par, true);
            m_dialogOK = false;
            setSize(650, 500);
            m_projRoot = "";
            m_projInternal = "";
            m_projName = "";
            m_srcRoot = "";
            m_locRoot = "";
            m_glosRoot = "";
            m_tmRoot = "";
            m_message = msg;
            m_browseTarget = 0;
            m_messageLabel = new JLabel();
            Box bMes = Box.createHorizontalBox();
            bMes.add(m_messageLabel);
            bMes.add(Box.createHorizontalGlue());
            m_projNameLabel = new JLabel();
            Box bName = Box.createHorizontalBox();
            bName.add(m_projNameLabel);
            bName.add(Box.createHorizontalGlue());
            m_projNameField = new JTextField();
            m_projRootLabel = new JLabel();
            Box bProj = Box.createHorizontalBox();
            bProj.add(m_projRootLabel);
            bProj.add(Box.createHorizontalGlue());
            m_projRootField = new JTextField();
            m_projRootField.setEditable(false);
            m_projInternalLabel = new JLabel();
            Box bInternal = Box.createHorizontalBox();
            bInternal.add(m_projInternalLabel);
            bInternal.add(Box.createHorizontalGlue());
            m_projInternalField = new JTextField();
            m_projInternalField.setEditable(false);
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
            m_okButton = new JButton();
            m_cancelButton = new JButton();
            Box b = Box.createVerticalBox();
            b.add(bMes);
            b.add(bName);
            b.add(m_projNameField);
            b.add(bProj);
            b.add(m_projRootField);
            b.add(bInternal);
            b.add(m_projInternalField);
            b.add(bSrc);
            b.add(m_srcRootField);
            b.add(bLoc);
            b.add(m_locRootField);
            b.add(bGlos);
            b.add(m_glosRootField);
            b.add(bTM);
            b.add(m_tmRootField);
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
                JFileChooser jfc = new JFileChooser();
                String label;
                label = OStrings.PP_SAVE_PROJECT_FILE;
                jfc.setDialogTitle(label);
                int val = jfc.showOpenDialog(this);
                if (val == JFileChooser.APPROVE_OPTION) {
                    m_projFile = jfc.getSelectedFile().getAbsolutePath();
                    if ((m_projFile == null) || (m_projFile.equals(""))) {
                        m_dialogCancelled = true;
                        return;
                    }
                    if (m_projFile.endsWith(OConsts.PROJ_EXTENSION) == false) {
                        m_projFile += OConsts.PROJ_EXTENSION;
                    }
                } else {
                    m_dialogCancelled = true;
                    return;
                }
                File projDir = jfc.getCurrentDirectory();
                m_projRoot = projDir.getAbsolutePath();
                m_projRoot = verifyPathString(m_projRoot, "");
            } else {
                m_projFile = projFileName;
                m_projRoot = m_projFile.substring(0, m_projFile.lastIndexOf(File.separator));
                m_projRoot = verifyPathString(m_projRoot, "");
            }
            m_projBase = m_projFile.substring(0, m_projFile.lastIndexOf("."));
            m_projInternal = m_projRoot + OConsts.DEFAULT_INTERNAL;
            m_srcRoot = m_projRoot + OConsts.DEFAULT_SRC;
            m_locRoot = m_projRoot + OConsts.DEFAULT_LOC;
            m_glosRoot = m_projRoot + OConsts.DEFAULT_GLOS;
            m_tmRoot = m_projRoot + OConsts.DEFAULT_TM;
            m_projRootField.setText(m_projRoot);
            m_projInternalField.setText(m_projInternal);
            m_srcRootField.setText(m_srcRoot);
            m_locRootField.setText(m_locRoot);
            m_glosRootField.setText(m_glosRoot);
            m_tmRootField.setText(m_tmRoot);
            updateUIText();
        }
