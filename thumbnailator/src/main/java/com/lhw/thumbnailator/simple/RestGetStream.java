package com.lhw.thumbnailator.simple;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author ：linhw
 * @date ：23.2.7 14:06
 * @description：
 * @modified By：
 */
public class RestGetStream {

    private static final String url = "http://192.168.200.90:16080/arcgis/rest/directories/arcgisjobs/exportwebmapbyselfdesigntemplate_gpserver/j6bbe2f12d49f45e48ee9cd1a91bbc51c/scratch/25a4bffc8f424b7db75261a31ff4a003.png";
    private static final String outPath = "E:\\temp\\thumbnailator\\watermark\\download.png";

    public static void main(String[] args) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Resource> entity = restTemplate.getForEntity(url, Resource.class);
        InputStream inputStream = Objects.requireNonNull(entity.getBody()).getInputStream();
        System.out.println(entity.getBody().getFilename());
        IOUtils.copy(inputStream, new FileOutputStream(new File(outPath)));
    }

}
