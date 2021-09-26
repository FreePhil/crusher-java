package com.hanlin.crusher.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.UriComponentsBuilderMethodArgumentResolver;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.tomcat.util.http.fileupload.IOUtils.copyLarge;

@RestController
@RequestMapping(value="api", produces="application/zip")
public class MeshController {

    private final static String CONTENT_HEADER = "Content-Disposition";
    private final static String CONTENT_HEADER_FORMAT = "attachment; filename=\"%s\"";

    @GetMapping("zip")
    public void getZip(HttpServletResponse response) throws IOException, URISyntaxException {

        var filename = "my-test.zip";
        var contentHeaderValue = String.format(CONTENT_HEADER_FORMAT, filename);

        Map<String, String> keyValues = new HashMap<String, String>() {{
            put("1.pptx", "110上高中英文(一)教學PPT-L7(110f342572).pptx");
            put("2.pptx", "110上高中英文(一)教學PPT-L9(110f342576).pptx");
            put("3.pptx", "110上高中英文(一)教學PPT-L8(110f342574).pptx");
            put("4.pptx", "110上高中英文(一)教學PPT-L1(110f342560).pptx");
            put("5.pptx", "110上高中英文(一)教學PPT-L5(110f342568).pptx");
            put("6.pptx", "110上高中英文(一)教學PPT-L4(110f342566).pptx");
            put("7.pptx", "110上高中英文(一)教學PPT-L2(110f342562).pptx");
            put("8.pptx", "110上高中英文(一)教學PPT-L6(110f342570).pptx");
        }};

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(CONTENT_HEADER, contentHeaderValue);
        var zipStream = new ZipOutputStream(response.getOutputStream());

        for (String key: keyValues.keySet()) {
            zipStream.putNextEntry(new ZipEntry(key));
            var fileUrl = URLEncoder.encode(keyValues.get(key), StandardCharsets.UTF_8);
            var url = String.format("https://resource.hle.com.tw/Books/BooksResource/%s", fileUrl);
            try (var urlStream = new URL(url).openStream()) {
                long byteCount = IOUtils.copyLarge(urlStream, zipStream);
                System.out.printf("transfer %d bytes from %s\n", byteCount, url);
            }
            zipStream.closeEntry();
        }
        zipStream.close();
    }
}
