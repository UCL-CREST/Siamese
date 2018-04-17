    public BlogEntry[] process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlogUser user, Map context, BlogEntry[] entries) throws BlojsomPluginException {
        entries = super.process(httpServletRequest, httpServletResponse, user, context, entries);
        String page = BlojsomUtils.getRequestValue(PAGE_PARAM, httpServletRequest);
        if (ADMIN_LOGIN_PAGE.equals(page)) {
            return entries;
        } else {
            Map fetchParameters = new HashMap();
            fetchParameters.put(BlojsomFetcher.FETCHER_FLAVOR, user.getBlog().getBlogDefaultFlavor());
            fetchParameters.put(BlojsomFetcher.FETCHER_NUM_POSTS_INTEGER, new Integer(-1));
            try {
                BlogEntry[] allEntries = _fetcher.fetchEntries(fetchParameters, user);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String exportDate = simpleDateFormat.format(new Date());
                httpServletResponse.setContentType("application/zip");
                httpServletResponse.setHeader("Content-Disposition", "filename=blojsom-export-" + exportDate + ".zip");
                ZipOutputStream zipOutputStream = new ZipOutputStream(httpServletResponse.getOutputStream());
                zipOutputStream.putNextEntry(new ZipEntry("entries.xml"));
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(zipOutputStream, UTF8);
                XStream xStream = new XStream();
                xStream.toXML(allEntries, outputStreamWriter);
                zipOutputStream.closeEntry();
                int length;
                File resourcesDirectory = new File(_blojsomConfiguration.getQualifiedResourceDirectory() + "/" + user.getId() + "/");
                String resourcesDirName = _blojsomConfiguration.getResourceDirectory();
                File[] resourceFiles = resourcesDirectory.listFiles();
                if (resourceFiles != null && resourceFiles.length > 0) {
                    for (int i = 0; i < resourceFiles.length; i++) {
                        File resourceFile = resourceFiles[i];
                        if (!resourceFile.isDirectory()) {
                            byte[] buffer = new byte[1024];
                            zipOutputStream.putNextEntry(new ZipEntry(resourcesDirName + user.getId() + "/" + resourceFile.getName()));
                            FileInputStream in = new FileInputStream(resourceFile.getAbsolutePath());
                            while ((length = in.read(buffer)) > 0) {
                                zipOutputStream.write(buffer, 0, length);
                            }
                            zipOutputStream.closeEntry();
                        }
                    }
                }
                File templatesDirectory = new File(_blojsomConfiguration.getInstallationDirectory() + _blojsomConfiguration.getBaseConfigurationDirectory() + user.getId() + _blojsomConfiguration.getTemplatesDirectory());
                String templateDirName = _blojsomConfiguration.getTemplatesDirectory();
                File[] templateFiles = templatesDirectory.listFiles();
                if (templateFiles != null && templateFiles.length > 0) {
                    for (int i = 0; i < templateFiles.length; i++) {
                        File templateFile = templateFiles[i];
                        if (!templateFile.isDirectory()) {
                            byte[] buffer = new byte[1024];
                            zipOutputStream.putNextEntry(new ZipEntry(templateDirName + user.getId() + "/" + templateFile.getName()));
                            FileInputStream in = new FileInputStream(templateFile.getAbsolutePath());
                            while ((length = in.read(buffer)) > 0) {
                                zipOutputStream.write(buffer, 0, length);
                            }
                            zipOutputStream.closeEntry();
                        }
                    }
                }
                zipOutputStream.close();
            } catch (BlojsomFetcherException e) {
                _logger.error(e);
                addOperationResultMessage(context, formatAdminResource(FAILED_LOADING_ENTRIES_KEY, FAILED_LOADING_ENTRIES_KEY, user.getBlog().getBlogAdministrationLocale(), new Object[] { user.getId() }));
            } catch (IOException e) {
                _logger.error(e);
                addOperationResultMessage(context, formatAdminResource(FAILED_XML_ARCHIVE_CREATE_KEY, FAILED_XML_ARCHIVE_CREATE_KEY, user.getBlog().getBlogAdministrationLocale(), new Object[] { user.getId() }));
            }
        }
        return entries;
    }
