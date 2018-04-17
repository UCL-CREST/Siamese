                        public void run() {
                            progressLabel.updateUI();
                            thisInstance.setEnabled(false);
                            Map<Object, Object> parameters = searcher.getParameters();
                            if (parameters == null) parameters = new HashMap<Object, Object>();
                            if (target instanceof LuceneDataStoreImpl) {
                                URL indexLocationURL = (URL) ((LuceneDataStoreImpl) target).getIndexer().getParameters().get(Constants.INDEX_LOCATION_URL);
                                String indexLocation = null;
                                try {
                                    indexLocation = new File(indexLocationURL.toURI()).getAbsolutePath();
                                } catch (URISyntaxException use) {
                                    indexLocation = new File(indexLocationURL.getFile()).getAbsolutePath();
                                }
                                ArrayList<String> indexLocations = new ArrayList<String>();
                                indexLocations.add(indexLocation);
                                parameters.put(Constants.INDEX_LOCATIONS, indexLocations);
                                String corpus2SearchIn = (corpusToSearchIn.getSelectedItem().equals(Constants.ENTIRE_DATASTORE)) ? null : (String) corpusIds.get(corpusToSearchIn.getSelectedIndex() - 1);
                                parameters.put(Constants.CORPUS_ID, corpus2SearchIn);
                            }
                            int noOfPatterns = ((Number) numberOfResultsSpinner.getValue()).intValue();
                            int contextWindow = ((Number) contextSizeSpinner.getValue()).intValue();
                            String query = newQuery.getText().trim();
                            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\{([^\\{=]+)==");
                            Matcher matcher = pattern.matcher(query);
                            int start = 0;
                            while (matcher.find(start)) {
                                start = matcher.end(1);
                                for (int row = 0; row < numShortcuts; row++) {
                                    if (shortcuts[row][SHORTCUT].equals(matcher.group(1))) {
                                        query = query.substring(0, matcher.start(1)) + shortcuts[row][ANNOTATION_TYPE] + "." + shortcuts[row][FEATURE] + query.substring(matcher.end(1));
                                        matcher = pattern.matcher(query);
                                        break;
                                    }
                                }
                            }
                            parameters.put(Constants.CONTEXT_WINDOW, new Integer(contextWindow));
                            if (annotationSetToSearchIn.getSelectedItem().equals(Constants.ALL_SETS)) {
                                parameters.remove(Constants.ANNOTATION_SET_ID);
                            } else {
                                String annotationSet = (String) annotationSetToSearchIn.getSelectedItem();
                                parameters.put(Constants.ANNOTATION_SET_ID, annotationSet);
                            }
                            try {
                                if (searcher.search(query, parameters)) {
                                    searcher.next(noOfPatterns);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                thisInstance.setEnabled(true);
                            }
                            processFinished();
                            pageOfResults = 1;
                            titleResults.setText("Results - Page " + pageOfResults);
                            thisInstance.setEnabled(true);
                        }
