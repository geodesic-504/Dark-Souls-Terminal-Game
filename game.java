// Michael Owen
// Homework #7
// this game is based on the Dark Souls expansion called Artorias of the abyss, the goal is to make your way through the levels by killing enemies/bosses, the game concludes when the last boss is killed or the players HP reaches 0
// there are 7 seperate levels, one being a bonus level you don't have to go to, there are 3 required boss fights and 1 optional boss fight
// bosses don't respawn, but when a player rests at a bonfire or changes levels, all of the enemies for that level will be alive
// every enemy is either susceptable lightning or holy damage, and good weapon/spell choices will make the game a lot easier
// the 2 spells are located in 2 chests in the game, and some useful armor drops from specific enemies


import java.util.*;

public class game
{
	// to show color when printing world
	static String CLS = "\033[2J\033[1;1H";
	static String Red = "\033[31;1m";
	static String Green = "\033[32;1m";
	static String Yellow = "\033[33;1m";
	static String Blue = "\033[34;1m";
	static String Purple = "\033[35;1m";
	static String Cyan = "\033[36;1m";
	static String White = "\033[37;1m";
	static String Default = "\033[0m"; // default gray color & reset background to black


   public static void main(String[] args)
   {
	   Scanner in = new Scanner(System.in);
	   // shows intro screens
	   FirstIntroScreen();
	   SecondIntroScreen();
	   // first initialization of a world, the rest are initiated in the players movement code and actually initialized in GameObject when they walk into a '#'
	   GameObject.InitializeWorld1();
	   Player P = new Player("Ashen One");
	   // creates empty array, enemies are put into array when world is initialized outside of the first intialization above
	   ArrayList<Enemy> Enemies = new ArrayList<Enemy>();
	   char Choice = ' '; // used for player input
	   int ManusKilled = 0; // used to track if last boss is killed, toggles end game screen
	   String CurrentWeapon = "Bastard Sword";


	   // enemies for first level when you first start the game
	    Enemies.add(new Enemy("Hollow1"));
		Enemies.add(new Enemy("Hollow2"));
		Enemies.add(new Enemy("Ent1"));
		Enemies.add(new Enemy("Ent2"));

	   while (Choice != 'm' && P.HP > 0 && ManusKilled == 0)  // game loop
	   {
		   GameObject.PrintWorld();
		   System.out.println();
		   GameObject.Announcement();
		   System.out.println();
		   System.out.println("Health: " + P.HP + "\tEstus: " + P.Estus + "\t\tr - Attack"  + "\t\tw a s d - Movement");
		   System.out.println("Attack: " + P.Attack + "\tSpell Charges: " + P.SpellCharges + "\tz - Lightning Bolt" + "\tx - Holy Blast");
		   System.out.println("Armor: " + P.Armor + "\tWeapon = " + CurrentWeapon + "\tt - Switch Weapon" + "\tf- Use Estus" +"\t\tm - Quit Game");
		   System.out.println();
		   System.out.print("Enter your command: ");

			// start of input commands
		   Choice = in.next().charAt(0);
		   if (Choice == 'w')
		   	P.MoveUp();
		   else if (Choice == 's')
		   	P.MoveDown();
		   else if (Choice == 'a')
		    P.MoveLeft();
		   else if (Choice == 'd')
		    P.MoveRight();
		    // used for healing
		   else if (Choice == 'f')
		   {
		    if (P.Estus != 0)
			{
				P.HP = P.MaxHP;
				P.Estus -= 1;
			}
	   	   }
	   	   // used to switch weapon, takes into account whether player has a weapon or not by using a static variable
	   	   else if (Choice == 't')
		    {
				if (P.BastardSwordActive == 1 && P.HaveDragonSlayerSpear == 1)
				{
					CurrentWeapon = "Dragon Slayer Spear";
					P.Attack += 5;
					P.BastardSwordActive = 0;
					P.DragonSlayerSpearActive = 1;
				}
				else if (P.DragonSlayerSpearActive == 1 && P.HaveGreatSwordOfArtorias == 1)
				{
					CurrentWeapon = "Greatsword of Artorias";
					P.DragonSlayerSpearActive = 0;
					P.GreatSwordOfArtoriasActive = 1;
				}
				else if (P.DragonSlayerSpearActive == 1 && P.HaveGreatSwordOfArtorias == 0)
				{
					CurrentWeapon = "Bastard Sword";
					P.Attack -= 5;
					P.DragonSlayerSpearActive = 0;
					P.BastardSwordActive = 1;
				}
				else if (P.GreatSwordOfArtoriasActive == 1)
				{
					CurrentWeapon = "Bastard Sword";
					P.GreatSwordOfArtoriasActive = 0;
					P.BastardSwordActive = 1;
					P.Attack -=5;
				}
				else
				{

				}
			}
			// for player attack
		   else if (Choice == 'r')
		   {
		    for (int i=0; i<Enemies.size(); i++)
			{
				if (GameObject.TempDamage == 0) // makes it so player only hits one enemy
				{
				if ((Math.abs(Enemies.get(i).Ypos - P.Ypos) <= 1) && (Math.abs(Enemies.get(i).Xpos - P.Xpos) <= 1))  // player is within 1 space in any direction from the enemy
				{
					// When Greatsword of Artorias is equipped, player does more damaage to the enemies with the avatars below
					if (P.GreatSwordOfArtoriasActive == 1 && (Enemies.get(i).Avatar == 'M' || Enemies.get(i).Avatar == 'C' || Enemies.get(i).Avatar == 'Q' || Enemies.get(i).Avatar == 'D' || Enemies.get(i).Avatar == 'H'))
					{
						int Temp = Enemies.get(i).HP;
						Enemies.get(i).HP -= P.Attack * 3 / 2 * ((100 - Enemies.get(i).Armor) * .01);
						GameObject.TempDamage = Temp - Enemies.get(i).HP;
						GameObject.AnnouncementActive = 1;
						GameObject.WhichAnnouncement = 12;
					}
					// When Dragon Slayer Spear  is equipped, player does more damaage to the enemies with the avatars below
					else if (P.DragonSlayerSpearActive == 1 && (Enemies.get(i).Avatar == 'U' || Enemies.get(i).Avatar == 'K' || Enemies.get(i).Avatar == 'E' || Enemies.get(i).Avatar == 'G'))
					{
						int Temp = Enemies.get(i).HP;
						Enemies.get(i).HP -= P.Attack * 3 / 2 * ((100 - Enemies.get(i).Armor) * .01);
						GameObject.TempDamage = Temp - Enemies.get(i).HP;
						GameObject.AnnouncementActive = 1;
						GameObject.WhichAnnouncement = 12;
					}
					// for regular attacks
					else
					{
						int Temp = Enemies.get(i).HP;
						Enemies.get(i).HP -= P.Attack * ((100 - Enemies.get(i).Armor) * .01);
						GameObject.TempDamage = Temp - Enemies.get(i).HP;
						GameObject.AnnouncementActive = 1;
						GameObject.WhichAnnouncement = 12;
					}

					// used to remove enemy avatars when they die, and used to drop items in their place if they drop items
					if (Enemies.get(i).HP <= 0)
					{
						if (Enemies.get(i).Avatar == 'Z')
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '+';
						else if (Enemies.get(i).Avatar == 'T')
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '+';
						else if (Enemies.get(i).Avatar == 'U')
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '^';
						else if (Enemies.get(i).Avatar == 'K')
						{
							int temp = (int) (Math.random() * 100);
							if (temp > 60)
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '!';
							else
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';
						}
						else if (Enemies.get(i).Avatar == 'C')
						{
							int temp = (int) (Math.random() * 100);
							if (temp > 70)
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '(';
							else
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';
						}
						else if (Enemies.get(i).Avatar == 'G')
						{
							int temp = (int) (Math.random() * 100);
							if (temp > 80)
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ')';
							else
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';
						}
						else
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';

						Enemies.remove(i); // remove enemy from world
					}
				}
			}
		   }
	   }
	   	   // Lightning Bolt spell
		   else if (Choice == 'z')
		   {
			if (P.SpellCharges != 0 && P.HaveLightning == 1) // makes sure player has spellcharges and the spell itself
			{
				P.SpellCharges -= 1;
		    for (int i=0; i<Enemies.size(); i++)
			{
				if ((Math.abs(Enemies.get(i).Ypos - P.Ypos) <= 5) && (Math.abs(Enemies.get(i).Xpos - P.Xpos) <= 8))  // range of the spell, Y axis is smaller since it appears longer in command prompt
				{
					// announcements used to show damage dealt
					GameObject.AnnouncementActive = 1;
					GameObject.WhichAnnouncement = 12;
					// increases damage for certain enemies
					if (Enemies.get(i).Avatar == 'U' || Enemies.get(i).Avatar == 'K' || Enemies.get(i).Avatar == 'E' || Enemies.get(i).Avatar == 'G')
					{
						Enemies.get(i).HP -= P.Attack * 2;
						GameObject.TempDamage += P.Attack * 2;
					}
					// regular damage
					else
					{
						Enemies.get(i).HP -= P.Attack * 3 / 2;
						GameObject.TempDamage += P.Attack * 3 / 2;
					}
					// if enemy dies, removes their icon and them from the array list, and will drop items for certain enemies
					if (Enemies.get(i).HP <= 0)
					{
						if (Enemies.get(i).Avatar == 'Z')
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '+';
						else if (Enemies.get(i).Avatar == 'T')
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '+';
						else if (Enemies.get(i).Avatar == 'U')
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '^';
						else if (Enemies.get(i).Avatar == 'K')
						{
							int temp = (int) (Math.random() * 100);
							if (temp > 60)
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '!';
							else
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';
						}
						else if (Enemies.get(i).Avatar == 'C')
						{
							int temp = (int) (Math.random() * 100);
							if (temp > 70)
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '(';
							else
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';
						}
						else if (Enemies.get(i).Avatar == 'G')
						{
							int temp = (int) (Math.random() * 100);
							if (temp > 80)
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ')';
							else
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';
						}
						else
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';

						Enemies.remove(i); // remove enemy from world
					}
				}
			}
		   }
		   // for when player hasn't acquired the spell yet
		   else if (P.HaveLightning == 0)
		   {
		   	GameObject.WhichAnnouncement = 10;
		   	GameObject.AnnouncementActive = 1;
			}
			// for when the player is out of spell charges
			else
			{
				GameObject.WhichAnnouncement = 11;
				GameObject.AnnouncementActive = 1;
			}
	   }
			// holy blast spell
		   else if (Choice == 'x')
		   {
			   // checks if player has spell charges and the spell itself
		    if (P.SpellCharges != 0 && P.HaveHoly == 1)
		    {
				P.SpellCharges -= 1; // reduces spell charges when spell is cast
		    for (int i=0; i<Enemies.size(); i++)
			{
				if ((Math.abs(Enemies.get(i).Ypos - P.Ypos) <= 5) && (Math.abs(Enemies.get(i).Xpos - P.Xpos) <= 8)) // range of the spell
				{
					// announcements used to show damage dealt
					GameObject.AnnouncementActive = 1;
					GameObject.WhichAnnouncement = 12;
					// increases damage towards certain enemies
					if (Enemies.get(i).Avatar == 'T' || Enemies.get(i).Avatar == 'M' || Enemies.get(i).Avatar == 'C' || Enemies.get(i).Avatar == 'Q' || Enemies.get(i).Avatar == 'D' || Enemies.get(i).Avatar == 'H')
					{
						Enemies.get(i).HP -= P.Attack * 2;
						GameObject.TempDamage += P.Attack * 2;
					}
					// regular damage
					else
					{
						Enemies.get(i).HP -= P.Attack * 3 / 2;
						GameObject.TempDamage += P.Attack * 3 / 2;
					}
					// this section removes enemy avatar, enemy from array list, and drops items for certain enemies
					if (Enemies.get(i).HP <= 0) // enemy dies
					{
						if (Enemies.get(i).Avatar == 'Z')
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '+';
						else if (Enemies.get(i).Avatar == 'T')
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '+';
						else if (Enemies.get(i).Avatar == 'U')
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '^';
						else if (Enemies.get(i).Avatar == 'K')
						{
							int temp = (int) (Math.random() * 100);
							if (temp > 60)
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '!';
							else
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';
						}
						else if (Enemies.get(i).Avatar == 'C')
						{
							int temp = (int) (Math.random() * 100);
							if (temp > 70)
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = '(';
							else
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';
						}
						else if (Enemies.get(i).Avatar == 'G')
						{
							int temp = (int) (Math.random() * 100);
							if (temp > 80)
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ')';
							else
								GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';
						}
						else
							GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';

						Enemies.remove(i); // remove enemy from world
					}
				}
			}
		   }
		   // announces player doesn't have the spell
		   else if (P.HaveHoly == 0)
		   {
		   	GameObject.WhichAnnouncement = 10;
		   	GameObject.AnnouncementActive = 1;
			}
			// announces player has no spell charges
			else
			{
				GameObject.WhichAnnouncement = 11;
				GameObject.AnnouncementActive = 1;
			}
	   }

		// for adding enemies when switching levels or resting at a bonfire, ActivatingWorld is set to 1 when player moves onto a '#' character, or rests at a bonfire
	   if (GameObject.ActivatingWorld == 1)
	   {
			Enemies.clear(); // clears the enemies currently in the Enemy array list

		   if (GameObject.ActiveWorld == 1)
		   {
			   Enemies.add(new Enemy("Hollow1"));
			   Enemies.add(new Enemy("Hollow2"));
			   Enemies.add(new Enemy("Ent1"));
			   Enemies.add(new Enemy("Ent2"));

		   }
		   else if (GameObject.ActiveWorld == 2)
		   {
			   if (GameObject.Boss1Dead == 0)
			   	Enemies.add(new Enemy("Sanctuary Guardian"));
		   }
		   else if (GameObject.ActiveWorld == 3)
		   {
			   Enemies.add(new Enemy("Abyss Attack Dog1"));
			   Enemies.add(new Enemy("Abyss Attack Dog2"));
			   Enemies.add(new Enemy("Abyss Attack Dog3"));
			   Enemies.add(new Enemy("Abyss Attack Dog4"));
			   Enemies.add(new Enemy("Silver Knight"));
			   Enemies.add(new Enemy("Corrupted Abyss Knight"));
			   Enemies.add(new Enemy("Guardian1"));
			   Enemies.add(new Enemy("Guardian2"));
		   }
		   else if (GameObject.ActiveWorld == 7)
		   {
			   if (GameObject.Boss3Dead == 0)
			   	Enemies.add(new Enemy("Black Dragon Kalameet"));
		   }
		   else if (GameObject.ActiveWorld == 4)
		   {
			   if (GameObject.Boss2Dead == 0)
			   	Enemies.add(new Enemy("Knight Artorias"));
		   }
		   else if (GameObject.ActiveWorld == 5)
		   {
			   Enemies.add(new Enemy("Humanity Phantom1"));
			   Enemies.add(new Enemy("Humanity Phantom2"));
			   Enemies.add(new Enemy("Humanity Phantom3"));
			   Enemies.add(new Enemy("Humanity Phantom4"));
			   Enemies.add(new Enemy("Humanity Phantom5"));
			   Enemies.add(new Enemy("Humanity Phantom6"));
			   Enemies.add(new Enemy("Humanity Phantom7"));
			   Enemies.add(new Enemy("Humanity Phantom8"));
			   Enemies.add(new Enemy("Humanity Phantom9"));
			   Enemies.add(new Enemy("Humanity Phantom10"));
			   Enemies.add(new Enemy("Humanity Phantom11"));
			   Enemies.add(new Enemy("Humanity Phantom12"));
			   Enemies.add(new Enemy("Humanity Phantom13"));
			   Enemies.add(new Enemy("Humanity Phantom14"));
			   Enemies.add(new Enemy("Humanity Phantom15"));
			   Enemies.add(new Enemy("Humanity Phantom16"));
		   }
		   else if (GameObject.ActiveWorld == 6)
		   {
				Enemies.add(new Enemy("Manus, Father of the Abyss"));
		   }

			// sets flag for this code section back to 0
		   GameObject.ActivatingWorld = 0;

		 } // end of code to add enemies for specific levels

		// enemy attack
		for (int i=0; i<Enemies.size(); i++)
		{
			if ((Math.abs(Enemies.get(i).Ypos - P.Ypos) <= 1) &&  (Math.abs(Enemies.get(i).Xpos - P.Xpos) <= 1))  // attacks player if enemy is 1 unit from the player, can attack diagonally
			{
				P.HP -= Enemies.get(i).Attack * ((100 -P.Armor) * .01);
			}
		}


			// Enemy Movement
			for (int i=0; i<Enemies.size(); i++)
		    {
				if (Math.abs(P.Xpos - Enemies.get(i).Xpos) <= Enemies.get(i).Range &&
					(Math.abs(P.Ypos - Enemies.get(i).Ypos)) <= Enemies.get(i).Range / 2) // see if player is in range of enemy, Y axis divided by 2 as spaces are larger
				{
					if (Enemies.get(i).Xpos > P.Xpos) // player to the right of the enemy
					{
						if (GameObject.World[Enemies.get(i).Xpos-1][Enemies.get(i).Ypos] == ' ')
							Enemies.get(i).MoveLeft();
						else
						{
							Enemies.get(i).MoveUp();
							Enemies.get(i).MoveLeft();
						}
					}

					else if (Enemies.get(i).Xpos < P.Xpos) // player is to the left of the enemy
					{
						if (GameObject.World[Enemies.get(i).Xpos+1][Enemies.get(i).Ypos] == ' ')
							Enemies.get(i).MoveRight();
						else
						{
							Enemies.get(i).MoveUp();
							Enemies.get(i).MoveRight();
						}
					}

					else if (Enemies.get(i).Ypos > P.Ypos) // player is below the enemy
					{
						if (GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos-1] == ' ')
							Enemies.get(i).MoveUp();
						else
						{
							Enemies.get(i).MoveRight();
							Enemies.get(i).MoveUp();
						}
					}

					else if (Enemies.get(i).Ypos < P.Ypos) // player is above the enemy
					{
						if (GameObject.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos+1] == ' ')
							Enemies.get(i).MoveDown();
						else
						{
							Enemies.get(i).MoveRight();
							Enemies.get(i).MoveDown();
						}
					}
				}

				else // when not in range, enemies move randomly
				{
					int D = (int)(Math.random()*20);
					if (D == 0)
						Enemies.get(i).MoveLeft();
					else if (D == 1)
						Enemies.get(i).MoveRight();
					else if (D == 2)
						Enemies.get(i).MoveUp();
					else if (D == 3)
						Enemies.get(i).MoveDown();

				}

				}

