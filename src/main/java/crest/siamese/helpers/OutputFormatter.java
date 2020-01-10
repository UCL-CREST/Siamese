/*
   Copyright 2018 Chaiyong Ragkhitwetsagul and Jens Krinke

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package crest.siamese.helpers;

import crest.siamese.document.Document;

import java.util.ArrayList;

public class OutputFormatter {
    /* csv, xml, json */
    private String format;
    private boolean addStartEndLine;
    private XMLFormatter xmlFormatter;
    private JSONFormatter jsonFormatter;
    private Document query;
    private int ccid = 1;
    private boolean addLicense;

    public OutputFormatter() {
        format = "csv";
        addStartEndLine = true;
        xmlFormatter = new XMLFormatter();
        jsonFormatter = new JSONFormatter();
        addLicense = false;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
        if (this.format.toLowerCase().equals("addClone")) {
            xmlFormatter.createDocument();
        }
    }

    public boolean isAddStartEndLine() {
        return addStartEndLine;
    }

    public void setAddStartEndLine(boolean addStartEndLine) {
        this.addStartEndLine = addStartEndLine;
    }

    public boolean isAddLicense() {
        return addLicense;
    }

    public void setAddLicense(boolean addLicense) {
        this.addLicense = addLicense;
    }

    public OutputFormatter(String format, boolean addStartEndLine, boolean addLicense) {
        this.format = format;
        this.addStartEndLine = addStartEndLine;
        this.addLicense = addLicense;
    }

    public String format(Document query, String prefixToRemove, String license) {
        StringBuilder sb = new StringBuilder();
        if (this.format.equals("csv")) {
            sb.append(query.getFile().replace(prefixToRemove, ""));
            if (addStartEndLine) {
                sb.append("#" + query.getStartLine() + "#" + query.getEndLine());
            }
            if (addLicense) {
                sb.append("#" + license);
            }
            sb.append(",");
            return sb.toString();
        } else if (this.format.equals("gcf") || this.format.equals("json")) {
            /* query, don't do anything yet */
            this.query = query;
            return "";
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }

    public String format(ArrayList<Document> results, int[] sim, String[] thresholds, String prefixToRemove) {
        StringBuilder sb = new StringBuilder();
        int resultCount = 0;
        int[] simT = new int[4];
        for (int i=0; i<4; i++)
            simT[i] = Integer.parseInt(thresholds[i].split("%")[0]);
        if (this.format.equals("csv")) {
            for (int i =0; i<results.size(); i++) {
                Document d = results.get(i);
                // only add the results that has similarity higher than the threshold
                // (only based on the original representation)
                if (sim[i] >= simT[0]) {
                    if (resultCount > 0)
                        sb.append(","); // add comma in between
                    sb.append(d.getFile().replace(prefixToRemove, ""));
                    if (addStartEndLine) {
                        sb.append("#" + d.getStartLine() + "#" + d.getEndLine() + "#" + sim[i] + "$0$0$0");
                    }
                    if (addLicense) {
                        sb.append("#" + d.getLicense());
                    }
                    resultCount++;
                }
            }
            sb.append("\n");
            return sb.toString();
        } else if (this.format.equals("gcf")) {
            /* put query at the front */
            results.add(0, this.query);
            xmlFormatter.addCloneClass(this.ccid, -1, results);
            this.ccid++;
            return "";
        } else if (this.format.equals("json")) {
            /* put query at the front */
            results.add(0, this.query);
            jsonFormatter.addCloneClass(this.ccid, -1, results);
            return "";
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }

    public String format(ArrayList<Document> results, String prefixToRemove,
                         boolean ignoreQueryClones, Document query, String queryText) {
        StringBuilder sb = new StringBuilder();
        int resultCount = 0;
        if (this.format.equals("csv")) {
            for (Document d : results) {
                if (ignoreQueryClones && d.getFile().equals(query.getFile())
                        && d.getStartLine() == query.getStartLine()
                        && d.getEndLine() == query.getEndLine())
                    continue; // skip the query clones
                if (resultCount > 0)
                    sb.append(","); // add comma in between
                sb.append(d.getFile().replace(prefixToRemove, ""));
                if (addStartEndLine) {
                    sb.append("#" + d.getStartLine() + "#" + d.getEndLine());
                }
                if (addLicense) {
                    sb.append("#" + d.getLicense());
                }
                resultCount++;
            }

            if (resultCount > 0) {
                sb.append("\n");
                sb.insert(0, queryText);
                return sb.toString();
            } else {
                return "";
            }
        } else if (this.format.equals("gcf")) {
            /* put query at the front */
            results.add(0, this.query);
            xmlFormatter.addCloneClass(this.ccid, -1, results);
            this.ccid++;
            return "";
        } else if (this.format.equals("json")) {
            /* put query at the front */
            results.add(0, this.query);
            jsonFormatter.addCloneClass(this.ccid, -1, results);
            return "";
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }

    public String format(ArrayList<Document> results, int[][] sim, String[] thresholds,
                         String prefixToRemove, boolean ignoreQueryClones, Document query, String queryText) {
        StringBuilder sb = new StringBuilder();
        int resultCount = 0;
        int[] simT = new int[4];
        for (int i=0; i<4; i++)
            simT[i] = Integer.parseInt(thresholds[i].split("%")[0]);
        if (this.format.equals("csv")) {
            for (int i =0; i<results.size(); i++) {
                Document d = results.get(i);
                if (ignoreQueryClones && d.getFile().equals(query.getFile())
                        && d.getStartLine() == query.getStartLine()
                        && d.getEndLine() == query.getEndLine())
                    continue; // skip the query clones
                // only add the results that has similarity higher than the threshold
                if (sim[i][0] >= simT[0] && sim[i][1] >= simT[1]
                        && sim[i][2] >= simT[2] && sim[i][3] >= simT[3]) {
                    if (resultCount > 0)
                        sb.append(","); // add comma in between
                    sb.append(d.getFile().replace(prefixToRemove, ""));
                    if (addStartEndLine) {
                        sb.append("#" + d.getStartLine() + "#" + d.getEndLine() + "#" +
                                sim[i][0] + "$" + sim[i][1] + "$" + sim[i][2] + "$" + sim[i][3]);
                    }
                    if (addLicense) {
                        sb.append("#" + d.getLicense());
                    }
                    resultCount++;
                }
            }
            if (resultCount > 0) {
                sb.append("\n");
                sb.insert(0, queryText);
                return sb.toString();
            } else {
                return "";
            }
        } else if (this.format.equals("gcf")) {
            /* put query at the front */
            results.add(0, this.query);
            xmlFormatter.addCloneClass(this.ccid, -1, results);
            this.ccid++;
            return "";
        } else if (this.format.equals("json")) {
            /* put query at the front */
            results.add(0, this.query);
            jsonFormatter.addCloneClass(this.ccid, -1, results);
            return "";
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }

    public String getXML() {
        return xmlFormatter.getXMLAsString();
    }

    public String getJSON() {
        return jsonFormatter.getJSONString();
    }
}
