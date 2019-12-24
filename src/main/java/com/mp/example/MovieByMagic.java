package com.mp.example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class MovieByMagic implements PageProcessor {

    private   Site site= Site.me().setRetrySleepTime(3).setSleepTime(1000);


    /**
     * 内容解析
     * 1.正则表达式
     * 2.CSSStyle
     * 3.jsoup
     * 4.xpath
     *
     * */
    private String movieDetailPageLinkRegex="/html/gndy/\\w{4}/\\d{8}/\\d{5}.html";
    private Pattern movieDetailPageLinkPattern=Pattern.compile(movieDetailPageLinkRegex);
    @Override
    public void process(Page page) {
        if(page.getUrl().equals("http://www.dytt8.net")) {
            Html html = page.getHtml();
            String movieDetailPageLinkTagXpath = "//div[@class='bd3l']//div[@class='co_content2']/ul/a[@href]";
            Selectable movieDetailPageLinkTagselect = html.xpath(movieDetailPageLinkTagXpath);
            List<String> movieDetailPageLinkTagList = movieDetailPageLinkTagselect.all();
            for (int i = 1; i < movieDetailPageLinkTagList.size(); i++) {
                String movieDetailPageLinkTag = movieDetailPageLinkTagList.get(i);
                Matcher movieDetailPageLinkTagMatcher = movieDetailPageLinkPattern.matcher(movieDetailPageLinkTag);
                if (movieDetailPageLinkTagMatcher.find()) {
                    System.out.println("查询出来的结果" + movieDetailPageLinkTagMatcher.group());
                    String movieDetailPageLink = movieDetailPageLinkTagMatcher.group();
                    page.addTargetRequest(movieDetailPageLink);
                }
            }
        }else {
           Html  html=page.getHtml();
           String movieNameXpath="//title/text()";
           String movieLinkXpath="//a[starts-with(@herf,'ftp')]/text()";
           Selectable movieNameXpathselect = html.xpath(movieNameXpath);
           Selectable movieLinkXpathselect = html.xpath(movieLinkXpath);
           if( movieNameXpathselect.match() &&  movieLinkXpathselect.match() ){
               String movieName=movieNameXpathselect.get();
               String movieLink=movieLinkXpathselect.get();
               System.out.println(""+movieName+""+movieLink);
           }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     *
     * 1.网络请求
     * 2.内容分析和抽取
     * */
    public static void main(String[] args) {

        Spider.create(new MovieByMagic()).addUrl("http://www.dytt8.net").thread(1).run();

    }
}
