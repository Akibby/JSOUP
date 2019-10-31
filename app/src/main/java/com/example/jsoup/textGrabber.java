package com.example.jsoup;

import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class textGrabber {


    private final String url = "https://www.spaceappsottawa.com/mobile/";
    private String textContent = "Page hasn't finished loading.";

    public String getTextContent(){
        return textContent;
    }

    private ArrayList<Element> getChildren(Element e) {
        ArrayList<Element> childList = new ArrayList<Element>();
        if (e.children() != null){
            for (int i = 0; i < e.children().size(); i++){
                childList.add(e.child(i));
                ArrayList<Element> grandchildList = getChildren(e.child(i));
                for (int j = 0; j < grandchildList.size(); j++){
                    childList.add(grandchildList.get(j));
                }
            }
        }
        return childList;
    }

    private ArrayList<String> getBaseTags(ArrayList<Element> elist){
        ArrayList<String> base = new ArrayList<>();
        for (int i = 0; i < elist.size(); i++){
            Element curE = elist.get(i);
            String tag = curE.tagName();
            if (curE.childNodeSize() == 0 ||
                    curE.childNodeSize() == 1 ||
                    tag == "h1" ||
                    tag == "h2" ||
                    tag == "li" ||
                    tag == "p" ||
                    tag == "a"){
//                baseE.add(elist.get(i).toString());
                base.add(curE.toString());
            }
        }
        return base;
    }

    public void getWebsite(final String page){
        new Thread(new Runnable() {
            @Override
            public void run() {
                textContent = "Page hasn't finished loading.";
                final StringBuilder builder = new StringBuilder();
                final String urlRequested = url + page + "/index.html";
                try {
                    Document document = Jsoup.connect(urlRequested).get();

                    Element genesis = document.getElementById("genesis-content");

                    ArrayList<String> baseE = getBaseTags(getChildren(genesis));
                    for (int i = 0; i < baseE.size(); i++){
                        builder.append(baseE.get(i)).append("\n\n");
                    }

//                    for (int i = 0; i < genesis.children().size(); i++) {
//                        Element e = genesis.child(i);
//                        ArrayList<Element> elist = getChildren(e);
//                        for (int j = 0; j < elist.size(); j++){
//                            builder.append(elist.get(j)).append("Section "+ (j+1) + " of " + elist.size() + "\n\n");
//                        }
//                    }

//                    builder.append("h1\n");
//                    Elements h1 = genesis.getElementsByTag("h1");
//                    for (int i = 0; i < h1.size(); i++){
//                        builder.append(h1.get(i).text()).append("\n");
//                    }
//                    builder.append("\n");
//
//                    builder.append("h2\n");
//                    Elements h2 = genesis.getElementsByTag("h2");
//                    for (int i = 0; i < h2.size(); i++){
//                        builder.append(h2.get(i).text()).append("\n");
//                    }
//                    builder.append("\n");
//
//                    builder.append("p\n");
//                    Elements p = genesis.getElementsByTag("p");
//                    for (int i = 0; i < p.size(); i++){
//                        builder.append(p.get(i).text()).append("\n");
//                    }
//                    builder.append("\n");
//
//                    builder.append("li\n");
//                    Elements li = genesis.getElementsByTag("li");
//                    for (int i = 0; i < li.size(); i++){
//                        builder.append(li.get(i).text()).append("\n");
//                    }
//                    builder.append("\n");
//
//                    builder.append("a\n");
//                    Elements a = genesis.getElementsByTag("a");
//                    for (int i = 0; i < a.size(); i++){
//                        builder.append(a.get(i).attr("href")).append("\n");
//                    }
//                    builder.append("\n");


                    textContent = builder.toString();
                    if (textContent == ""){
                        textContent = "No content on " + page;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    textContent = "Error grabbing content on " + page;
                }
            }
        }).start();
    }
}
