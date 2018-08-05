    private JPanel getOpisAutora() {
        if (panelOpisaAutora == null) {
            panelOpisaAutora = new JPanel();
            panelOpisaAutora.setLayout(new GridLayout(6, 1));
            JLabel jLabel = new JLabel();
            jLabel.setText("Аутор програма је студент Милан Алексић 63/02 - ЕТФ Београд");
            jLabel.setHorizontalAlignment(JLabel.CENTER);
            JLabel jLabel4 = new JLabel();
            jLabel4.setText("ВЕРЗИЈА 3 (март 2007)");
            jLabel4.setHorizontalAlignment(JLabel.CENTER);
            JLabel jLabel2 = new JLabel();
            jLabel2.setText("http://drop.to/goblin");
            jLabel2.setHorizontalAlignment(JLabel.CENTER);
            jLabel2.setForeground(Color.blue);
            jLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
            jLabel2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    new Thread(new Runnable() {

                        public void run() {
                            if (Desktop.isDesktopSupported()) {
                                Desktop desktop = Desktop.getDesktop();
                                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                                    try {
                                        desktop.browse(URI.create("http://drop.to/goblin"));
                                    } catch (Exception exc) {
                                        System.err.println("Nije omoguceno krstarenje Internetom");
                                    }
                                }
                            }
                        }
                    }).start();
                }
            });
            JLabel jLabel3 = new JLabel();
            jLabel3.setText("milan.aleksic@gmail.com");
            jLabel3.setHorizontalAlignment(JLabel.CENTER);
            jLabel3.setForeground(Color.blue);
            jLabel3.setFont(new Font("Dialog", Font.BOLD, 12));
            jLabel3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    new Thread(new Runnable() {

                        public void run() {
                            if (Desktop.isDesktopSupported()) {
                                Desktop desktop = Desktop.getDesktop();
                                if (desktop.isSupported(Desktop.Action.MAIL)) {
                                    try {
                                        desktop.mail(new URI("mailto:milan.aleksic@gmail.com"));
                                    } catch (Exception exc) {
                                        System.err.println("Nemoguce slanje elektronske poste");
                                    }
                                }
                            }
                        }
                    }).start();
                }
            });
            panelOpisaAutora.add(new JLabel(""));
            panelOpisaAutora.add(jLabel);
            panelOpisaAutora.add(jLabel4);
            panelOpisaAutora.add(jLabel2);
            panelOpisaAutora.add(jLabel3);
            panelOpisaAutora.add(new JLabel(""));
        }
        return panelOpisaAutora;
    }
