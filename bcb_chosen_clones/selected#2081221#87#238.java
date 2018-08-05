    public ConversionStatus convert(ConversionMode mode, Locale language, String phaseName, int maxPhase, Charset nativeEncoding, FileType nativeFileType, String nativeFileName, String baseDir, Notifier notifier, SegmentBoundary boundary, StringWriter generatedFileName) throws ConversionException {
        ConversionStatus status = ConversionStatus.CONVERSION_SUCCEEDED;
        if (!mode.equals(ConversionMode.FROM_XLIFF)) {
            throw new ConversionException("The OpenOffice.org Text Exporter supports" + " only conversions from XLIFF to OpenDocument Text format.");
        }
        if ((language == null) || (nativeFileName == null) || (nativeFileName.length() == 0) || (baseDir == null) || (baseDir.length() == 0)) {
            throw new ConversionException("Required parameter(s)" + " omitted, incomplete or incorrect.");
        }
        if (xliffOriginalFileName == null || xliffOriginalFileName.length() == 0) {
            xliffOriginalFileName = nativeFileName;
        }
        status = super.convert(mode, language, phaseName, maxPhase, nativeEncoding, nativeFileType, xliffOriginalFileName, baseDir, notifier, boundary, null);
        String oldOdtFileName = "";
        String newOdtFileName = "";
        if (xliffOriginalFileName.toLowerCase().endsWith(".odt")) {
            oldOdtFileName = baseDir + File.separator + xliffOriginalFileName;
            int extPos = xliffOriginalFileName.toLowerCase().lastIndexOf(".odt");
            newOdtFileName = baseDir + File.separator + xliffOriginalFileName.substring(0, extPos) + "." + language.toString() + ".odt";
            if (generatedFileName != null) {
                generatedFileName.write(xliffOriginalFileName.substring(0, extPos) + "." + language.toString() + ".odt");
            }
        } else if (xliffOriginalFileName.toLowerCase().endsWith(".ods")) {
            oldOdtFileName = baseDir + File.separator + xliffOriginalFileName;
            int extPos = xliffOriginalFileName.toLowerCase().lastIndexOf(".ods");
            newOdtFileName = baseDir + File.separator + xliffOriginalFileName.substring(0, extPos) + "." + language.toString() + ".ods";
            if (generatedFileName != null) {
                generatedFileName.write(xliffOriginalFileName.substring(0, extPos) + "." + language.toString() + ".ods");
            }
        } else if (xliffOriginalFileName.toLowerCase().endsWith(".odp")) {
            oldOdtFileName = baseDir + File.separator + xliffOriginalFileName;
            int extPos = xliffOriginalFileName.toLowerCase().lastIndexOf(".odp");
            newOdtFileName = baseDir + File.separator + xliffOriginalFileName.substring(0, extPos) + "." + language.toString() + ".odp";
            if (generatedFileName != null) {
                generatedFileName.write(xliffOriginalFileName.substring(0, extPos) + "." + language.toString() + ".odp");
            }
        } else if (xliffOriginalFileName.toLowerCase().endsWith(".ppt")) {
            oldOdtFileName = baseDir + File.separator + xliffOriginalFileName + ".odp";
            newOdtFileName = baseDir + File.separator + xliffOriginalFileName + "." + language.toString() + ".odp";
            if (generatedFileName != null) {
                generatedFileName.write(xliffOriginalFileName + "." + language.toString() + ".odp");
            }
        } else if (xliffOriginalFileName.toLowerCase().endsWith(".xls")) {
            oldOdtFileName = baseDir + File.separator + xliffOriginalFileName + ".ods";
            newOdtFileName = baseDir + File.separator + xliffOriginalFileName + "." + language.toString() + ".ods";
            if (generatedFileName != null) {
                generatedFileName.write(xliffOriginalFileName + "." + language.toString() + ".ods");
            }
        } else if (xliffOriginalFileName.toLowerCase().endsWith(".doc")) {
            oldOdtFileName = baseDir + File.separator + xliffOriginalFileName + ".odt";
            newOdtFileName = baseDir + File.separator + xliffOriginalFileName + "." + language.toString() + ".odt";
            if (generatedFileName != null) {
                generatedFileName.write(xliffOriginalFileName + "." + language.toString() + ".odt");
            }
        } else if (xliffOriginalFileName.toLowerCase().endsWith(".rtf")) {
            oldOdtFileName = baseDir + File.separator + xliffOriginalFileName + ".odt";
            newOdtFileName = baseDir + File.separator + xliffOriginalFileName + "." + language.toString() + ".odt";
            if (generatedFileName != null) {
                generatedFileName.write(xliffOriginalFileName + "." + language.toString() + ".odt");
            }
        } else {
            oldOdtFileName = baseDir + File.separator + xliffOriginalFileName;
            newOdtFileName = baseDir + File.separator + xliffOriginalFileName + "." + language.toString();
            if (generatedFileName != null) {
                generatedFileName.write(xliffOriginalFileName + "." + language.toString());
            }
        }
        try {
            byte[] byteBuf = new byte[BLKSIZE];
            int numRead;
            ZipFile odfZipFile = new ZipFile(oldOdtFileName);
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(newOdtFileName));
            Enumeration all = odfZipFile.entries();
            while (all.hasMoreElements()) {
                ZipEntry nextFile = (ZipEntry) all.nextElement();
                if (nextFile.getName().equals("content.xml")) {
                    zipOut.putNextEntry(new ZipEntry("content.xml"));
                    InputStream inOdt = new FileInputStream(baseDir + File.separator + "content." + language.toString() + ".xml");
                    while ((numRead = inOdt.read(byteBuf)) != -1) {
                        zipOut.write(byteBuf, 0, numRead);
                    }
                    inOdt.close();
                    zipOut.flush();
                } else if (nextFile.getName().equals("styles.xml")) {
                    zipOut.putNextEntry(new ZipEntry("styles.xml"));
                    StringBuilder stylesBuf = new StringBuilder();
                    File stylesLl = new File(baseDir + File.separator + "styles." + language.toString() + ".xml");
                    InputStreamReader inStyles = null;
                    if (stylesLl.exists()) {
                        inStyles = new InputStreamReader(new FileInputStream(baseDir + File.separator + "styles." + language.toString() + ".xml"), Charset.forName("UTF-8"));
                    } else {
                        inStyles = new InputStreamReader(odfZipFile.getInputStream(nextFile), Charset.forName("UTF-8"));
                    }
                    int b;
                    char[] charBuf = new char[BLKSIZE];
                    while ((numRead = inStyles.read(charBuf)) != -1) {
                        stylesBuf.append(charBuf, 0, numRead);
                    }
                    String stylesContent = stylesBuf.toString().replaceAll("(?s)(fo:language|number:language|style:language-complex|style:language-asian)=(['\"])[^'\"]*\\2", "$1=$2" + language.getLanguage() + "$2");
                    stylesContent = stylesContent.replaceAll("(?s)(fo:country|number:country|style:country-complex|style:country-asian)=(['\"])[^'\"]*\\2", "$1=$2" + language.getCountry() + "$2");
                    CharsetEncoder c2b = Charset.forName("UTF-8").newEncoder();
                    ByteBuffer stylesInBytes = c2b.encode(CharBuffer.wrap(stylesContent.subSequence(0, stylesContent.length()), 0, stylesContent.length()));
                    int numBytes = stylesInBytes.limit();
                    zipOut.write(stylesInBytes.array(), 0, numBytes);
                    inStyles.close();
                    zipOut.flush();
                } else if (nextFile.getName().equals("mimetype")) {
                    ZipEntry nextOutFile = new ZipEntry(nextFile.getName());
                    nextOutFile.setMethod(ZipEntry.STORED);
                    nextOutFile.setSize(nextFile.getSize());
                    nextOutFile.setCrc(nextFile.getCrc());
                    zipOut.putNextEntry(nextOutFile);
                    InputStream zin = odfZipFile.getInputStream(nextFile);
                    while ((numRead = zin.read(byteBuf)) != -1) {
                        zipOut.write(byteBuf, 0, numRead);
                    }
                    zipOut.flush();
                    zin.close();
                } else if (nextFile.getName().equals("meta.xml")) {
                    zipOut.putNextEntry(new ZipEntry("meta.xml"));
                    StringBuilder metaBuf = new StringBuilder();
                    InputStreamReader zin = new InputStreamReader(odfZipFile.getInputStream(nextFile));
                    int b;
                    char[] charBuf = new char[BLKSIZE];
                    while ((numRead = zin.read(charBuf)) != -1) {
                        metaBuf.append(charBuf, 0, numRead);
                    }
                    String metaContent = metaBuf.toString().replaceFirst("(?s)^(.*?<dc:language>).*?(</dc:language>.*)$", "$1" + language.toString() + "$2");
                    for (int i = 0; i < metaContent.length(); i++) {
                        zipOut.write(metaContent.charAt(i));
                    }
                    zipOut.flush();
                    zin.close();
                } else {
                    ZipEntry nextOutFile = new ZipEntry(nextFile.getName());
                    zipOut.putNextEntry(nextOutFile);
                    InputStream zin = odfZipFile.getInputStream(nextFile);
                    while ((numRead = zin.read(byteBuf)) != -1) {
                        zipOut.write(byteBuf, 0, numRead);
                    }
                    zipOut.flush();
                    zin.close();
                }
                zipOut.closeEntry();
            }
            odfZipFile.close();
            zipOut.close();
        } catch (IOException e) {
            System.err.println("Cannot create (ZIP format) OpenOffice Text file " + newOdtFileName + ": " + e.getMessage());
            throw new ConversionException("Cannot create (ZIP format) OpenOffice Text file " + newOdtFileName + ": " + e.getMessage());
        }
        return status;
    }
