    private int mergeFiles(Merge merge) throws MojoExecutionException {
        String encoding = DEFAULT_ENCODING;
        if (merge.getEncoding() != null && merge.getEncoding().length() > 0) {
            encoding = merge.getEncoding();
        }
        int numMergedFiles = 0;
        Writer ostream = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(merge.getTargetFile(), true);
            ostream = new OutputStreamWriter(fos, encoding);
            BufferedWriter output = new BufferedWriter(ostream);
            for (String orderingName : this.orderingNames) {
                List<File> files = this.orderedFiles.get(orderingName);
                if (files != null) {
                    getLog().info("Appending: " + files.size() + " files that matched the name: " + orderingName + " to the target file: " + merge.getTargetFile().getAbsolutePath() + "...");
                    for (File file : files) {
                        String fileName = file.getName();
                        getLog().info("Appending file: " + fileName + " to the target file: " + merge.getTargetFile().getAbsolutePath() + "...");
                        InputStream input = null;
                        try {
                            input = new FileInputStream(file);
                            if (merge.getSeparator() != null && merge.getSeparator().trim().length() > 0) {
                                String replaced = merge.getSeparator().trim();
                                replaced = replaced.replace("\n", "");
                                replaced = replaced.replace("\t", "");
                                replaced = replaced.replace("#{file.name}", fileName);
                                replaced = replaced.replace("#{parent.name}", file.getParentFile() != null ? file.getParentFile().getName() : "");
                                replaced = replaced.replace("\\n", "\n");
                                replaced = replaced.replace("\\t", "\t");
                                getLog().debug("Appending separator: " + replaced);
                                IOUtils.copy(new StringReader(replaced), output);
                            }
                            IOUtils.copy(input, output, encoding);
                        } catch (IOException ioe) {
                            throw new MojoExecutionException("Failed to append file: " + fileName + " to output file", ioe);
                        } finally {
                            IOUtils.closeQuietly(input);
                        }
                        numMergedFiles++;
                    }
                }
            }
            output.flush();
        } catch (IOException ioe) {
            throw new MojoExecutionException("Failed to open stream file to output file: " + merge.getTargetFile().getAbsolutePath(), ioe);
        } finally {
            if (fos != null) {
                IOUtils.closeQuietly(fos);
            }
            if (ostream != null) {
                IOUtils.closeQuietly(ostream);
            }
        }
        return numMergedFiles;
    }
