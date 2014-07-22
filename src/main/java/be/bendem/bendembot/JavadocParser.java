package be.bendem.bendembot;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bendem
 */
public class JavadocParser {
    // NOTE The allclasses frame all information about packages
    // (remove the base url, remove .html, split by "/", last is class name, join by ".")
    // TODO Search by class name
    // TODO Search by package
    // TODO Use wildcards in package name

    private String jdUrl;
    private int    maxResults;

    public JavadocParser(String jdUrl) {
        this(jdUrl, 5);
    }

    public JavadocParser(String jdUrl, int maxResults) {
        this.jdUrl = jdUrl;
        this.maxResults = maxResults;
    }

    public ArrayList<String> searchAllClasses(String search) {
        ArrayList<String> exact = new ArrayList<>();
        ArrayList<String> beginWith = new ArrayList<>();
        ArrayList<String> contains = new ArrayList<>();

        HashMap<String, String> allPackages;
        try {
            allPackages = getAllPackages();
        } catch(IOException e) {
            return null;
        }

        for(Map.Entry<String, String> linkPair : allPackages.entrySet()) {
            String link = linkPair.getKey();
            String clazz = linkPair.getValue();

            if(clazz.equalsIgnoreCase(search)) {
                exact.add(jdUrl + link);

            } else if(StringUtils.startsWithIgnoreCase(clazz, search)) {
                beginWith.add(jdUrl + link);

            } else if(StringUtils.containsIgnoreCase(clazz, search)) {
                contains.add(jdUrl + link);
            }
            if(exact.size() > maxResults) {
                break;
            }
        }

        if(exact.size() < maxResults) {
            exact.addAll(beginWith);
            if(exact.size() < maxResults) {
                exact.addAll(contains);
            }
        }
        if(exact.size() > maxResults) {
            exact = new ArrayList<>(exact.subList(0, maxResults));
        }

        return exact;
    }

    public ArrayList<String> searchByPackageName(String packageName) {
        String[] pack = packageName.split("\\.");
        String clazz = pack[pack.length-1];
        pack = ArrayUtils.subarray(pack, 0, pack.length-1);
        packageName = StringUtils.join(pack, ".");

        ArrayList<String> classFounds = new ArrayList<>();
        ArrayList<String> packageExactFounds = new ArrayList<>();
        ArrayList<String> packageFounds = new ArrayList<>();

        HashMap<String, String> allPackages;
        try {
            allPackages = getAllPackages();
        } catch(IOException e) {
            return null;
        }

        for(Map.Entry<String, String> pair : allPackages.entrySet()) {
            String link = pair.getKey();
            if(StringUtils.containsIgnoreCase(linkToPackage(link), packageName)
                    && pair.getValue().equalsIgnoreCase(clazz)) {
                classFounds.add(jdUrl + link);
            } else if(linkToPackage(link).equalsIgnoreCase(packageName)) {
                packageExactFounds.add(jdUrl + link);
            } else if(StringUtils.containsIgnoreCase(link, packageName)) {
                packageFounds.add(jdUrl + link);
            }
            if(classFounds.size() > maxResults) {
                break;
            }
        }

        if(classFounds.size() < maxResults) {
            classFounds.addAll(packageExactFounds);
            if(classFounds.size() < maxResults) {
                classFounds.addAll(packageFounds);
            }
        }

        if(classFounds.size() > maxResults) {
            classFounds = new ArrayList<>(classFounds.subList(0, maxResults));
        }

        return classFounds;
    }

    protected HashMap<String, String> getAllPackages() throws IOException {
        Document doc;
        doc = Jsoup.connect(jdUrl + "allclasses-frame.html").get();
        HashMap<String, String> packages = new HashMap<>();
        for(Element link : doc.select("a")) {
            packages.put(link.attr("href"), link.text());
        }

        return packages;
    }

    protected String linkToClassPath(String link) {
        return link.replace("/", ".").replace(".html", "");
    }

    protected String linkToPackage(String link) {
        return classPathToPackage(linkToClassPath(link));
    }

    protected String classPathToPackage(String classPath) {
        String[] parts = classPath.split("\\.");
        return StringUtils.join(ArrayUtils.subarray(parts, 0, parts.length-1), ".");
    }

    public int getMaxResults() {
        return maxResults;
    }

    public String getJdUrl() {
        return jdUrl;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public void setJdUrl(String jdUrl) {
        this.jdUrl = jdUrl;
    }

}
