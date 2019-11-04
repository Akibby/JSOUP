package com.example.jsoup;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class textGrabber {


    private final String url = "https://www.spaceappsottawa.com/mobile/";
    private String textContent = "Page hasn't finished loading.";
    private String title = "";
    private double gpsX = 0.0;
    private double gpsY = 0.0;

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
            if (tag == "a") {
                String link = curE.attr("href");
                if (link.contains("www.google.com/maps")) {
                    try {
                        String[] splitLink = link.split(",");
                        gpsX = Double.parseDouble(splitLink[0].split("@")[1]);
                        gpsY = Double.parseDouble(splitLink[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        gpsX = 0.0;
                        gpsY = 0.0;
                    }

                }
            } else if (curE.childNodeSize() == 0 ||
                    curE.childNodeSize() == 1 ||
                    tag == "h1" ||
                    tag == "h2" ||
                    tag == "li" ||
                    tag == "p"){
                base.add(curE.text());
            }
        }
        return base;
    }

    public double[] getCoords(){
        return new double[] {gpsX, gpsY};
    }

    public String getWebsite(final String page){
        textContent = "Page hasn't finished loading.";
        final StringBuilder builder = new StringBuilder();
        final String urlRequested = url + page + "/index.html";
        try {
            Document document = Jsoup.connect(urlRequested).get();

            Element genesis = document.getElementById("genesis-content");
            title = genesis.getElementsByClass("entry-title").get(0).text();

            ArrayList<String> baseE = getBaseTags(getChildren(genesis));
            for (int i = 0; i < baseE.size(); i++){
                builder.append(baseE.get(i)).append("\n\n");
            }

            double coord1 = getCoords()[0];
            double coord2 = getCoords()[1];
            builder.append("COORDS: "+coord1 + ","+ coord2+"\n\n");
            textContent = builder.toString();
            if (textContent == ""){
                textContent = "No content on " + page;
            }
        } catch (IOException e) {
            e.printStackTrace();
            textContent = "Error grabbing content on " + page;
        }
    return textContent;
    }
}
