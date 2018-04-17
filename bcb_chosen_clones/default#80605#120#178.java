    private static JComponent processMimeBodyPart(final MimeBodyPart mbp, final JInternalFrame parent) throws MessagingException, IOException {
        NumberFormat format = NumberFormat.getIntegerInstance();
        String disposition = mbp.getDisposition();
        String fileName = mbp.getFileName();
        String contentType = mbp.getContentType();
        JComponent jc;
        if (fileName == null && !(disposition != null && disposition.equalsIgnoreCase(Part.ATTACHMENT))) {
            StringBuffer sb = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(mbp.getInputStream()));
            char[] buf = new char[4096];
            int nch;
            while ((nch = in.read(buf)) != -1) {
                sb.append(new String(buf, 0, nch));
            }
            jc = processString(sb.toString());
        } else {
            jc = new JPanel(new GridLayout(7, 2));
            jc.add(new JLabel("Disposition:"));
            jc.add(new JLabel(disposition, JLabel.RIGHT));
            jc.add(new JLabel("FileName:"));
            JButton jb;
            if (fileName == null) jb = new JButton("Save"); else {
                fileName = MimeUtility.decodeText(fileName);
                jb = new JButton(fileName);
            }
            jb.addActionListener(new SaveStreamAction(fileName, mbp.getInputStream(), parent));
            jc.add(jb);
            jc.add(new JLabel("Description:"));
            jc.add(new JLabel(mbp.getDescription(), JLabel.RIGHT));
            jc.add(new JLabel("Size:"));
            int size = mbp.getSize();
            jc.add(new JLabel(new StringBuffer(format.format(size)).append(" bytes").toString(), JLabel.RIGHT));
            jc.add(new JLabel("ContentID:"));
            jc.add(new JLabel(mbp.getContentID(), JLabel.RIGHT));
            jc.add(new JLabel("Encoding:"));
            jc.add(new JLabel(mbp.getEncoding(), JLabel.RIGHT));
            jc.add(new JLabel("Raw:"));
            jb = new JButton("Save as raw");
            jb.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Options.saveFileChooser.setSelectedFile(null);
                    int returnVal = Options.saveFileChooser.showSaveDialog(parent);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = Options.saveFileChooser.getSelectedFile();
                        try {
                            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                            mbp.writeTo(os);
                            os.close();
                        } catch (Exception ex) {
                            LogFrame.log(ex);
                        }
                    }
                }
            });
            jc.add(jb);
        }
        return jc;
    }
