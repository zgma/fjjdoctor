package com.qingeng.fjjdoctor.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlUtils {
    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
    public static String getNewContent(String htmltext) {
        try {
            Document doc = Jsoup.parse(htmltext);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                // elements.removeAttr("style");
                element.attr("width", "95%").attr("height", "auto").attr("style", "text-align:center;margin:0 auto");
            }
            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }

    public static String escapeHTML(String data){
        return data.replace("&nbsp;"," ")
                .replace("&lt;","<")
                .replace("&gt;",">")
                .replace("&amp;","&")
                .replace("&quot;","\"")
                .replace("\\n","</br>")
                .replace("\\\"","\"");
    }

}
