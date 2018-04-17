    private void renameTo(File from, File to) {
        if (!from.exists()) return;
        if (to.exists()) to.delete();
        boolean worked = false;
        try {
            worked = from.renameTo(to);
        } catch (Exception e) {
            database.logError(this, "" + e, null);
        }
        if (!worked) {
            database.logWarning(this, "Could not rename GEDCOM to " + to.getAbsolutePath(), null);
            try {
                to.delete();
                final FileReader in = new FileReader(from);
                final FileWriter out = new FileWriter(to);
                int c;
                while ((c = in.read()) != -1) out.write(c);
                in.close();
                out.close();
                from.delete();
            } catch (Exception e) {
                database.logError(this, "" + e, null);
            }
        }
    }
