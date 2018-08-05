        private void saveZipEntries(Object[] entries, ZipOutputStream zos) throws IOException {
            for (int i = 0; i < entries.length; ) {
                String entryName = (String) entries[i++];
                Object entry = entries[i++];
                zos.putNextEntry(new ZipEntry(entryName));
                if (entry instanceof String) {
                    this.saveContents((String) entry, zos);
                } else {
                    this.saveZipEntries((Object[]) entry, zos);
                }
            }
        }
