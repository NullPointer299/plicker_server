package main;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Capture {

    static byte[] getScreenCapture(final float quality) throws IOException, AWTException {

        Robot robot = new Robot();
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage org = robot.createScreenCapture(screenRect);
        ImageOutputStream imageOut = ImageIO.createImageOutputStream(byteOut);

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);
        writer.setOutput(imageOut);
        writer.write(null, new IIOImage(org, null, null), param);
        writer.dispose();

        return byteOut.toByteArray();
    }
}
