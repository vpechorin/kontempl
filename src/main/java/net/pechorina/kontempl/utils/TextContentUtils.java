package net.pechorina.kontempl.utils;

import org.jsoup.Jsoup;

public class TextContentUtils {

    public TextContentUtils() {
    }
    
    public String convertLineBreaks(String text) {
        if (text == null) {
            return "";
        }
        text = text.replaceAll("\\r", "");
        text = text.trim();
        text = text.replaceAll("\\n{2,}", "\\\n\\\n");
        text = text.replaceAll("\\n", "<br>\\\n");
        //text = StringUtils.convertLFtoBRs(text);
        
        return text;
    }
        
    public String extractKeywords(String text) {
        if (text == null) {
            return text;
        }
        text = text.replaceAll("\\r", "");
        text = text.trim();
        text = text.replaceAll("\\n{2,}", "\\\n\\\n");
        text = text.replaceAll("\\n", "<br>\\\n");
        //text = StringUtils.convertLFtoBRs(text);
        
        return text;
    }
    
    public String extractTextFromHTML(String html) {
    	if (html == null) {
    		return "";
    	}
    	return Jsoup.parse(html).text();
    }
}
