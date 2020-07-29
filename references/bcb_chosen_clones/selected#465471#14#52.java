    @Override
    public Navigation run() throws Exception {
        String template = this.requestScope("template");
        String engine = this.requestScope("engine");
        boolean zip = "yes".equals(this.requestScope("download_zip"));
        if (StringUtils.isBlank(engine)) {
            engine = "velocity";
            this.requestScope("engine", engine);
        }
        if (zip) {
            this.response.setHeader("Content-Disposition", "attachment; filename=result.zip");
            this.response.setContentType("application/zip");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(baos, "UTF-8");
            TextGenerator.generate(template, engine, writer);
            writer.close();
            ZipOutputStream zipOutputStream = null;
            ZipEntry entry = null;
            try {
                zipOutputStream = new ZipOutputStream(response.getOutputStream());
                entry = new ZipEntry("result.txt");
                entry.setMethod(ZipOutputStream.DEFLATED);
                zipOutputStream.putNextEntry(entry);
                byte[] data = baos.toByteArray();
                zipOutputStream.write(data, 0, data.length);
            } finally {
                if (zipOutputStream != null) {
                    zipOutputStream.closeEntry();
                    zipOutputStream.close();
                }
            }
        } else {
            this.response.setHeader("Content-Disposition", "attachment; filename=result.txt");
            this.response.setContentType("application/octet-stream");
            this.response.setCharacterEncoding("UTF-8");
            TextGenerator.generate(template, engine, this.response.getWriter());
        }
        return null;
    }
