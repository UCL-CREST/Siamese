    @Override
    public void execute() throws BuildException {
        final String GC_USERNAME = "google-code-username";
        final String GC_PASSWORD = "google-code-password";
        if (StringUtils.isBlank(this.projectName)) throw new BuildException("undefined project");
        if (this.file == null) throw new BuildException("undefined file");
        if (!this.file.exists()) throw new BuildException("file not found :" + file);
        if (!this.file.isFile()) throw new BuildException("not a file :" + file);
        if (this.config == null) throw new BuildException("undefined config");
        if (!this.config.exists()) throw new BuildException("file not found :" + config);
        if (!this.config.isFile()) throw new BuildException("not a file :" + config);
        PostMethod post = null;
        try {
            Properties cfg = new Properties();
            FileInputStream fin = new FileInputStream(this.config);
            cfg.loadFromXML(fin);
            fin.close();
            if (!cfg.containsKey(GC_USERNAME)) throw new BuildException("undefined " + GC_USERNAME + " in " + this.config);
            if (!cfg.containsKey(GC_PASSWORD)) throw new BuildException("undefined " + GC_PASSWORD + " in " + this.config);
            HttpClient client = new HttpClient();
            post = new PostMethod("https://" + projectName + ".googlecode.com/files");
            post.addRequestHeader("User-Agent", getClass().getName());
            post.addRequestHeader("Authorization", "Basic " + Base64.encode(cfg.getProperty(GC_USERNAME) + ":" + cfg.getProperty(GC_PASSWORD)));
            List<Part> parts = new ArrayList<Part>();
            String s = this.summary;
            if (StringUtils.isBlank(s)) {
                s = this.file.getName() + " (" + TimeUtils.toYYYYMMDD() + ")";
            }
            parts.add(new StringPart("summary", s));
            for (String lbl : this.labels.split("[, \t\n]+")) {
                if (StringUtils.isBlank(lbl)) continue;
                parts.add(new StringPart("label", lbl.trim()));
            }
            parts.add(new FilePart("filename", this.file));
            MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), post.getParams());
            post.setRequestEntity(requestEntity);
            int status = client.executeMethod(post);
            if (status != 201) {
                throw new BuildException("http status !=201 : " + post.getResponseBodyAsString());
            } else {
                IOUtils.copyTo(post.getResponseBodyAsStream(), new NullOutputStream());
            }
        } catch (BuildException e) {
            throw e;
        } catch (Exception e) {
            throw new BuildException(e);
        } finally {
            if (post != null) post.releaseConnection();
        }
    }
