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

    public String format(ArrayList<Document> results, String prefixToRemove) {
        if (this.format.equals("csv")) {
            StringBuilder sb = new StringBuilder();
            int resultCount = 0;
            for (Document d : results) {
                if (resultCount > 0)
                    sb.append(","); // add comma in between

                sb.append(d.getFile().replace(prefixToRemove, ""));

                if (addStartEndLine) {
                    sb.append("#" + d.getStartLine() + "#" + d.getEndLine());
                }
                resultCount++;
            }
            return sb.toString();
        } else {
            System.out.println("ERROR: unsupported format.");
            return null;
        }
    }
}
