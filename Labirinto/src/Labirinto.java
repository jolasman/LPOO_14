import java.util.Scanner;
import java.lang.String;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;

public class Labirinto {
	static int[][] labyrinth;
	static final int WALL = 1 << 0; //"X"
	static final int BORDER = 1 << 1; //"X"
	static final int HERO = 1 << 2; //"H"
	static final int ARMED = 1 << 3; //"A"
	static final int DRAGON = 1 << 4; //"D"
	static final int WEAPON = 1 << 5; //"E"
	static final int SLEEPING = 1 << 6; //"d" NOT DONE
	static final int EAGLE = 1 << 7; //"a" NOT DONE
	static final int EXIT = 1 << 8; //"S" NOT DONE
	static final int VISITED = 1 << 9;

	static char lab[][] = {
		{'X','X','X','X','X','X','X','X','X','X'},
		{'X','H',' ',' ',' ',' ',' ',' ',' ','X'},
		{'X',' ','X','X',' ','X',' ','X',' ','X'},
		{'X','D','X','X',' ','X',' ','X',' ','X'},
		{'X',' ','X','X',' ','X',' ','X',' ','X'},
		{'X',' ',' ',' ',' ',' ',' ','X',' ','S'},
		{'X',' ','X','X',' ','X',' ','X',' ','X'},
		{'X',' ','X','X',' ','X',' ','X',' ','X'},
		{'X','E','X','X',' ',' ',' ',' ',' ','X'},
		{'X','X','X','X','X','X','X','X','X','X'}};

