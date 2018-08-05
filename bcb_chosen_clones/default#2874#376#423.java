        private void doBrowseDirectoy() {
            String title = "";
            switch(m_browseTarget) {
                case 1:
                    title = OStrings.PP_BROWSE_TITLE_SOURCE;
                    break;
                case 2:
                    title = OStrings.PP_BROWSE_TITLE_TARGET;
                    break;
                case 3:
                    title = OStrings.PP_BROWSE_TITLE_GLOS;
                    break;
                case 4:
                    title = OStrings.PP_BROWSE_TITLE_TM;
                    break;
                default:
                    return;
            }
            ;
            JFileChooser browser = new JFileChooser();
            String str = OStrings.PP_BUTTON_SELECT;
            browser.setApproveButtonText(str);
            browser.setDialogTitle(title);
            browser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int res = browser.showOpenDialog(this);
            File dir = browser.getSelectedFile();
            if (dir == null) return;
            str = dir.getAbsolutePath() + File.separator;
            switch(m_browseTarget) {
                case 1:
                    m_srcRoot = str;
                    m_srcRootField.setText(m_srcRoot);
                    break;
                case 2:
                    m_locRoot = str;
                    m_locRootField.setText(m_locRoot);
                    break;
                case 3:
                    m_glosRoot = str;
                    m_glosRootField.setText(m_glosRoot);
                    break;
                case 4:
                    m_tmRoot = str;
                    m_tmRootField.setText(m_tmRoot);
                    break;
            }
            ;
        }
