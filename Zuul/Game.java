package Zuul;

import java.util.ArrayList;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initializes all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

class Game 
{
    private Parser parser;
    private Room currentRoom;
    Room outside, theatre, pub, lab, office, onetwenty, library, gym, cafeteria, lobby, printroom, shop, field, bandroom, pool;
    ArrayList<Item> inventory = new ArrayList<Item>();
    /**
     * Create the game and initialize its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    public static void main(String[] args) {
    	Game mygame = new Game();
    	mygame.play();
    }
    
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // create the rooms
        outside = new Room("outside the main entrance of the university.");
        theatre = new Room("in a lecture theatre.");
        pub = new Room("in the campus pub.");
        lab = new Room("in a computing lab.");
        office = new Room("in the computing admin office.");
        onetwenty = new Room("in the coolest place in the world.");
        library = new Room("in the school library.");
        gym = new Room("in the school gym.");
        cafeteria = new Room("in the lunch room.");
        lobby = new Room("in the place where you can hang out.");
        printroom = new Room("in the place where you print out your papers.");
        shop = new Room("in the school student store.");
        field = new Room("in the school football field.");
        bandroom = new Room("in the place where the band kids play.");
        pool = new Room("in the swimming pool.");
        
        // Initialize room exits
        shop.setExit("east", printroom);
        shop.setExit("south", gym);

        printroom.setExit("east", lobby);
        printroom.setExit("west", shop);
        printroom.setExit("south", onetwenty);
        
        lobby.setExit("west", printroom);
        lobby.setExit("south", library);
        
        gym.setExit("north", shop);
        gym.setExit("south", pub);
        gym.setExit("east", onetwenty);
        
        onetwenty.setExit("south", outside);
        onetwenty.setExit("north", printroom);
        onetwenty.setExit("west", gym);
        onetwenty.setExit("east", library);
                
        library.setExit("north", lobby);
        library.setExit("west", onetwenty);
        library.setExit("south", theatre);

        pub.setExit("north", gym);
        pub.setExit("south", cafeteria);
        pub.setExit("east", outside);
        
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", onetwenty);

        theatre.setExit("west", outside);
        theatre.setExit("north", library);
        theatre.setExit("south", office);

        cafeteria.setExit("east", lab);
        cafeteria.setExit("north", pub);
        cafeteria.setExit("south", pool);
        
        lab.setExit("north", outside);
        lab.setExit("east", office);
        lab.setExit("south", field);
        lab.setExit("west", cafeteria);

        office.setExit("west", lab);
        office.setExit("north", theatre);
        office.setExit("south", bandroom);
        
        pool.setExit("north", cafeteria);
        pool.setExit("east", field);
        
        field.setExit("north", lab);
        field.setExit("east", bandroom);
        field.setExit("west", pool);
        
        bandroom.setExit("north", office);
        bandroom.setExit("west", field);
        
        currentRoom = lobby;  // start game outside
        
        inventory.add(new Item("Computer"));
        onetwenty.setItem(new Item("Robot"));
        printroom.setItem(new Item("Printer"));
        library.setItem(new Item("Book"));
        gym.setItem(new Item("Basketball"));
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Adventure!");
        System.out.println("Adventure is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
            wantToQuit = goRoom(command);
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("inventory")) {
        	printInventory();
        }
        else if (commandWord.equals("get")) {
        	getItem(command);
        }
        else if (commandWord.equals("drop")) {
        	dropItem(command);
        }
        return wantToQuit;

    }
    private void dropItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            System.out.println("Drop what?");
            return;
        }

        String item = command.getSecondWord();

        // Try to leave current room.
        Item newItem = null;
        int index = 0;
        for (int i = 0; i < inventory.size(); i++) {
        	if (inventory.get(i).getDescription().equals(item)) {
        		newItem = inventory.get(i);
        		index = i;
        	}
        }

        if (newItem == null)
            System.out.println("That item is not in your invetory!");
        else {
        	inventory.remove(index);
        	currentRoom.setItem(new Item(item));
        	System.out.println("Dropped:" + item);
        }
    }
    private void getItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to pick up...
            System.out.println("Get what?");
            return;
        }

        String item = command.getSecondWord();

        // Try to leave current room.
        Item newItem = currentRoom.getItem(item);

        if (newItem == null)
            System.out.println("That item is not here!");
        else {
        	inventory.add(newItem);
        	currentRoom.removeItem(item);
        	System.out.println("Picked up:" + item);
        }
    }
    private void printInventory() {
    	String output = "";
    	for (int i = 0; i < inventory.size(); i++) {
    		output += inventory.get(i).getDescription() + " ";
    	}
		System.out.println("You are carrying:");
		System.out.println(output);
	}

	// implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private boolean goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return false;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null)
            System.out.println("There is no door!");
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            if (currentRoom == outside) {
            	for (int i = 0; i < inventory.size(); i++) {
            		if (inventory.get(i).getDescription().equals("Robot")) {
            			return true;
            		}
            	}
            }
        }
        return false;
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else
            return true;  // signal that we want to quit
    }
}
