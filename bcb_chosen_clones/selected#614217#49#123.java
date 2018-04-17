    public void onUpload$btnFileUpload(UploadEvent ue) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        if (ue == null) {
            System.out.println("unable to upload file");
            return;
        } else {
            System.out.println("fileUploaded()");
        }
        try {
            Media m = ue.getMedia();
            System.out.println("m.getContentType(): " + m.getContentType());
            System.out.println("m.getFormat(): " + m.getFormat());
            try {
                InputStream is = m.getStreamData();
                in = new BufferedInputStream(is);
                File baseDir = new File(UPLOAD_PATH);
                if (!baseDir.exists()) {
                    baseDir.mkdirs();
                }
                final File file = new File(UPLOAD_PATH + m.getName());
                OutputStream fout = new FileOutputStream(file);
                out = new BufferedOutputStream(fout);
                IOUtils.copy(in, out);
                if (m.getFormat().equals("zip") || m.getFormat().equals("x-gzip")) {
                    final String filename = m.getName();
                    Messagebox.show("Archive file detected. Would you like to unzip this file?", "ALA Spatial Portal", Messagebox.YES + Messagebox.NO, Messagebox.QUESTION, new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            try {
                                int response = ((Integer) event.getData()).intValue();
                                if (response == Messagebox.YES) {
                                    System.out.println("unzipping file to: " + UPLOAD_PATH);
                                    boolean success = Zipper.unzipFile(filename, new FileInputStream(file), UPLOAD_PATH, false);
                                    if (success) {
                                        Messagebox.show("File unzipped: '" + filename + "'");
                                    } else {
                                        Messagebox.show("Unable to unzip '" + filename + "' ");
                                    }
                                } else {
                                    System.out.println("leaving archive file alone");
                                }
                            } catch (NumberFormatException nfe) {
                                System.out.println("Not a valid response");
                            }
                        }
                    });
                } else {
                    Messagebox.show("File '" + m.getName() + "' successfully uploaded");
                }
            } catch (IOException e) {
                System.out.println("IO Exception while saving file: ");
                e.printStackTrace(System.out);
            } catch (Exception e) {
                System.out.println("General Exception: ");
                e.printStackTrace(System.out);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    System.out.println("IO Exception while closing stream: ");
                    e.printStackTrace(System.out);
                }
            }
        } catch (Exception e) {
            System.out.println("Error uploading file.");
            e.printStackTrace(System.out);
        }
    }
