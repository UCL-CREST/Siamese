        public void actionPerformed(ActionEvent ev) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(m_UrlToOpen.toURI());
                }
            } catch (Exception ex) {
                String msg = MBeanUtils.getMessage("lic_browser_error_message", ex.toString());
                log.error(msg, ex);
                JOptionPane.showMessageDialog(null, msg);
            }
        }
