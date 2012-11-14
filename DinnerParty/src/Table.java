
/**
 * Class which represent an instance of the table.
 * Some seats may not be occupied.
 * @author Princy Razafimanantsoa
 *
 */
public class Table {
	private static int MALE = -1;
	private static int FEMALE = -2;
	private int nbAddedPeople=0;
	private int[] seats; // index=seat number, value=person number
	private int nbGuest;
	private int swipeSeat2=-1;
	private int swipeSeat1=-1;
	private double score=0; //used for debugging purpose
	
	public int getNbAddedPeople() {
		return nbAddedPeople;
	}

	public void setNbAddedPeople(int nbAddedPeople) {
		this.nbAddedPeople = nbAddedPeople;
	}
	public int getSwipeSeat2() {
		return swipeSeat2;
	}

	public void setSwipeSeat2(int swipeSeat2) {
		this.swipeSeat2 = swipeSeat2;
	}

	public int getSwipeSeat1() {
		return swipeSeat1;
	}

	public void setSwipeSeat1(int swipeSeat1) {
		this.swipeSeat1 = swipeSeat1;
	}
	
	
	
	/**
	 * Constructor of an empty table.
	 * @param nbGuest Number of guest.
	 */
	public Table(int nbGuest) {
		seats = new int[nbGuest+1]; //contains {0, 0, 0, ...,0}
		this.getScore();
		this.nbGuest=nbGuest;
	}
	
	/**
	 * Constructor of an non-empty table.
	 * @param nbGuest Number of guest.
	 * @param seats Seats with their guests.
	 */
	public Table(int nbGuest, int[] seats) {
		this.seats = seats;
		this.nbGuest=nbGuest;
		this.getScore();
	}
	
	
	public Table(Table t) {
		this.seats = new int[t.seats.length];
		for(int i=0;i<t.seats.length;i++) //test the lenght of t.seats and this.seats!!!!
			this.seats[i] = t.seats[i];
		this.nbGuest=t.nbGuest;
		this.nbAddedPeople=t.nbAddedPeople;
		this.swipeSeat1=t.swipeSeat1;
		this.swipeSeat2=t.swipeSeat2;
		this.score=t.score;
	}
	
	/**
	 * Return the score of the current table.
	 * @return The score of the table.
	 */
	public double getScore()
	{
		int j=2;
		double score = 0;
		//1 point for every adjacent pair (seated next to each other) of people with one female and the other male
		//top half of the table
		for(int i=1;i<=(nbGuest/2) && j<=(nbGuest/2);i++)
		{
			if(gender(seats[i])!=gender(seats[j]))
			{
				score++;
			}
			else //For every adjacent or opposite pair of people p1, p2, h(p1, p2) + h(p2, p1) points.
			{
				score = score + Main.h(seats[i], seats[j])+Main.h(seats[j], seats[i]);
			}
			j++;
		}
		
		//1 point for every adjacent pair (seated next to each other) of people with one female and the other male
		//bottom half of the table
		j=(nbGuest/2)+2;
		for(int i=(nbGuest/2)+1;i<=nbGuest && j<=nbGuest;i++)
		{
			if(gender(seats[i])!=gender(seats[j]))
			{
				score++;
			}
			else //For every adjacent or opposite pair of people p1, p2, h(p1, p2) + h(p2, p1) points.
			{
				score = score + Main.h(seats[i], seats[j])+Main.h(seats[j], seats[i]);
			}
			j++;
		}
		
		//2 points for every opposite pair (seated across from each other) of people with one female and the other male.
		j=(nbGuest/2)+1;
		for(int i=1;i<=(nbGuest/2) && j<=nbGuest;i++)
		{
			if(gender(seats[i])!=gender(seats[j]))
			{
				score=score+2;
			}
			else //For every adjacent or opposite pair of people p1, p2, h(p1, p2) + h(p2, p1) points.
			{
				score = score + Main.h(seats[i], seats[j])+Main.h(seats[j], seats[i]);
			}
			j++;
		}
		this.score = score; //used for debugging purpose
		return score;
	}
	/**
	 * Return the gender of a person
	 * @param person
	 * @return Either MALE or FEMALE
	 */
	private int gender(int person)
	{
		if (person>=1 && person<=(nbGuest/2))
		{
			return FEMALE;
		}
		return MALE;
	}
	/**
	 * Add the person personNumber on the seat seatNumber.
	 * @param personNumber The person number
	 * @param seatNumber The seat number
	 */
	public void addPersonOnSeat(int personNumber,int seatNumber)
	{
		if(seats[seatNumber] != 0)
		{
			seats[seatNumber]=personNumber;
			nbAddedPeople++;
		}
		
	}
	
	/**
	 * Test if the table is full.
	 * @return Return true when the table is full and false otherwise.
	 */
	public boolean isFull()
	{
		return (nbAddedPeople==nbGuest);
	}
	
	/**
	 * Test if the seat seatNumber is occupied by a guest.
	 * @param seatNumber The tested seat
	 * @return Return true when the seat seatNumber is occupied and false otherwise.
	 */
	public boolean occupiedSeat(int seatNumber)
	{
		return (seats[seatNumber]!=0);
	}
	
	/**
	 * Swap two people respectively on seatNumber1 and seatNumber2
	 * @param seatNumber1 seat number of one of the people
	 * @param seatNumber2 seat number of the other person
	 */
	public void swap(int seatNumber1, int seatNumber2)
	{
		if(seatNumber1!=seatNumber2)
		{
			int temp;
			swipeSeat1=seatNumber1;
			swipeSeat2=seatNumber2;
			temp = seats[seatNumber1];
			seats[seatNumber1]=seats[seatNumber2];
			seats[seatNumber2]=temp;
		}
	}
	/**
	 * Implementation of the equals methods
	 * @param t Table to be compared we the current one.
	 * @return true they are equals else false
	 */
	public boolean equals(Table t)
	{
		if(this.nbGuest==t.nbGuest && this.score==t.score && this.seats.equals(t.seats))
			return true;
		return false;
	}
	/**
	 * Return the seats of this table
	 * @return the seats of this table
	 */
	public int[] getSeats()
	{
		return this.seats;
	}
}
