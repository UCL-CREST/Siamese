    public static void copyResource(Resource source, Resource dest, FilterSetCollection filters, Vector filterChains, boolean overwrite, boolean preserveLastModified, boolean append, String inputEncoding, String outputEncoding, Project project) throws IOException {
        if (!(overwrite || SelectorUtils.isOutOfDate(source, dest, FileUtils.getFileUtils().getFileTimestampGranularity()))) {
            return;
        }
        final boolean filterSetsAvailable = (filters != null && filters.hasFilters());
        final boolean filterChainsAvailable = (filterChains != null && filterChains.size() > 0);
        if (filterSetsAvailable) {
            BufferedReader in = null;
            BufferedWriter out = null;
            try {
                InputStreamReader isr = null;
                if (inputEncoding == null) {
                    isr = new InputStreamReader(source.getInputStream());
                } else {
                    isr = new InputStreamReader(source.getInputStream(), inputEncoding);
                }
                in = new BufferedReader(isr);
                OutputStream os = getOutputStream(dest, append, project);
                OutputStreamWriter osw;
                if (outputEncoding == null) {
                    osw = new OutputStreamWriter(os);
                } else {
                    osw = new OutputStreamWriter(os, outputEncoding);
                }
                out = new BufferedWriter(osw);
                if (filterChainsAvailable) {
                    ChainReaderHelper crh = new ChainReaderHelper();
                    crh.setBufferSize(FileUtils.BUF_SIZE);
                    crh.setPrimaryReader(in);
                    crh.setFilterChains(filterChains);
                    crh.setProject(project);
                    Reader rdr = crh.getAssembledReader();
                    in = new BufferedReader(rdr);
                }
                LineTokenizer lineTokenizer = new LineTokenizer();
                lineTokenizer.setIncludeDelims(true);
                String newline = null;
                String line = lineTokenizer.getToken(in);
                while (line != null) {
                    if (line.length() == 0) {
                        out.newLine();
                    } else {
                        newline = filters.replaceTokens(line);
                        out.write(newline);
                    }
                    line = lineTokenizer.getToken(in);
                }
            } finally {
                FileUtils.close(out);
                FileUtils.close(in);
            }
        } else if (filterChainsAvailable || (inputEncoding != null && !inputEncoding.equals(outputEncoding)) || (inputEncoding == null && outputEncoding != null)) {
            BufferedReader in = null;
            BufferedWriter out = null;
            try {
                InputStreamReader isr = null;
                if (inputEncoding == null) {
                    isr = new InputStreamReader(source.getInputStream());
                } else {
                    isr = new InputStreamReader(source.getInputStream(), inputEncoding);
                }
                in = new BufferedReader(isr);
                OutputStream os = getOutputStream(dest, append, project);
                OutputStreamWriter osw;
                if (outputEncoding == null) {
                    osw = new OutputStreamWriter(os);
                } else {
                    osw = new OutputStreamWriter(os, outputEncoding);
                }
                out = new BufferedWriter(osw);
                if (filterChainsAvailable) {
                    ChainReaderHelper crh = new ChainReaderHelper();
                    crh.setBufferSize(FileUtils.BUF_SIZE);
                    crh.setPrimaryReader(in);
                    crh.setFilterChains(filterChains);
                    crh.setProject(project);
                    Reader rdr = crh.getAssembledReader();
                    in = new BufferedReader(rdr);
                }
                char[] buffer = new char[FileUtils.BUF_SIZE];
                while (true) {
                    int nRead = in.read(buffer, 0, buffer.length);
                    if (nRead == -1) {
                        break;
                    }
                    out.write(buffer, 0, nRead);
                }
            } finally {
                FileUtils.close(out);
                FileUtils.close(in);
            }
        } else if (source.as(FileProvider.class) != null && dest.as(FileProvider.class) != null) {
            File sourceFile = ((FileProvider) source.as(FileProvider.class)).getFile();
            File destFile = ((FileProvider) dest.as(FileProvider.class)).getFile();
            File parent = destFile.getParentFile();
            if (parent != null && !parent.isDirectory() && !destFile.getParentFile().mkdirs()) {
                throw new IOException("failed to create the parent directory" + " for " + destFile);
            }
            FileInputStream in = null;
            FileOutputStream out = null;
            FileChannel srcChannel = null;
            FileChannel destChannel = null;
            try {
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                srcChannel = in.getChannel();
                destChannel = out.getChannel();
                long position = 0;
                long count = srcChannel.size();
                while (position < count) {
                    position += srcChannel.transferTo(position, FileUtils.BUF_SIZE, destChannel);
                }
            } finally {
                FileUtils.close(srcChannel);
                FileUtils.close(destChannel);
                FileUtils.close(out);
                FileUtils.close(in);
            }
        } else {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = source.getInputStream();
                out = getOutputStream(dest, append, project);
                byte[] buffer = new byte[FileUtils.BUF_SIZE];
                int count = 0;
                do {
                    out.write(buffer, 0, count);
                    count = in.read(buffer, 0, buffer.length);
                } while (count != -1);
            } finally {
                FileUtils.close(out);
                FileUtils.close(in);
            }
        }
        if (preserveLastModified) {
            Touchable t = (Touchable) dest.as(Touchable.class);
            if (t != null) {
                setLastModified(t, source.getLastModified());
            }
        }
    }
