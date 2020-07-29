    @RequestMapping(value = "/verdocumentoFisico.html", method = RequestMethod.GET)
    public String editFile(ModelMap model, @RequestParam("id") int idAnexo) {
        Anexo anexo = anexoService.selectById(idAnexo);
        model.addAttribute("path", anexo.getAnexoCaminho());
        try {
            InputStream is = new FileInputStream(new File(config.baseDir + "/arquivos_upload_direto/" + anexo.getAnexoCaminho()));
            FileOutputStream fos = new FileOutputStream(new File(config.baseDir + "/temp/" + anexo.getAnexoCaminho()));
            IOUtils.copy(is, fos);
            Runtime.getRuntime().exec("chmod 777 " + config.tempDir + anexo.getAnexoCaminho());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "verdocumentoFisico";
    }
