    public void process(@NotNull Template tpl, @NotNull Document model, @NotNull String packageName, @NotNull String outFileName, Map<String, String> xsltParam, String artifact) throws ModelGenerationException {
        System.out.print("Processing with " + tpl);
        String destinationPath;
        switch(tpl.destination) {
            case target:
                if (tpl.path == null) {
                    destinationPath = where.buildDir + separatorChar + "generated-sources" + separatorChar + GENERATION_TARGET;
                } else {
                    destinationPath = where.buildDir;
                }
                break;
            case source:
                if (tpl.path == null) {
                    destinationPath = where.sourceDir;
                } else {
                    destinationPath = "src";
                }
                break;
            case redora:
                destinationPath = where.redoraDir;
                break;
            default:
                throw new IllegalArgumentException("Unused destination " + tpl.destination);
        }
        if (tpl.path == null) {
            if (tpl.destination == Destination.redora) destinationPath += separator + artifact; else destinationPath += separator + packageName.replace('.', separatorChar);
        } else {
            destinationPath += separator + tpl.path.replace('/', separatorChar).replace('\\', separatorChar);
        }
        System.out.println(" to " + destinationPath + "..." + outFileName);
        if (tpl.destination == Destination.source) {
            if (new File(destinationPath, outFileName).exists()) {
                System.out.println("Stub " + outFileName + " already exists.");
                return;
            }
        }
        new File(destinationPath).mkdirs();
        InputStream in = null;
        Writer out;
        try {
            out = new FileWriter(new File(destinationPath, outFileName));
        } catch (IOException e) {
            throw new ModelGenerationException("Can't find: " + destinationPath + separatorChar + outFileName, e);
        }
        switch(tpl.type) {
            case freemarker:
                Map<String, NodeModel> root = new HashMap<String, NodeModel>();
                root.put("doc", NodeModel.wrap(model));
                try {
                    freemarker.template.Template template = freemarkerConf.getTemplate(tpl.getTemplateFileName());
                    template.process(root, out);
                } catch (ParseException e) {
                    throw new ModelGenerationException("There is an error in template: " + tpl + ". I found it when generating " + outFileName, e);
                } catch (IOException e) {
                    throw new ModelGenerationException("Can't find '" + tpl + "' when generating " + outFileName, e);
                } catch (TemplateException e) {
                    throw new ModelGenerationException("There is an error in template: " + tpl + ". I found it when generating " + outFileName, e);
                } catch (RuntimeException e) {
                    throw new ModelGenerationException("There is another error while trying this template: " + tpl + ". I found it when generating " + outFileName, e);
                }
                break;
            case xslt:
                try {
                    in = new FileInputStream(tpl.getAbsolutePath());
                    xsltTransform(model.getFirstChild(), in, out, xsltParam);
                } catch (FileNotFoundException e) {
                    throw new ModelGenerationException("Can't find " + tpl, e);
                } catch (TransformerException e) {
                    throw new ModelGenerationException("Sorry, i failed to use this template: " + tpl + ". It broke when generating " + outFileName, e);
                } finally {
                    IOUtils.closeQuietly(in);
                }
                break;
            case copy:
                try {
                    in = new FileInputStream(tpl.getAbsolutePath());
                    IOUtils.copy(in, out);
                } catch (IOException e) {
                    throw new ModelGenerationException("File copy failed " + tpl.getTemplateFileName(), e);
                } finally {
                    IOUtils.closeQuietly(in);
                }
        }
        IOUtils.closeQuietly(out);
    }
