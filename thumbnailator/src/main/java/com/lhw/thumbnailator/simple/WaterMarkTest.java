package com.lhw.thumbnailator.simple;

import lombok.Data;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：linhw
 * @date ：23.1.29 16:57
 * @description：添加水印测试
 * @modified By：
 */
public class WaterMarkTest {

    public static void main(String[] args) throws IOException {

        BufferedImage image = ImageIO.read(new File("E:\\temp\\thumbnailator\\watermark\\480dpi.png"));
        System.out.println(image.getWidth());
        System.out.println(image.getHeight());
        System.out.println(image.getMinX());
        System.out.println(image.getType());

        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();

        String msg = "上海数慧";


//        Rectangle rectangle = calcWaterMarkRectangle(imgHeight * 0.8, imgWidth * 0.2);

        BufferedImage target = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics2D = target.createGraphics();

        graphics2D.drawImage(image, 0, 0, imgWidth, imgHeight, null);

        BufferedImage waterMarkImage = ImageIO.read(new File("E:\\temp\\thumbnailator\\watermark\\1.jpg"));
        graphics2D.drawImage(waterMarkImage, imgWidth / 2, imgHeight / 2, waterMarkImage.getWidth(), waterMarkImage.getHeight(), null);

        FileOutputStream outputStream = new FileOutputStream(new File("E:\\temp\\thumbnailator\\watermark\\test1.png"));
        ImageIO.write(target, "png", outputStream);
        outputStream.close();
        graphics2D.dispose();
    }

    private static BufferedImage createWaterMarkLayer(String text, int height, int width) {
        String fontStyle = "微软雅黑";
        int xPadding = width / 20;
        int yPadding = height / 20;
        int angel = 315;
        int fontSize = 50;
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics2D = target.createGraphics();


//        graphics2D.drawImage(image, 0, 0, imgWidth, imgHeight, null);

        Font font = new Font(fontStyle, Font.ITALIC, fontSize);
//        graphics2D.setColor(new Color(5,20,55));
        graphics2D.setColor(Color.decode("#000000"));
        graphics2D.setFont(font);
        //水印串宽度
        int stringWidth = graphics2D.getFontMetrics(graphics2D.getFont()).charsWidth(text.toCharArray(), 0, text.length());
        //设置透明度
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3f));
        graphics2D.rotate(Math.toRadians(angel), (double)width / 2, (double)height / 2);

        int x = -height / 2;
        int y;
        while (x < width + width / 2) {
            y = -height / 2;
            while (y < height + height / 2) {
                graphics2D.drawString(text, x , y);
                y += yPadding;
            }
            x += stringWidth + xPadding;
        }
        return target;
    }

    private static Rectangle calcWaterMarkRectangle(double height, double width) {
        double diagonal = Math.sqrt(height * height + width * width);
        double tempX = (Math.pow(height, 2) - Math.pow(width, 2) + Math.pow(diagonal, 2)) / (2 * diagonal);
        double tempY = Math.sqrt(Math.pow(height, 2) - Math.pow(tempX, 2));

        Rectangle rectangle = new Rectangle();
        rectangle.setLength(diagonal);
        rectangle.setWidth(2 * tempY);

        List<Point> points = new ArrayList<>();
        points.add(new Point(tempX, 0));
        points.add(new Point(0, tempY));
        points.add(new Point(diagonal, tempY));
        points.add(new Point(diagonal - tempX, 2 * tempY));
        rectangle.setPointList(points);

        return rectangle;
    }

    private static boolean checkPoint(Rectangle rectangle, double x, double y) {
        List<Point> points = rectangle.getPointList();
        if (points.size() < 3) {
            return false;
        }
        int sum = 0;
        double tempX;
        Point point, otherPoint;
        for (int i = 0; i < points.size(); i++) {
            point = points.get(i);
            if (i == points.size() - 1) {
                otherPoint = points.get(0);
            }else {
                otherPoint = points.get(i + 1);
            }

//            if ((y >= point.getY() && y < otherPoint.getY())
//                    ||(y >= otherPoint.getY() && y < point.getY())) {
                if (Math.abs(point.getY() - otherPoint.getY()) > 0) {
                    tempX = point.getX() -
                            (((point.getX() - otherPoint.getX()) * (point.getY() - y))
                                    / (point.getY() - otherPoint.getY()));

                    if (tempX < x) {
                        sum++;
                    }
                }
//            }
        }

        if (sum == 2) {
            return true;
        }

        return false;
    }

    @Data
    static class Point {
        double x;
        double y;

        public Point(){}

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    @Data
    static class Rectangle {
        double length;
        double width;
        List<Point> pointList;
    }

}
