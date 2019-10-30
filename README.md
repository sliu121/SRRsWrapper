# Application for SearchEngine Wiki

*This project aims at creating an application that can **help our main project by adding and supplementing pieces of information for our project’s database**. As mentioned above, we are trying to develop a search engine wiki that could contain all websites with search query tools as we could. Since it is a wiki, there should be too many websites links we need to add it to our database, which is impossible for people to work manually*



 ## Connector
There are step works that connector needs to do, build a connection with Bing, send search queries to Bing and acquire results message (websites).
* ### Connect with Bing
	* In class `Main().Connector().connectwBing()`, there is one incoming parameter, `input(String)`, which is the query user wants to input in the searching box. Here is a part of the codes in the class:
		```Java
		String url = "https://www.bing.com";
        WebDriver browser = new ChromeDriver();
		browser.get(url);
    	```	
* ### Send query to Bing
	* In class `Main().Connector().InputQuery()`, there are two incoming parameters, `input(String)`, `browser(Webdriver)`. Input, as mentioned above, is the query. `Browser` is the variable I created in `connectwBing()` method, and as it is supposed, the home page of Bing. Here are the codes in the class: 
		```Java
       WebElement search_box = browser.findElement(By.id("sb_form_q"));
		search_box.sendKeys(input);
		search_box.sendKeys(Keys.ENTER);
        ```
	* Since in this project, Bing is the only search engine concerned with, I just read lots of HTML files from Bing and found  `"sb_form_q"` is where the query should be inputted. Furthermore, I also found there will be a problem in the WebElement variable if you want to click (by programming) the search button after query input, so I choose `sendKeys(Keys.ENTER)` rather than `findElement`. 


 ## Wrapper
 After receiving the query from users, the server will give a lot of response pages and we call those pages as Search result records (SRRs). However, in those response pages, there are some unnecessary items such as advertisements or something user not looking for, then the metasearch engine's job is extracting the correct SRRs and sorting them for users, which we called it extraction wrapper. 
* ### Find the location of SRRs
	* After checking original Html files from Bing search, I found all information about SRRs is in the division b_results of division b_content. In class `Main().Ex_Wrapper().findSRRs()`, our main idea is separating SRRs with all the other contents in the Html file. 
		```java
		String first_mrak = "b_results";
		String SRRs = Compared2String(string,first_mrak);
		```
	* All separating operations are processing in `Ex_Wrapper().Compared2Strings()`, 
		```Java
    	for(int i = 0;i<str1.length();i++){
			if(str1.substring(i,i+str2_len).equals(str2)){
				return str1.substring(i,str1.length()-1);
			}
		}
    	```

* ### Separate every SRR info
	* In `Ex_Wrapper().divideSRRs()`, the main idea is separating SRRs to pieces, one link with one title, during this processing, HashSet could help to ger rid of some redundant SRRs such as the same webpage occurring several times in search results. About HashSet, This class implements the Set interface, backed by a hash table (actually a HashMap instance). It makes no guarantees as to the iteration order of the set; in particular, it does not guarantee that the order will remain constant over time. Also, HashSet.add() method would return false if there exists a same element in the set. 

	* After checking the results above, SRR is in `<h2>...</h2>`, so here find `<h2>` firstly,
		```JAVA
         if(string.substring(i,i+left_side.length()).equals(left_side)){
			foundh2head = true;
            position = i+left_side.length();
         }
		```
	* Then find </h2>, and save this string into the set. 
		```JAVA
       if(foundh2head && string.substring(i,i+right_side.length()).equals(right_side)){
       		res.add(string.substring(position,i));
            count++;
            foundh2head = false;
            position = 0;
       }
        ```

	* After the processing, all elements in the set are separated SRRs by each one. 

* ### Save links with titles
	* Finally, the last step needed to be finished is dividing the elements in the set into titles and links. As required, in this process, results should be link-only. 
		```JAVA
		for(int i = 0;i<linkWtitle.length();i++){
		
			if(i+link_left.length() == linkWtitle.length() || 
				i + link_right.length() == linkWtitle.length())  break;
   

			if(!ISleft && linkWtitle.substring(i,i+link_left.length()).equals(link_left)){
            	ISleft = true;
				left_position = i;
	 	  	}

			if(ISleft && linkWtitle.substring(i,i+link_right.length()).equals(link_right)){
				res.add(linkWtitle.substring(left_position,i));
	       			ISleft = false;
	       			left_position = 0;
	   		}
        }
        ```



