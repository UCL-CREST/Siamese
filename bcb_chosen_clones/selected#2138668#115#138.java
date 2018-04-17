        public File nextEntry() {
            try {
                while (hasNext()) {
                    String name = waitingArchEntry.getName();
                    name = name.substring(name.indexOf("/") + 1);
                    File file = new File(targetDir.getAbsolutePath() + "/" + name);
                    if (waitingArchEntry.isDirectory()) {
                        file.mkdirs();
                        waitingArchEntry = ais.getNextEntry();
                    } else {
                        OutputStream os = new FileOutputStream(file);
                        try {
                            IOUtils.copy(ais, os);
                        } finally {
                            IOUtils.closeQuietly(os);
                        }
                        return file;
                    }
                }
            } catch (IOException e) {
                return null;
            }
            return null;
        }
