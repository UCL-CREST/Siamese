    public void streamDataToZip(VolumeTimeSeries vol, ZipOutputStream zout) throws Exception {
        ZipEntry entry = new ZipEntry(vol.getId() + ".dat");
        zout.putNextEntry(entry);
        if (vol instanceof FloatVolumeTimeSeries) {
            streamData(((FloatVolumeTimeSeries) vol).getData(), zout);
        } else if (vol instanceof RGBVolumeTimeSeries) {
            streamData(((RGBVolumeTimeSeries) vol).getARGBData(), zout);
        } else if (vol instanceof BinaryVolumeTimeSeries) {
            streamData(((BinaryVolumeTimeSeries) vol).getBinaryData(), zout);
        }
    }
