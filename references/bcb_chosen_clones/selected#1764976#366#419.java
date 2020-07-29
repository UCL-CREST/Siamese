    public void onUploadClicked(Event event) {
        Media[] medias = null;
        try {
            medias = Fileupload.get("Select one or more files to upload to " + "the current directory.", "Upload Files", 5);
        } catch (Exception e) {
            log.error("An exception occurred when displaying the file " + "upload dialog", e);
        }
        if (medias == null) {
            return;
        }
        for (Media media : medias) {
            String name = media.getName();
            CSPath potentialFile = model.getPathForFile(name);
            if (media.isBinary()) {
                CSPathOutputStream writer = null;
                try {
                    potentialFile.createNewFile();
                    if (potentialFile.exists()) {
                        writer = new CSPathOutputStream(potentialFile);
                        IOUtils.copy(media.getStreamData(), writer);
                    }
                } catch (IOException e) {
                    displayError("An error occurred when uploading the file " + name + ": " + e.getMessage());
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                        }
                    }
                }
            } else {
                CSPathWriter writer = null;
                try {
                    potentialFile.createNewFile();
                    if (potentialFile.exists()) {
                        writer = new CSPathWriter(potentialFile);
                        IOUtils.write(media.getStringData(), writer);
                    }
                } catch (IOException e) {
                    displayError("An error occurred when uploading the file " + name + ": " + e.getMessage());
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
            model.fileCleanup(potentialFile);
            updateFileGrid();
        }
    }
