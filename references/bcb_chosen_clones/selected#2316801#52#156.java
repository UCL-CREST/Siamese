    public JTips(String tipFile) {
        super(new BorderLayout());
        BufferedReader in = null;
        try {
            URL url = getClass().getResource(tipFile);
            if (url != null) {
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while (null != (line = in.readLine())) {
                    if (sep.equals(line)) {
                        addTip(sb.toString());
                        sb = new StringBuffer();
                    } else {
                        sb.append(line);
                        sb.append("\n");
                    }
                }
                if (sb != null && sb.length() > 1) {
                    addTip(sb.toString());
                }
            } else {
                Activator.log.warn("No tip file: " + tipFile);
            }
        } catch (Exception e) {
            Activator.log.error("Failed to load tips from " + tipFile, e);
        } finally {
            try {
                in.close();
            } catch (Exception ignored) {
            }
        }
        if (tips.size() == 0) {
            tips.add(new Tip("", "No tips found", ""));
        } else {
            Activator.log.info("loaded " + tips.size() + " tips");
        }
        html = new JTextPane();
        html.setContentType("text/html");
        html.setEditable(false);
        html.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent ev) {
                if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    URL url = ev.getURL();
                    try {
                        Util.openExternalURL(url);
                    } catch (Exception e) {
                        Activator.log.warn("Failed to open external url=" + url, e);
                    }
                }
            }
        });
        scroll = new JScrollPane(html);
        scroll.setPreferredSize(new Dimension(350, 200));
        scroll.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createLoweredBevelBorder()));
        final ActionListener nextAction = new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                setTip((tipIx + 1) % tips.size());
            }
        };
        JButton closeButton = new JButton(Strings.get("close"));
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                if (frame != null) {
                    frame.setVisible(false);
                }
            }
        });
        nextButton = new JButton(Strings.get("next_tip"));
        nextButton.addActionListener(nextAction);
        prevButton = new JButton(Strings.get("prev_tip"));
        prevButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                setTip((tipIx + tips.size() - 1) % tips.size());
            }
        });
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        ctrlPanel = new JPanel(new FlowLayout());
        ctrlPanel.add(closeButton);
        ctrlPanel.add(prevButton);
        ctrlPanel.add(nextButton);
        bottomPanel.add(ctrlPanel, BorderLayout.EAST);
        JLabel titleC = new JLabel("Did you know that...");
        titleC.setFont(new Font("Dialog", Font.BOLD, 15));
        titleC.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        topPanel.add(titleC, BorderLayout.WEST);
        JLabel icon = new JLabel(Activator.desktop.tipIcon);
        icon.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent ev) {
                nextAction.actionPerformed(null);
            }
        });
        icon.setToolTipText(nextButton.getText());
        add(icon, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        setTip((int) (Math.random() * tips.size()));
    }
