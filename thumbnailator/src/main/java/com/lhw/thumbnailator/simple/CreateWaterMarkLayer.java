package com.lhw.thumbnailator.simple;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author ：linhw
 * @date ：23.1.30 16:14
 * @description：采用蒙版层的方式在图片部分区域添加水印
 * @modified By：
 */
public class CreateWaterMarkLayer {

    private static final String inputFileUrl = "E:\\temp\\thumbnailator\\watermark\\512dpi.png";
    private static final String outputFileUrl = "E:\\temp\\thumbnailator\\watermark\\test4.png";
    private static final String outputFileType = "png";
    private static final String fileUrl = "http://192.168.200.90:16080/arcgis/rest/directories/arcgisjobs/exportwebmapbyselfdesigntemplate_gpserver/j6bbe2f12d49f45e48ee9cd1a91bbc51c/scratch/25a4bffc8f424b7db75261a31ff4a003.png";
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws IOException {
//        BufferedImage target = getFromLocalFile();
        BufferedImage target = getFromUrl();

        FileOutputStream outputStream = new FileOutputStream(new File(outputFileUrl));
        ImageIO.write(target, outputFileType, outputStream);
        outputStream.close();
    }

    private static BufferedImage getFromUrl() throws IOException {
        ResponseEntity<Resource> resource = restTemplate.getForEntity(fileUrl, Resource.class);
        InputStream inputStream = Objects.requireNonNull(resource.getBody()).getInputStream();
        return dataHandler(ImageIO.read(inputStream));
    }

    private static BufferedImage getFromLocalFile() throws IOException {
        BufferedImage image = ImageIO.read(new File(inputFileUrl));
        return dataHandler(image);
    }

    private static BufferedImage dataHandler(BufferedImage image) throws IOException {
        BufferedImage target = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics2D = target.createGraphics();

        graphics2D.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

        addNorth(graphics2D, target);
        addWaterMark(graphics2D, image);
        graphics2D.dispose();
        return target;
    }

    private static void addWaterMark(Graphics2D graphics2D, BufferedImage image) throws IOException {
        BufferedImage waterMarkImage = createWaterMarkLayer("南宁市信息集团", image.getHeight(), image.getWidth());
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3f));
        graphics2D.drawImage(waterMarkImage, (int)(image.getWidth() * 0.03),
                (int)(image.getHeight() * 0.07),
                (int)(waterMarkImage.getWidth() * 0.95),
                (int)(waterMarkImage.getHeight() * 0.72), null);
    }

    private static void addNorth(Graphics2D graphics2D, BufferedImage target) throws IOException {
        BufferedImage northImage = ImageIO.read(new File("E:\\temp\\thumbnailator\\watermark\\north.png"));
        graphics2D.drawImage(northImage,
                (int)(target.getWidth() - (target.getWidth() * 0.04 + northImage.getWidth())),
                (int)(target.getHeight() * 0.1),
                northImage.getWidth(), northImage.getHeight(), null);
    }

    private static BufferedImage createWaterMarkLayer(String text, int height, int width) throws IOException {
        String fontStyle = "微软雅黑";
        int xPadding = width / 20;
        int yPadding = height / 20;
        int angel = 315;
        int fontSize = (int)(width * 0.01);
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics2D = target.createGraphics();

        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, width, height);
        Font font = new Font(fontStyle, Font.ITALIC, fontSize);
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(font);
        //水印串宽度
        int stringWidth = graphics2D.getFontMetrics(graphics2D.getFont()).charsWidth(text.toCharArray(), 0, text.length());
        //设置透明度
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3f));
        graphics2D.rotate(Math.toRadians(angel), (double)width / 2, (double)height / 2);

        int x = -height / 2;
        int y;
        int half = 2;
        while (x < width + width / half) {
            y = -height / 2;
            while (y < height + height / half) {
                graphics2D.drawString(text, x , y);
                y += yPadding;
            }
            x += stringWidth + xPadding;
        }
        graphics2D.dispose();
        return target;
    }

}
