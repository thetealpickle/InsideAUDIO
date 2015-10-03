package io.indico.api.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Created by Chris on 6/23/15.
 */
public class ImageUtils {
    public static String encodeImage(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.encodeBase64String(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageString;
    }

    @SuppressWarnings("unchecked")
    public static Rectangle getRectangle(Map<String, List<Double>> res) {
        List<Double> topLeft = res.get("top_left_corner");
        List<Double> bottomRight = res.get("bottom_right_corner");

        int top = topLeft.get(1).intValue();
        int left = topLeft.get(0).intValue();
        int bottom = bottomRight.get(1).intValue();
        int right = bottomRight.get(0).intValue();

        return new Rectangle((left + right) / 2, (top + bottom) / 2, right - left, top - bottom);
    }

    public static List<BufferedImage> convertToImage(List<?> images, int size, boolean minAxis)
        throws IOException {
        List<BufferedImage> convertedInput = new ArrayList<>();
        for (Object entry : images) {
            if (entry instanceof File) {
                convertedInput.add(convertToImage((File) entry, size, minAxis));
            } else if (entry instanceof String) {
                convertedInput.add(convertToImage(new File((String) entry), size, minAxis));
            } else {
                throw new IllegalArgumentException(
                    "imageCall method only supports lists of Files and lists of Strings"
                );
            }
        }
        return convertedInput;
    }

    public static BufferedImage convertToImage(File imageFile, int size, boolean minAxis) throws IOException {
        if (size == -1) {
            return ImageIO.read(imageFile);
        }

        BufferedImage image = ImageIO.read(imageFile);

        double aspect = image.getWidth() / image.getHeight();
        if (aspect >= 10 || aspect <= .1)
            System.out.println("WARNING: We we recommend images with aspect ratio less than 1:10");

        Scalr.Mode method = minAxis ? Scalr.Mode.AUTOMATIC : Scalr.Mode.FIT_EXACT;
        return Scalr.resize(image, method, size);
    }


    public static String grabType(String filePath) throws IOException {
        return grabType(new File(filePath));
    }

    public static String grabType(File imageFile) throws IOException {
        return FilenameUtils.getExtension(imageFile.getName());
    }

    public static String grabType(List<?> images) {
        String type;
        Object entry = images.get(0);

        if (entry instanceof File) {
            type = FilenameUtils.getExtension(((File) entry).getName());
        } else if (entry instanceof String) {
            type = FilenameUtils.getExtension((String) entry);
        } else {
            throw new IllegalArgumentException(
                "imageCall method only supports lists of Files and lists of Strings"
            );
        }

        return type;
    }
}
