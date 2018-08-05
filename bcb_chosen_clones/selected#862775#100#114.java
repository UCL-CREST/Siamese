        private void writeQuad() throws IOException {
            this.zos.putNextEntry(new ZipEntry("quad.bin"));
            Gbl.startMeasurement();
            this.quad = new OTFServerQuad(this.net);
            System.out.print("build Quad on Server: ");
            Gbl.printElapsedTime();
            onAdditionalQuadData();
            Gbl.startMeasurement();
            this.quad.fillQuadTree(new OTFDefaultNetWriterFactoryImpl());
            System.out.print("fill writer Quad on Server: ");
            Gbl.printElapsedTime();
            Gbl.startMeasurement();
            new ObjectOutputStream(this.zos).writeObject(this.quad);
            this.zos.closeEntry();
        }
