    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.8;
        c.weighty = 0.9;
        JScrollPane viewScroll = new JScrollPane(viewPane);
        viewScroll.setPreferredSize(new Dimension(400, 400));
        viewScroll.setMinimumSize(new Dimension(200, 200));
        add(viewScroll, c);
        viewPane.setEditable(false);
        if (isChannel) {
            participantListModel = new DefaultListModel();
            participantList = new JList(participantListModel);
            c.gridx = 1;
            c.weightx = 0.2;
            JScrollPane participantScroll = new JScrollPane(participantList);
            participantScroll.setPreferredSize(new Dimension(100, 300));
            participantScroll.setMinimumSize(new Dimension(100, 100));
            add(participantScroll, c);
            c.gridwidth = 2;
        }
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.8;
        c.weighty = 0.1;
        JScrollPane textEntryScroll = new JScrollPane(textEntry);
        textEntryScroll.setPreferredSize(new Dimension(500, 50));
        add(textEntryScroll, c);
        textEntry.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String text = textEntry.getText();
                    if (!text.isEmpty()) {
                        processInputMessage(text);
                        textEntry.setText("");
                    }
                    e.consume();
                }
            }
        });
        if (isChannel) {
            ArrayList<User> me = new ArrayList<User>();
            User user = new User();
            user.setName(myUserName);
            user.setHost("localhost");
            me.add(user);
            addParticipants(me);
        }
        initStyles(viewPane);
        setupRightClickMenu();
        viewPane.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(URI.create(e.getDescription()));
                                textEntry.requestFocusInWindow();
                                viewPane.setCaretPosition(viewPane.getStyledDocument().getLength());
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(viewPane, messages.getString("ChatPane.msg.CouldNotLaunchDefaultBrowserSeeLogForReason"));
                                logger.log(Level.INFO, messages.getString("ChatPane.msg.CouldNotLaunchBrowser"), ex);
                            }
                        }
                    }
                }
            }
        });
    }
