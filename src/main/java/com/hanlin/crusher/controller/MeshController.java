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

//        Map<String, String> keyValues = new HashMap<String, String>() {{
//            put("734.jpeg", "https://i.guim.co.uk/img/media/fe12f65605d563fef03ac97336034c67be449354/0_10_7360_4414/master/7360.jpg?width=1200&height=1200&quality=85&auto=format&fit=crop&s=ca94f6d0dfa8c0bc925d5a4c62b7596c");
//            put("a.jpeg", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvvtN3RqRevksgJli80I1eO8N5mW-UcAYm4Q&usqp=CAU");
//            put("test2.doc", "https://resource.hle.com.tw/Books/BooksResource/110%E4%B8%8A%E9%AB%98%E4%B8%AD%E8%8B%B1%E6%96%87%28%E4%B8%80%29%E6%95%99%E5%AD%B8PPT-L1%28110f342560%29.pptx");
//            put("test.doc", "https://resource.hle.com.tw/Books/BooksResource/110%E4%B8%8A%E9%AB%98%E4%B8%AD%E8%8B%B1%E6%96%87%28%E4%B8%80%29%E6%95%99%E5%AD%B8PPT-L6%28110f342570%29.pptx");
//        }};

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
