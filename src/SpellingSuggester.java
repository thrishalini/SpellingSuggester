/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Verma
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.util.*;

public class SpellingSuggester {
    //Variables Declaration 
    public static Map<String, Integer> dictionary = new HashMap<String, Integer>();
    static int cr = 0;
    static int ic = 0;
    static SpellingSuggester obj = new SpellingSuggester();
    static List<String> modifiers_list;
    static ArrayList MRR = new ArrayList();
    
    //Function to get the possible match based on the user input
    public static String exact(String term) {
        modifiers_list = modifiers(term);
        
        if(dictionary.containsKey(term)){
            return term;
        }
         //Mapping to count terms using dicationary
        Map<Integer, String> candidates = new HashMap<Integer, String>();
        //Iterating over the dictionary to store the term and its count
        for(String s : modifiers_list) {
            if(dictionary.containsKey(s)) {
                candidates.put(dictionary.get(s), s);
            }
        }
        // Returning the most popular or most weighted term from the possible matches
        if(candidates.size() > 0)
            return candidates.get(Collections.max(candidates.keySet()));
        // Iterate again in case we do not get a possible match
        for(String s : modifiers_list) {
            try{
                List<String> newModifiers = modifiers(s);
                for(String ns : newModifiers) {
                   if(dictionary.containsKey(ns)) {
                       candidates.put(dictionary.get(ns), ns);
                    }
                }
            }catch(Exception e){

            }
        }
        if(candidates.size() > 0){
            String a1 = candidates.get(Collections.max(candidates.keySet()));
            return a1;
        }
        else
            return term;
   }
   // Function to get all possible exactions c of a given term w
    public static List<String> modifiers(String term) {
    // Condition to check for a empty input to search which results in null output
    if(term == null || term.isEmpty())
      return null;

    List<String> list = new ArrayList<String>();

    String w = null;
    // Deletes one letter
    for (int count = 0; count < term.length(); count++) {
        w = term.substring(0, count) + term.substring(count + 1);
        list.add(w);
    }
    // Swapping the adjacent letters)
    for (int count = 0; count < term.length() - 1; count++) { 
        w = term.substring(0, count) + term.charAt(count + 1) + term.charAt(count) + term.substring(count + 2); 
        list.add(w);
    }
    // Replacing one letter with other letter
    for (int count = 0; count < term.length(); count++) {
        for (char c = 'a'; c <= 'z'; c++) {
            w = term.substring(0, count) + c + term.substring(count + 1); 
            list.add(w);
        }
    }
    // Inserting a letter
    for (int count = 0; count <= term.length(); count++) { 
        for (char c = 'a'; c <= 'z'; c++) {
            w = term.substring(0, count) + c + term.substring(count);
            list.add(w);
     }
    }
    // Getting the final list
    return list;
   }  
 public static void main(String[] args) {
    // Variables Declaration 
    BufferedReader read_file;
    ArrayList time = new ArrayList();
    Scanner sc = new Scanner(System.in);
    String str,ans;
    Boolean a;
    try {
			read_file = new BufferedReader(new FileReader("linuxwords.txt"));
			String file_line = read_file.readLine();       
			while (file_line != null) {
                            file_line = file_line.replace("\n", "");
                            dictionary.put(file_line, 1);
                            // read next file_line
			    file_line = read_file.readLine();
			}
                        read_file.close();
		} catch (IOException e) {
			e.printStackTrace();
        }
    // Test Cases and result preparation.
    time.add(obj.test("too", "too"));
    time.add(obj.test("dazzed", "dazzled"));
    time.add(obj.test("spelleng", "spelling"));
    time.add(obj.test("juiyy", "juicy"));
    time.add(obj.test("to", "to"));
    time.add(obj.test("five", "five"));
    time.add(obj.test("abble", "rabble"));
    time.add(obj.test("knowlege", "knowledge"));
    time.add(obj.test("quantizng", "quantizing"));
    Collections.sort(time);
    
    // Uncomment these two lines to display the result of Time and MRR after running above cases. 
    // System.out.println("Execution time for inputs: "+time);
    // System.out.println("MRR values for inputs: "+MRR);
      
    System.out.println("-------------------------------------------------------");
    char ch = 'y';
    while(ch == 'y')
    {
        System.out.print("Enter a string for spelling suggestion: ");
        str = sc.next();
        if(obj.isChars(str)){
            ans = exact(str);
            System.out.println("Input String was: "+str+", Suggested String is: "+ans);
        }
        else{
            System.out.println("Not a valid string");
        }
        System.out.print("Do you want to try another string (Press y for yes): ");
        ch = sc.next().charAt(0);
        System.out.println("-------------------------------------------------------");
        
    }
 }
    // Functnio to check entered input is character or not
    public boolean isChars(String str) {
        return str.matches("[a-zA-Z]+");
    }
    // Functnio to test the user input foe spelling suggestion 
    public double test(String a1, String a2){
        //Declaration and initialization of required objects and variables. 
        long start = System.currentTimeMillis(); 
        List<String> modifiersnew = new ArrayList();
        Random rand = new Random(); 
        String str1 = exact(a1);
        Boolean a = str1.equals(a2);
        int index = modifiers_list.indexOf(str1);
        int low = 1;
        int high = 3;
        int val = rand.nextInt(high-low) + low;
        String s;
        try{
            for (int count = 1;count<=val;count++){
                s = modifiers_list.get(index-count);
                modifiersnew.add(s);
            }
            modifiersnew.add(str1);
            val = rand.nextInt(high-low) + low; //generate a random numebr between two values
            for (int count = 1;count<=val;count++){
                s = modifiers_list.get(index+count);
                modifiersnew.add(s);
            }
        }catch(Exception e){
            
        }
        double mrr = calculateMRR(modifiersnew,str1);
        if(a)
            cr++;
        else
            ic++;
        long end = System.currentTimeMillis(); 
        return (end - start);
    }
    // Function to calculate MRR 
    public static double calculateMRR(List<String> suggestions,String term){
        //initialization of used variables
        int count = suggestions.indexOf(term);
        int j = count;
        double calc = 0;
        double mrr = 1.0;
        //try catch block for exception handling. 
        try{
            for(String s : suggestions) {
                if(s.equals(term)){ //Compare the strings
                    break;
                }
                else{
                    calc = calc + (1/count);
                    count=count-1;
                }    
            }
        mrr = (calc/j);
        }catch(ArithmeticException e){
        }
        if (suggestions.isEmpty()){
            mrr = 1;
        }
        MRR.add(mrr); //Add the MRR result to teh list
        Collections.sort(MRR);
        return mrr;
    }
}
