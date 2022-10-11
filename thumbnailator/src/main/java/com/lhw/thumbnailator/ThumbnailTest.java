package com.lhw.thumbnailator;

import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

/**
 * @author ：linhw
 * @date ：22.9.15 13:36
 * @description：
 * @modified By：
 */
public class ThumbnailTest {

    public static void main(String[] args) throws IOException {
        Thumbnails.of("E:\\temp\\1.jpg").size(200,200).toFile("E:\\temp\\1(1).jpg");
        Thumbnailator.createThumbnail(new File(""), 200, 200);
    }

}
