        protected void onSubmit() {
            super.onSubmit();
            if (!this.hasError()) {
                final FileUpload upload = fileUploadField.getFileUpload();
                if (upload != null) {
                    try {
                        StringWriter xmlSourceWriter = new StringWriter();
                        IOUtils.copy(upload.getInputStream(), xmlSourceWriter);
                        processSubmittedDoap(xmlSourceWriter.toString());
                    } catch (IOException e) {
                        setResponsePage(new ErrorReportPage(new UserReportableException("Unable to add doap using RDF supplied", DoapFormPage.class, e)));
                    }
                }
            }
        }