	public static void main(String[] args) throws InterruptedException {
		//TODO test code
		System.out.println("Input size:");
		Scanner kb = new Scanner(System.in);
		int size = kb.nextInt();
		genDepthFirst(size);
		ArrayList<MazeObject> objects = genMazeObjects(1);
		gamecycle:
			do {
				displayMaze();
				System.out.println("\n Enter A -> Left; D -> Right; U -> Up; S -> Down; Q -> Quit");
				Scanner keyboard = new Scanner(System.in);
				char command = keyboard.next().charAt(0);
				if (Character.isUpperCase(command)) {
					command = Character.toLowerCase(command);
				}
				int posx, posy;
				boolean win, lose;
				String status;
				switch (command) {
				case 'a':
					posx = objects.get(0).getX();
					posy = objects.get(0).getY();
					status = checkMovement(posx, posy-1);
					if (status == "WALL") {
						System.out.println("You hit a wall!");
						Thread.sleep(500);
					} else if (status == "WEAPON") {
						labyrinth[posx][posy] = 0;
						labyrinth[posx][posy-1] = 0;
						labyrinth[posx][posy-1] |= ARMED;
						objects.get(0).setY(posy-1);
						((Hero) objects.get(0)).setArmed(true);
					} else if (status == "EXIT") {
						if(((Hero) objects.get(0)).getArmed()) {
							win = true;
						} else System.out.println("You don't have the weapon!");
					} else if (status == "DRAGON") {
						if (((Hero) objects.get(0)).getArmed()) {
							ArrayList<MazeObject> adjacent = findAdjDragons(posx, posy-1);
							for (int i = 0; i < adjacent.size(); i++) {
								labyrinth[adjacent.get(i).getX()][adjacent.get(i).getY()] = 0;
							}
							for (int i = 0; i < adjacent.size(); i++) {
								boolean finished = false;
								for (int j = 2; j < objects.size() && !finished; j++) {
									if (adjacent.get(i) == objects.get(j)) {
										objects.remove(j);
										finished = true;
									}
								}
							}
						} else {
							lose = true;
						}
					} else if (status == "CLEAR") {
						labyrinth[posx][posy] = 0;
						labyrinth[posx][posy-1] |= HERO;
						objects.get(0).setY(posy-1);
					}
					break;
				case 'd':
					posx = objects.get(0).getX();
					posy = objects.get(0).getY();
					status = checkMovement(posx, posy+1);
					if (status == "WALL") {
						System.out.println("You hit a wall!");
						Thread.sleep(500);
					} else if (status == "WEAPON") {
						labyrinth[posx][posy] = 0;
						labyrinth[posx][posy+1] = 0;
						labyrinth[posx][posy+1] |= ARMED;
						objects.get(0).setY(posy+1);
						((Hero) objects.get(0)).setArmed(true);
					} else if (status == "EXIT") {
						if(((Hero) objects.get(0)).getArmed()) {
							win = true;
						} else System.out.println("You don't have the weapon!");
					} else if (status == "DRAGON") {
						if (((Hero) objects.get(0)).getArmed()) {
							ArrayList<MazeObject> adjacent = findAdjDragons(posx, posy+1);
							for (int i = 0; i < adjacent.size(); i++) {
								labyrinth[adjacent.get(i).getX()][adjacent.get(i).getY()] = 0;
							}
							for (int i = 0; i < adjacent.size(); i++) {
								boolean finished = false;
								for (int j = 2; j < objects.size() && !finished; j++) {
									if (adjacent.get(i) == objects.get(j)) {
										objects.remove(j);
										finished = true;
									}
								}
							}
						} else {
							lose = true;
						}
					} else if (status == "CLEAR") {
						labyrinth[posx][posy] = 0;
						labyrinth[posx][posy+1] |= HERO;
						objects.get(0).setY(posy+1);
					}
					break;
				case 'w':
					posx = objects.get(0).getX();
					posy = objects.get(0).getY();
					status = checkMovement(posx-1, posy);
					if (status == "WALL") {
						System.out.println("You hit a wall!");
						Thread.sleep(500);
					} else if (status == "WEAPON") {
						labyrinth[posx][posy] = 0;
						labyrinth[posx-1][posy] = 0;
						labyrinth[posx-1][posy] |= ARMED;
						objects.get(0).setX(posx-1);
						((Hero) objects.get(0)).setArmed(true);
					} else if (status == "EXIT") {
						if(((Hero) objects.get(0)).getArmed()) {
							win = true;
						} else System.out.println("You don't have the weapon!");
					} else if (status == "DRAGON") {
						if (((Hero) objects.get(0)).getArmed()) {
							ArrayList<MazeObject> adjacent = findAdjDragons(posx-1, posy);
							for (int i = 0; i < adjacent.size(); i++) {
								labyrinth[adjacent.get(i).getX()][adjacent.get(i).getY()] = 0;
							}
							for (int i = 0; i < adjacent.size(); i++) {
								boolean finished = false;
								for (int j = 2; j < objects.size() && !finished; j++) {
									if (adjacent.get(i) == objects.get(j)) {
										objects.remove(j);
										finished = true;
									}
								}
							}
						} else {
							lose = true;
						}
					} else if (status == "CLEAR") {
						labyrinth[posx][posy] = 0;
						labyrinth[posx-1][posy] |= HERO;
						objects.get(0).setX(posx-1);
					}
					break;
				case 's':
					posx = objects.get(0).getX();
					posy = objects.get(0).getY();
					status = checkMovement(posx+1, posy);
					if (status == "WALL") {
						System.out.println("You hit a wall!");
						Thread.sleep(500);
					} else if (status == "WEAPON") {
						labyrinth[posx][posy] = 0;
						labyrinth[posx+1][posy] = 0;
						labyrinth[posx+1][posy] |= ARMED;
						objects.get(0).setX(posx+1);
						((Hero) objects.get(0)).setArmed(true);
					} else if (status == "EXIT") {
						if(((Hero) objects.get(0)).getArmed()) {
							win = true;
						} else System.out.println("You don't have the weapon!");
					} else if (status == "DRAGON") {
						if (((Hero) objects.get(0)).getArmed()) {
							ArrayList<MazeObject> adjacent = findAdjDragons(posx+1, posy);
							for (int i = 0; i < adjacent.size(); i++) {
								labyrinth[adjacent.get(i).getX()][adjacent.get(i).getY()] = 0;
							}
							for (int i = 0; i < adjacent.size(); i++) {
								boolean finished = false;
								for (int j = 2; j < objects.size() && !finished; j++) {
									if (adjacent.get(i) == objects.get(j)) {
										objects.remove(j);
										finished = true;
									}
								}
							}
						} else {
							lose = true;
						}
					} else if (status == "CLEAR") {
						labyrinth[posx][posy] = 0;
						labyrinth[posx+1][posy] |= HERO;
						objects.get(0).setX(posx+1);
					}
					break;
				case 'q':
					break gamecycle;
				default:
					System.out.println("Not a valid input!");
					break;
				}
			} while (true);
	}

	//Checks if there is an object in the position x, y by calling all the other check functions
	//Used to know what is in the spot the player is trying to move to
	public static String checkMovement(int x, int y) {
		if(checkWall(x, y)) {
			return "WALL";
		} else if (checkWeapon(x, y)) {
			return "WEAPON";
		} else if (checkExit(x, y)) {
			return "EXIT";
		} else if (checkDragon(x, y)) {
			return "DRAGON";
		} else return "CLEAR";
	}

