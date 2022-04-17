import java.util.Random;
import java.util.Scanner;

public class mineTerminator {
    public static final int NOT_A_NUMBER = 27; // number that is impossible

    public static final String EMPTY = ".";
    public static final String MINE = "*";
    public static final String FLAG = "f";

    public static void main(String args[])
    {
        boolean quit = false;
        Scanner input = new Scanner(System.in);

        System.out.println("\nWelcome to Terminator - a terminal minesweeper!\n");

        while (!quit)
        {
            int size = 1 + requestIntroInput(input, "How large of a feild? Sizes range from 2 to 26 -> ", "Error - Please enter a number between 2 and 26, or type the letter 'q' to quit", 2, 26);
            if (size == 1 + NOT_A_NUMBER)
            {
                quit = true;
                break;
            }
            int difficulcy = requestIntroInput(input, "How many mines would you like? -> ", "Error - Please enter a reasonable positive number", 1, size*size);
            if (difficulcy == NOT_A_NUMBER)
            {
                quit = true;
                break;
            }

            String[][] answerFeild = newGame(size, difficulcy);
            String[][] cleanFeild = generateFeild(size);
            printFeild(cleanFeild);
            int cellsLeft = (size-1)*(size-1)-difficulcy;
            boolean game = true;

            while (game)
            {
                int[] coordinates = requestCoordinates(input, "", "", size);

                if (coordinates[2] == NOT_A_NUMBER)
                {
                    if (cleanFeild[coordinates[0]][coordinates[1]].equals(FLAG))
                    {
                    cleanFeild[coordinates[0]][coordinates[1]] = EMPTY;
                    }
                    else
                    {
                    cleanFeild[coordinates[0]][coordinates[1]] = FLAG;
                    }
                    printFeild(cleanFeild);
                }
                else
                {
                    if (coordinates[0] == NOT_A_NUMBER && coordinates[1] == NOT_A_NUMBER)
                    {
                        quit = true;
                        game = false;
                        break;
                    }
                    String chosenCell= answerFeild[coordinates[0]][coordinates[1]];
    
                    if ((cleanFeild[coordinates[0]][coordinates[1]].equals(EMPTY) || cleanFeild[coordinates[0]][coordinates[1]].equals(FLAG)) && !answerFeild[coordinates[0]][coordinates[1]].equals(MINE) && game)
                    {
                        
                        cellsLeft -= 1;
                        cleanFeild[coordinates[0]][coordinates[1]] = chosenCell;
                        printFeild(cleanFeild);
    
                        if (cellsLeft <= 0)
                        {
                            System.out.println("\n\nComplete!\n");
                            game = false;
                        }
                    }
                    else if (!answerFeild[coordinates[0]][coordinates[1]].equals(MINE) && game)
                    {
                        System.out.println("   Cell already unlocked\n");
                        printFeild(cleanFeild);
                    }
                    
                    cleanFeild[coordinates[0]][coordinates[1]] = chosenCell;
    
                    if (chosenCell.equals(MINE) && game)
                    {
                        printFeild(answerFeild);
                        System.out.println("** ** ** **\n");
                        game = false;
                    }
                }
            }
        }
        input.close();
    }

