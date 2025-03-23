package com.toolkit.spring.controller.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.toolkit.spring.util.APIResponse;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/image")
public class ImageController
{

    @Value("${image.root}")
    private String uploads;

    private final SpringTemplateEngine templateEngine;

    public ImageController(SpringTemplateEngine templateEngine)
    { this.templateEngine = templateEngine; } 


    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<APIResponse> uploadImage(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty())
        { return APIResponse.error(400, Map.of("message", "No file uploaded")); }

        
        try 
        {
            String filename = file.getOriginalFilename();
            String extension = ".png";
            if(filename == null)
            { filename = ""; }

            int extensionIndex = filename.lastIndexOf(".");
            if(extensionIndex >= 0)
            {
                extension = filename.substring(extensionIndex + 1);
                filename = filename.substring(0, extensionIndex);
            }

            filename = filename + "_" + System.currentTimeMillis() + "." + extension;
            File uploadFile = new File(uploads + filename);
            file.transferTo(uploadFile);

            return APIResponse.success(201, Map.of(
                "message", "Image saved successfuly",
                "filename", filename
            ));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return APIResponse.error(500, Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/pdf")
    public void pdf(
        @RequestParam("name") String exportName,
        @RequestParam("file") String imageSource,
        @RequestParam("title") String title,
        Model model,
        HttpServletResponse response
    )
    {
        try
        {    
            model.addAttribute("image", "file:///" + uploads.replace('\\', '/') + imageSource);

            model.addAttribute("title", title);

            Context context = new Context();
            model.asMap().forEach(context::setVariable);
    
            String htmlContent = templateEngine.process("/pages/image/pdf", context);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + exportName + ".pdf");
    
            try (OutputStream os = response.getOutputStream()) {
                ITextRenderer renderer = new ITextRenderer();
    
                renderer.setDocumentFromString(htmlContent);
                renderer.layout();
                renderer.createPDF(os, false);
                renderer.finishPDF();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
