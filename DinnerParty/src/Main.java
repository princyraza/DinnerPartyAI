import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;


public class Main {
	private static int matrix [][];
	private static int nbGuest=0;
	public Main() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		FileReader fr;
		try {
			
//			String filename="hw1-inst1.txt";
			String filename="hw1-inst2.txt";
//			String filename="hw1-inst3.txt";
			String s="";
			ArrayList<String> a;
			ArrayList<Table> path = new ArrayList<Table>();
			
			fr = new FileReader(filename);
			BufferedReader in = new BufferedReader(fr);
			//reading number of guest
			s = in.readLine();
			nbGuest=Integer.parseInt(s);
			System.out.println("Number of guests : "+s);
			nbGuest=Integer.parseInt(s);
			matrix = new int[nbGuest+1][nbGuest+1];
			//reading preference matrix
			for(int i=1;i<nbGuest;i++)
			{
				s = in.readLine();
				a = StringToArrayList(s);
				int k=0;
				for(int j=1;j<=nbGuest;j++)
				{
					matrix[i][j]=Integer.parseInt(a.get(k));
					k++;
				}
			}
			
			Table initTable;
			Table resultTable = new Table(nbGuest);
			Table bestTable = resultTable;
			long init = System.currentTimeMillis();
			long duration = System.currentTimeMillis() - init;
			long seed=init; 
			while(duration<=1000*60)
			{
				initTable=randomTable(seed);
				resultTable=localSearch(initTable, initTable,path);
				if(resultTable.getScore()>bestTable.getScore())
				{
					bestTable = resultTable;
					System.out.println("\nscore " + resultTable.getScore());
					for(int i=1;i<resultTable.getSeats().length;i++)
					{
						System.out.print(resultTable.getSeats()[i]+" ");
					}
				}
				resultTable = new Table(nbGuest);
				duration = System.currentTimeMillis() - init;
			}
			System.out.println();
			System.out.println("running time : " +duration/1000+" seconds");
			System.out.println("Final table : ");
			System.out.println("score " + bestTable.getScore());
			String str = bestTable.getScore()+"\n"; 
			for(int i=1;i<bestTable.getSeats().length;i++)
			{
				System.out.println(bestTable.getSeats()[i]+" "+i);
				str += bestTable.getSeats()[i]+" "+i+"\n";
			}
//			FileWriter fw = new FileWriter(new File("hw1-soln1.txt"));
			FileWriter fw = new FileWriter(new File("hw1-soln2.txt"));
//			FileWriter fw = new FileWriter(new File("hw1-soln3.txt"));

			BufferedWriter out = new BufferedWriter(fw);
			out.write(str);
			out.close();
			in.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Generate a random Table
	 * @return The generated Table
	 */
	private static Table randomTable(long seed) {
		//generate a random table
		Random random = new Random(seed);
		int[] seats = new int[nbGuest+1];
		for(int i = 1;i<=nbGuest;i++)
		{
			int randomNumber = (int) Math.floor(Math.random()*nbGuest)+1;
			randomNumber = Math.abs(random.nextInt()%nbGuest);
			while(contains(seats,randomNumber))
				randomNumber = (int) Math.floor(Math.random()*nbGuest)+1;
			seats[i]=randomNumber;
			
		}
		return new Table(nbGuest, seats);
	}

	/**
	 * Fonction interne de decomposition d'une ligne de texte en ArrayList A
	 * chaque espace, un nouvel element est ajoute a l'ArrayList
	 *
	 * @param s
	 *            chaine de caracteres
	 * @return La liste des mots de la chaine de caracteres dans une ArrayList
	 * @author Stephane Bonnevay - Professor at Polytech Lyon, Universite Lyon 1, Lyon, France
	 */
	private static ArrayList<String> StringToArrayList(String s) {
		ArrayList<String> v = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(s);

		while (st.hasMoreTokens())
			v.add(st.nextToken());
		return v;
	}
	
	/**
	 * Preference function
	 * @param p1 Number of a person
	 * @param p2 Number of a person
	 * @return An integer which indicates the preference between p1 and p2 
	 */
	public static int h(int p1,int p2)
	{
		return matrix[p1][p2];
	}
	
	/**
	 * Recursive algorithm to find a "good" table.
	 * The algorithm starts with a table (currentTable) instantiate outside of this method.
	 * Here is the pseudocode :
	 * localSearch(currentTable,bestTable,path) :
	 * 		if we explore too many table
	 * 			return bestTable
	 * 		if currentTable is not visited yet then
	 * 			bestTableYet <- bestTable
	 * 			Add currentTable into path //path is the list of visited state
	 * 			ListOfNeighbors <- some neighbors of currentTable
	 * 			for each neighbor n in ListOfNeighbors
	 * 				add n into path
	 * 				if score(n)score(bestTableYet)
	 * 					bestTableYet = n
	 * 				bestTableYet = localSearch(n,bestTableYet,path)
	 * 		return bestTableYet
	 * 
	 * The search space is a graph of all the tables. Each table table is a node of the graph. 
	 * There's a vertex between two tables t1 and t2 if we can swap 2 people of t1 to obtain t2.
	 * The algorithm begins with a random table. Then we select some children (or neighbors or adjacent tables) of the initial table.
	 * This is the selection process :
	 * - pick a random neighbor out of the graph (or landscape)
	 * - if this neighbor has a better than score than his parent then select it
	 * - else we try to select this neighbor with a probability p to be selected
	 * The last step of the selection process allows the algorithm to explore the landscape. Thus we can reach a better solution by selecting bad tables sometimes.
	 * I add this step to avoid being stuck in a local maxima.
	 * After selecting a set of neighbor we recursively call the algorithm for each of them. Of course the best table discovered yet is recorded :
	 * for each neighbor n in ListOfNeighbors
	 * 				if score(n)score(bestTableYet)
	 * 					bestTableYet = n
	 * 				bestTableYet = localSearch(n,bestTableYet,path)
	 * 
	 * Finally we return the best table discovered during the recursive calls. The algorithm will stop the search if 
	 * 
	 * This algorithm is repeated several time during 60 seconds using a different starting random table each time.
	 * The best table of all the calls is returned as a good solution and output into a file.
	 * @param currentTable Table currently visited
	 * @param bestTable The best table discovered yet
	 * @param path The path from the starting table to currentTable. It is used as a stop-list of visited table.
	 * @return A "good" table. 
	 */
	public static Table localSearch(Table currentTable,Table bestTable, ArrayList<Table> path)
	{
		Table tmp = bestTable;
		Table returnTable = bestTable;
		if(path.size()>3000) //stop condition : maximum depth
		{
			return returnTable;
		}
		
		if(!path.contains(currentTable))//avoid loop on the graph of states
		{
			path.add(currentTable);
			ArrayList<Table> neighbors = neighbors(currentTable, 40, 0.2,path);
			for(Table t:neighbors)
			{
				if(t.getScore()>tmp.getScore())
				{
					tmp=t;
				}
				tmp=localSearch(t, tmp,path);
				if(!(tmp.getScore()>bestTable.getScore()))
				{
					returnTable=bestTable;
				}
				else
					returnTable=tmp;
			}
		}
		return returnTable;
	}
	
	/**
	 * Check if an integer is found inside an array.
	 * @param array The array where we're searching in. 
	 * @param integer The integer we're looking for.
	 * @return Return true if the integer was found else false.
	 */
	private static boolean contains(int[] array, int integer)
	{
		for(int i=0;i<array.length;i++)
		{
			if(array[i]==integer)
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Return an ArrayList of the neighbors of the currentTable. 
	 * That ArrayList is randomly filled with a some neighbors from the neighborhood of currentTable. 
	 * The number of selected neighbor is a parameter of this method.
	 * The neighbor selection process is :
	 * - pick out a random neighbor from the neighborhood
	 * - if that neighbor is better (has a better score) than currentTable add it to an array of selected neighbors
	 * - else (the neighbor has a worst score)  apply a probability p (random number >=p) to find if we keep it on the selected neighbors or not.
	 * return the array of selected neighbors
	 * @param currentTable the current Table
	 * @param number the size of the returned ArrayList
	 * @param p probability to add a "bad" neighbor
	 * @param path The path (ArrayList of Table) followed by the search algorithm to get to the current Table (currentTable).
	 * It avoid dealing with a table already "visited".
	 * @return An ArrayList of some neighbors of currentTable
	 */
	private static ArrayList<Table> neighbors(Table currentTable, int number, double p, ArrayList<Table> path)
	{
		ArrayList<Table> selectedNeighbors = new ArrayList<Table>();
		ArrayList<Table> allNeighbors = new ArrayList<Table>();
		Table tmp = new Table(currentTable);
		Table bestTable=currentTable;
		
		//finding all neighbors
		for(int i=1;i<=nbGuest;i++)
		{
			for(int j =i+1;j<=nbGuest;j++)
			{
				tmp.swap(i, j);
				if(!path.contains(tmp))
				{
					if(!allNeighbors.contains(tmp))
					{
						
						allNeighbors.add(tmp);
						//path.add(tmp);
					}
				}
				tmp = new Table(currentTable); //reinit tmp with t
			}
		}
		
		for(int i = 1;i<=number;i++) //selection of a set of neighbors
		{
			tmp = allNeighbors.get( (int) Math.floor(Math.random()*allNeighbors.size()) ); //select a random neighbor
			if(!selectedNeighbors.contains(tmp)) //avoid to deal with the same neighbor several times
			{
				if(tmp.getScore()>bestTable.getScore()) //if that neighbor has a better score
				{
					selectedNeighbors.add(tmp); //add it to the selectedNeighbors
				}
				else if(Math.random()>=p) //else apply a probability to add it or not
				{
					selectedNeighbors.add(tmp);
				}
			}
		}
		
		return selectedNeighbors;
	}
	
}
