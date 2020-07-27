// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.Context;

import java.util.Locale;

public class TVMultilingualText
{
    private static final String TAG = "TVMultilingualText";
    
    public static String getText(final Context context, final String formatText, String lang) {
        String ret = "";
        boolean useFirst = false;
        if (formatText == null || lang == null || formatText.isEmpty()) {
            return ret;
        }
        if (lang.equalsIgnoreCase("local")) {
            final Locale defaultLocale = Locale.getDefault();
            if (defaultLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
                lang = "chs";
            }
            else if (defaultLocale.equals(Locale.TRADITIONAL_CHINESE)) {
                lang = "chi";
            }
            else {
                lang = Locale.getDefault().getISO3Language();
            }
        }
        else if (lang.equalsIgnoreCase("first")) {
            useFirst = true;
        }
        String split;
        if (formatText.contains(new String(new byte[] { -128 }))) {
            split = new String(new byte[] { -128 });
        }
        else {
            split = new String(new char[] { '\u0080' });
        }
        final String[] langText = formatText.split(split);
        for (int i = 0; langText != null && i < langText.length; ++i) {
            final TVMultilingualText inst = new TVMultilingualText();
            final MultilingualText text = inst.new MultilingualText(context, langText[i]);
            if (useFirst || text.getLangage().equalsIgnoreCase(lang)) {
                ret = text.getText();
                break;
            }
        }
        return ret;
    }
    
    public static String getText(final Context context, final String formatText) {
        String ret = "";
        final String configLangs = TVConfigResolver.getConfig(context, "tv:scan:dtv:ordered_text_languages", "local first");
        if (configLangs == null || configLangs.isEmpty()) {
            return ret;
        }
        final String[] langs = configLangs.split(" ");
        if (langs != null && langs.length > 0) {
            for (int i = 0; i < langs.length; ++i) {
                ret = getText(context, formatText, langs[i]);
                if (ret != null && !ret.isEmpty()) {
                    break;
                }
            }
        }
        return ret;
    }
    
    private class MultilingualText
    {
        private String language;
        private String text;
        
        public MultilingualText(final Context context, final String formatString) {
            if (formatString != null && formatString.length() >= 3) {
                this.language = formatString.substring(0, 3);
                if (this.language.equalsIgnoreCase("xxx")) {
                    this.language = TVConfigResolver.getConfig(context, "tv:scan:dtv:default_text_language", "eng");
                }
                if (formatString.length() > 3) {
                    this.text = formatString.substring(3, formatString.length());
                }
                else {
                    this.text = "";
                }
            }
            else {
                this.language = "";
                this.text = "";
            }
        }
        
        public String getLangage() {
            return this.language;
        }
        
        public String getText() {
            return this.text;
        }
    }
}
