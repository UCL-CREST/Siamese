        private DataFileType[] getDataFiles(Collection<ContentToSend> contentsToSend) {
            DataFileType[] files = new DataFileType[contentsToSend.size()];
            int fileIndex = 0;
            for (ContentToSend contentToSend : contentsToSend) {
                DataFileType dataFile = DataFileType.Factory.newInstance();
                dataFile.setFilename(contentToSend.getFileName());
                dataFile.setId("D" + fileIndex);
                dataFile.setMimeType(contentToSend.getMimeType());
                dataFile.setContentType(DataFileType.ContentType.EMBEDDED_BASE_64);
                final StringWriter stringWriter = new StringWriter();
                final OutputStream encodeStream = Base64.newEncoder(stringWriter, 0, null);
                final InputStream is = contentToSend.getInputStream();
                try {
                    long sizeCopied = IOUtils.copyLarge(is, encodeStream);
                    dataFile.setSize(BigDecimal.valueOf(sizeCopied));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to get input to the file to be sent", e);
                } finally {
                    IOUtils.closeQuietly(encodeStream);
                    IOUtils.closeQuietly(is);
                }
                dataFile.setStringValue(stringWriter.toString());
                files[fileIndex++] = dataFile;
            }
            return files;
        }
