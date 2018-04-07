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

    private String gcf(Document d, String prefixToRemove) {
        StringBuilder sb = new StringBuilder();
        sb.append("<Fragment>\n");
        sb.append("<File>" + d.getFile().replace(prefixToRemove, "") + "</File>\n");
        sb.append("<Start>" + d.getStartLine() + "</Start>\n");
        sb.append("<End>" + d.getEndLine() + "</End>\n");
        sb.append("<License>" + d.getLicense() + "</License>\n");
        sb.append("</Fragment>\n");
        return sb.toString();
    }

    private String gcf(ArrayList<Document> results, String prefixToRemove) {
        StringBuilder sb = new StringBuilder();
        for (int i =0; i<results.size(); i++) {
            Document d = results.get(i);
            sb.append("<Fragment>\n");
            sb.append("<File>" + d.getFile().replace(prefixToRemove, "") + "</File>\n");
            sb.append("<Start>" + d.getStartLine() + "</Start>\n");
            sb.append("<End>" + d.getEndLine() + "</End>\n");
            sb.append("<License>" + d.getLicense() + "</License>\n");
            sb.append("</Fragment>\n");
        }
        return sb.toString();
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
            sb.append("<Clone>\n");
            sb.append(gcf(query, prefixToRemove));
            return sb.toString();
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
            sb.append(gcf(results, prefixToRemove));
            sb.append("</Clone>\n");
            return sb.toString();
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
            sb.append(gcf(results, prefixToRemove));
            sb.append("</Clone>\n");
            return sb.toString();
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }
}
