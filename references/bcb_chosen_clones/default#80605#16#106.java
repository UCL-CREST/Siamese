    public MessageViewer(final MimeMessage msg, final JMenu windowMenu) {
        super(windowMenu, "<NONE>", true, true, true, true);
        final CardLayout cards = new CardLayout();
        setLayout(cards);
        try {
            JComponent firstCard = processPart(msg, this);
            add(firstCard, STD_VIEWER);
        } catch (Exception ex) {
            LogFrame.log(ex);
        }
        try {
            String raw;
            if (msg.getSize() > 1024 * 1024) {
                raw = "Message is too big!";
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                msg.writeTo(baos);
                raw = baos.toString();
            }
            JTextArea jta = new JTextArea(raw);
            jta.setFont(Options.monoFont);
            jta.setEditable(false);
            JScrollPane secondCard = new JScrollPane(jta);
            add(secondCard, RAW_VIEWER);
        } catch (Exception ex) {
            LogFrame.log(ex);
        }
        JMenuBar jmb = new JMenuBar();
        setJMenuBar(jmb);
        JMenu mMessage = new JMenu("Message");
        jmb.add(mMessage);
        final JCheckBoxMenuItem mRaw = new JCheckBoxMenuItem("Raw view");
        mMessage.add(mRaw);
        mRaw.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (mRaw.getState()) cards.show(getContentPane(), RAW_VIEWER); else cards.show(getContentPane(), STD_VIEWER);
            }
        });
        JMenuItem mReply = new JMenuItem("Reply");
        mMessage.add(mReply);
        mReply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JInternalFrame jif = new Composer(msg, windowMenu);
                jif.setSize(640, 480);
                jif.show();
                getDesktopPane().add(jif);
                try {
                    jif.setSelected(true);
                } catch (Exception ex) {
                    LogFrame.log(ex);
                }
            }
        });
        JMenuItem mForward = new JMenuItem("Forward");
        mMessage.add(mForward);
        mForward.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JInternalFrame jif = new Forward(msg, windowMenu);
                jif.pack();
                jif.show();
                getDesktopPane().add(jif);
                try {
                    jif.setSelected(true);
                } catch (Exception ex) {
                    LogFrame.log(ex);
                }
            }
        });
        JMenuItem mSaveAs = new JMenuItem("Save As...");
        mMessage.add(mSaveAs);
        mSaveAs.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Options.saveFileChooser.setSelectedFile(null);
                int returnVal = Options.saveFileChooser.showSaveDialog(MessageViewer.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = Options.saveFileChooser.getSelectedFile();
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        msg.writeTo(os);
                        os.close();
                    } catch (Exception ex) {
                        LogFrame.log(ex);
                    }
                }
            }
        });
    }