		// sets flag mankilled to 1, so that game loop ends and winning screen is shown
		if (GameObject.ActiveWorld == 6)
		{
			if (Enemies.size() == 0)
				ManusKilled = 1;
		}





		} // ends game loop, which is kept active by the 'while' statement that checks whether manus is killed, player HP is zero, or input is m

		// go to winning, losing, or quit screen
	   if (Choice !='m' && P.HP <= 0)
	   		LosingScreen();
	   	else if (Choice == 'm')
	   		QuitScreen();
	   	else if (Choice !='m' && P.HP >=0)
		WinningScreen();

	   } // ends main and ends input loop


	static void FirstIntroScreen()
	{
		System.out.print(CLS);
		System.out.println("		______  ___  ______ _   __  _____  _____ _   _ _      _____");
		System.out.println("		|  _  \\/ _ \\ | ___ \\ | / / /  ___||  _  | | | | |    /  ___|");
		System.out.println("		| | | / /_\\ \\| |_/ / |/ /  \\ `--. | | | | | | | |    \\ `--.");
		System.out.println("		| | | |  _  ||    /|    \\   `--. \\| | | | | | | |     `--. \\");
		System.out.println("		| |/ /| | | || |\\ \\| |\\  \\ /\\__/ /\\ \\_/ / |_| | |____/\\__/ /");
		System.out.println("		|___/ \\_| |_/\\_| \\_\\_| \\_/ \\____/  \\___/ \\___/\\_____/\\____/");
		System.out.println();
		System.out.println("   _          _                 _                       __   _    _               _    _");
		System.out.println("  /_\\   _ __ | |_   ___   _ __ (_)  __ _  ___    ___   / _| | |_ | |__    ___    /_\\  | |__   _   _  ___  ___");
		System.out.println(" //_\\\\ | '__|| __| / _ \\ | '__|| | / _` |/ __|  / _ \\ | |_  | __|| '_ \\  / _ \\  //_\\\\ | '_ \\ | | | |/ __|/ __|");
		System.out.println("/  _  \\| |   | |_ | (_) || |   | || (_| |\\__ \\ | (_) ||  _| | |_ | | | ||  __/ /  _  \\| |_) || |_| |\\__ \\\\__ \\");
		System.out.println("\\_/ \\_/|_|    \\__| \\___/ |_|   |_| \\__,_||___/  \\___/ |_|    \\__||_| |_| \\___| \\_/ \\_/|_.__/  \\__, ||___/|___/");
		System.out.println("                                                                                              |___/");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("				 Press <Enter> To Continue");

		Scanner in = new Scanner(System.in);
		in.nextLine();



	}

	static void SecondIntroScreen()
	{
		System.out.println("You are a hollowed knight on a pilgrimage to the land of	|              ----");
		System.out.println("ancient Lords. Eons have passed since the First Flame was	|              \\   \\");
		System.out.println("linked by Lord Gwyn, and the flame dimmers. A calling to	|               \\___\\");
		System.out.println("all hollowed, you desire to link the First Flame again and	|                 \\\\\\");
		System.out.println("continue the age of fire as the new Lord.			|                  \\\\\\");
		System.out.println(" 								|                   \\\\\\");
		System.out.println("On your journey, you find a young maiden trapped in a 		|                    \\\\\\");
		System.out.println("crystal creature. Upon freeing her, a portal appears.		|                  \\______\\");
		System.out.println("As you approach it, you are pulled into the land of Oolacile,	|                     \\\\\\");
		System.out.println("which is being ravaged by the abyss. Fight your way through	|                      \\\\\\");
		System.out.println("this new land and kill Manus, Father of the Abyss, to stop	|                       \\\\\\");
		System.out.println("this plague from spreading further.				|                        \\\\\\");
		System.out.println("								|                         \\\\\\  ##");
		System.out.println("You will find bonfires denoted by the @ sign, resting at	|                      ##  \\\\\\   ##   ##");
		System.out.println("one of these will bring you back to full health and completely	|                     ##    \\\\\\ ##     ##");
		System.out.println("refill your Estus Flask - which can heal you a number of times.	|                      ##    \\##        ##");
		System.out.println("								|                        ## ##\\\\\\      ##");
		System.out.println("								|                          ####\\\\\\    ##   ##");
		System.out.println("								|                           ### \\##  ##     ##");
		System.out.println("								|                     ##    #####\\\\###      ##");
		System.out.println("Keybind List:							|                    ##       ####\\\\####   ##");
		System.out.println("w, a, s, d - Movement						|                  ##       #######\\#######          ##");
		System.out.println("f - Use Estus Flask						|               ##    ##  ###################         ##");
		System.out.println("r - Attack							|              ##     ## #######################      ##");
		System.out.println("z - Lightning Spell, must be obtained first			|               ##     ##########################    ##");
		System.out.println("x - Holy Spell, must be obtaind first				|                ##   ############################  ##");
		System.out.println("m - Quit Game	     					        |                 ## ##############################");
		System.out.println("	Press <Enter> To Start         			        |                   ###############################");
		System.out.println("								|                   ################################");

		Scanner in = new Scanner(System.in);
		in.nextLine();
	}

	static void QuitScreen()
	{
		System.out.print(CLS);
		System.out.println("Until next time...");
		System.out.println();
	}

	static void LosingScreen()
	{
		System.out.print(CLS);
		System.out.println("__   _______ _   _   _   _   ___  _   _ _____  ______ _____ ___________ ");
		System.out.println("\\ \\ / /  _  | | | | | | | | / _ \\| | | |  ___| |  _  \\_   _|  ___|  _  \\");
		System.out.println(" \\ V /| | | | | | | | |_| |/ /_\\ \\ | | | |__   | | | | | | | |__ | | | |");
		System.out.println("  \\ / | | | | | | | |  _  ||  _  | | | |  __|  | | | | | | |  __|| | | |");
		System.out.println("  | | \\ \\_/ / |_| | | | | || | | \\ \\_/ / |___  | |/ / _| |_| |___| |/ / ");
		System.out.println("  \\_/  \\___/ \\___/  \\_| |_/\\_| |_/\\___/\\____/  |___/  \\___/\\____/|___/  ");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("It seems that your journey ends here. Neither you, nor Knight Artorias could");
		System.out.println("stop the spread of the abyss, and the town of Oolacile will sucuumb to it.");
		System.out.println("Without you, the First Flame dimmers until it fades, leading to the Age of Dark.");

	}

	static void WinningScreen()
	{
		System.out.print(CLS);
		System.out.println(" _____ _____ _   _ _____   ___ _____ _   _ _       ___ _____ _____ _____ _   _  _____          ");
		System.out.println("/  __ \\  _  | \\ | |  __ \\ / _ \\_   _| | | | |     / _ \\_   _|_   _|  _  | \\ | |/  ___|         ");
		System.out.println("| /  \\/ | | |  \\| | |  \\// /_\\ \\| | | | | | |    / /_\\ \\| |   | | | | | |  \\| |\\ `--.          ");
		System.out.println("| |   | | | | . ` | | __ |  _  || | | | | | |    |  _  || |   | | | | | | . ` | `--. \\         ");
		System.out.println("| \\__/\\ \\_/ / |\\  | |_\\ \\| | | || | | |_| | |____| | | || |  _| |_\\ \\_/ / |\\  |/\\__/ /         ");
		System.out.println(" \\____/\\___/\\_| \\_/\\____/\\_| |_/\\_/  \\___/\\_____/\\_| |_/\\_/  \\___/ \\___/\\_| \\_/\\____/          ");
		System.out.println("");
		System.out.println("");
		System.out.println("__   _______ _   _  ______ _____  ___ _____   _____ _   _  _____   _____   ___  ___  ___ _____ ");
		System.out.println("\\ \\ / /  _  | | | | | ___ \\  ___|/ _ \\_   _| |_   _| | | ||  ___| |  __ \\ / _ \\ |  \\/  ||  ___|");
		System.out.println(" \\ V /| | | | | | | | |_/ / |__ / /_\\ \\| |     | | | |_| || |__   | |  \\// /_\\ \\| .  . || |__  ");
		System.out.println("  \\ / | | | | | | | | ___ \\  __||  _  || |     | | |  _  ||  __|  | | __ |  _  || |\\/| ||  __| ");
		System.out.println("  | | \\ \\_/ / |_| | | |_/ / |___| | | || |     | | | | | || |___  | |_\\ \\| | | || |  | || |___ ");
		System.out.println("  \\_/  \\___/ \\___/  \\____/\\____/\\_| |_/\\_/     \\_/ \\_| |_/\\____/   \\____/\\_| |_/\\_|  |_/\\____/ ");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("With Manus, Father of the Abyss defeated, the town of Oolacile can heal and go back to normal.");
		System.out.println("When you killed Manus, a portal back to your land appeared. Now you can continue your journey");
		System.out.println("to become the Lord of Fire.");
	}

} // ends main()/game


