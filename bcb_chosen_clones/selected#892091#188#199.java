    void writeMapIndex(ZipOutputStream output) throws IOException {
        DataOutputStream os;
        output.putNextEntry(new ZipEntry(MAP_INDEX_FILE));
        os = new DataOutputStream(output);
        os.writeInt(this.jarMapIds.length + 1);
        for (int i = 0; i < this.jarMapIds.length; ++i) {
            os.writeInt(this.jarMapIds[i]);
        }
        os.writeInt(this.newMapId);
        os.flush();
        output.closeEntry();
    }
