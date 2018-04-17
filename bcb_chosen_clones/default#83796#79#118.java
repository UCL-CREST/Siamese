    public void actionPerformed(java.awt.event.ActionEvent e) {
        String command = e.getActionCommand();
        ippacket = ip.get();
        tcppacket = (TCPPacket) payload.get();
        ippacket.setPayload(tcppacket);
        ippacket.doCheckSum();
        tcppacket.doCheckSum();
        if (command.equals(SAVE)) {
            try {
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    SimpleIPWriter writer = new SimpleIPWriter(chooser.getSelectedFile().getCanonicalPath());
                    writer.writePacket(ippacket);
                }
            } catch (IOException exception) {
            }
        }
        if (command.equals(LOAD)) {
            try {
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    SimpleIPReader reader = new SimpleIPReader(chooser.getSelectedFile().getCanonicalPath());
                    ip.set((IPPacket) reader.nextPacket());
                }
            } catch (IOException exception) {
            }
        }
        if (command.equals(SEND)) {
            if (pw != null) {
                pw.writePacket(ippacket);
            }
        }
        if (command.equals(REPLY)) {
            PacketCooking.reply(ippacket);
            ip.refresh();
            payload.refresh();
        }
    }
