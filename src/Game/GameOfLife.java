package Game;

/*Joseph Pacitto
 * ID: 01328408
 * CIS 181 - Project 2
 * Date: April 10, 2016
 */

public class GameOfLife implements GameInterface {
    private boolean[][] originalMap,    // The initial cell configuration
                                map,    // The current cell configuration
                             newMap;    // The next generation configuration
    private GameGUI gui;
    private int generation = 0;
    private FileIO fileIO;

    // GameOfLife constructor
    public GameOfLife() {
        originalMap = new boolean[MAX_ROWS][MAX_COLS];
        map = new boolean[MAX_ROWS][MAX_COLS];
        newMap = new boolean[MAX_ROWS][MAX_COLS];
        gui = new GameGUI(this, map);
        gui.setTitle("CIS 181 Array Based Game Of Life");
        fileIO = new FileIO("life", "Game of Life Text Files");
        readExample(2);
    }

    //  ====>>>>> Complete the methods below this line! <<<<<====

    private void copyMap(boolean sourceMap[][]) {
        for(int i = 0; i < MAX_COLS; i++){		//nested for loop that goes through every square setting map equal to sourceMap
        	for(int j = 0; j < MAX_ROWS; j++){
        		map[j][i] = sourceMap[j][i];
        	}
        }
    }

    private void clearMap(boolean targetMap[][]) {
    	for(int i = 0; i < MAX_COLS; i++){
        	for(int j = 0; j < MAX_ROWS; j++){		//nested for loop that sets all of targetMap to DEAD
        		targetMap[j][i] = DEAD;
        	}
        }
    }
    //getFlatNeighbor was implemented for both the Flat grid and the torus grid.
    //The getFlatNeighborcount is used to cound the majority of the cell neighbors for the torus grid
    //The remaining cells that are used for the torus effect is calculated by getTorusNeighborCount.
    //getFlatNeghborCount is called inside of getTorusNeighborCount.
    private int getFlatNeighborCount(int row, int col){		//neighbor count for Flat grid
        int count = 0;
        /*counts the number of neighbors for each square.
         * checks for corners and edge pieces.
         */
        if(col != 49)
        	if(map[row][col+1]){count++;}
        if(col != 0)
        	if(map[row][col-1]){count++;}
        if(row != 49)
			if(map[row+1][col]){count++;}
        if(row != 0)
        	if(map[row-1][col]){count++;}
        if(row != 0 && col != 0)
			if(map[row-1][col-1]){count++;}
        if(row != 49 && col != 49)
			if(map[row+1][col+1]){count++;}
        if(row != 0 && col != 49)
			if(map[row-1][col+1]){count++;}
        if(row != 49 && col != 0)
			if(map[row+1][col-1]){count++;}

        return count;
    }
    
    public  void nextGenerationForFlatGrid()	//sets up next generation of the grid
    {
    	for(int i = 0; i < MAX_COLS; i++){		//nested for loop that checks the neighbor count of each square
        	for(int j = 0; j < MAX_ROWS; j++){
        		int neighbors = getFlatNeighborCount(j,i); //calls on getFlatNeighborCount to get number of neighbors for specific cell
        		if(map[j][i]){
        			if(neighbors == 3 || neighbors == 2)	//controls the alive state of alive cells
        				newMap[j][i] = ALIVE;
        			else
        				newMap[j][i] = DEAD;		//sets alive cells to dead if not the correct amount of neighbors
				}else if(!map[j][i]){
        			if(neighbors == 3)			//controls the state of dead cells
        				newMap[j][i] = ALIVE;
        			else
        				newMap[j][i] = DEAD;
        		}
        	}
		}
    	
    	for(int i = 0; i < MAX_COLS; i++){		//changes the current map to new generation
    		for(int j = 0; j < MAX_ROWS; j++){
    			map[j][i] = newMap[j][i];
    		}
    	}
    	
    	generation++;	//advances generation by one
    }
    //getTorusNeighborCount:
    /*Precondtions:  0 <= row < MAX_ROWS and 0 <= col < MAX_COLS.
     * PostCondition: A count of all LIVE neighbors of the cell at [row,col] is returned.
     * 				  Cells at the edges (not including corners) count the ones they neighbor
     * 				  including the cells that are all the way on the other side of the grid.
     * 				  For CORNER cells, these cells count the ones they boarder, including the 
     * 				  cells all the way on the other side of the grid. For corners, the other side
     * 				  means on the bottom/top, left/right, and diagonal.
     */
    private int getTorusNeighborCount(int row, int col){	//Counts the number of neighbors for the torus map
    	int count = 0;
        count = getFlatNeighborCount(row,col);
        //getFlatNeighborcount does not count all of the neighbors for the corners or edges
        //This getTorus accounts for the remaining neighboring cells that getFlat does not take care of
        int limit = 49;
        
        //The if-statement below, containing a for-loop, is used to get the neighbor count for the edge cells.
        //Not including corner pieces.
        if(((row == 0 || row == 49) && (col > 0 && col < 49)) || ((col == 0 || col == 49) && (row > 0 && row < 49))){
        		for(int i = -1; i <= 1; i++){
        			if((col==49) && row >0 && row<49){
        				if(map[row+i][col-limit]){count++;}
        			}else if(col == 0 && row > 0 && row < 49){	
        				if(map[row+i][col+limit]){count++;}
        			}else if(row == 49 && col > 0 && col < 49){
        				if(map[row-limit][col+i]){count++;}
        			}else if(row == 0 && col > 0 && col < 49){
        				if(map[row+limit][col+i]){count++;}}}}

        	//Corner pieces 
          if(row == 0 && col == 0){				//top-left corner cell
 			if(map[row+49][col+49]){count++;}
         	if(map[row+1][col+49]){count++;}
         	if(map[row][col+49]){count++;}
         	if(map[row+49][col]){count++;}
         	if(map[row+49][col+1]){count++;}
         }else if(row == 49 && col == 49){				//bottom-right corner cell
 			if(map[row-49][col-49]){count++;}
         	if(map[row][col-49]){count++;}
         	if(map[row-1][col-49]){count++;}
         	if(map[row-49][col]){count++;}
         	if(map[row-49][col-1]){count++;}
         }else if(row == 0 && col == 49){				//top-right corner cell
  			if(map[row+49][col-49]){count++;}
         	if(map[row][col-49]){count++;}
         	if(map[row+1][col-49]){count++;}
         	if(map[row+49][col]){count++;}
         	if(map[row+49][col-1]){count++;}
         }else if(row == 49 && col == 0){				//bottom-left corner cell
 			if(map[row-49][col+49]){count++;}
 			if(map[row-1][col+49]){count++;}
         	if(map[row][col+49]){count++;}
         	if(map[row-49][col]){count++;}
         	if(map[row-49][col+1]){count++;}
         }
         return count;
        }
    
    
    // ==> 5. Implement the game of life for torus grid.
    public  void nextGenerationForTorusGrid() {
    	for(int i = 0; i < MAX_COLS; i++){				//nested for loop that checks each cells neighbor count
        	for(int j = 0; j < MAX_ROWS; j++){
        		int neighbors = getTorusNeighborCount(j,i);		//calls on getTorusNeighborCount to get number of neighbors for specific cell
        		if(map[j][i]){	
        			if(neighbors == 3 || neighbors == 2)		//controls the next state of alive cells
        				newMap[j][i] = ALIVE;				//stays alive if 2 or 3 neighbors
        			else
        				newMap[j][i] = DEAD;			//else the cell dies
				}else if(!map[j][i]){
        			if(neighbors == 3)
        				newMap[j][i] = ALIVE;			//dead cell becomes alive if it has exactly 3 neighbors
        			else
        				newMap[j][i] = DEAD;		//cells stays dead if it does not have 3 neighbors
        		}
        	}
		}
    	
    	for(int i = 0; i < MAX_COLS; i++){
    		for(int j = 0; j < MAX_ROWS; j++){
    			map[j][i] = newMap[j][i];			//nested for loop that turns the display map into the new generation
    		}
    	}
    	
    	generation++;			//increases generation count by one
    }

