package com.lhw.thumbnailator.simple;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ：linhw
 * @date ：23.1.30 16:14
 * @description：采用蒙版层的方式在图片部分区域添加水印
 * @modified By：
 */
public class CreateWaterMarkLayer {

    public static void main(String[] args) throws IOException {

        BufferedImage image = ImageIO.read(new File("E:\\temp\\thumbnailator\\watermark\\480dpi.png"));

        BufferedImage target = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics2D = target.createGraphics();

        graphics2D.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

        addNorth(graphics2D, target);
        addWaterMark(graphics2D, image);

        FileOutputStream outputStream = new FileOutputStream(new File("E:\\temp\\thumbnailator\\watermark\\test2.png"));
        ImageIO.write(target, "png", outputStream);
        outputStream.close();
        graphics2D.dispose();
    }

    private static void addWaterMark(Graphics2D graphics2D, BufferedImage image) throws IOException {
        BufferedImage waterMarkImage = createWaterMarkLayer("南宁市信息集团", image.getHeight(), image.getWidth());
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3f));
        graphics2D.drawImage(waterMarkImage, (int)(image.getWidth() * 0.03),
                (int)(image.getHeight() * 0.07),
                waterMarkImage.getWidth(),
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
        int fontSize = 60;
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