class GameObject // superclass
{
	// CLS clears console, need to enable ANSI escape sequences for this to work by inputing below command into CMD with admin privileges
	// reg add HKEY_CURRENT_USER\Console /v VirtualTerminalLevel /t REG_DWORD /d 0x00000001 /f
	static int ActiveWorld = 1; // keeps track of which world the player is in, used when changing worlds, resting at a bonfire, and flags for certain scenarios such as item drops
	static char[][] World = new char[81][21]; // sets up World array, this is initialized by the 7 InitializeWorlds
	int Xpos, Ypos; // coordinates used by player and enemies
	char Avatar; // symbol representing player or enemies
	int HP; // current health
	int MaxHP; //  maximum health
	int Attack; // damage inflicted by an attack, spell damage is a multiple of this value
	int Armor; // Percentage based, ie 25 will reduce damage by 25 percent, spells ignore armor
	int SpellCharges; // counter for how many spell charges player has left
	int Estus; // counter for how many Estus Flask uses the player has left
	int HaveBastardSword; // base weapon
	int HaveDragonSlayerSpear; // flag to check whether you have this weapon or not when switching weapons
	int HaveGreatSwordOfArtorias; // flag to check whether you have this weapon or not when switching weapons
	int BastardSwordActive; // used for switching weapons
	int DragonSlayerSpearActive; // used for switching weapons and modifying damage
	int GreatSwordOfArtoriasActive; // used for switching weapons and modifying damage
	static int TempDamage; // used to show player damage in announcements
	static int HaveLightning; // used to check if player has acquired the lightning spell
	static int HaveHoly; // used to check if player has acquired the holy spell
	static int HaveHelmet; // used to check if player has a helmet already, if they do an announcement is made stating that and armor isn't increased
	static int HaveChestArmor; // ^
	static int HaveLegArmor;// ^^
	static int AnnouncementActive; // used to activate Announcement in game loop, it is set to 1 when an announcement is to be made, and set to 0 after the announcement is made
	static int WhichAnnouncement; // used to change which announcements are made
	static int Chest1Flag = 0; // flag for showing or not showing chest if it hasn't or has been opeened
	static int Chest2Flag = 0; // ^
	static int ActivatingWorld = 0; // set to 1 when player is changing worlds or rests at bonfire, used to activate code that puts enemies in the world
	static int Boss1Dead = 0;  // flag for sanctuary gardian
	static int Boss2Dead = 0; // flag for Artorias
	static int Boss3Dead = 0; // flag for Black Dragon Kalameet


