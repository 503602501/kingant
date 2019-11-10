package com.crawler.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

public class JLabelUnderLineSimple extends JLabel {
	private Color underLineColor;

    public JLabelUnderLineSimple() {
        this("");
    }

    public JLabelUnderLineSimple(String text) {
        super(text);
    }

    /**
     * @return the underLineColor
     */
    public Color getUnderLineColor() {
        return underLineColor;
    }

    /**
     * @param pUnderLineColor the underLineColor to set
     */
    public void setUnderLineColor(Color pUnderLineColor) {
        underLineColor = pUnderLineColor;
    }

    public void paint(Graphics g) {
        Rectangle r;
        super.paint(g);
        r = g.getClipBounds();
        if (null != underLineColor) {
            g.setColor(underLineColor);
        }
        // 画出下划线
        g.drawLine(0, r.height - getFontMetrics(getFont()).getDescent(),
                getFontMetrics(getFont()).stringWidth(getText()), r.height
                        - getFontMetrics(getFont()).getDescent());
    }

}
