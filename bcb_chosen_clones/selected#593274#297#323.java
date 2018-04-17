    private void readIntoList(URL url, Map<String, JMenuItem> list) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                int commandNameBegin = inputLine.indexOf(">") + 1;
                int commandNameEnd = inputLine.indexOf("</a>");
                JMenuItem item = new JMenuItem("<html>" + inputLine + "</html>");
                if (list == allRooms) {
                    item.setActionCommand("/room " + inputLine.substring(commandNameBegin, commandNameEnd));
                } else {
                    item.setActionCommand("/" + inputLine.substring(commandNameBegin, commandNameEnd) + " ");
                }
                item.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        jTextField1.setText(e.getActionCommand());
                        popup.setVisible(false);
                    }
                });
                list.put(inputLine.substring(commandNameBegin, commandNameEnd), item);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