	static String CLS = "\033[2J\033[1;1H";
	static String Red = "\033[31;1m";
	static String Green = "\033[32;1m";
	static String Yellow = "\033[33;1m";
	static String Blue = "\033[34;1m";
	static String Purple = "\033[35;1m";
	static String Cyan = "\033[36;1m";
	static String White = "\033[37;1m";
	static String Default = "\033[0m"; // default gray color & reset background to black

	static String WhiteOnRed = "\033[41;1m";
	static String WhiteOnGreen = "\033[42;1m";
	static String WhiteOnYellow = "\033[43;1m";
	static String WhiteOnBlue = "\033[44;1m";
	static String WhiteOnPurple = "\033[45;1m";
	static String WhiteOnCyan = "\033[46;1m";


	// for changing worlds when player is using MoveUp()
	static void ChangeWorldUp()
	{
		ActiveWorld += 1;
		ActivatingWorld = 1;

		if (ActiveWorld == 2)
		{
			InitializeWorld2();
		}
		else if (ActiveWorld == 3)
		{
			InitializeWorld3();
		}
		else if (ActiveWorld == 4)
		{
			InitializeWorld4();
		}
		else if (ActiveWorld == 5)
		{
			InitializeWorld5();
		}
		else if (ActiveWorld == 6)
		{
			InitializeWorld6();
		}
	}

	// for changing worlds when player is using MoveDown()
	static void ChangeWorldDown()
		{

			ActiveWorld -= 1;
			ActivatingWorld = 1;

			if (ActiveWorld == 1)
			{
				InitializeWorld1();
			}
			else if (ActiveWorld == 2)
			{
				InitializeWorld2();
			}
			else if (ActiveWorld == 3)
			{
				InitializeWorld3();
			}
			else if (ActiveWorld == 4)
			{
				InitializeWorld4();
			}
			else if (ActiveWorld == 5)
			{
				InitializeWorld5();
			}
	}

	// for changing worlds when player moves between 7'th (extra) world and 3'rd world, only world that you access using MoveLeft() and MoveRight()
	static void ChangeExtraWorld()
	{

		if (ActiveWorld == 3)
		{
			ActiveWorld +=4;
			ActivatingWorld = 1;
			InitializeWorld7();
		}
		else if (ActiveWorld == 7)
		{
			ActiveWorld -=4;
			ActivatingWorld = 1;
			InitializeWorld3();
		}
	}

	// used when you rest at a bonfire
	static void ReloadWorld()
	{
		if (ActiveWorld == 1)
		{
			InitializeWorld1();
		}
		else if (ActiveWorld == 2)
		{
			InitializeWorld2();
		}
		else if (ActiveWorld == 3)
		{
			InitializeWorld3();
		}
		else if (ActiveWorld == 4)
		{
			InitializeWorld4();
		}
		else if (ActiveWorld == 5)
		{
			InitializeWorld5();
		}
		else if (ActiveWorld == 6)
		{
			InitializeWorld6();
		}
		else if (ActiveWorld == 7)
		{
			InitializeWorld7();
		}
	}


	// used to place an announcement when desired
   static void Announcement()
	{
	if (AnnouncementActive == 1)
	{
		if (WhichAnnouncement == 1)
			System.out.println("You already have this item.");
		else if (WhichAnnouncement == 2)
			System.out.println("You have acquired the spell - Thunderbolt. This spell brings thunder down upon all nearby enemies.");
		else if (WhichAnnouncement == 3)
			System.out.println("You have acquired the spell - Holy Blast. This spell creates an explosion of holy energy, damaging all nearby enemies.  This spell is very effective against the abyss.");
		else if (WhichAnnouncement == 4)
			System.out.println("You have acquired the Ring Of Favor and leveled up! Your max HP and attack power have increased. This ring increases your attack power, health, and armor.");
		else if (WhichAnnouncement == 5)
			System.out.println("You have acquired the Silver Knight Helm. Your armor has increased.");
		else if (WhichAnnouncement == 6)
			System.out.println("You have acquired the Abyss Knight Chest Piece. Your armor has increased.");
		else if (WhichAnnouncement == 7)
			System.out.println("You have acquired the Guardians Leg Piece. Your armor has increased.");
		else if (WhichAnnouncement == 8)
			System.out.println("You have acquired the Dragonslayer Spear and leveled up! Your max HP and attack power have increased. This weapon is infused with lightning, and is effective against enemies       suspectible to lightning damage. Lord Gwyn knew of dragons susceptability to "
								+ "lightning, so he crafted weapons like this one to aid his knights.");
		else if (WhichAnnouncement == 9)
			System.out.println("You have acquired the Greatsword of Artorias and leveled up! Your max HP and attack power have increased. Oolaciles strongest knight - Artorias tried to stop the spread of the abyss, but was killed by Manus. " +
							   "This weapon still carries some holy essence, and is effective against enemies suspectible to holy damage.");
		else if (WhichAnnouncement == 10)
			System.out.println("You do not have that spell!");
		else if (WhichAnnouncement == 11)
			System.out.println("You do not have any spell charges left!");
		else if (WhichAnnouncement == 12)
		{
			System.out.println("You did " + TempDamage + " Damage!");
			TempDamage = 0;
		}
		else if (WhichAnnouncement == 13)
			System.out.println("You rested at a bonfire, your health, estus, and spell charges have been restored.");
		else if (WhichAnnouncement == 14)
			System.out.println("You see the entrance to Oolacile, but a Sanctuary Guardian is blocking it!");
		else if (WhichAnnouncement == 15)
			System.out.println("You see a Knight roaming around aimlessly. His body is disfigured by the abyss, and he does not appear to be sentient.");
		else if (WhichAnnouncement == 16)
			System.out.println("You are in the presence of Manus, Father of the Abyss!");
		else if (WhichAnnouncement == 17)
			System.out.println("You walk into an open field that appears to be scorched. There are multiple charred bodies through out the area. In the distance you see Black Dragon Kalameet.");

	}
	AnnouncementActive = 0;
	}