    public static String[][] generateFeild(int size)
    {
        String [][] feild = new String[size][size];
        
        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++)
            {
                if (x == 0 && y!= 0)
                {
                    feild[y][x] = (y<10? " ": "") + y;
                }
                else if (y ==0)
                {
                    feild[0][x] = String.valueOf((char) (x+0x60));
                }
                else
                {
                    feild[y][x] = EMPTY;
                }
            }
        }
        feild[0][0] = "  ";
        
        return feild;
    }

    public static String[][] newMines(int size, int difficulcy)
    {
        String[][] feild = generateFeild(size);
                
        for (int x = 1; x < feild.length; x++)
        {
            for (int y = 1; y < feild[x].length && difficulcy > 0; y++)
            {
                if (difficulcy > 0)
                {
                    feild[x][y] = MINE;
                    difficulcy --;
                }
                else
                {
                    feild[x][y] = "" + 0;
                }
            }
        }
        
        Random generator = new Random();
        for (int x = 1; x < feild.length; x++)
        {
            for (int y = 1; y < feild[x].length; y++)
            {
                int newX = generator.nextInt(1, feild.length);
                int newY = generator.nextInt(1, feild.length);

                String tempOld = feild[x][y];
                feild[x][y] = feild[newX][newY];
                feild[newX][newY] = tempOld;
            }
        }

        return feild;
    }

    public static String[][] newGame(int size, int difficulcy)
    {
        String[][] feild = newMines(size, difficulcy);
        
        for (int x = 1; x < feild.length; x++)
        {
            for (int y = 1; y < feild[x].length && difficulcy > 0; y++)
            {
                if (x != 0 && y != 0 && feild[x][y] != MINE)
                {
                    int minesNearby = 0;
                    int tempX = 0;
                    int tempY = 0;
                    
                    tempX = x + 1;
                    tempY = y + 1;
                    if (tempX < size && tempY < size && feild[tempX][tempY].equals(MINE))
                    {
                        minesNearby ++;
                    }
                    
                    tempX = x + 1;
                    tempY = y;
                    if (tempX < size && feild[tempX][tempY].equals(MINE))
                    {
                        minesNearby ++;
                    }
                    
                    tempX = x + 1;
                    tempY = y - 1;
                    if (tempX < size && tempY > 0 && feild[tempX][tempY].equals(MINE))
                    {
                        minesNearby ++;
                    }
                    
                    tempX = x;
                    tempY = y - 1;
                    if (tempY > 0 && feild[tempX][tempY].equals(MINE))
                    {
                        minesNearby ++;
                    }
                    
                    tempX = x - 1;
                    tempY = y - 1;
                    if (tempX > 0 && tempY > 0 && feild[tempX][tempY].equals(MINE))
                    {
                        minesNearby ++;
                    }
                    
                    tempX = x - 1;
                    tempY = y;
                    if (tempX > 0 && feild[tempX][tempY].equals(MINE))
                    {
                        minesNearby ++;
                    }
                    
                    tempX = x - 1;
                    tempY = y + 1;
                    if (tempX > 0 && tempY < size && feild[tempX][tempY].equals(MINE))
                    {
                        minesNearby ++;
                    }
                    
                    tempX = x;
                    tempY = y + 1;
                    if (tempY < size && feild[tempX][tempY].equals(MINE))
                    {
                        minesNearby ++;
                    }

                    feild[x][y] = "" + minesNearby;
                }
            }
        }
        return feild;
    }

    public static void printFeild(String [][] feild)
    {
        System.out.print("\n");
        for (int x = 0; x < feild.length; x++)
        {
            System.out.print("   ");
            for (int y = 0; y < feild[0].length; y++)
                {
                    System.out.print(" " + feild[x][y]);
                }
            System.out.println("");
        }
        System.out.println("");
    }

    public static int requestIntroInput(Scanner input, String request, String error, int min, int max)
    {
        boolean finished = false;

        int number = NOT_A_NUMBER;
        while (!finished)
        {
            System.out.print(request);
            try
            {
                if (input.hasNext())
                {
                    if (input.hasNextInt())
                    {
                        number = input.nextInt();
                        if (number >= min && number <= max)
                        {
                            finished = true;
                        }
                        else
                        {
                            System.out.println("  " + error + "\n");
                        }

                    }
                    else
                    {
                        String message = input.nextLine();
                        if (message.equals("quit") || message.equals("q") || message.equals("exit"))
                        {
                            System.out.println("\nThanks for playing!\n");
                            finished = true;
                            break;
                        }
                        else
                        {
                            System.out.println("  " + error + "\n");
                        }
                    }
                }
                else
                {
                    System.err.println("  Error - Scanner cannot detect input\n");
                }
            }
            catch(Exception exception)
            {
                System.err.println("  Error - exception raised\n");
            }
        }
        
        return number;
    }

    public static int[] requestCoordinates(Scanner input, String request, String error, int size)
    {
        int[] coordinates = {NOT_A_NUMBER, NOT_A_NUMBER, NOT_A_NUMBER};
        boolean finished = false;
        while (!finished)
        {
            System.out.print("Please input the coordinates of the cell by format X0, with '-f'to mark a mine  > ");
            try
            {
                if (input.hasNext())
                {
                    String message = input.nextLine();
                    message = message.toLowerCase();

                    if (message.equals("quit") || message.equals("q") || message.equals("exit"))
                    {
                        System.out.println("\nThanks for playing!\n");
                        finished = true;
                        break;
                    }
                    else
                    {
                        char[] coordinateChars = message.toCharArray();
                        if ((coordinateChars.length == 4 && coordinateChars[2] == '-' && coordinateChars[3] == 'f') || (coordinateChars.length == 5 && coordinateChars[3] == '-' && coordinateChars[4] == 'f') || coordinateChars.length == 2 || coordinateChars.length == 3)
                        {
                            try
                            {
                                int x = ((int) coordinateChars[0]) - 0x60;
                                int y = ((int) coordinateChars[1]) - 0x30;
                                if (size > 10 && (coordinateChars.length == 3 || (coordinateChars.length == 5 && coordinateChars[3] == '-' && coordinateChars[4] == 'f')))
                                {
                                    y = 10*y + ((int) coordinateChars[2]) - 0x30;
                                }
    
                                if (y > size - 1 || y < 1 || x > size - 1 || x < 1)
                                {
                                    System.out.println("  Error - coordinates out of bounds\n");
                                }
                                else
                                {
                                    coordinates[0] = y;
                                    coordinates[1] = x;
                                    if (coordinateChars.length > 3)
                                    {
                                        coordinates[2] = NOT_A_NUMBER;
                                    }
                                    finished = true;
                                    return coordinates;
                                }
                            }
                            catch (Exception exception)
                            {
                                System.out.println("  Error - formatting raises exception\n");
                            }
                        }
                        else
                        {
                            System.out.println("  Error - incorrect formatting\n");
                        }
                    }
                }
                else
                {
                    System.out.println("  Error - Scanner cannot detect input\n");
                }
            }
            catch(Exception exception)
            {
                System.out.println("  Error - exception raised\n");
            }
        }

        return coordinates;
    }
}