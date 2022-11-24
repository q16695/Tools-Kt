package com.github.q16695.notifications;

import java.awt.*;

public class Theme {
    final Color panel_BG;
    final Color titleText_FG;
    final Color mainText_FG;
    final Color closeX_FG;
    final Color progress_FG;
    final Font titleTextFont;
    final Font mainTextFont;

    Theme(String titleTextFont, String mainTextFont, boolean isDarkTheme) {
        this.titleTextFont = FontUtil.parseFont(titleTextFont);
        this.mainTextFont = FontUtil.parseFont(mainTextFont);
        if (isDarkTheme) {
            this.panel_BG = Color.DARK_GRAY;
            this.titleText_FG = Color.GRAY;
            this.mainText_FG = Color.LIGHT_GRAY;
            this.closeX_FG = Color.GRAY;
            this.progress_FG = Color.gray;
        } else {
            this.panel_BG = Color.WHITE;
            this.titleText_FG = Color.GRAY.darker();
            this.mainText_FG = Color.GRAY;
            this.closeX_FG = Color.LIGHT_GRAY;
            this.progress_FG = new Color(4367861);
        }

    }

    public Theme(String titleTextFont, String mainTextFont, Color panel_BG, Color titleText_FG, Color mainText_FG, Color closeX_FG, Color progress_FG) {
        this.titleTextFont = FontUtil.parseFont(titleTextFont);
        this.mainTextFont = FontUtil.parseFont(mainTextFont);
        this.panel_BG = panel_BG;
        this.titleText_FG = titleText_FG;
        this.mainText_FG = mainText_FG;
        this.closeX_FG = closeX_FG;
        this.progress_FG = progress_FG;
    }
}
