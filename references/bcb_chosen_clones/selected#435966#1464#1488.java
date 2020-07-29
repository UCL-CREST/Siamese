    @Override
    public void sendErrorMessage(String message) throws EntriesException, StatementNotExecutedException, NotConnectedException, MessagingException {
        if (query == null) {
            throw new NotConnectedException();
        }
        ArrayList<String> recipients = query.getUserManager().getTecMail();
        Mail mail = new Mail(recipients);
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("log/ossobooklog.zip"));
            FileInputStream fis = new FileInputStream("log/ossobook.log");
            ZipEntry entry = new ZipEntry("ossobook.log");
            zos.putNextEntry(entry);
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = fis.read(buffer, 0, 1024)) != -1) {
                zos.write(buffer, 0, read);
            }
            zos.closeEntry();
            fis.close();
            zos.close();
            mail.sendErrorMessage(message, new File("log/ossobooklog.zip"), getUserName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
