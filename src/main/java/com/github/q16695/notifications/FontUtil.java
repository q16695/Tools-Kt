package com.github.q16695.notifications;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

public class FontUtil {
    @Property
    public static String FONTS_LOCATION = "resources/fonts";

    public FontUtil() {
    }

    public static void loadAllFonts() {
        boolean isJava6 = OS.javaVersion == 6;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Enumeration<URL> fonts = LocationResolver.getResources(FONTS_LOCATION);
        if (fonts.hasMoreElements()) {
            fonts.nextElement();

            while(fonts.hasMoreElements()) {
                URL url = (URL)fonts.nextElement();
                InputStream is = null;

                try {
                    String path = url.toURI().getPath();
                    if (path.endsWith(".ttf") || !isJava6 && path.endsWith(".otf")) {
                        is = url.openStream();
                        Font newFont = Font.createFont(0, is);
                        ge.registerFont(newFont);
                    }
                } catch (IOException var19) {
                    var19.printStackTrace();
                } catch (URISyntaxException var20) {
                    var20.printStackTrace();
                } catch (FontFormatException var21) {
                    var21.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException var18) {
                            var18.printStackTrace();
                        }
                    }

                }
            }
        }

    }

    public static Font parseFont(String fontInfo) {
        try {
            int sizeIndex = fontInfo.lastIndexOf(" ");
            String size = fontInfo.substring(sizeIndex + 1);
            int styleIndex = fontInfo.indexOf(" ", sizeIndex - 7);
            String styleString = fontInfo.substring(styleIndex + 1, sizeIndex);
            int style = 0;
            if (styleString.equalsIgnoreCase("bold")) {
                style = 1;
            } else if (styleString.equalsIgnoreCase("italic")) {
                style = 2;
            }

            String fontName = fontInfo.substring(0, styleIndex);
            return new Font(fontName, style, Integer.parseInt(size));
        } catch (Exception var7) {
            throw new RuntimeException("Unable to load font info from '" + fontInfo + "'", var7);
        }
    }

    public static Font getFontForSpecificHeight(Font font, int height) {
        int size = font.getSize();
        Boolean lastAction = null;

        while(true) {
            while(true) {
                Font fontCheck = new Font(font.getName(), 0, size);
                int maxFontHeight = getMaxFontHeight(fontCheck);
                if (maxFontHeight >= height || lastAction == Boolean.FALSE) {
                    if (maxFontHeight <= height || lastAction == Boolean.TRUE) {
                        return fontCheck;
                    }

                    --size;
                    lastAction = Boolean.FALSE;
                } else {
                    ++size;
                    lastAction = Boolean.TRUE;
                }
            }
        }
    }

    public static int getFontHeight(Font font, String string) {
        BufferedImage image = new BufferedImage(1, 1, 2);
        Graphics2D g = image.createGraphics();
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, string);
        int height = gv.getPixelBounds((FontRenderContext)null, 0.0F, 0.0F).height;
        g.dispose();
        return height;
    }

    public static int getFontWidth(Font font, String string) {
        BufferedImage image = new BufferedImage(1, 1, 2);
        Graphics2D g = image.createGraphics();
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, string);
        int width = gv.getPixelBounds((FontRenderContext)null, 0.0F, 0.0F).width;
        g.dispose();
        return width;
    }

    public static int getAlphaNumericFontHeight(Font font) {
        BufferedImage image = new BufferedImage(1, 1, 2);
        Graphics2D g = image.createGraphics();
        FontMetrics metrics = g.getFontMetrics(font);
        int height = metrics.getAscent() + metrics.getDescent();
        g.dispose();
        return height;
    }

    public static int getMaxFontHeight(Font font) {
        BufferedImage image = new BufferedImage(1, 1, 2);
        Graphics2D g = image.createGraphics();
        FontMetrics metrics = g.getFontMetrics(font);
        int height = metrics.getMaxAscent() + metrics.getMaxDescent();
        g.dispose();
        return height;
    }

    public static BufferedImage getFontAsImage(Font font, String text, Color foregroundColor) {
        BufferedImage img = new BufferedImage(1, 1, 2);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();
        if (width > height) {
            height = width;
        } else {
            width = height;
        }

        img = new BufferedImage(width, height, 2);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(foregroundColor);
        g2d.drawString(text, (float)width / 4.0F, (float)fm.getAscent());
        g2d.dispose();
        return img;
    }
}
