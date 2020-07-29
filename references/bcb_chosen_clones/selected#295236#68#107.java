    private void execute(File file) throws IOException {
        if (file == null) throw new RuntimeException("undefined file");
        if (!file.exists()) throw new RuntimeException("file not found :" + file);
        if (!file.isFile()) throw new RuntimeException("not a file :" + file);
        String login = cfg.getProperty(GC_USERNAME);
        String password = null;
        if (cfg.containsKey(GC_PASSWORD)) {
            password = cfg.getProperty(GC_PASSWORD);
        } else {
            password = new String(Base64.decode(cfg.getProperty(GC_PASSWORD64)));
        }
        PostMethod post = null;
        try {
            HttpClient client = new HttpClient();
            post = new PostMethod("https://" + projectName + ".googlecode.com/files");
            post.addRequestHeader("User-Agent", getClass().getName());
            post.addRequestHeader("Authorization", "Basic " + Base64.encode(login + ":" + password));
            List<Part> parts = new ArrayList<Part>();
            String s = this.summary;
            if (StringUtils.isBlank(s)) {
                s = file.getName() + " (" + TimeUtils.toYYYYMMDD() + ")";
            }
            parts.add(new StringPart("summary", s));
            for (String lbl : this.labels) {
                if (StringUtils.isBlank(lbl)) continue;
                parts.add(new StringPart("label", lbl.trim()));
            }
            parts.add(new FilePart("filename", file));
            MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), post.getParams());
            post.setRequestEntity(requestEntity);
            int status = client.executeMethod(post);
            if (status != 201) {
                throw new IOException("http status !=201 : " + post.getResponseBodyAsString());
            } else {
                IOUtils.copyTo(post.getResponseBodyAsStream(), new NullOutputStream());
            }
        } finally {
            if (post != null) post.releaseConnection();
        }
    }
