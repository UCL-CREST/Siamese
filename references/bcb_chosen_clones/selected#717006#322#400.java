        private boolean doCSVImport(String tableName, final boolean hasHeader) {
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO ");
            sql.append(tableName + "(");
            for (int i = 0; i < colNames.size(); i++) {
                sql.append("" + colNames.get(i) + ",");
            }
            sql.setLength(sql.length() - 1);
            sql.append(") VALUES( ");
            for (int i = 0; i < colNames.size(); i++) {
                sql.append("?,");
            }
            sql.setLength(sql.length() - 1);
            sql.append(")");
            Connection conn = null;
            int lineNumber = 0;
            int colNumber = 0;
            String line[] = null;
            try {
                conn = DBExplorer.getConnection(false);
                conn.setAutoCommit(false);
                PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                for (; lineNumber < csvData.size(); lineNumber++) {
                    if (hasHeader && lineNumber == 0) continue;
                    dlg.UpdateProgressBar(lineNumber);
                    if (cancel) {
                        break;
                    }
                    line = (String[]) csvData.get(lineNumber);
                    pstmt.clearParameters();
                    for (colNumber = 0; colNumber < colTypes.size(); colNumber++) {
                        if (line[colNumber].equals("") && colNullAllowed.get(colNumber).toString().equals("true")) {
                            pstmt.setNull(colNumber + 1, Integer.parseInt(colTypeInt.get(colNumber).toString()));
                        } else {
                            pstmt.setObject(colNumber + 1, line[colNumber], Integer.parseInt(colTypeInt.get(colNumber).toString()));
                        }
                    }
                    pstmt.executeUpdate();
                }
                if (cancel) conn.rollback(); else conn.commit();
                conn.setAutoCommit(true);
                conn.close();
                conn = null;
                dialog.getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        if (!cancel) {
                            dlg.showMessage(title, "Imported " + maxsize + " rows successfully.");
                            statusLabel.setText("Import complete.");
                        } else {
                            dlg.UpdateProgressBar(0);
                            statusLabel.setText("Import aborted.");
                        }
                    }
                });
                return true;
            } catch (final Exception e) {
                if (conn != null) try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                dialog.getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        statusLabel.setText("Import failed");
                        dlg.showError(title, e.getMessage());
                        dlg.UpdateProgressBar(0);
                    }
                });
                return false;
            } finally {
                if (conn != null) try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
