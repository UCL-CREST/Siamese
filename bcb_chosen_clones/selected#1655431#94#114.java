        private void writeQuad() throws IOException {
            this.zos.putNextEntry(new ZipEntry("quad.bin"));
            Gbl.startMeasurement();
            this.quad = new OTFServerQuad(this.net);
            System.out.print("build Quad on Server: ");
            Gbl.printElapsedTime();
            Gbl.startMeasurement();
            OTFConnectionManager connect = new OTFConnectionManager();
            connect.add(QueueLink.class, OTFLinkLanesAgentsNoParkingHandler.Writer.class);
            connect.add(QueueNode.class, OTFDefaultNodeHandler.Writer.class);
            onAdditionalQuadData(connect);
            this.quad.fillQuadTree(connect);
            System.out.print("fill writer Quad on Server: ");
            Gbl.printElapsedTime();
            Gbl.startMeasurement();
            new ObjectOutputStream(this.zos).writeObject(this.quad);
            this.zos.closeEntry();
            this.zos.putNextEntry(new ZipEntry("connect.bin"));
            new ObjectOutputStream(this.zos).writeObject(connect);
            this.zos.closeEntry();
        }
