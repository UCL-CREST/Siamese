package crest.siamese.helpers;

import crest.siamese.document.Document;

import java.util.ArrayList;

public class OutputFormatter {

    /* csv, xml, json */
    private String format;
    private boolean addStartEndLine;

    public OutputFormatter() {
        format = "csv";
        addStartEndLine = true;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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
        if (this.format.equals("csv")) {
            StringBuilder sb = new StringBuilder();
            sb.append(query.getFile().replace(prefixToRemove, ""));
            if (addStartEndLine) {
                sb.append("#" + query.getStartLine() + "#" + query.getEndLine() + "#" + license);
            }
            return sb.toString();
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }

    public String format(ArrayList<ArrayList<Document>> results, String prefixToRemove) {
        if (this.format.equals("csv")) {
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<results.size(); i++) {
                int resultCount = 0;
                ArrayList<Document> qResults = results.get(i);
                for (Document d : qResults) {
                    if (resultCount > 0)
                        sb.append(","); // add comma in between
                    sb.append(d.getFile().replace(prefixToRemove, ""));
                    if (addStartEndLine) {
                        sb.append("#" + d.getStartLine() + "#" + d.getEndLine());
                    }
                    resultCount++;
                }
                sb.append("\n");
            }
            return sb.toString();
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }

    public String format(ArrayList<ArrayList<Document>> results, int[][] sim, int threshod, String prefixToRemove) {
        if (this.format.equals("csv")) {
            StringBuilder sb = new StringBuilder();
            for (int i =0; i<results.size(); i++) {
                int resultCount = 0;
                ArrayList<Document> qResults = results.get(i);
                for (int j=0; j<qResults.size(); j++) {
                    Document d = qResults.get(i);
                    if (resultCount > 0)
                        sb.append(","); // add comma in between
                    // only add the results that has similarity higher than the threshold
                    if (sim[i][j] >= threshod) {
                        sb.append(d.getFile().replace(prefixToRemove, ""));
                        if (addStartEndLine) {
                            sb.append("#" + d.getStartLine() + "#" + d.getEndLine() + "#" + sim[i] + "#" + d.getLicense());
                        }
                        resultCount++;
                    }
                }
                sb.append("\n");
            }
            return sb.toString();
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }
}
