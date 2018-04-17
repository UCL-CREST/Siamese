    public ItemLink(ChatLink chatLink, int leftGap, int rightGap, int vOffset) {
        super(new JLabel("[" + chatLink.getTitle() + "]"), leftGap, rightGap, vOffset);
        this.chatLink = chatLink;
        this.leftGap = leftGap;
        this.rightGap = rightGap;
        this.vOffset = vOffset;
        label = (JLabel) comp;
        allowLineBreak = false;
        label.setFont(FONT);
        label.setForeground(forgroundColor);
        label.setIconTextGap(1);
        label.setVerticalAlignment(JLabel.TOP);
        label.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                long delay = System.currentTimeMillis() - lastClicked;
                if (e.getButton() == MouseEvent.BUTTON1 && delay > 1000) {
                    e.consume();
                    lastClicked = System.currentTimeMillis();
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(new URI("http://www.lootdb.com/eq2/item/" + ItemLink.this.chatLink.getId()));
                            } catch (Exception ex) {
                                System.err.println(ex.getMessage());
                            }
                        }
                    }
                }
            }

            public void mouseEntered(MouseEvent e) {
                forgroundColor = label.getForeground();
                label.setForeground(mouseOverForgroundColor);
            }

            public void mouseExited(MouseEvent e) {
                label.setForeground(forgroundColor);
            }

            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    e.consume();
                    popupMenu.show(ItemLink.this.label, e.getX(), e.getY());
                }
            }
        });
        popupMenu.add(copyItemLinkItem);
        copyItemLinkItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard systemClip = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection(ItemLink.this.chatLink.getCompleteLink());
                systemClip.setContents(stringSelection, stringSelection);
            }
        });
        if (httpIcon == null) {
            httpIcon = new ImageIcon(getClass().getResource("orb.png"));
        }
        label.setIcon(httpIcon);
    }
