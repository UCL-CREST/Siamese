    public void readBooklist(String filename) {
        Reader input = null;
        try {
            if (filename.startsWith("http:")) {
                URL url = new URL(filename);
                URLConnection conn = url.openConnection();
                input = new InputStreamReader(conn.getInputStream());
            } else {
                String fileNameAll = filename;
                try {
                    fileNameAll = new File(filename).getCanonicalPath();
                } catch (IOException e) {
                    fileNameAll = new File(filename).getAbsolutePath();
                }
                input = new FileReader(new File(fileNameAll));
            }
            BufferedReader reader = new BufferedReader(input);
            String line;
            Date today = new Date();
            while ((line = reader.readLine()) != null) {
                if (shuttingDown) break;
                String fields[] = line.split("\\|");
                Map<String, String> valuesToAdd = new LinkedHashMap<String, String>();
                valuesToAdd.put("fund_code_facet", fields[11]);
                valuesToAdd.put("date_received_facet", fields[0]);
                DateFormat format = new SimpleDateFormat("yyyyMMdd");
                Date dateReceived = format.parse(fields[0], new ParsePosition(0));
                if (dateReceived.after(today)) continue;
                String docID = "u" + fields[9];
                try {
                    Map<String, Object> docMap = getDocumentMap(docID);
                    if (docMap != null) {
                        addNewDataToRecord(docMap, valuesToAdd);
                        documentCache.put(docID, docMap);
                        if (doUpdate && docMap != null && docMap.size() != 0) {
                            update(docMap);
                        }
                    }
                } catch (SolrMarcIndexerException e) {
                    if (e.getLevel() == SolrMarcIndexerException.IGNORE) {
                        logger.error("Indexing routine says record " + docID + " should be ignored");
                    } else if (e.getLevel() == SolrMarcIndexerException.DELETE) {
                        logger.error("Indexing routine says record " + docID + " should be deleted");
                    }
                    if (e.getLevel() == SolrMarcIndexerException.EXIT) {
                        logger.error("Indexing routine says processing should be terminated by record " + docID);
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
            logger.error(e.getCause());
        } catch (IOException e) {
            logger.info(e.getMessage());
            logger.error(e.getCause());
        }
    }
