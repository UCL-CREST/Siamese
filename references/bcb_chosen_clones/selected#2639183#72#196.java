    public BufferedImage extractUsingCompositor() throws IOException, DjatokaException {
        boolean useRegion = false;
        int left = 0;
        int top = 0;
        int width = 50;
        int height = 50;
        boolean useleftDouble = false;
        Double leftDouble = 0.0;
        boolean usetopDouble = false;
        Double topDouble = 0.0;
        boolean usewidthDouble = false;
        Double widthDouble = 0.0;
        boolean useheightDouble = false;
        Double heightDouble = 0.0;
        if (params.getRegion() != null) {
            StringTokenizer st = new StringTokenizer(params.getRegion(), "{},");
            String token;
            if ((token = st.nextToken()).contains(".")) {
                topDouble = Double.parseDouble(token);
                usetopDouble = true;
            } else top = Integer.parseInt(token);
            if ((token = st.nextToken()).contains(".")) {
                leftDouble = Double.parseDouble(token);
                useleftDouble = true;
            } else left = Integer.parseInt(token);
            if ((token = st.nextToken()).contains(".")) {
                heightDouble = Double.parseDouble(token);
                useheightDouble = true;
            } else height = Integer.parseInt(token);
            if ((token = st.nextToken()).contains(".")) {
                widthDouble = Double.parseDouble(token);
                usewidthDouble = true;
            } else width = Integer.parseInt(token);
            useRegion = true;
        }
        if (is != null) {
            File f = File.createTempFile("tmp", ".jp2");
            f.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(f);
            sourceFile = f.getAbsolutePath();
            IOUtils.copyStream(is, fos);
        }
        Kdu_simple_file_source raw_src = null;
        Jp2_family_src family_src = new Jp2_family_src();
        Jpx_source wrapped_src = new Jpx_source();
        Kdu_region_compositor compositor = null;
        BufferedImage image = null;
        try {
            family_src.Open(sourceFile);
            int success = wrapped_src.Open(family_src, true);
            if (success < 0) {
                family_src.Close();
                wrapped_src.Close();
                raw_src = new Kdu_simple_file_source(sourceFile);
            }
            compositor = new Kdu_region_compositor();
            if (raw_src != null) compositor.Create(raw_src); else compositor.Create(wrapped_src);
            Kdu_dims imageDimensions = new Kdu_dims();
            compositor.Get_total_composition_dims(imageDimensions);
            Kdu_coords imageSize = imageDimensions.Access_size();
            Kdu_coords imagePosition = imageDimensions.Access_pos();
            if (useleftDouble) left = imagePosition.Get_x() + (int) Math.round(leftDouble * imageSize.Get_x());
            if (usetopDouble) top = imagePosition.Get_y() + (int) Math.round(topDouble * imageSize.Get_y());
            if (useheightDouble) height = (int) Math.round(heightDouble * imageSize.Get_y());
            if (usewidthDouble) width = (int) Math.round(widthDouble * imageSize.Get_x());
            if (useRegion) {
                imageSize.Set_x(width);
                imageSize.Set_y(height);
                imagePosition.Set_x(left);
                imagePosition.Set_y(top);
            }
            int reduce = 1 << params.getLevelReductionFactor();
            imageSize.Set_x(imageSize.Get_x());
            imageSize.Set_y(imageSize.Get_y());
            imagePosition.Set_x(imagePosition.Get_x() / reduce - (1 / reduce - 1) / 2);
            imagePosition.Set_y(imagePosition.Get_y() / reduce - (1 / reduce - 1) / 2);
            Kdu_dims viewDims = new Kdu_dims();
            viewDims.Assign(imageDimensions);
            viewDims.Access_size().Set_x(imageSize.Get_x());
            viewDims.Access_size().Set_y(imageSize.Get_y());
            compositor.Add_compositing_layer(0, viewDims, viewDims);
            if (params.getRotationDegree() == 90) compositor.Set_scale(true, false, true, 1.0F); else if (params.getRotationDegree() == 180) compositor.Set_scale(false, true, true, 1.0F); else if (params.getRotationDegree() == 270) compositor.Set_scale(true, true, false, 1.0F); else compositor.Set_scale(false, false, false, 1.0F);
            compositor.Get_total_composition_dims(viewDims);
            Kdu_coords viewSize = viewDims.Access_size();
            compositor.Set_buffer_surface(viewDims);
            int[] imgBuffer = new int[viewSize.Get_x() * viewSize.Get_y()];
            Kdu_compositor_buf compositorBuffer = compositor.Get_composition_buffer(viewDims);
            int regionBufferSize = 0;
            int[] kduBuffer = null;
            Kdu_dims newRegion = new Kdu_dims();
            while (compositor.Process(100000, newRegion)) {
                Kdu_coords newOffset = newRegion.Access_pos();
                Kdu_coords newSize = newRegion.Access_size();
                newOffset.Subtract(viewDims.Access_pos());
                int newPixels = newSize.Get_x() * newSize.Get_y();
                if (newPixels == 0) continue;
                if (newPixels > regionBufferSize) {
                    regionBufferSize = newPixels;
                    kduBuffer = new int[regionBufferSize];
                }
                compositorBuffer.Get_region(newRegion, kduBuffer);
                int imgBuffereIdx = newOffset.Get_x() + newOffset.Get_y() * viewSize.Get_x();
                int kduBufferIdx = 0;
                int xDiff = viewSize.Get_x() - newSize.Get_x();
                for (int j = 0; j < newSize.Get_y(); j++, imgBuffereIdx += xDiff) {
                    for (int i = 0; i < newSize.Get_x(); i++) {
                        imgBuffer[imgBuffereIdx++] = kduBuffer[kduBufferIdx++];
                    }
                }
            }
            if (params.getRotationDegree() == 90 || params.getRotationDegree() == 270) image = new BufferedImage(imageSize.Get_y(), imageSize.Get_x(), BufferedImage.TYPE_INT_RGB); else image = new BufferedImage(imageSize.Get_x(), imageSize.Get_y(), BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, viewSize.Get_x(), viewSize.Get_y(), imgBuffer, 0, viewSize.Get_x());
            if (compositor != null) compositor.Native_destroy();
            wrapped_src.Native_destroy();
            family_src.Native_destroy();
            if (raw_src != null) raw_src.Native_destroy();
            return image;
        } catch (KduException e) {
            e.printStackTrace();
            throw new DjatokaException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DjatokaException(e);
        }
    }