	// used to show damage player deals
	static void Damage()
	{
		AnnouncementActive = 1;

	}

	// for the two chests, considers which world it is in to show correct message
	static void Chest()
	{
		AnnouncementActive = 1;
		if (ActiveWorld == 1)
		{
			WhichAnnouncement = 2;
		}
		else if (ActiveWorld == 3)
		{
			WhichAnnouncement = 3;
		}
	}

	// for item dropped by kalameet
	static void RingOfFavor()
	{
		AnnouncementActive = 1;
		WhichAnnouncement = 4;
	}

	static void Helmet()
	{
		AnnouncementActive = 1;
		if (HaveHelmet == 1)
			WhichAnnouncement = 1;
		else if (HaveHelmet == 0)
		{
			HaveHelmet = 1;
			WhichAnnouncement = 5;
		}
	}


	static void ChestArmor()
	{
		AnnouncementActive = 1;
		if (HaveChestArmor == 1)
			WhichAnnouncement = 1;
		else if (HaveChestArmor == 0)
		{
			HaveChestArmor = 1;
			WhichAnnouncement = 6;
		}

	}

	static void LegArmor()
	{
		AnnouncementActive = 1;
		if (HaveLegArmor == 1)
			WhichAnnouncement = 1;
		else if (HaveLegArmor == 0)
		{
			HaveLegArmor = 1;
			WhichAnnouncement = 7;
		}

	}
	// displays a message when player acquires each of the two weapons in this game
	static void Weapon()
	{
		AnnouncementActive = 1;
		if (ActiveWorld == 2)
			WhichAnnouncement = 8;
		else if (ActiveWorld == 4)
			WhichAnnouncement = 9;
	}


	// message for when player rests at a bonfire
	static void Bonfire()
	{
		AnnouncementActive = 1;
		WhichAnnouncement = 13;
	}


	// this prints the world array, it is changed in many section of this program, and is initialized by InitializeWorld1-7 when player rests at a bonfire or changes levels
	static void PrintWorld()
	{
		System.out.print(CLS);
		for (int y=1; y<=20; y++)
		{
			// for changing the colors of things
			for (int x=1; x<=80; x++)
			{
				if (World[x][y] == ' ')
					System.out.print(" ");
				else if (World[x][y] == '*')
					System.out.print(Green+"*"+Default);
				else if (World[x][y] == 'P')
					System.out.print(Cyan+"P"+Default);
				else if (World[x][y] == '#' || World[x][y] == '|')
					System.out.print(White+ World[x][y]);
				else if (World[x][y] == '@')
					System.out.print(Yellow + '@');
				else if (World[x][y] == '%' || World[x][y] == '^' || World[x][y] == '&' || World[x][y] == '+' || World[x][y] == '!' || World[x][y] == '(' || World[x][y] == ')')
					System.out.print(Blue + World[x][y]);
				else if (World[x][y] == 'Z' || World[x][y] == 'T' || World[x][y] == 'U' || World[x][y] == 'M')
					System.out.print(Purple+ World[x][y]);
				else
					System.out.print(Red+ World[x][y]);
			}
			System.out.println();
		}

	}

	// below is the code used when initializing each world
	static void InitializeWorld1()
	{
		// sets base template of World
		// coordinates of each corner inside the border - (2,2)(2,19)(78,2)(78,19)
		for (int y=1; y<=20; y++)
		{
			for (int x=1; x<=80; x++)
			{
				if (y == 1 && x%2 != 1) // spaces between top row asteriks
					World[x][y] = ' ';
				else if (y == 1) // top row
					World[x][y] = '*';
				else if (y == 20 && x%2 != 1) // spaces between bottom row asteriks
					World[x][y] = ' ';
				else if (y == 20) // bottom row
					World[x][y] = '*';
				else if (x == 1) // left column
					World[x][y] = '*';
				else if (x == 79) // right column
					World[x][y] = '*';
				else  // non border spaces set to space
					World[x][y] = ' ';
			}
		}

		// Entrance to world 2
		World[37][1] = '#';
		World[38][1] = '#';
		World[39][1] = '#';
		World[40][1] = '#';
		World[41][1] = '#';
		World[42][1] = '#';
		World[43][1] = '#';
		// checks if player already has opened the chest
		if (Chest1Flag == 0)
			World[24][8] = '%'; // chest with lightning spell

		World[44][4] = '@'; // bonfire

		/*
		World[39][15] = '@'; // bonefires
		World[34][13] = '^'; // ring of favor
		World[32][12] = '+'; // weapons from bosses
		World[30][11] = '&'; // armor
		*/


	}

	static void InitializeWorld2()
		{
			// sets base template of World
			// coordinates of each corner inside the border - (2,2)(2,19)(78,2)(78,19)
			for (int y=1; y<=20; y++)
			{
				for (int x=1; x<=80; x++)
				{
					if (y == 1 && x%2 != 1) // spaces between top row asteriks
						World[x][y] = ' ';
					else if (y == 1) // top row
						World[x][y] = '*';
					else if (y == 20 && x%2 != 1) // spaces between bottom row asteriks
						World[x][y] = ' ';
					else if (y == 20) // bottom row
						World[x][y] = '*';
					else if (x == 1) // left column
						World[x][y] = '*';
					else if (x == 79) // right column
						World[x][y] = '*';
					else  // non border spaces set to space
						World[x][y] = ' ';
				}
			}

			// makes an announcement when world is initailized and boss is not dead
			if (Boss1Dead == 0)
			{
				AnnouncementActive = 1;
				WhichAnnouncement = 14;
			}

			// entrance to World 3, active when boss is dead
			if (Boss1Dead == 1)
			{
				World[37][1] = '#';
				World[38][1] = '#';
				World[39][1] = '#';
				World[40][1] = '#';
				World[41][1] = '#';
				World[42][1] = '#';
				World[43][1] = '#';

				// Entrance to world 1
				World[37][20] = '#';
				World[38][20] = '#';
				World[39][20] = '#';
				World[40][20] = '#';
				World[41][20] = '#';
				World[42][20] = '#';
				World[43][20] = '#';
			}



	}

	static void InitializeWorld3()
		{
			// sets base template of World
			// coordinates of each corner inside the border - (2,2)(2,19)(78,2)(78,19)
			for (int y=1; y<=20; y++)
			{
				for (int x=1; x<=80; x++)
				{
					if (y == 1 && x%2 != 1) // spaces between top row asteriks
						World[x][y] = ' ';
					else if (y == 1) // top row
						World[x][y] = '*';
					else if (y == 20 && x%2 != 1) // spaces between bottom row asteriks
						World[x][y] = ' ';
					else if (y == 20) // bottom row
						World[x][y] = '*';
					else if (x == 1) // left column
						World[x][y] = '*';
					else if (x == 79) // right column
						World[x][y] = '*';
					else  // non border spaces set to space
						World[x][y] = ' ';
				}
			}

			// entrance to World 4
			World[37][1] = '#';
			World[38][1] = '#';
			World[39][1] = '#';
			World[40][1] = '#';
			World[41][1] = '#';
			World[42][1] = '#';
			World[43][1] = '#';

			// Entrance to world 2
			World[37][20] = '#';
			World[38][20] = '#';
			World[39][20] = '#';
			World[40][20] = '#';
			World[41][20] = '#';
			World[42][20] = '#';
			World[43][20] = '#';

			// Entrance to World 7 OPTIONAL AREA
			World[1][8] = '|';
			World[1][9] = '|';
			World[1][10] = '|';
			World[1][11] = '|';
			World[1][12] = '|';

			World[34][17] = '@'; // bonfire

			// checks if player already opened chest
			if (Chest2Flag == 0)
				World[12][3] = '%'; // chest with holy blast

	}



