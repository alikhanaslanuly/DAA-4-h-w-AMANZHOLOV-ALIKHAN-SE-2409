package util.io;
import java.nio.file.*;
import java.util.*;

public class JsonGraph {
    public boolean directed;
    public int n;
    public List<Edge> edges = new ArrayList<>();
    public int source = 0;
    public String weight_model = "edge";

    public static class Edge { public int u; public int v; public long w; public Edge(int u,int v,long w){this.u=u;this.v=v;this.w=w;} }

    public static JsonGraph readFromFile(Path p) throws Exception {
        String s = Files.readString(p);
        JsonGraph g = new JsonGraph();
        g.directed = s.contains("\"directed\": true");
        int idxN = s.indexOf("\"n\"");
        if (idxN >= 0) {
            int colon = s.indexOf(':', idxN);
            if (colon >= 0) {
                int i = colon+1;
                while (i < s.length() && (s.charAt(i)==' ' || s.charAt(i)=='\n' || s.charAt(i)=='\r' || s.charAt(i)=='\t')) i++;
                StringBuilder num = new StringBuilder();
                while (i < s.length() && Character.isDigit(s.charAt(i))) { num.append(s.charAt(i)); i++; }
                if (num.length()>0) g.n = Integer.parseInt(num.toString());
            }
        }
        int idxS = s.indexOf("\"source\"");
        if (idxS >= 0) {
            int colon = s.indexOf(':', idxS);
            if (colon >= 0) {
                int i = colon+1;
                while (i < s.length() && !Character.isDigit(s.charAt(i)) && s.charAt(i)!='-') i++;
                StringBuilder num = new StringBuilder();
                while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i)=='-')) { num.append(s.charAt(i)); i++; }
                if (num.length()>0) g.source = Integer.parseInt(num.toString());
            }
        }
        int idxW = s.indexOf("\"weight_model\""); 
        if (idxW >= 0) {
            int q1 = s.indexOf('"', idxW + 1);
            if (q1 >= 0) {
                int q2 = s.indexOf('"', q1+1);
                if (q2 >= 0) {
                    int tokenStart = s.indexOf('"', q2+1);
                    if (tokenStart >= 0) {
                        int tokenEnd = s.indexOf('"', tokenStart+1);
                        if (tokenEnd > tokenStart) {
                            String token = s.substring(tokenStart+1, tokenEnd);
                            if (token.equals("edge") || token.equals("node")) g.weight_model = token;
                        }
                    }
                }
            }
        }
        int idxE = s.indexOf("\"edges\"");
        if (idxE >= 0) {
            int start = s.indexOf('[', idxE);
            int end = s.indexOf(']', start);
            if (start >= 0 && end > start) {
                String block = s.substring(start+1, end);
                int i = 0;
                while (i < block.length()) {
                    int open = block.indexOf('{', i);
                    if (open < 0) break;
                    int close = block.indexOf('}', open);
                    if (close < 0) break;
                    String obj = block.substring(open+1, close);
                    Integer u = null, v = null; Long w = null;
                    String[] parts = obj.split(",");
                    for (String ppart : parts) {
                        String t = ppart.trim();
                        if (t.contains("\"u\"")) {
                            int colon = t.indexOf(':');
                            if (colon>=0) {
                                String num = extractNumber(t, colon+1);
                                if (num!=null) u = Integer.parseInt(num);
                            }
                        } else if (t.contains("\"v\"")) {
                            int colon = t.indexOf(':');
                            if (colon>=0) {
                                String num = extractNumber(t, colon+1);
                                if (num!=null) v = Integer.parseInt(num);
                            }
                        } else if (t.contains("\"w\"")) {
                            int colon = t.indexOf(':');
                            if (colon>=0) {
                                String num = extractNumber(t, colon+1);
                                if (num!=null) w = Long.parseLong(num);
                            }
                        }
                    }
                    if (u!=null && v!=null) {
                        if (w==null) w = 1L;
                        g.edges.add(new Edge(u, v, w));
                    }
                    i = close + 1;
                }
            }
        }
        return g;
    }

    private static String extractNumber(String s, int start) {
        int i = start;
        while (i < s.length() && (s.charAt(i)==' ' || s.charAt(i)=='\n' || s.charAt(i)=='\r' || s.charAt(i)=='\t')) i++;
        StringBuilder num = new StringBuilder();
        boolean neg = false;
        if (i < s.length() && s.charAt(i)=='-'){ neg=true; i++; }
        while (i < s.length() && Character.isDigit(s.charAt(i))) { num.append(s.charAt(i)); i++; }
        if (num.length()==0) return null;
        return (neg?"-":"") + num.toString();
    }
}
