        @Override
        public void actionPerformed(ActionEvent e) {
            MetaCampaignFile selectedCampaign = (MetaCampaignFile) metaCampaignComboBox.getSelectedItem();
            try {
                StringWriter output = new StringWriter();
                PythonInterpreter interp = new PythonInterpreter(new org.python.core.PyStringMap(), new org.python.core.PySystemState());
                interp.setOut(output);
                interp.setErr(output);
                interp.cleanup();
                String args = "import sys;sys.argv[1:]= [r'" + selectedCampaign.getFileName() + "']";
                interp.exec(args);
                interp.exec("__name__ = '__main__'");
                String s = "execfile(r'tools/TestProcedureDoc/generateTestCampaignDoc.py')";
                interp.exec(s);
                interp.cleanup();
                interp = null;
                File campaingFile = new File(selectedCampaign.getFileName());
                if (campaingFile.exists()) {
                    File resultsFile = new File(campaingFile.getParentFile().getCanonicalPath() + "/" + selectedCampaign.getCampaignName() + "-doc.html");
                    if (resultsFile.exists()) {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(resultsFile);
                        } else {
                            logger.error("Feature not supported by this platform");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Error during generation of TPO file", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (PyException ex) {
                logger.error(ex);
                JOptionPane.showMessageDialog(null, "Error during generation of TPO file\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                logger.error(ex);
                JOptionPane.showMessageDialog(null, "Error during generation of TPO file\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
            }
        }
