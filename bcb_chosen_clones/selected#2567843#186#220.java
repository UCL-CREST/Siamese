    public void concatFiles() throws IOException {
        Writer writer = null;
        try {
            final File targetFile = new File(getTargetDirectory(), getTargetFile());
            targetFile.getParentFile().mkdirs();
            if (null != getEncoding()) {
                getLog().info("Writing aggregated file with encoding '" + getEncoding() + "'");
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), getEncoding()));
            } else {
                getLog().info("WARNING: writing aggregated file with system encoding");
                writer = new FileWriter(targetFile);
            }
            for (File file : getFiles()) {
                Reader reader = null;
                try {
                    if (null != getEncoding()) {
                        getLog().info("Reading file " + file.getCanonicalPath() + " with encoding  '" + getEncoding() + "'");
                        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), getEncoding()));
                    } else {
                        getLog().info("WARNING: Reading file " + file.getCanonicalPath() + " with system encoding");
                        reader = new FileReader(file);
                    }
                    IOUtils.copy(reader, writer);
                    final String delimiter = getDelimiter();
                    if (delimiter != null) {
                        writer.write(delimiter.toCharArray());
                    }
                } finally {
                    IOUtils.closeQuietly(reader);
                }
            }
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