	static void InitializeWorld4()
		{
			// sets base template of World
			// coordinates of each corner inside the border - (2,2)(2,19)(78,2)(78,19)
			for (int y=1; y<=20; y++)
			{
				for (int x=1; x<=80; x++)
				{
					if (y == 1 && x%2 != 1) // spaces between top row asteriks
						World[x][y] = ' ';
					else if (y == 1) // top row
						World[x][y] = '*';
					else if (y == 20 && x%2 != 1) // spaces between bottom row asteriks
						World[x][y] = ' ';
					else if (y == 20) // bottom row
						World[x][y] = '*';
					else if (x == 1) // left column
						World[x][y] = '*';
					else if (x == 79) // right column
						World[x][y] = '*';
					else  // non border spaces set to space
						World[x][y] = ' ';
				}
			}

			if (Boss2Dead == 0)
			{
				AnnouncementActive = 1;
				WhichAnnouncement = 15;
			}

			if (Boss2Dead == 1)
			{
			// entrance to World 5
			World[37][1] = '#';
			World[38][1] = '#';
			World[39][1] = '#';
			World[40][1] = '#';
			World[41][1] = '#';
			World[42][1] = '#';
			World[43][1] = '#';

			// Entrance to world 3
			World[37][20] = '#';
			World[38][20] = '#';
			World[39][20] = '#';
			World[40][20] = '#';
			World[41][20] = '#';
			World[42][20] = '#';
			World[43][20] = '#';

			World[42][15] = '@';
			}

	}

	static void InitializeWorld5()
		{
			// sets base template of World
			// coordinates of each corner inside the border - (2,2)(2,19)(78,2)(78,19)
			for (int y=1; y<=20; y++)
			{
				for (int x=1; x<=80; x++)
				{
					if (y == 1 && x%2 != 1) // spaces between top row asteriks
						World[x][y] = ' ';
					else if (y == 1) // top row
						World[x][y] = '*';
					else if (y == 20 && x%2 != 1) // spaces between bottom row asteriks
						World[x][y] = ' ';
					else if (y == 20) // bottom row
						World[x][y] = '*';
					else if (x == 1) // left column
						World[x][y] = '*';
					else if (x == 79) // right column
						World[x][y] = '*';
					else  // non border spaces set to space
						World[x][y] = ' ';
				}
			}

				// entrance to World 6
				World[37][1] = '#';
				World[38][1] = '#';
				World[39][1] = '#';
				World[40][1] = '#';
				World[41][1] = '#';
				World[42][1] = '#';
				World[43][1] = '#';

				// Entrance to world 4
				World[37][20] = '#';
				World[38][20] = '#';
				World[39][20] = '#';
				World[40][20] = '#';
				World[41][20] = '#';
				World[42][20] = '#';
				World[43][20] = '#';


			World[44][18] = '@';

		}

	static void InitializeWorld6()
	{
		// sets base template of World
		// coordinates of each corner inside the border - (2,2)(2,19)(78,2)(78,19)
		for (int y=1; y<=20; y++)
		{
			for (int x=1; x<=80; x++)
			{
				if (y == 1 && x%2 != 1) // spaces between top row asteriks
					World[x][y] = ' ';
				else if (y == 1) // top row
					World[x][y] = '*';
				else if (y == 20 && x%2 != 1) // spaces between bottom row asteriks
					World[x][y] = ' ';
				else if (y == 20) // bottom row
					World[x][y] = '*';
				else if (x == 1) // left column
					World[x][y] = '*';
				else if (x == 79) // right column
					World[x][y] = '*';
				else  // non border spaces set to space
					World[x][y] = ' ';
			}
		}

		AnnouncementActive = 1;
		WhichAnnouncement = 16;
	}

		// OPTIONAL BOSS/WORLD
		static void InitializeWorld7()
		{
			// sets base template of World
			// coordinates of each corner inside the border - (2,2)(2,19)(78,2)(78,19)
			for (int y=1; y<=20; y++)
			{
				for (int x=1; x<=80; x++)
				{
					if (y == 1 && x%2 != 1) // spaces between top row asteriks
						World[x][y] = ' ';
					else if (y == 1) // top row
						World[x][y] = '*';
					else if (y == 20 && x%2 != 1) // spaces between bottom row asteriks
						World[x][y] = ' ';
					else if (y == 20) // bottom row
						World[x][y] = '*';
					else if (x == 1) // left column
						World[x][y] = '*';
					else if (x == 79) // right column
						World[x][y] = '*';
					else  // non border spaces set to space
						World[x][y] = ' ';
				}
			}

			if (Boss3Dead == 0)
			{
				AnnouncementActive = 1;
				WhichAnnouncement = 17;
			}

			if (Boss3Dead == 1)
			{
				// Entrance to World 3
				World[79][8] = '|';
				World[79][9] = '|';
				World[79][10] = '|';
				World[79][11] = '|';
				World[79][12] = '|';
				World[72][13] = '@';
				}


	}

}

