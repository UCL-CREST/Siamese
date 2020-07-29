    public InstuctionsPanel() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double size[][] = { { 10, p }, { 10, f } };
        TableLayout layout = new TableLayout(size);
        setLayout(layout);
        setBackground(Color.white);
        JEditorPane inst = new JEditorPane();
        inst.setContentType("text/html");
        inst.setCaretPosition(0);
        inst.setEditable(false);
        inst.setText("<html>\n" + "<head>\n" + "  <title>GWrap GUI Command Line Wrapper</title>\n" + "<style type='text/css'>\n" + "  BODY {color:black; background-color:#FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size:14;}  \n" + "  A:link    {text-decoration: none; color: #000000; font-weight: bold}  \n" + "  A:visited {text-decoration: none; color: #000000; font-weight: bold}   \n" + "  A:hover   {text-decoration: none; color: #FFCC66; font-weight: bold} \n" + "  A:active  {text-decoration: none; color: #000000; font-weight: bold}  \n" + "  .ttL {	\n" + "  color: #0D2E8D; \n" + "  font-size: 20pt; \n" + "  FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;\n" + "  FONT-WEIGHT: Bold;  \n" + "} \n" + "</style>\n" + "</head>\n" + "<body>\n" + "<table border='0' width='750' cellpadding='10'>\n" + "<tr>\n" + "  <td align='center' colspan='2'>\n" + "  <FONT class='ttL'>Welcome to the GWrap Command <br>Line GUI Wrapper</FONT></td>\n" + "</tr>\n" + "<tr>\n" + "  <td>\n" + "  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + "  </td>\n" + "  <td>\n" + "  So, you don't want to learn command line programming!  No problem...\n" + "  </td>\n" + "</tr>\n" + "<tr>\n" + "  <td>\n" + "  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + "  </td>\n" + "  <td>\n" + "  Use this application to launch USeq (<a href='http://useq.sourceforge.net'>http://useq.sourceforge.net</a>) and \n" + " 	T2 (<a href='http://timat2.sourceforge.net'>http://timat2.sourceforge.net</a>)\n" + " 	command line tools in a GUI!\n" + "  </td>\n" + "</tr>\n" + "<tr>\n" + "  <td>\n" + "  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + "  </td>\n" + "  <td>\n" + "  If needed, complete the preferences, select an application to run, fill in the \n" + "  options, drag in data files and hit start.  View your job(s) status, \n" + "  results, and help documentation by selecting the appropriate menus.\n" + "  </td>\n" + "</tr>\n" + "<tr>\n" + "  <td>\n" + "  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + "  </td>\n" + "  <td>\n" + "  Note! For parameters requiring no text, enter a 'Y' to select it.\n" + "  </td>\n" + "</tr>\n" + "<tr>\n" + "  <td>\n" + "  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + "  </td>\n" + "  <td>\n" + "  Questions, comments, suggestions? <a href='http://bioserver.hci.utah.edu'>http://bioserver.hci.utah.edu</a>\n" + "  </td>\n" + "</tr>\n" + "<tr>\n" + "  <td>\n" + "  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + "  </td>\n" + "  <td>\n" + "  Authors: <a href='mailto:David.Nix@hci.utah.edu'>David.Nix@hci.utah.edu</a> and <a href='mailto:Robb.Cundick@hci.utah.edu'>Robb.Cundick@hci.utah.edu</a>\n" + "    \n" + "  </td>\n" + "</tr>\n" + "</table>\n" + "</body>\n" + "</html>   \n");
        inst.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    boolean browserLaunched = false;
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        URL u = e.getURL();
                        if (u.getProtocol().equalsIgnoreCase("mailto")) {
                            if (desktop.isSupported(Desktop.Action.MAIL)) {
                                browserLaunched = true;
                                try {
                                    desktop.mail(u.toURI());
                                } catch (IOException ioe) {
                                    browserLaunched = false;
                                } catch (URISyntaxException es) {
                                    browserLaunched = false;
                                }
                            }
                        } else {
                            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                                browserLaunched = true;
                                try {
                                    desktop.browse(u.toURI());
                                } catch (IOException ioe) {
                                    browserLaunched = false;
                                } catch (URISyntaxException es) {
                                    browserLaunched = false;
                                }
                            }
                        }
                    }
                    if (!browserLaunched) {
                        JOptionPane.showMessageDialog(null, "Sorry. The application was unable to interact with the desktop to launch the URL. Desktop functionality may not be enabled on this computer.");
                    }
                }
            }
        });
        inst.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(inst, "1, 1");
    }
