    public static JEditorPane createURLLabel(String htmlMessage) {
        Font font = UIManager.getFont("Label.font");
        String rgb = Integer.toHexString(new JPanel().getBackground().getRGB());
        rgb = rgb.substring(2, rgb.length());
        String bodyRule = "body { background: #" + rgb + "; font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; }";
        JEditorPane jEditorPane = new JEditorPane(new HTMLEditorKit().getContentType(), htmlMessage);
        ((HTMLDocument) jEditorPane.getDocument()).getStyleSheet().addRule(bodyRule);
        jEditorPane.setEditable(false);
        jEditorPane.setBorder(null);
        jEditorPane.setOpaque(false);
        jEditorPane.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    HyperlinkEvent.EventType eventType = hyperlinkEvent.getEventType();
                    if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
                        try {
                            Desktop.getDesktop().browse(hyperlinkEvent.getURL().toURI());
                        } catch (URISyntaxException uriSyntaxException) {
                            showMessageDialog(null, new MessageFormat(Messages.getString("MessageDialog.6")).format(new Object[] { hyperlinkEvent.getURL() }), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                        } catch (IOException ioException) {
                            showMessageDialog(null, Messages.getString("MessageDialog.8"), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                        }
                    }
                } else {
                    showMessageDialog(null, Messages.getString("MessageDialog.7"), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                }
            }
        });
        return jEditorPane;
    }
