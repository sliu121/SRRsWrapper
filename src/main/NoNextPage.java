package main;

public class NoNextPage {
    public static boolean confirmnextpage(String string){
        return (string.contains("title=\"下一页\""))? true:false;
    }
}
