 Author : Princy RAZAFIMANANTSOA, Exchange student at PSU Computer Science Department

 mail : princy@pdx.edu


 This is the description of a solution of the dinner party problem (https://github.com/princyraza/DinnerPartyAI/blob/master/DinnerPartyProblemDescription.txt).
 The solution as been developed using java and Eclipse Juno. 
 To launch the solution you may build it with an IDE (Eclipse) and run it using your java   virtual machine. 
 To change the input and output files you have to modify the code.

 Hardware specification :
  - MacBook Pro
  - Processor : Intel Core i7
  - Memory : 8 GB
 
 OS : OSX 10.8.2 Mountain Lion
 
 Search space :  
 The search space is a graph of all the tables. Each table table is a node of the graph. 
 There's a vertex between two tables t1 and t2 if we can swap 2 people of t1 to obtain t2.
 
 Algorithm pseudocode :


 localSearch(currentTable,bestTable,path) :
 
  if we explore too many table
  
		return bestTable
		
	if currentTable is not visited yet then
	
		bestTableYet <- bestTable
		
		Add currentTable into path //path is the list of visited state
		
		ListOfNeighbors <- some neighbors of currentTable
		
		for each neighbor n in ListOfNeighbors
		
			add n into path
			
			if score(n)score(bestTableYet)
			
				bestTableYet = n
				
			bestTableYet = localSearch(n,bestTableYet,path)
			
	return bestTableYet
	
  
 Algorithm description:
 The algorithm begins with a random table. Then we select some children (or neighbors or adjacent tables) of the initial table.
 This is the selection process :
   - pick a random neighbor out of the graph (or landscape)
   - if this neighbor has a better than score than his parent then select it
   - else we try to select this neighbor with a probability p to be selected
 The last step of the selection process allows the algorithm to explore the landscape. Thus we can reach a better solution by selecting bad tables sometimes.
 I add this step to avoid being stuck in a local maxima.
 After selecting a set of neighbor we recursively call the algorithm for each of them. Of course the best table discovered yet is recorded :
	 for each neighbor n in ListOfNeighbors
		if score(n)score(bestTableYet)
			bestTableYet = n
		bestTableYet = localSearch(n,bestTableYet,path)
  
 Finally we return the best table discovered during the recursive calls. The algorithm will stop the search if a maximum depth is reached.
  
 This algorithm is repeated several time during 60 seconds using a different starting random table each time.
 The best table of all the calls is returned as a good solution and output into a file.
