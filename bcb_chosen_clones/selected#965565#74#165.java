        private void importExample(boolean server) throws IOException, XMLStreamException, FactoryConfigurationError {
            InputStream example = null;
            if (server) {
                monitor.setNote(Messages.getString("ImportExampleDialog.Cont"));
                monitor.setProgress(0);
                String page = engine.getConfiguration().getProperty("example.url");
                URL url = new URL(page);
                BufferedReader rr = new BufferedReader(new InputStreamReader(url.openStream()));
                try {
                    sleep(3000);
                } catch (InterruptedException e1) {
                    Logger.getLogger(this.getClass()).debug("Internal error.", e1);
                }
                if (monitor.isCanceled()) {
                    return;
                }
                try {
                    while (rr.ready()) {
                        if (monitor.isCanceled()) {
                            return;
                        }
                        String l = rr.readLine();
                        if (example == null) {
                            int i = l.indexOf("id=\"example\"");
                            if (i > 0) {
                                l = l.substring(i + 19);
                                l = l.substring(0, l.indexOf('"'));
                                url = new URL(l);
                                example = url.openStream();
                            }
                        }
                    }
                } catch (IOException ex) {
                    throw ex;
                } finally {
                    if (rr != null) {
                        try {
                            rr.close();
                        } catch (Exception e) {
                            Logger.getLogger(this.getClass()).debug("Internal error.", e);
                        }
                    }
                }
            } else {
                InputStream is = ApplicationHelper.class.getClassLoader().getResourceAsStream("gtd-free-example.xml");
                if (is != null) {
                    example = is;
                }
            }
            if (example != null) {
                if (monitor.isCanceled()) {
                    try {
                        example.close();
                    } catch (IOException e) {
                        Logger.getLogger(this.getClass()).debug("Internal error.", e);
                    }
                    return;
                }
                monitor.setNote(Messages.getString("ImportExampleDialog.Read"));
                monitor.setProgress(1);
                model = new GTDModel(null);
                GTDDataXMLTools.importFile(model, example);
                try {
                    example.close();
                } catch (IOException e) {
                    Logger.getLogger(this.getClass()).debug("Internal error.", e);
                }
                if (monitor.isCanceled()) {
                    return;
                }
                monitor.setNote(Messages.getString("ImportExampleDialog.Imp.File"));
                monitor.setProgress(2);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        @Override
                        public void run() {
                            if (monitor.isCanceled()) {
                                return;
                            }
                            engine.getGTDModel().importData(model);
                        }
                    });
                } catch (InterruptedException e1) {
                    Logger.getLogger(this.getClass()).debug("Internal error.", e1);
                } catch (InvocationTargetException e1) {
                    Logger.getLogger(this.getClass()).debug("Internal error.", e1);
                }
            } else {
                throw new IOException("Failed to obtain remote example file.");
            }
        }
