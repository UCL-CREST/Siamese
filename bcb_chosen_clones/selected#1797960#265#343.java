        Slideshow(Vector<Item> items) {
            super((JFrame) null, "Slideshow", true);
            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            this.items = items;
            this.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    Slideshow.this.setVisible(false);
                    Slideshow.this.dispose();
                }
            });
            this.next = new AbstractAction("Next") {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    setImageIndex(imageIndex + 1);
                }
            };
            this.prev = new AbstractAction("Prev") {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    setImageIndex(imageIndex - 1);
                }
            };
            JPanel top = new JPanel(new BorderLayout());
            top.add(new JButton(this.prev), BorderLayout.WEST);
            top.add(new JButton(this.next), BorderLayout.EAST);
            JPanel content = new JPanel(new BorderLayout());
            setContentPane(content);
            content.add(top, BorderLayout.NORTH);
            JScrollBar bar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 1, 0, items.size() - 1);
            top.add(bar);
            bar.addAdjustmentListener(new AdjustmentListener() {

                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    if (e.getValueIsAdjusting()) return;
                    setImageIndex(e.getValue());
                }
            });
            this.label = new JLabel();
            this.label.setOpaque(true);
            this.label.setBackground(Color.BLACK);
            this.label.setHorizontalAlignment(JLabel.CENTER);
            content.add(new JScrollPane(this.label), BorderLayout.CENTER);
            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.TRAILING));
            content.add(bottom, BorderLayout.SOUTH);
            bottom.add(new JButton(new AbstractAction("Open URL") {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!Desktop.isDesktopSupported()) return;
                    try {
                        Desktop.getDesktop().browse(URI.create(Slideshow.this.items.elementAt(imageIndex).link));
                    } catch (Exception e2) {
                        JOptionPane.showMessageDialog(Slideshow.this, e2.getMessage());
                    }
                }
            }));
            bottom.add(new JButton(new AbstractAction("Close") {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    Slideshow.this.setVisible(false);
                }
            }));
            SwingUtils.center(this, 100);
            setImageIndex(0);
        }
