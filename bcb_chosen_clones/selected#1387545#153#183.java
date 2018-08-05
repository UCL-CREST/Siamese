    private void copyJdbcDriverToWL(final WLPropertyPage page) {
        final File url = new File(page.getDomainDirectory());
        final File lib = new File(url, "lib");
        final File mysqlLibrary = new File(lib, NexOpenUIActivator.getDefault().getMySQLDriver());
        if (!mysqlLibrary.exists()) {
            InputStream driver = null;
            FileOutputStream fos = null;
            try {
                driver = AppServerPropertyPage.toInputStream(new Path("jdbc/" + NexOpenUIActivator.getDefault().getMySQLDriver()));
                fos = new FileOutputStream(mysqlLibrary);
                IOUtils.copy(driver, fos);
            } catch (final IOException e) {
                Logger.log(Logger.ERROR, "Could not copy the MySQL Driver jar file to Bea WL", e);
                final Status status = new Status(Status.ERROR, NexOpenUIActivator.PLUGIN_ID, Status.ERROR, "Could not copy the MySQL Driver jar file to Bea WL", e);
                ErrorDialog.openError(page.getShell(), "Bea WebLogic MSQL support", "Could not copy the MySQL Driver jar file to Bea WL", status);
            } finally {
                try {
                    if (driver != null) {
                        driver.close();
                        driver = null;
                    }
                    if (fos != null) {
                        fos.flush();
                        fos.close();
                        fos = null;
                    }
                } catch (IOException e) {
                }
            }
        }
    }
