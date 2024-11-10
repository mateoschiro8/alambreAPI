package com.alambre.models;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import com.google.zxing.WriterException;

public class QRGenerator {
    private static final String QR_CODE_IMAGE_PATH = "src/main/resources/static/qrcodes/";

    private static QRGenerator instance = new QRGenerator();

    private QRGenerator() {}

    public static QRGenerator getInstance() {
        return instance;
    }

    public BufferedImage generateQRCodeImage(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        } catch (WriterException e) {
            e.printStackTrace(); 
            return null;
        }

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    public boolean saveQRCodeImage(String text, String fileName) {
        BufferedImage qrImage = generateQRCodeImage(text);
        
        if (qrImage == null) return false;

        String restaurantName = fileName.split("/")[0];
        Path directoryPath = Paths.get(QR_CODE_IMAGE_PATH + restaurantName);

        if (!directoryPath.toFile().exists()) {
            directoryPath.toFile().mkdirs();
        }

        File outputFile = new File(QR_CODE_IMAGE_PATH + "/" + fileName + ".png");
        try {
            return ImageIO.write(qrImage, "PNG", outputFile);
        } catch (IOException e) {
            e.printStackTrace();  
            return false;
        }
    }
}
