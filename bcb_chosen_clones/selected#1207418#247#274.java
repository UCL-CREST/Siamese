        @Override
        public void run() {
            updateStatus("manreports.downloading", UIHelper.getErrorMsgForeground());
            try {
                GeneratedReportDataVO reportWithData = AdminInterfacesFactory.getGeneratedReportsManager().getReport(report.getId());
                FileOutputStream fos = new FileOutputStream(outputFile);
                fos.write(reportWithData.getData());
                fos.close();
                if (open) {
                    updateStatus("manreports.openingreport", UIHelper.getNormalMsgForeground());
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(outputFile);
                        closeWindow();
                    } else {
                        updateStatus("manreports.desktopnotsupported", UIHelper.getErrorMsgForeground());
                    }
                } else {
                    JOptionPane.showMessageDialog(thisPanel, UIHelper.getText("manreports.filedownloadedmsg") + ": " + outputFile, UIHelper.getText("manreports.filedownloadedtitle"), JOptionPane.INFORMATION_MESSAGE);
                }
                updateStatus("manreports.downloadcomplete", UIHelper.getNormalMsgForeground());
            } catch (IOException e1) {
                LocalLog.getLogger().log(Level.SEVERE, "Error occured when writing report to file : " + e1.getMessage(), e1);
                AdminUIUtils.showErrorMsg("manreports.errorwritingtofile", thisPanel);
            } catch (AuthorizationDeniedException_Exception e) {
                LocalLog.getLogger().log(Level.SEVERE, "Error not authorized to download report: " + e.getMessage(), e);
                AdminUIUtils.showErrorMsg("manreports.errornotauthtoreport", thisPanel);
            }
        }
