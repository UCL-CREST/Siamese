    private void doConvert(HttpServletResponse response, ConversionRequestResolver rr, EGE ege, ConversionsPath cpath) throws FileUploadException, IOException, RequestResolvingException, EGEException, FileNotFoundException, ConverterException, ZipException {
        InputStream is = null;
        OutputStream os = null;
        if (ServletFileUpload.isMultipartContent(rr.getRequest())) {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(rr.getRequest());
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                if (!item.isFormField()) {
                    is = item.openStream();
                    applyConversionsProperties(rr.getConversionProperties(), cpath);
                    DataBuffer buffer = new DataBuffer(0, EGEConstants.BUFFER_TEMP_PATH);
                    String alloc = buffer.allocate(is);
                    InputStream ins = buffer.getDataAsStream(alloc);
                    is.close();
                    try {
                        ValidationResult vRes = ege.performValidation(ins, cpath.getInputDataType());
                        if (vRes.getStatus().equals(ValidationResult.Status.FATAL)) {
                            ValidationServlet valServ = new ValidationServlet();
                            valServ.printValidationResult(response, vRes);
                            try {
                                ins.close();
                            } finally {
                                buffer.removeData(alloc, true);
                            }
                            return;
                        }
                    } catch (ValidatorException vex) {
                        LOGGER.warn(vex.getMessage());
                    } finally {
                        try {
                            ins.close();
                        } catch (Exception ex) {
                        }
                    }
                    File zipFile = null;
                    FileOutputStream fos = null;
                    String newTemp = UUID.randomUUID().toString();
                    IOResolver ior = EGEConfigurationManager.getInstance().getStandardIOResolver();
                    File buffDir = new File(buffer.getDataDir(alloc));
                    zipFile = new File(EGEConstants.BUFFER_TEMP_PATH + File.separator + newTemp + EZP_EXT);
                    fos = new FileOutputStream(zipFile);
                    ior.compressData(buffDir, fos);
                    ins = new FileInputStream(zipFile);
                    File szipFile = new File(EGEConstants.BUFFER_TEMP_PATH + File.separator + newTemp + ZIP_EXT);
                    fos = new FileOutputStream(szipFile);
                    try {
                        try {
                            ege.performConversion(ins, fos, cpath);
                        } finally {
                            fos.close();
                        }
                        boolean isComplex = EGEIOUtils.isComplexZip(szipFile);
                        response.setContentType(APPLICATION_OCTET_STREAM);
                        String fN = item.getName().substring(0, item.getName().lastIndexOf("."));
                        if (isComplex) {
                            String fileExt;
                            if (cpath.getOutputDataType().getMimeType().equals(APPLICATION_MSWORD)) {
                                fileExt = DOCX_EXT;
                            } else {
                                fileExt = ZIP_EXT;
                            }
                            response.setHeader("Content-Disposition", "attachment; filename=\"" + fN + fileExt + "\"");
                            FileInputStream fis = new FileInputStream(szipFile);
                            os = response.getOutputStream();
                            try {
                                EGEIOUtils.copyStream(fis, os);
                            } finally {
                                fis.close();
                            }
                        } else {
                            String fileExt = getMimeExtensionProvider().getFileExtension(cpath.getOutputDataType().getMimeType());
                            response.setHeader("Content-Disposition", "attachment; filename=\"" + fN + fileExt + "\"");
                            os = response.getOutputStream();
                            EGEIOUtils.unzipSingleFile(new ZipFile(szipFile), os);
                        }
                    } finally {
                        ins.close();
                        if (os != null) {
                            os.flush();
                            os.close();
                        }
                        buffer.clear(true);
                        szipFile.delete();
                        if (zipFile != null) {
                            zipFile.delete();
                        }
                    }
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }
