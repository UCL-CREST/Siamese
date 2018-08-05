package crest.siamese.helpers;

import crest.siamese.document.Document;

import java.util.ArrayList;

public class OutputFormatter {
    /* csv, xml, json */
    private String format;
    private boolean addStartEndLine;
    private XMLFormatter xmlFormatter;
    private Document query;
    private int ccid = 1;

    public OutputFormatter() {
        format = "csv";
        addStartEndLine = true;
        xmlFormatter = new XMLFormatter();
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

    public OutputFormatter(String format, boolean addStartEndLine) {
        this.format = format;
        this.addStartEndLine = addStartEndLine;
    }

    public String format(Document query, String prefixToRemove, String license) {
        StringBuilder sb = new StringBuilder();
        if (this.format.equals("csv")) {
            sb.append(query.getFile().replace(prefixToRemove, ""));
            if (addStartEndLine) {
                sb.append("#" + query.getStartLine() + "#" + query.getEndLine() + "#" + license);
            }
            sb.append(",");
            return sb.toString();
        } else if (this.format.equals("gcf")) {
            /* query, don't do anything yet */
            this.query = query;
            return "";
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }

    public String format(ArrayList<Document> results, String prefixToRemove) {
        StringBuilder sb = new StringBuilder();
        int resultCount = 0;
        if (this.format.equals("csv")) {
            for (Document d : results) {
                if (resultCount > 0)
                    sb.append(","); // add comma in between
                sb.append(d.getFile().replace(prefixToRemove, ""));
                if (addStartEndLine) {
                    sb.append("#" + d.getStartLine() + "#" + d.getEndLine());
                }
                resultCount++;
            }
            sb.append("\n");
            return sb.toString();
        } else if (this.format.equals("gcf")) {
            /* put query at the front */
            results.add(0, this.query);
            xmlFormatter.addCloneClass(this.ccid, -1, results);
            this.ccid++;
            return "";
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }

    public String format(ArrayList<Document> results, int[] sim, int threshod, String prefixToRemove) {
        StringBuilder sb = new StringBuilder();
        int resultCount = 0;
        if (this.format.equals("csv")) {
            for (int i =0; i<results.size(); i++) {
                Document d = results.get(i);
                if (resultCount > 0)
                    sb.append(","); // add comma in between
                // only add the results that has similarity higher than the threshold
                if (sim[i] >= threshod) {
                    sb.append(d.getFile().replace(prefixToRemove, ""));
                    if (addStartEndLine) {
                        sb.append("#" + d.getStartLine() + "#" + d.getEndLine() + "#" + sim[i] + "#" + d.getLicense());
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
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }

    public String getXML() {
        return xmlFormatter.getXMLAsString();
    }
}