    //  ====>>>>> Don't touch the code below this line! <<<<<====

    // Return the next generation
    public int getGeneration() {
        return generation;
    }

    // Reset the map to the original map
    public void reset() {
        copyMap(originalMap);
        generation = 0;
        gui.repaint();
    }

    // Game of life examples 1-4: Fish, Plus, Glider, FlyingMachine
    public void readExample(int n) {
        System.out.println("Initializing with example " + n + " ...");
        clearMap(originalMap);

        switch (n) {
           case 1: // Example 1: Fish
             for (int col = 23; col <= 26; col++)
                 originalMap[13][col] = ALIVE;
             originalMap[14][22] = ALIVE;
             originalMap[14][26] = ALIVE;
             originalMap[15][26] = ALIVE;
             originalMap[16][22] = ALIVE;
             originalMap[16][25] = ALIVE;
             break;
           case 2: // Example 2: Plus
               for (int col = 6; col < 43; col++)
                 originalMap[24][col] = ALIVE;
             for (int row = 6; row < 43; row++)
                 originalMap[row][24] = ALIVE;
             break;
           case 3: // Example 3: Glider
                originalMap[14][23] = ALIVE;
             originalMap[15][24] = ALIVE;
             for (int row = 13; row <= 15; row++)
                 originalMap[row][25] = ALIVE;
             break;
           case 4: // Example 4: FlyingMachine
                for (int col = 22; col <= 25; col++) {
                 originalMap[11][col] = ALIVE;
                 originalMap[19][col] = ALIVE;
             }
             for (int row = 14; row <= 16; row++)
                 for (int col = 17; col <= 18; col++)
                     originalMap[row][col] = ALIVE;
             originalMap[15][19] = ALIVE;
             for (int row = 12; row <= 18; row = row+2)
                 originalMap[row][21] = ALIVE;
             originalMap[14][24] = ALIVE;
             originalMap[16][24] = ALIVE;
             originalMap[12][25] = ALIVE;
             originalMap[13][25] = ALIVE;
             originalMap[17][25] = ALIVE;
             originalMap[18][25] = ALIVE;
             break;
           default: // Default Example: ClearSpace
             break;
        }

        copyMap(originalMap);
        generation = 0;
        gui.repaint();
    }

    //  Read map from file
    public void readInMap() {
        clearMap(originalMap);
        if (fileIO.read(originalMap)) {
            copyMap(originalMap);
            generation = 0;
        } else
            readExample(2);
        gui.repaint();
    }

    // Write map to file
    public void writeMap() {
       fileIO.write(map);
    }

    // Change the state of a cell
    public void updateMap(int row, int col) {
        map[row][col] = !map[row][col];

    }

    // Destroy the GUI window
    public void destroy() {
        gui.dispose();
    }

    // The main method of GameOfLife
    public static void  main(String[] args) {
        GameOfLife game = new GameOfLife();
    }
}
