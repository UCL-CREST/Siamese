            @Override
            public boolean visit(ProcessEntry entry) {
                log("Visiting: " + entry.getLocation());
                try {
                    Process process = new Process(entry.retrieveXML());
                    process.getRootOperator().walk(stripParametersVisitor);
                    ZipEntry zipEntry = new ZipEntry(count.incrementAndGet() + ".xml");
                    zipOut.putNextEntry(zipEntry);
                    zipOut.write(process.getRootOperator().getXML(false).getBytes(XMLImporter.PROCESS_FILE_CHARSET));
                    zipOut.closeEntry();
                } catch (Exception e) {
                    SwingTools.showSimpleErrorMessage("community.error_uploading_process_bundle", e, e.toString());
                    return false;
                }
                return true;
            }