	//Checks for a wall at position x, y
	public static boolean checkWall(int x, int y) {
		if (((labyrinth[x][y] & WALL) | (labyrinth[x][y] & BORDER)) != 0) {
			return true; //found wall
		} else return false; //didn't find wall
	}

	//Checks for a weapon at position x, y
	public static boolean checkWeapon(int x, int y) {
		if ((labyrinth[x][y] & WEAPON) != 0) {
			return true; //found weapon
		} else return false; //didn't find weapon
	}

	//Checks for a weapon at position x, y
	public static boolean checkExit(int x, int y) {
		if ((labyrinth[x][y] & EXIT) != 0) {
			return true; //found exit
		} else return false; //didn't find exit
	}

	//Checks for a dragon at position x, y
	public static boolean checkDragon(int x, int y) {
		if ((labyrinth[x][y-1] & DRAGON) != 0 && (labyrinth[x][y+1] & DRAGON) != 0 && (labyrinth[x-1][y] & DRAGON) != 0 && (labyrinth[x+1][y]) != 0) {
			return true;
		} else return false;
	}

	//Finds all the adjacent dragons to position x, y
	public static ArrayList<MazeObject> findAdjDragons(int x, int y) {
		ArrayList<MazeObject> adjacent = new ArrayList<MazeObject>();
		if ((labyrinth[x][y-1] & DRAGON) != 0) {
			adjacent.add(new Dragon(x, y-1));
		}
		if ((labyrinth[x][y+1] & DRAGON) != 0) {
			adjacent.add(new Dragon(x, y+1));
		}
		if ((labyrinth[x-1][y+1] & DRAGON) != 0) {
			adjacent.add(new Dragon(x-1, y));
		}
		if ((labyrinth[x+1][y] & DRAGON) != 0) {
			adjacent.add(new Dragon(x+1, y));
		}
		return adjacent;
	}

	//Handles winning/losing the game
	public static void gameStatus(String status) {
		if (status == "lose") {
			System.out.println("You just lost the game!");
		} else if (status == "win") {
			System.out.println("A winner is you!");
		}
		System.exit(0);
	}