class Enemy extends GameObject
{
	String Type;
	int Range; // how far an enemy sees the player
	Enemy(String theType)
	{
		Type = theType;
		if (Type.equals("Hollow1"))
		{
			Avatar = 'H';
			HP = 20;
			Attack = 30;
			Armor = 5;
			Range = 6;
			Xpos = 32;
			Ypos = 9;
			World[Xpos][Ypos] = Avatar;
		}
		Type = theType;
		if (Type.equals("Hollow2"))
		{
			Avatar = 'H';
			HP = 20;
			Attack = 30;
			Armor = 5;
			Range = 6;
			Xpos = 28;
			Ypos = 11;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Ent1"))
		{
			Avatar = 'E';
			HP = 30;
			Attack = 25;
			Armor = 3;
			Range = 6;
			Xpos = 18;
			Ypos = 13;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Ent2"))
		{
			Avatar = 'E';
			HP = 30;
			Attack = 25;
			Armor = 3;
			Range = 6;
			Xpos = 20;
			Ypos = 14;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Abyss Attack Dog1"))
		{
			Avatar = 'D';
			HP = 10;
			Attack = 30;
			Armor = 0;
			Range = 8;
			Xpos = 9;
			Ypos = 11;
			World[Xpos][Ypos] = Avatar;

		}
		if (Type.equals("Abyss Attack Dog2"))
		{
			Avatar = 'D';
			HP = 10;
			Attack = 30;
			Armor = 0;
			Range = 8;
			Xpos = 8;
			Ypos = 13;
			World[Xpos][Ypos] = Avatar;

		}
		if (Type.equals("Abyss Attack Dog3"))
		{
			Avatar = 'D';
			HP = 10;
			Attack = 30;
			Armor = 0;
			Range = 8;
			Xpos = 12;
			Ypos = 9;
			World[Xpos][Ypos] = Avatar;

		}
		if (Type.equals("Abyss Attack Dog4"))
		{
			Avatar = 'D';
			HP = 10;
			Attack = 30;
			Armor = 0;
			Range = 8;
			Xpos = 13;
			Ypos = 10;
			World[Xpos][Ypos] = Avatar;

		}
		if (Type.equals("Humanity Phantom1"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 38;
			Ypos = 6;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom2"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 36;
			Ypos = 6;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom3"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 34;
			Ypos = 6;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom4"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 42;
			Ypos = 7;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom5"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 38;
			Ypos = 10;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom6"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 30;
			Ypos = 9;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom7"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 43;
			Ypos = 11;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom8"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 40;
			Ypos = 11;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom9"))
		{
			Avatar = 'Q';
			HP = 40;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 36;
			Ypos = 10;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom10"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 33;
			Ypos = 9;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom11"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 41;
			Ypos = 11;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom12"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 37;
			Ypos = 12;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom13"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 35;
			Ypos = 14;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom14"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 32;
			Ypos = 14;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom15"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 30;
			Ypos = 13;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Humanity Phantom16"))
		{
			Avatar = 'Q';
			HP = 50;
			Attack = 30;
			Armor = 0;
			Range = 6;
			Xpos = 41;
			Ypos = 12;
			World[Xpos][Ypos] = Avatar;
		}

		if (Type.equals("Silver Knight"))
		{
			Avatar = 'K';
			HP = 75;
			Attack = 35;
			Armor = 15;
			Range = 5;
			Xpos = 26;
			Ypos = 10;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Corrupted Abyss Knight"))
		{
			Avatar = 'C';
			HP = 60;
			Attack = 40;
			Armor = 10;
			Range = 5;
			Xpos = 13;
			Ypos = 4;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Guardian1"))
		{
			Avatar = 'G';
			HP = 100;
			Attack = 30;
			Armor = 20;
			Range = 7;
			Xpos = 35;
			Ypos = 3;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Guardian2"))
		{
			Avatar = 'G';
			HP = 100;
			Attack = 30;
			Armor = 20;
			Range = 7;
			Xpos = 45;
			Ypos = 3;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Sanctuary Guardian")) // first boss
		{
			Avatar = 'Z';
			HP = 260;
			Attack = 30;
			Armor = 20;
			Range = 10;
			Xpos = 38;
			Ypos = 6;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Knight Artorias")) // second required boss
		{
			Avatar = 'T';
			HP = 500;
			Attack = 40;
			Armor = 30;
			Range = 10;
			Xpos = 38;
			Ypos = 6;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Black Dragon Kalameet")) // optional boss available after first boss, can be fought after second boss
		{
			Avatar = 'U';
			HP = 580;
			Attack = 60;
			Armor = 40;
			Range = 10;
			Xpos = 36;
			Ypos = 10;
			World[Xpos][Ypos] = Avatar;
		}
		if (Type.equals("Manus, Father of the Abyss")) // Last boss, kill him to complete the game
		{
			Avatar = 'M';
			HP = 780;
			Attack = 60;
			Armor = 35;
			Range = 10;
			Xpos = 38;
			Ypos = 6;
			World[Xpos][Ypos] = Avatar;
		}

	}

	// used in game loop to move enemies
	void MoveRight()
	{
		if (World[Xpos+1][Ypos] == ' ' && (World[Xpos+1][Ypos+1] != '*' || World[Xpos+1][Ypos-1] != '*'))
		{
			World[Xpos][Ypos] = ' ';
			Xpos++;
			World[Xpos][Ypos] = Avatar;
		}
	}

		void MoveLeft()
		{
			if (World[Xpos-1][Ypos] == ' ' && (World[Xpos-1][Ypos+1] != '*' || World[Xpos-1][Ypos-1] != '*'))
			{
				World[Xpos][Ypos] = ' ';
				Xpos--;
				World[Xpos][Ypos] = Avatar;
			}
		}

		void MoveUp()
		{
			if (World[Xpos][Ypos-1] == ' ' && (World[Xpos+1][Ypos-1] != '*' || World[Xpos-1][Ypos-1] != '*'))
			{
				World[Xpos][Ypos] = ' ';
				Ypos--;
				World[Xpos][Ypos] = Avatar;
			}

		}

		void MoveDown()
		{

			if (World[Xpos][Ypos+1] == ' ' && (World[Xpos+1][Ypos+1] != '*' || World[Xpos-1][Ypos+1] != '*'))
			{
				World[Xpos][Ypos] = ' ';
				Ypos++;
				World[Xpos][Ypos] = Avatar;
			}
		}

}



class Player extends GameObject
{
	String Name;
	Player(String theName)
	{
		Name = theName;
		Avatar = 'P';
		HP = 100;
		MaxHP = 100;
		Attack = 20;
		Armor = 20;
		Estus = 4;
		HaveBastardSword = 1;
		HaveDragonSlayerSpear = 0;
		HaveGreatSwordOfArtorias = 0;
		BastardSwordActive = 1;
		DragonSlayerSpearActive = 0;
		GreatSwordOfArtoriasActive = 0;
		HaveLightning = 0;
		HaveHoly = 0;
		HaveHelmet = 0;
		HaveChestArmor = 0;
		HaveLegArmor = 0;
		SpellCharges = 0;
		Xpos=39; Ypos=19;
		World[Xpos][Ypos] = Avatar;

	}

	// used in game loop to move player, comments mainly in MoveRight()
	void MoveRight()
	{
		if (World[Xpos+1][Ypos] == ' ' && (World[Xpos+1][Ypos+1] != '*' || World[Xpos+1][Ypos-1] != '*')) // checks to make sure player can't move out of bounds
		{
			World[Xpos][Ypos] = ' ';
			Xpos++;
			World[Xpos][Ypos] = Avatar;
		}
		else if (World[Xpos+1][Ypos] == '|') // used to move player inbetween world 3 and 7
		{
			GameObject.ChangeExtraWorld();
			RelocatePlayerExtraWorld();

		}
		else if (World[Xpos+1][Ypos] == '%') // used to active chest
		{
			// for making an announcement
			GameObject.Chest();
			// for chest in first world
			if (GameObject.ActiveWorld == 1)
			{
				HaveLightning = 1;
				SpellCharges = 3;
				GameObject.Chest1Flag = 1;

			}
			// for chest in third world
			else if (GameObject.ActiveWorld == 3)
			{
				HaveHoly = 1;
				SpellCharges = 3;
				GameObject.Chest2Flag = 1;
			}
			World[Xpos+1][Ypos] = ' ';
			MoveLeft();

		}
		// ring of favor, dropped from kalameet, gives player more MaxHP, attack power, and armor
		else if (World[Xpos+1][Ypos] == '^')
		{
			GameObject.RingOfFavor();
			MaxHP += 50;
			Attack += 15;
			Armor += 5;
			GameObject.Boss3Dead = 1;
			GameObject.ReloadWorld();
			World[Xpos+1][Ypos] = ' ';
			MoveLeft();
		}
		// for weapon drops from sanctuary guardian and artorias, also levels up the player increasing MaxHP and attack power
		else if (World[Xpos+1][Ypos] == '+')
		{
			GameObject.Weapon();
			if (GameObject.ActiveWorld == 2)
			{
				GameObject.Boss1Dead = 1;
				HaveDragonSlayerSpear = 1;
				GameObject.ReloadWorld();


			}
			else if (GameObject.ActiveWorld == 4)
			{
				GameObject.Boss2Dead = 1;
				HaveGreatSwordOfArtorias = 1;
				GameObject.ReloadWorld();
			}
			MaxHP += 50;
			Attack += 10;
			World[Xpos+1][Ypos] = ' ';
			MoveLeft();
		}
		// armor dropped from silver knight
		else if (World[Xpos+1][Ypos] == '!')
		{
			if (GameObject.HaveHelmet == 0)
				Armor += 5;

			GameObject.Helmet();
			World[Xpos+1][Ypos] = ' ';
		}
		// armor dropped from corrupted abyss knight
		else if (World[Xpos+1][Ypos] == '(')
		{
			if (GameObject.HaveChestArmor == 0)
				Armor += 10;

			GameObject.ChestArmor();
			World[Xpos+1][Ypos] = ' ';
		}
		// armor dropped from guardian
		else if (World[Xpos+1][Ypos] == ')')
		{
			if (GameObject.HaveLegArmor == 0)
				Armor += 5;

			GameObject.LegArmor();
			World[Xpos+1][Ypos] = ' ';
		}
		// for resting at bonfires
		else if (World[Xpos+1][Ypos] == '@')
		{
			HP = MaxHP;
			Estus = 4;
			if (HaveHoly == 1 || HaveLightning == 1)
					SpellCharges = 3;
			GameObject.Bonfire();
			GameObject.ReloadWorld();
			MoveLeft();
			GameObject.ActivatingWorld = 1;
		}

	}

		void MoveLeft()
		{

			if (World[Xpos-1][Ypos] == ' ' && (World[Xpos-1][Ypos+1] != '*' || World[Xpos-1][Ypos-1] != '*'))
			{
				World[Xpos][Ypos] = ' ';
				Xpos--;
				World[Xpos][Ypos] = Avatar;
			}
		else if (World[Xpos-1][Ypos] == '|')
		{
			GameObject.ChangeExtraWorld();
			RelocatePlayerExtraWorld();

		}
		else if (World[Xpos-1][Ypos] == '%')
		{
			GameObject.Chest();
			if (GameObject.ActiveWorld == 1)
			{
				HaveLightning = 1;
				SpellCharges = 3;
				GameObject.Chest1Flag = 1;

			}
			else if (GameObject.ActiveWorld == 3)
			{
				HaveHoly = 1;
				SpellCharges = 3;
				GameObject.Chest2Flag = 1;
			}
			World[Xpos-1][Ypos] = ' ';
			MoveRight();

		}
		else if (World[Xpos-1][Ypos] == '^')
		{
			GameObject.RingOfFavor();
			MaxHP += 50;
			Attack += 15;
			Armor += 5;
			GameObject.Boss3Dead = 1;
			GameObject.ReloadWorld();
			World[Xpos-1][Ypos] = ' ';
			MoveRight();
		}
		else if (World[Xpos-1][Ypos] == '+')
		{
			GameObject.Weapon();
			if (GameObject.ActiveWorld == 2)
			{

				GameObject.Boss1Dead = 1;
				HaveDragonSlayerSpear = 1;
				GameObject.ReloadWorld();


			}
			else if (GameObject.ActiveWorld == 4)
			{
				GameObject.Boss2Dead = 1;
				HaveGreatSwordOfArtorias = 1;
				GameObject.ReloadWorld();
			}
			MaxHP += 50;
			Attack += 10;
			World[Xpos-1][Ypos] = ' ';
			MoveRight();
		}
		else if (World[Xpos-1][Ypos] == '!')
		{
			if (GameObject.HaveHelmet == 0)
				Armor += 5;

			GameObject.Helmet();
			World[Xpos-1][Ypos] = ' ';
		}
		else if (World[Xpos-1][Ypos] == '(')
		{
			if (GameObject.HaveChestArmor == 0)
				Armor += 10;

			GameObject.ChestArmor();
			World[Xpos-1][Ypos] = ' ';
		}
		else if (World[Xpos-1][Ypos] == ')')
		{
			if (GameObject.HaveLegArmor == 0)
				Armor += 5;

			GameObject.LegArmor();
			World[Xpos-1][Ypos] = ' ';
		}
		else if (World[Xpos-1][Ypos] == '@')
		{
			HP = MaxHP;
			Estus = 4;
			if (HaveHoly ==1 || HaveLightning == 1)
					SpellCharges = 3;
			GameObject.Bonfire();
			GameObject.ReloadWorld();
			MoveRight();
			GameObject.ActivatingWorld = 1;
		}

		}


		void MoveUp()
		{

			if (World[Xpos][Ypos-1] == ' ' && (World[Xpos+1][Ypos-1] != '*' || World[Xpos-1][Ypos-1] != '*'))
			{
				World[Xpos][Ypos] = ' ';
				Ypos--;
				World[Xpos][Ypos] = Avatar;
			}
			else if (World[Xpos][Ypos-1] == '#')
			{

				GameObject.ChangeWorldUp();
				RelocatePlayerUp();
			}
			else if (World[Xpos][Ypos-1] == '%')
			{
				GameObject.Chest();
				if (GameObject.ActiveWorld == 1)
				{
					HaveLightning = 1;
					SpellCharges = 3;
					GameObject.Chest1Flag = 1;

				}
				else if (GameObject.ActiveWorld == 3)
				{
					HaveHoly = 1;
					SpellCharges = 3;
					GameObject.Chest2Flag = 1;
				}
				World[Xpos][Ypos-1] = ' ';
				MoveDown();

			}
			else if (World[Xpos][Ypos-1] == '^')
			{
				GameObject.RingOfFavor();
				MaxHP += 50;
				Attack += 15;
				Armor += 5;
				GameObject.Boss3Dead = 1;
				GameObject.ReloadWorld();
				World[Xpos][Ypos-1] = ' ';
				MoveDown();
			}
			else if (World[Xpos][Ypos-1] == '+')
			{
				GameObject.Weapon();
				if (GameObject.ActiveWorld == 2)
				{

					GameObject.Boss1Dead = 1;
					HaveDragonSlayerSpear = 1;
					GameObject.ReloadWorld();


				}
				else if (GameObject.ActiveWorld == 4)
				{
					GameObject.Boss2Dead = 1;
					HaveGreatSwordOfArtorias = 1;
					GameObject.ReloadWorld();
				}
				MaxHP += 50;
				Attack += 10;
				World[Xpos][Ypos-1] = ' ';
				MoveDown();
			}
			else if (World[Xpos][Ypos-1] == '!')
			{
				if (GameObject.HaveHelmet == 0)
					Armor += 5;

				GameObject.Helmet();
				World[Xpos][Ypos-1] = ' ';
			}
			else if (World[Xpos][Ypos-1] == '(')
			{
				if (GameObject.HaveChestArmor == 0)
					Armor += 10;

				GameObject.ChestArmor();
				World[Xpos][Ypos-1] = ' ';
			}
			else if (World[Xpos][Ypos-1] == ')')
			{
				if (GameObject.HaveLegArmor == 0)
					Armor += 5;

				GameObject.LegArmor();
				World[Xpos][Ypos-1] = ' ';
			}
			else if (World[Xpos][Ypos-1] == '@')
			{
				HP = MaxHP;
				Estus = 4;
				if (HaveHoly ==1 || HaveLightning == 1)
					SpellCharges = 3;
				GameObject.Bonfire();
				GameObject.ReloadWorld();
				MoveDown();
				GameObject.ActivatingWorld = 1;
			}


		}

		void MoveDown()
		{
			if (World[Xpos][Ypos+1] == ' ' && (World[Xpos+1][Ypos+1] != '*' || World[Xpos-1][Ypos+1] != '*'))
			{
				World[Xpos][Ypos] = ' ';
				Ypos++;
				World[Xpos][Ypos] = Avatar;
			}
			else if (World[Xpos][Ypos+1] == '#')
			{
				GameObject.ChangeWorldDown();
				RelocatePlayerDown();
			}
			else if (World[Xpos][Ypos+1] == '%')
			{
				GameObject.Chest();
				if (GameObject.ActiveWorld == 1)
				{
					HaveLightning = 1;
					SpellCharges = 3;
					GameObject.Chest1Flag = 1;

				}
				else if (GameObject.ActiveWorld == 3)
				{
					HaveHoly = 1;
					SpellCharges = 3;
					GameObject.Chest2Flag = 1;
				}
				World[Xpos][Ypos+1] = ' ';
				MoveUp();

			}
			else if (World[Xpos][Ypos+1] == '^')
			{
				GameObject.RingOfFavor();
				MaxHP += 50;
				Attack += 15;
				Armor += 5;
				GameObject.Boss3Dead = 1;
				GameObject.ReloadWorld();
				World[Xpos][Ypos+1] = ' ';
				MoveUp();
			}
			else if (World[Xpos][Ypos+1] == '+')
			{
				GameObject.Weapon();
				if (GameObject.ActiveWorld == 2)
				{

					GameObject.Boss1Dead = 1;
					HaveDragonSlayerSpear = 1;
					GameObject.ReloadWorld();


				}
				else if (GameObject.ActiveWorld == 4)
				{
					GameObject.Boss2Dead = 1;
					HaveGreatSwordOfArtorias = 1;
					GameObject.ReloadWorld();
				}
				MaxHP += 50;
				Attack += 10;
				World[Xpos][Ypos+1] = ' ';
				MoveUp();
			}
			else if (World[Xpos][Ypos+1] == '!')
			{
				if (GameObject.HaveHelmet == 0)
					Armor += 5;

				GameObject.Helmet();
				World[Xpos][Ypos+1] = ' ';
			}
			else if (World[Xpos][Ypos+1] == '(')
			{
				if (GameObject.HaveChestArmor == 0)
					Armor += 10;

				GameObject.ChestArmor();
				World[Xpos][Ypos+1] = ' ';
			}
			else if (World[Xpos][Ypos+1] == ')')
			{
				if (GameObject.HaveLegArmor == 0)
					Armor += 5;

				GameObject.LegArmor();
				World[Xpos][Ypos+1] = ' ';
			}
			else if (World[Xpos][Ypos+1] == '@')
			{
				HP = MaxHP;
				Estus = 4;
				if (HaveHoly ==1 || HaveLightning == 1)
					SpellCharges = 3;
				GameObject.Bonfire();
				GameObject.ReloadWorld();
				MoveUp();
				GameObject.ActivatingWorld = 1;
		}



		}






		// used when new world is initialized to place player in correct location when going to next world
		void RelocatePlayerUp()
		{
			if (GameObject.ActiveWorld == 2)
			{
				Xpos = 39;
				Ypos = 18;
				MoveDown(); // after placing player 2 spaces above border, moves them down so they're visible without having to move themselves, otherwise they won't show up initially
			}
			if (GameObject.ActiveWorld == 3)
			{
				Xpos = 39;
				Ypos = 18;
				MoveDown(); // after placing player 2 spaces above border, moves them down so they're visible without having to move themselves, otherwise they won't show up initially
			}
			if (GameObject.ActiveWorld == 4)
			{
				Xpos = 39;
				Ypos = 18;
				MoveDown(); // after placing player 2 spaces above border, moves them down so they're visible without having to move themselves, otherwise they won't show up initially
			}
			if (GameObject.ActiveWorld == 5)
			{
				Xpos = 39;
				Ypos = 18;
				MoveDown(); // after placing player 2 spaces above border, moves them down so they're visible without having to move themselves, otherwise they won't show up initially
			}
			if (GameObject.ActiveWorld == 6)
			{
				Xpos = 39;
				Ypos = 18;
				MoveDown(); // after placing player 2 spaces above border, moves them down so they're visible without having to move themselves, otherwise they won't show up initially
			}
		}

		// used when new world is initialized to place player in correct location when going to previous world
		void RelocatePlayerDown()
		{
			if (GameObject.ActiveWorld == 1)
			{
				Xpos = 39;
				Ypos = 3;
				MoveUp(); // after placing player 2 spaces below border, moves them up so they're visible, as otherwise they aren't until they move themselves
			}
			if (GameObject.ActiveWorld == 2)
			{
				Xpos = 39;
				Ypos = 3;
				MoveUp(); // after placing player 2 spaces below border, moves them up so they're visible, as otherwise they aren't until they move themselves
			}
			if (GameObject.ActiveWorld == 3)
			{
				Xpos = 39;
				Ypos = 3;
				MoveUp(); // after placing player 2 spaces below border, moves them up so they're visible, as otherwise they aren't until they move themselves
			}
			if (GameObject.ActiveWorld == 4)
			{
				Xpos = 39;
				Ypos = 3;
				MoveUp(); // after placing player 2 spaces below border, moves them up so they're visible, as otherwise they aren't until they move themselves
			}
		}

		// used to place character in correct location when moving between extra world, whose entrance is the only one that is to the left or right
		void RelocatePlayerExtraWorld()
		{
			if (GameObject.ActiveWorld == 7)
			{
				Xpos = 77;
				Ypos = 10;
				MoveRight();
			}
			else if (GameObject.ActiveWorld == 3)
			{
				Xpos = 3;
				Ypos = 10;
				MoveLeft();
			}
		}

}
