    private boolean handlePart(Part p) throws MessagingException, GetterException {
        String filename = p.getFileName();
        if (!p.isMimeType("multipart/*")) {
            String disp = p.getDisposition();
            if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
                if (checkCriteria(p)) {
                    if (filename == null) filename = "Attachment" + attnum++;
                    if (result == null) {
                        try {
                            File f = File.createTempFile("amorph_pop3-", ".tmp");
                            f.deleteOnExit();
                            OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
                            InputStream is = p.getInputStream();
                            int c;
                            while ((c = is.read()) != -1) os.write(c);
                            os.close();
                            result = new FileInputStream(f);
                            System.out.println("saved attachment to file: " + f.getAbsolutePath());
                            return true;
                        } catch (IOException ex) {
                            throw new GetterException(ex, "Failed to save attachment: " + ex);
                        }
                    }
                }
            }
        }
        return false;
    }