	//Generates maze with Depth-first search method
	//Visited 9 Exit 8 Eagle(A) 7 E+D(F) 6 Weapon(E) 5 Dragon 4 Armed 3 Hero 2 Border 1 Wall 0
	public static void genDepthFirst(int size) {
		labyrinth = new int[size][size];
		int visitable = 0;
		int coordX = 1; //initial cell x coord
		int coordY = 1; //initial cell y coord
		Random random = new Random();

		//Generates walls initial state
		for (int i = 1; i < size-1; i++) {
			for (int j = 1; j < size-1; j++) {
				if (i % 2 == 0) { //Checks for even number
					labyrinth[i][j] = labyrinth[i][j] | (WALL);
				} else if (j % 2 == 0) {
					labyrinth[i][j] = labyrinth[i][j] | (WALL);
				} else visitable++; //Counts how many empty cells there are
			}
		}

		//Generates border
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if ((i == 0) || (i == size-1)) {
					labyrinth[i][j] = labyrinth[i][j] | (BORDER);
				} else if ((j == 0) || (j == size-1)) {
					labyrinth[i][j] = labyrinth[i][j] | (BORDER);
				}
			}
		}

		//Choose a random initial cell
		boolean finished = false;
		for (int i = 1; i < size-1 && !finished; i++) {
			for (int j = 1; j < size-1; j++) {
				if (labyrinth[i][j] == 0) {
					if (random.nextDouble() < 0.15) {
						finished = true;
						coordX = i;
						coordY = j;
						break;
					}
				}
			}
		}

		//Recursive Backtracking with Depth-First Search
		Stack<SimpleEntry<Integer, Integer>> coords = new Stack<SimpleEntry<Integer, Integer>>(); //Stores visited cells
		ArrayList<SimpleEntry<Integer, Integer>> canVisit = new ArrayList<SimpleEntry<Integer, Integer>>(); //Stores possible moves from current cell
		SimpleEntry<Integer, Integer> currCell = new SimpleEntry<Integer, Integer>(coordX, coordY); //Stores coordinates of current cell as an AbstractMap
		coords.push(currCell);
		int visited = 1;
		while (visited < visitable) {
			canVisit.clear();
			coordX = currCell.getKey();
			coordY = currCell.getValue();
			labyrinth[coordX][coordY] = labyrinth[coordX][coordY] | VISITED; //Set current cell as visited
			//Checks if there's a cell to visit up
			if (coordX - 2 > 0) {
				if ((labyrinth[coordX-2][coordY] & VISITED) == 0) {
					canVisit.add(new SimpleEntry<Integer, Integer>((coordX-2), coordY));
				}
			}
			//Checks if there's a cell to visit down
			if (coordX + 2 < size) {
				if ((labyrinth[coordX+2][coordY] & VISITED) == 0) {
					canVisit.add(new SimpleEntry<Integer, Integer>((coordX+2), coordY));
				}
			}
			//Checks if there's a cell to visit left
			if (coordY - 2 > 0) {
				if ((labyrinth[coordX][coordY-2] & VISITED) == 0) {
					canVisit.add(new SimpleEntry<Integer, Integer>((coordX), coordY-2));
				}
			}
			//Checks if there's a cell to visit right
			if (coordY + 2 < size) {
				if ((labyrinth[coordX][coordY+2] & VISITED) == 0) {
					canVisit.add(new SimpleEntry<Integer, Integer>((coordX), coordY+2));
				}
			}
			//Cycle that opens up walls
			if (!(canVisit.isEmpty())) {
				int rnd = (int)(Math.random() * canVisit.size());
				int diffX = canVisit.get(rnd).getKey() - coordX;
				int diffY = canVisit.get(rnd).getValue() - coordY;
				if (!(diffX == 0)) {
					if (diffX > 0) {
						labyrinth[canVisit.get(rnd).getKey()-1][canVisit.get(rnd).getValue()] ^= WALL;
					}
					if (diffX < 0) {
						labyrinth[canVisit.get(rnd).getKey()+1][canVisit.get(rnd).getValue()] ^= WALL;
					}
				}
				if (!(diffY == 0)) {
					if (diffY > 0) {
						labyrinth[canVisit.get(rnd).getKey()][canVisit.get(rnd).getValue()-1] ^= WALL;
					}
					if (diffY < 0) {
						labyrinth[canVisit.get(rnd).getKey()][canVisit.get(rnd).getValue()+1] ^= WALL;
					}
				}
				currCell = canVisit.get(rnd); //Sets current cell to the newly visited cell
				coords.push(currCell);
				visited++;
			} else {
				coords.pop();
				currCell = coords.peek();
			}
		}
	}

	//Places maze objects randomly
	public static ArrayList<MazeObject> genMazeObjects(int numDragons) {
		Random random = new Random();
		ArrayList<MazeObject> objects = new ArrayList<MazeObject>();
		boolean finished = false;
		boolean hero = false;
		boolean dragon = false;
		boolean weapon = false;
		while (!finished) {
			for (int i = 1; i < labyrinth.length-1; i++) {
				for (int j = 1; j < labyrinth.length-1; j++) {
					if ((labyrinth[i][j] & (WALL | HERO | WEAPON | DRAGON)) == 0) {
						if (random.nextDouble() < 0.0005) { //0.05% chance to place an object
							if (!hero) {
								hero = true;
								objects.add(new Hero(i, j));
								labyrinth[i][j] |= HERO;
							} else if (!weapon) {
								weapon = true;
								objects.add(new Sword(i, j));
								labyrinth[i][j] |= WEAPON;
							} else if (!dragon) {
								if (numDragons == 0) {
									dragon = true;
								} else {
									objects.add(new Dragon(i, j));
									labyrinth[i][j] |= DRAGON;
									numDragons--;
								}
							} else finished = true;
						}
					}
				}
			}
		}
		return objects;
	}

	//Displays labyrinth
	//TODO add argument to pass any labyrinth to display
	public static void displayMaze() {
		for(int i = 0; i < labyrinth.length; i++) {
			for (int j = 0; j < labyrinth[i].length; j++) {
				if (((labyrinth[i][j] & WALL) | (labyrinth[i][j] & BORDER)) != 0) {
					System.out.print("X");
				} else if ((labyrinth[i][j] & DRAGON) != 0) {
					System.out.print("D");
				} else if ((labyrinth[i][j] & WEAPON) != 0) {
					System.out.print("E");
				} else if ((labyrinth[i][j] & HERO) != 0) {
					System.out.print("H");
				} else if ((labyrinth[i][j] & ARMED) != 0) {
					System.out.print("A");
				} else 	System.out.print(" ");
			}
			System.out.println();
		}

	}
}