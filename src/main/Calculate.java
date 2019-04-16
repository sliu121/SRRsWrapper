package main;

import java.io.*;
import java.util.*;

public class Calculate {
    /**
     * # This program is to calculate depth for each first-level title
     * # since we need to find 10 first levels with 10 sub level each,
     * # I want to write this program to find a better result
     * # first I will check how many sub levels in each first level and the depth in each first level
     */
    private static class ValueComparator implements Comparator<Map.Entry<String, Integer>>{
        @Override
        public int compare(Map.Entry<String, Integer> m, Map.Entry<String, Integer> n) {
            return n.getValue()-m.getValue();
        }
    }

    public static void main(String[] args) throws IOException {
//        File filename = new File("category_path.txt","r");
        Map<String,Integer> title_counts = new HashMap<>();
        Map<String,Integer> title_depth = new HashMap<>();
        String filepath = "category_path.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line = reader.readLine();

        while (line!=null){
            String catogery = line;
            if(!catogery.contains(">")) {
                line = reader.readLine();
            }else {
//                int first_path_mark = catogery.indexOf(" > ");
//                String title_name = catogery.substring(0,first_path_mark);
//                /**
//                 * Now here we calculate the number of each title.
//                 */
//                if(title_counts.containsKey(title_name)){
//                    title_counts.put(title_name,title_counts.get(title_name)+1);
//                }else{
//                    title_counts.put(title_name,1);
//                }
//
//                /**
//                 * Here we find the depth the number of each title
//                 */
//                int level =1;
//                while(catogery.contains(" > ")){
//                    level+=1;
//                    catogery = catogery.substring(catogery.indexOf(" > ")+1);
//                }
//                if(title_depth.containsKey(title_name)){
//                    int depth = (title_depth.get(title_name)>level)? title_depth.get(title_name):level;
//                    title_depth.put(title_name,depth);
//                }else{
//                    title_depth.put(title_name,level);
//                }
                int level =1;
                int count = 1;
                String string = line;
                String level_3 = "";
                while(level<4){
                    if(string.isEmpty()) break;

                    String mark = " > ";
                    if(string.contains(mark)){
                        level_3+=string.substring(0,string.indexOf(mark)+mark.length());
                        string = string.substring(string.indexOf(mark)+mark.length());
                    }else if(!string.contains(mark)&&level == 3) {
                        level_3+=string;
                    }else if(!string.contains(mark) && level!=3){
                        break;
                    }
                    level++;
                    if(level == 4) {
                        if(title_counts.containsKey(level_3)){
                            title_counts.put(level_3,title_counts.get(level_3)+1);
                        }else{
                            title_counts.put(level_3,count);
                        }
                    }
                }
                level_3 = "";
                line = reader.readLine();
            }
        }
        reader.close();

        List<Map.Entry<String,Integer>> list_count=new ArrayList<>();
        List<Map.Entry<String,Integer>> list_depth=new ArrayList<>();

        list_count.addAll(title_counts.entrySet());
        list_depth.addAll(title_depth.entrySet());
        ValueComparator valueComparator_count = new ValueComparator();
        ValueComparator valueComparator_depth = new ValueComparator();
        Collections.sort(list_count,valueComparator_count);
        Collections.sort(list_depth,valueComparator_depth);

        System.out.println("Count:");
        for(Iterator<Map.Entry<String,Integer>> it=list_count.iterator();it.hasNext();)
        {
            System.out.println(it.next());
        }
        list_count.clear();
        list_depth.clear();
        title_counts.clear();
        title_depth.clear();

    }

}
