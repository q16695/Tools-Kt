package com.github.q16695.utils

import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.ImageIcon

object ImageUtil {
    fun toBufferedImage(image: Image): BufferedImage {
        var image = image
        if (image is BufferedImage) {
            return image
        }

        // This code ensures that all the pixels in the image are loaded
        image = ImageIcon(image).image

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        //boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        var bimage: BufferedImage? = null
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        try {
            // Determine the type of transparency of the new buffered image
            val transparency = Transparency.OPAQUE
            /* if (hasAlpha) {
             transparency = Transparency.BITMASK;
             }*/

            // Create the buffered image
            val gs = ge.defaultScreenDevice
            val gc = gs.defaultConfiguration
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency
            )
        } catch (e: HeadlessException) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            val type = BufferedImage.TYPE_INT_RGB
            //int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
            /*if (hasAlpha) {
             type = BufferedImage.TYPE_INT_ARGB;
             }*/bimage = BufferedImage(image.getWidth(null), image.getHeight(null), type)
        }

        // Copy image to buffered image
        val g: Graphics = bimage.createGraphics()

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null)
        g.dispose()
        return bimage
    }
}
