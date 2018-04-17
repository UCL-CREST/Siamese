            @Override
            public void actionPerformed(ActionEvent e) {
                email = loginName.getText().trim();
                pawo = loginPassword.getPassword();
                String summary = summaryField.getText().trim();
                String description = descriptionField.getText().trim();
                final String version = RapidMiner.getShortVersion();
                if (!useAnonymousLogin.isSelected()) {
                    if (email.length() <= 0) {
                        SwingTools.showVerySimpleErrorMessage("enter_email");
                        return;
                    }
                    if (!email.matches("(.+?)@(.+?)[.](.+?)")) {
                        SwingTools.showVerySimpleErrorMessage("enter_correct_email");
                        return;
                    }
                    boolean noPW = true;
                    for (char c : pawo) {
                        if (c != ' ') {
                            noPW = false;
                            break;
                        }
                    }
                    if (noPW) {
                        SwingTools.showVerySimpleErrorMessage("enter_password");
                        return;
                    }
                } else {
                    email = "bugs@rapid-i.com";
                    pawo = new char[] { '!', 'z', '4', '8', '#', 'H', 'c', '2', '$', '%', 'm', ')', '9', '+', '*', '*' };
                }
                if (summary.length() <= 0) {
                    SwingTools.showVerySimpleErrorMessage("enter_summary");
                    return;
                }
                String[] splitResult = summary.trim().split("\\s");
                if (splitResult.length < 2) {
                    SwingTools.showVerySimpleErrorMessage("enter_descriptive_summary");
                    return;
                }
                if (description.length() <= 0 || description.equals(descriptionText)) {
                    SwingTools.showVerySimpleErrorMessage("enter_description");
                    return;
                }
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            String bugzillaSearchString = "http://bugs.rapid-i.com/buglist.cgi?field0-0-0=attach_data.thedata&type0-0-1=allwordssubstr&field0-0-1=longdesc&query_format=advanced&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED&value0-0-1=" + exception.getMessage() + "&type0-0-0=allwordssubstr&value0-0-0=" + exception.getMessage();
                            URL bugzillaURL = new URL(bugzillaSearchString);
                            URI bugzillaURI = new URI(bugzillaURL.getProtocol(), bugzillaURL.getHost(), bugzillaURL.getPath(), bugzillaURL.getQuery(), null);
                            desktop.browse(bugzillaURI);
                            int returnVal = SwingTools.showConfirmDialog("send_bugreport.check_browser_for_duplicates", ConfirmDialog.YES_NO_OPTION);
                            if (returnVal == ConfirmDialog.NO_OPTION) {
                                return;
                            }
                        }
                    }
                } catch (URISyntaxException e1) {
                } catch (IOException e1) {
                }
                submitButton.setEnabled(false);
                new ProgressThread("send_report_to_bugzilla", false) {

                    @Override
                    public void run() {
                        try {
                            getProgressListener().setTotal(100);
                            ListModel model = attachments.getModel();
                            File[] attachments = new File[model.getSize()];
                            for (int i = 0; i < attachments.length; i++) {
                                attachments[i] = (File) model.getElementAt(i);
                            }
                            getProgressListener().setCompleted(20);
                            XmlRpcClient client = XmlRpcHandler.login(XmlRpcHandler.BUGZILLA_URL, email, pawo);
                            getProgressListener().setCompleted(40);
                            BugReport.createBugZillaReport(client, exception, summaryField.getText().trim(), descriptionField.getText().trim(), String.valueOf(compBox.getSelectedItem()), version, String.valueOf(severityBox.getSelectedItem()), String.valueOf(platformBox.getSelectedItem()), String.valueOf(osBox.getSelectedItem()), RapidMinerGUI.getMainFrame().getProcess(), RapidMinerGUI.getMainFrame().getMessageViewer().getLogMessage(), attachments, addProcessCheckBox.isSelected(), addSysPropsCheckBox.isSelected());
                            getProgressListener().setCompleted(100);
                            SwingTools.showMessageDialog("bugreport_successful");
                            dispose();
                        } catch (XmlRpcException e1) {
                            SwingTools.showVerySimpleErrorMessage("bugreport_xmlrpc_error", e1.getLocalizedMessage());
                        } catch (Exception e2) {
                            LogService.getRoot().warning(e2.getLocalizedMessage());
                            SwingTools.showVerySimpleErrorMessage("bugreport_creation_failed");
                        } finally {
                            getProgressListener().complete();
                            for (int i = 0; i < pawo.length; i++) {
                                pawo[i] = 0;
                            }
                            submitButton.setEnabled(true);
                        }
                    }
                }.start();
            }
