//Imported for using images
import java.awt.*;
//Imported for using events such as Mouse, Key and Action Listener
import java.awt.event.*;
//Imported in order to use double buffering
import java.awt.Graphics;
//Imported in order to use array lists
import java.util.ArrayList;
//Imported in ordder to use text and audio files
import java.io.*;
//Imported in order to get audio in our algorythm
import sun.audio.*;
 
public class KHMoM extends Frame implements KeyListener, Runnable, ActionListener, MouseListener{
    //The constant occuring background in our program. This changes every state
    Image background;
    //A white background
    Image blankScreen;
    //A logo which appears at the end of the screen
    Image kh3Logo;
    //A heart which appears on top of the character when he dies
    Image floatingHeart;
    //Images that have to do with the user interface of the game
    Image ui, questionMarkIcon, flashIcon, homeRunBlowIcon, homeRunBlowIcon2, lightSeekerIcon, healIcon, stunBulletIcon, stunBulletIcon2;
    //A seperate thread for the program to run in
    Thread animeThread;
    //The toolkit used to import the images
    Toolkit tk = Toolkit.getDefaultToolkit();
    //The subclasses names which will be used in our program
    CharacterStats cs = new CharacterStats();
    Shop shop = new Shop();
    Base base = new Base();
    Food food = new Food();
    GoldCollection gold = new GoldCollection();
    MeleeEnemy me = new MeleeEnemy();
    ChooseWeapon cw = new ChooseWeapon();
    RangedEnemy re = new RangedEnemy();
    FinalBoss fb = new FinalBoss();
    SlideShow credits = new SlideShow();
    SlideShow instructions = new SlideShow();
    LevelUpScreen lus = new LevelUpScreen();
    SaveLoad sl = new SaveLoad();
    EnemyDefeatedScreen eds = new EnemyDefeatedScreen();
    DefeatScreen ds = new DefeatScreen();
    PauseScreen ps = new PauseScreen();
    Conversation begin = new Conversation();
    Conversation end = new Conversation();
    /*The state is the current situation that the user is in within the game.
      state: 1 = Welcome, 2 = Room of Options, 3 = Shop, 4 = Food, 5 = GoldCollection, 6 = MeleeEnemy,
      7 = AnemyHasAppeared interface, 8 = RangedEnemy, 9 = final boss
      10 = credits, 11 = level up, 12 = instructions, 13 = Save/Load, 14 = Enemy Defeated Screen, 15: You have been defeated screen 
      16 = instructions, 17 = story start part, 18 = story end part, 19 = end screen*/
    int state = 1;
    //meleeWeaponChoice: 0 = Bazika(white), 1 = Fallen Shard(green), 2 = Fira(red), 2 = Dark Stick(black), 4 = Last Resort(golden)
    //rangedWeaponChoice: 6 = Terra Torquis(rock), 7 = Corrupt Alloy Bond(metal), 8 = Aqua Catena(Water), 9 = Inferno(Fire), = Shadow Anklet(Dark)
    int meleeWeaponChoice = 0, rangedWeaponChoice = 0;
    //A number which will be used later on. It is initialised here since it will stay the same for a number of different methods
    int meleeOrRanged;
    //This is activated when the user tries to sell all his melee items. 
    //It determines the duration of the error message on the screen.
    long meleeItemTimer;
    //This is activated when the user tries to buy more than 10 items. 
    //It determines the duration of the error message on the screen.
    long itemLimitTimer;
    //This is used as to prevent a bug where the enemies hit the user multiple times in one attack since it was in the same frame.
    //By using a timer to make the enemy not be able to attack more than once in one time frame, we can prevent this from happening.
    long canBeDamagedTimer = System.currentTimeMillis();
    //This is triggerred when the user tries to buy something which is worth more gold than he actually has.
    //It determines how long the error message will appaer on screen.
    long notEnoughGoldTimer;
    //This is triggerred when the user tries to load something faulty.
    //It determines how long the error message will appaer on screen.
    long cannotLoadTimer;
    //These make sure that there is an adequate amount of time between one quote and another 
    //in order for them not to be stacked
    long battleQuoteTimer, rikuDamagedQuoteTimer;
    //These are used to loop the music used in the program when they end
    long mineshaftTheme, mainRoomTheme, battleTheme, foodTheme, finalBossTheme;
    //These are used in the key listener, when a button is pressed it is listed in the keysDown and 
    //removed from the keys up and vice versa when a key is released
    ArrayList<Integer> keysDown;
    ArrayList<Integer> keysUp;
    //Items used in the double bufferring
    Image dbi;
    Graphics dbg;
    //A copy of the shop.booleanOwned in order to update one after the other when 
    //selling items so the items stay on screen for the time being even though they are sold.
    //They are then updated when the user exits the selling section of the shop.
    boolean booleanOwned[] = new boolean[15];
    //This boolean is used so the user cannot hold down the 'SPACE' key and continuously damage the enemy.
    //The user must type in this command repeatedly in order to attack
    boolean canHit = true;
    //At certain points of the game it may be paused, and this is the boolean which determines that
    boolean paused = false; 
    //These are what the character gets as he gains levels. 
    //There are 9 entries since the character starts at level 1 and the max level is 10
    int hpIncrease[] = new int[]{
        0,50,0,0,150,0,150,0,250
    };
    int speedIncrease[] = new int[]{
        0,0,0,1,0,0,0,0,1
    };
    //These are what the enemies gets as the user gains levels. 
    int MeleeEnemySpeedIncrease[] = new int[]{
        0,0,1,0,0,0,1,0,0
    };
    int MeleeEnemyDamageIncrease[] = new int[]{
        0,0,30,0,0,0,140,0,0
    };
    int enemiesHealthIncrease[] = new int[]{
        0,0,150,0,0,0,500,0,0
    };
    int noOfBulletsIncrease[] = new int[]{
        0,0,1,0,0,0,1,0,0
    };
    int rangedEnemyDamageIncrease[] = new int[]{
        0,0,20,0,0,0,30,0,0
    };
    //Thse are how much XP/Gold the user gets at each of his levels. 
    //There are 10 entries since there are 10 levels
    int enemyXP[] = new int[]{
        20,20,20,50,50,50,50,100,100,100,100
    };
    int sheepXP[] = new int[]{
        5,5,5,10,10,10,10,10,25,25
    };
    int enemyGold[] = new int[]{
        1000,1000,1000,2000,2000,2000,2000,5000,5000,5000
    };
    //These are further on displayed in labels in order to tell the user what he has unlocked at each level
    String abilityName[] = new String[]{
        "Flash Unlocked","","Enemies Levelled Up","Stun Bullet Unlocked","","Light Seeker Unlocked","Enemies Levelled Up","Home Run Blow Unlocked","Heal Unlocked"
    };
    //These are audio streams for each part of music.
    AudioStream mineshaftMusic, mainRoomMusic, finalBossMusic, foodRoomMusic, battleRoomMusic;
    /*This particular audio stream is used for the many short parts. 
      They all use the same one as they are usually less than 2 seconds long and they don't loop therefore there is no need
      for a new audio stream for each of these files.*/
    AudioStream sfx;
    //This is used as the 'disk' to be inserted into the audio player and play the sound.
    //There is only 1 as we only need 1 'disk' and change it's contents as we insert it into multiple AudioStreams
    FileInputStream  music;
    //These are booleans used in the butons to determine whether each of the following may be played
    boolean canPlaySFX = true, canPlayMusic = true, canPlayVoices = true;
    Button btnSFX, btnMusic, btnVoices;
    public KHMoM(){
        //start thread
        animeThread = new Thread(this);
        animeThread.start();
        //setting interface
        setTitle("Kingdom Hearts: Mark of Mastery");
        setLayout(null);
        setIconImage(tk.createImage("RoxasKeychain.png"));
        //A floating heart which appears on top of the character when he dies
        floatingHeart = tk.createImage("HeartSprite.gif");
        //Setting up the Images which will be used in the UI(icons and display)
        ui = tk.createImage("UI.png");
        questionMarkIcon = tk.createImage("QuestionMarkIcon.png");
        flashIcon = tk.createImage("Flash.png");
        homeRunBlowIcon = tk.createImage("HomeRunBlowIcon.png");
        homeRunBlowIcon2 = tk.createImage("HomeRunBlowIcon2.png");
        lightSeekerIcon = tk.createImage("LightSeekerIcon.png");
        healIcon = tk.createImage("Heal.png");
        stunBulletIcon = tk.createImage("ShockBulletIcon.png");
        stunBulletIcon2 = tk.createImage("ShockBulletIcon2.png");
        //The blank screen is set
        blankScreen = tk.createImage("BlankScreen.png");
        //The logo is set
        kh3Logo = tk.createImage("Kingdom Hearts 3 logo.gif");
        //Setting up the buttons for not/playing the SFX, Music and Voices
        btnSFX = new Button("SFX");
        btnSFX.setBounds(1073,25,90,25);
        btnSFX.addActionListener(this);
        btnSFX.setVisible(false);
        add(btnSFX);
        
        btnMusic = new Button("Music");
        btnMusic.setBounds(1163,25,90,25);
        btnMusic.addActionListener(this);
        btnMusic.setVisible(false);
        add(btnMusic);
        
        btnVoices = new Button("Voices");
        btnVoices.setBounds(1253,25,90,25);
        btnVoices.addActionListener(this);
        btnVoices.setVisible(false);
        add(btnVoices);
        
        //setting the button pressing keys to an array of integers
        keysDown = new ArrayList<Integer>();
        keysUp = new ArrayList<Integer>();
        //These are done in order to prevent bugs
        //moving
        keysUp.add(87);//w
        keysUp.add(65);//a
        keysUp.add(83);//s
        keysUp.add(68);//d
        //attacking
        keysUp.add(32);//space
        //entering rooms
        keysUp.add(69);//e
        //special abilities
        keysUp.add(81);//q
        keysUp.add(90);//z
        keysUp.add(88);//x
        keysUp.add(70);//f
        
        //setting up the character Stats
        cs.food = cs.maxFood = 25;
        cs.hp = 50;
        cs.maxhp = 50;
        cs.gold = 0;
        cs.speed = cs.defaultSpeed = 5;
        cs.bulletSpeed = 10;
        //The percentage of the damage that is reduced when the character takes damage
        cs.armor = 0;
        
        cs.level = 1;
        cs.xp = 0;
        //This variable represents how much xp is needed to gain a level
        cs.xpForNextLevel = new int[]{
            40, 120, 200, 500, 900, 1400, 2000, 2800, 4000
        };
        //The direction the character is facing. 8 in total
        cs.direction = 3;
        //The amount of owned items th character has. Canot exceed 10
        cs.ownedItems = 1;
        //The amount of unites the character travels when he flashes
        cs.dashRange = 250;
        
        //still = not moving
        cs.upStill = tk.createImage("SoraUpStill.png");
        cs.downStill = tk.createImage("SoraDownStill.png");
        cs.leftStill = tk.createImage("SoraLeftStill.png");
        cs.rightStill = tk.createImage("SoraRightStill.png");
        cs.upLeftStill = tk.createImage("SoraUpLeftStill.png");
        cs.upRightStill = tk.createImage("SoraUpRightStill.png");
        cs.downLeftStill = tk.createImage("SoraDownLeftStill.png");
        cs.downRightStill = tk.createImage("SoraDownRightStill.png");
        //moving images
        cs.upMove = tk.createImage("SoraUpMove.gif");
        cs.downMove = tk.createImage("SoraDownMove.gif");
        cs.leftMove = tk.createImage("SoraLeftMove.gif");
        cs.rightMove = tk.createImage("SoraRightMove.gif");
        cs.upLeftMove = tk.createImage("SoraUpLeftMove.gif");
        cs.upRightMove = tk.createImage("SoraUpRightMove.gif");
        cs.downLeftMove = tk.createImage("SoraDownLeftMove.gif");
        cs.downRightMove = tk.createImage("SoraDownRightMove.gif");
        //attacking images for different weapons with different colours
        cs.upAttack = new Image[5];
        cs.upAttack[0] = tk.createImage("SoraWhiteUpAttack.gif");
        cs.upAttack[1] = tk.createImage("SoraGreenUpAttack.gif");
        cs.upAttack[2] = tk.createImage("SoraRedUpAttack.gif");
        cs.upAttack[3] = tk.createImage("SoraBlackUpAttack.gif");
        cs.upAttack[4] = tk.createImage("SoraGoldUpAttack.gif");
        
        cs.downAttack = new Image[5];
        cs.downAttack[0] = tk.createImage("SoraWhiteDownAttack.gif");
        cs.downAttack[1] = tk.createImage("SoraGreenDownAttack.gif");
        cs.downAttack[2] = tk.createImage("SoraRedDownAttack.gif");
        cs.downAttack[3] = tk.createImage("SoraBlackDownAttack.gif");
        cs.downAttack[4] = tk.createImage("SoraGoldDownAttack.gif");
        
        cs.leftAttack = new Image[5];
        cs.leftAttack[0] = tk.createImage("SoraWhiteLeftAttack.gif");
        cs.leftAttack[1] = tk.createImage("SoraGreenLeftAttack.gif");
        cs.leftAttack[2] = tk.createImage("SoraRedLeftAttack.gif");
        cs.leftAttack[3] = tk.createImage("SoraBlackLeftAttack.gif");
        cs.leftAttack[4] = tk.createImage("SoraGoldLeftAttack.gif");
        
        cs.rightAttack = new Image[5];
        cs.rightAttack[0] = tk.createImage("SoraWhiteRightAttack.gif");
        cs.rightAttack[1] = tk.createImage("SoraGreenRightAttack.gif");
        cs.rightAttack[2] = tk.createImage("SoraRedRightAttack.gif");
        cs.rightAttack[3] = tk.createImage("SoraBlackRightAttack.gif");
        cs.rightAttack[4] = tk.createImage("SoraGoldRightAttack.gif");
        
        cs.upLeftAttack = new Image[5];
        cs.upLeftAttack[0] = tk.createImage("SoraWhiteUpLeftAttack.gif");
        cs.upLeftAttack[1] = tk.createImage("SoraGreenUpLeftAttack.gif");
        cs.upLeftAttack[2] = tk.createImage("SoraRedUpLeftAttack.gif");
        cs.upLeftAttack[3] = tk.createImage("SoraBlackUpLeftAttack.gif");
        cs.upLeftAttack[4] = tk.createImage("SoraGoldUpLeftAttack.gif");
        
        cs.upRightAttack = new Image[5];
        cs.upRightAttack[0] = tk.createImage("SoraWhiteUpRightAttack.gif");
        cs.upRightAttack[1] = tk.createImage("SoraGreenUpRightAttack.gif");
        cs.upRightAttack[2] = tk.createImage("SoraRedUpRightAttack.gif");
        cs.upRightAttack[3] = tk.createImage("SoraBlackUpRightAttack.gif");
        cs.upRightAttack[4] = tk.createImage("SoraGoldUpRightAttack.gif");
        
        cs.downLeftAttack = new Image[5];
        cs.downLeftAttack[0] = tk.createImage("SoraWhiteDownLeftAttack.gif");
        cs.downLeftAttack[1] = tk.createImage("SoraGreenDownLeftAttack.gif");
        cs.downLeftAttack[2] = tk.createImage("SoraRedDownLeftAttack.gif");
        cs.downLeftAttack[3] = tk.createImage("SoraBlackDownLeftAttack.gif");
        cs.downLeftAttack[4] = tk.createImage("SoraGoldDownLeftAttack.gif");
        
        cs.downRightAttack = new Image[5];
        cs.downRightAttack[0] = tk.createImage("SoraWhiteDownRightAttack.gif");
        cs.downRightAttack[1] = tk.createImage("SoraGreenDownRightAttack.gif");
        cs.downRightAttack[2] = tk.createImage("SoraRedDownRightAttack.gif");
        cs.downRightAttack[3] = tk.createImage("SoraBlackDownRightAttack.gif");
        cs.downRightAttack[4] = tk.createImage("SoraGoldDownRightAttack.gif");
        //The image of the character when he dies
        cs.dead = tk.createImage("SoraDeadAnimation.gif");
        //The image played when the user uses heal
        cs.healAnimation = tk.createImage("HealAnimaition.gif");
        //setting the bounds for the attacks and character touching
        //Outer bounds are those that detect whether the user hit the enemy or not
        cs.boundsUpLeft = tk.createImage("SoraBoundsUpLeft.png");
        cs.boundsUp = tk.createImage("SoraBoundsUp.png");
        cs.boundsUpRight = tk.createImage("SoraBoundsUpRight.png");
        cs.boundsLeft = tk.createImage("SoraBoundsLeft.png");
        //This is the bound that the user needs to protect
        cs.boundsMiddle = tk.createImage("SoraBoundsMiddle.png");
        cs.boundsRight = tk.createImage("SoraBoundsRight.png");
        cs.boundsDownLeft = tk.createImage("SoraBoundsDownLeft.png");
        cs.boundsDown = tk.createImage("SoraBoundsDown.png");
        cs.boundsDownRight = tk.createImage("SoraBoundsDownRight.png");
        //The sprites of the images. 
        //This is done in order to be able to access methods from the Sprite class
        cs.charSora = new Sprite (cs.downStill, 630, 320, 85, 93);
        cs.s_boundsUpLeft = new Sprite (cs.boundsUpLeft,0,0,28,29);
        cs.s_boundsUp = new Sprite (cs.boundsUp,0,0,26,29);
        cs.s_boundsUpRight = new Sprite (cs.boundsUpRight,0,0,29,29);
        cs.s_boundsLeft = new Sprite (cs.boundsLeft,0,0,28,45);
        cs.s_boundsMiddle = new Sprite (cs.boundsMiddle,0,0,26,45);
        cs.s_boundsRight = new Sprite (cs.boundsRight,0,0,29,45);
        cs.s_boundsDownLeft = new Sprite (cs.boundsDownLeft,0,0,28,29);
        cs.s_boundsDown = new Sprite (cs.boundsDown,0,0,26,29);
        cs.s_boundsDownRight = new Sprite (cs.boundsDownRight,0,0,29,29);
        //The different types of bullets images
        cs.bulletImage = new Image[5];
        cs.bulletImage[0] = tk.createImage("EarthBullet.png");
        cs.bulletImage[1] = tk.createImage("AlloyBullet.png");
        cs.bulletImage[2] = tk.createImage("WaterBullet.png");
        cs.bulletImage[3] = tk.createImage("BulletSprite.gif");
        cs.bulletImage[4] = tk.createImage("DarkBullet.gif");
        cs.stunBulletImage = tk.createImage("ShockBullet.png");
        //The bullet Sprite. It's image changes depending on the current weapon in hand
        cs.bullet = new CircleSprite(cs.bulletImage[0],100,100,10);
        //This is animation that the character leaves behind him when he flashes
        cs.flashImage = tk.createImage("TwinklingStars.gif");
        cs.flash = new Sprite(cs.flashImage,0,0,37,54);
        //This is the animation that the character makes when Light Seeker is activated
        //There are 2 in order to stack them on top of eachother
        cs.lightSeekerImage = tk.createImage("LightSeeker.gif");
        cs.starExplosionImage = tk.createImage("StarExplosion.gif");
        cs.starExplosion = new Sprite(cs.starExplosionImage,0,0,630,360);
        cs.starExplosion.setVisible(false);
        //The distance that the enemy moves when the user his him with a Home Run Blow
        cs.homerunBlowDistance = 200;
        //The method that makes every bound go in place
        cs.resetBounds();
        
        //setting up the shop items
        shop.name = new String[]{
            "Bazika", "Fallen Shard", "Fira", "Dark Stick", "Last Resort", "Terra Torquis", "Corrupt Alloy Bond", "Aqua Catena", "Inferno", "Shadow Anklet", "Protego Ring", "Charged Aegis", "Skeletal Gauntlet","Flared Bulwark","Celestial Guard" 
        };
        shop.mindmg = new int[]{
            10,15,20,35,50,5,10,15,20,30,0,0,0,0,0
        };
        shop.maxdmg = new int[]{
            15,25,45,60,100,10,15,20,30,40,0,0,0,0,0
        };
        shop.price = new int[]{
            0,15000,35000,50000,75000,10000,30000,50000,70000,95000,10000,20000,35000,55000,70000
        };
        shop.armor = new int[]{
            0,0,0,0,0,0,0,0,0,0,1,2,3,7,9
        };
        //Set the shop Buttons and Labels and Booleans
        shop.btnBack = new Button("BACK");
        shop.btnBack.setBounds(101,605,60,40);
        shop.btnBack.addActionListener(this);
        shop.btnBack.setVisible(false);
        add(shop.btnBack);
        
        shop.btnShop = new Button("SHOP");
        shop.btnShop.setBounds(250,280,380,230);
        shop.btnShop.addActionListener(this);
        shop.btnShop.setVisible(false);
        add(shop.btnShop);
        
        shop.btnSell = new Button("SELL");
        shop.btnSell.setBounds(730,280,380,230);
        shop.btnSell.addActionListener(this);
        shop.btnSell.setVisible(false);
        add(shop.btnSell);
        
        shop.btnMelee = new Button("MELEE WEAPONS");
        shop.btnMelee.setBounds(577,200,170,80);
        shop.btnMelee.addActionListener(this);
        shop.btnMelee.setVisible(false);
        add(shop.btnMelee);
        
        shop.btnRanged = new Button("RANGED WEAPONS");
        shop.btnRanged.setBounds(577,310,170,80);
        shop.btnRanged.addActionListener(this);
        shop.btnRanged.setVisible(false);
        add(shop.btnRanged);
        
        shop.btnArmor = new Button("ARMOR");
        shop.btnArmor.setBounds(577,420,170,80);
        shop.btnArmor.addActionListener(this);
        shop.btnArmor.setVisible(false);
        add(shop.btnArmor);
        
        shop.booleanOwned = new boolean[15];
        shop.booleanMelee =new boolean[15];
        shop.booleanRanged =new boolean[15];
        shop.booleanArmor =new boolean[15];
        
        for(int i = 0; i<15; i++){
            //Only the first item is owned
            if(i >= 1 && i <= 14){
                shop.booleanOwned[i] = false;
            }else{
                shop.booleanOwned[i] = true;
            }
            //The first 5 items are melee itmes
            if(i >= 0 && i <= 4){
                shop.booleanMelee[i] = true;
            }else{
                shop.booleanMelee[i] = false;
            }
            //The second 5 items are ranged items
            if(i >= 5 && i <= 9){
                shop.booleanRanged[i] = true;
            }else{
                shop.booleanRanged[i] = false;
            }
            //The third 5 items are armor items
            if(i >= 10 && i <= 14){
                shop.booleanArmor[i] = true;
            }else{
                shop.booleanArmor[i] = false;
            }
        }
        //Set the copy of boolean owned to equal
        for(int i = 0;i<booleanOwned.length;i++){
            booleanOwned[i] = shop.booleanOwned[i];
        }
        
        shop.lblName = new Label[shop.name.length];
        shop.lblPrice = new Label[shop.name.length];
        shop.lblMindmg = new Label[shop.name.length];
        shop.lblMaxdmg = new Label[shop.name.length];
        shop.lblArmor = new Label[shop.name.length];
        shop.btnBuyItem = new Button[shop.name.length];
        for(int x = 0;x<3;x++){
            for(int i=0;i<5;i++){
                shop.lblName[i+x*5] = new Label(""+shop.name[i+x*5]);
                shop.lblName[i+x*5].setBounds(200,200 + i*73,90,50);
                shop.lblName[i+x*5].setVisible(false);
                add(shop.lblName[i+x*5]);
                
                shop.lblPrice[i+x*5] = new Label(""+shop.price[i+x*5]);
                shop.lblPrice[i+x*5].setBounds(389,(200 + i*73),90,50);
                shop.lblPrice[i+x*5].setVisible(false);
                add(shop.lblPrice[i+x*5]);
                
                shop.lblMindmg[i+x*5] = new Label(""+shop.mindmg[i+x*5]);
                shop.lblMindmg[i+x*5].setBounds(578,200 + i*73,90,50);
                shop.lblMindmg[i+x*5].setVisible(false);
                add(shop.lblMindmg[i+x*5]);
                
                shop.lblMaxdmg[i+x*5] = new Label(""+shop.maxdmg[i+x*5]);
                shop.lblMaxdmg[i+x*5].setBounds(767,200 + i*73,90,50);
                shop.lblMaxdmg[i+x*5].setVisible(false);
                add(shop.lblMaxdmg[i+x*5]);
                
                shop.lblArmor[i+x*5] = new Label(""+shop.armor[i+x*5]);
                shop.lblArmor[i+x*5].setBounds(956,200 + i*73,90,50);
                shop.lblArmor[i+x*5].setVisible(false);
                add(shop.lblArmor[i+x*5]);
                
                shop.btnBuyItem[i+x*5] = new Button("BUY");
                shop.btnBuyItem[i+x*5].setBounds(1087,200 + i*73,90,50);
                shop.btnBuyItem[i+x*5].setVisible(false);
                add(shop.btnBuyItem[i+x*5]);
                shop.btnBuyItem[i+x*5].addActionListener(this);
            }
        }
        
        shop.btnSellItem = new Button[10];
        for(int i=0;i<shop.btnSellItem.length;i++){
            shop.btnSellItem[i] = new Button("SELL");
            shop.btnSellItem[i].setBounds(1087,205 + i*35,90,25);
            shop.btnSellItem[i].setVisible(false);
            add(shop.btnSellItem[i]);
            shop.btnSellItem[i].addActionListener(this);
        }
        
        shop.weaponImage = new Image[15];
        shop.weaponImage[0] = tk.createImage("Bazika.png");
        shop.weaponImage[1] = tk.createImage("FallenShard.png");
        shop.weaponImage[2] = tk.createImage("Fira.png");
        shop.weaponImage[3] = tk.createImage("DarkStick.png");
        shop.weaponImage[4] = tk.createImage("LastResort.png");
        shop.weaponImage[5] = tk.createImage("EarthBullet.png");
        shop.weaponImage[6] = tk.createImage("AlloyBullet.png");
        shop.weaponImage[7] = tk.createImage("WaterBullet.png");
        shop.weaponImage[8] = tk.createImage("FireBullet.png");
        shop.weaponImage[9] = tk.createImage("DarkBullet.png");
        shop.weaponImage[10] = tk.createImage("Protego Ring.png");
        shop.weaponImage[11] = tk.createImage("Charged Aegis.png");
        shop.weaponImage[12] = tk.createImage("Skeletal Gauntlet.png");
        shop.weaponImage[13] = tk.createImage("Flared Bulwark.png");
        shop.weaponImage[14] = tk.createImage("Celestial Guard.png");
        
        shop.meleeVisible = new boolean[5];
        shop.rangedVisible = new boolean[5];
        shop.armorVisible = new boolean[5];
        //Make items invisible
        shop.setInvisibleBooleans();
        
        //The shop Background border
        shop.background = tk.createImage("StarBorder.jpg");
        //The normal background
        base.background = tk.createImage("MainRoom1.png");
        //The background at level 10 which includes a final boss battle
        base.levelTenBackground = tk.createImage("MainRoom2.png");
        //The rectangles(or portals in game) which are used to determine where the user wants to go
        base.mineshaft = new Rectangle(691,234,70,70);
        base.food = new Rectangle(590,234,70,70);
        base.save = new Rectangle(439,396,70,70);
        base.shop = new Rectangle(853,295,70,70);
        base.finalBoss = new Rectangle(691,445,70,70);
        base.enemy = new Rectangle(439,295,70,70);
        base.credits = new Rectangle(590,445, 70, 70);
        base.instructions = new Rectangle(853,396,70,70);
        
        //setting up the sheep images to be displayed as the sheep Sprite
        food.whiteSheepUp = tk.createImage("WhiteSheepUpMove.gif");
        food.whiteSheepLeft = tk.createImage("WhiteSheepLeftMove.gif");
        food.whiteSheepDown = tk.createImage("WhiteSheepDownMove.gif");
        food.whiteSheepRight = tk.createImage("WhiteSheepRightMove.gif");
        food.blackSheepUp = tk.createImage("BlackSheepUpMove.gif");
        food.blackSheepLeft = tk.createImage("BlackSheepLeftMove.gif");
        food.blackSheepDown = tk.createImage("BlackSheepDownMove.gif");
        food.blackSheepRight = tk.createImage("BlackSheepRightMove.gif");
        food.goldenSheepUp = tk.createImage("GoldenSheepUpMove.gif");
        food.goldenSheepLeft = tk.createImage("GoldenSheepLeftMove.gif");
        food.goldenSheepDown = tk.createImage("GoldenSheepDownMove.gif");
        food.goldenSheepRight = tk.createImage("GoldenSheepRightMove.gif");
        //The sheep Sprite which changes it's image depending on the situation
        food.sheep = new Sprite (food.whiteSheepLeft,0,0,70,61);
        //Setting up the bullet's details
        food.bulletsLeft = 40;
        food.bulletSpeed = 10;
        food.bulletImage = tk.createImage("BulletSprite.gif");
        food.bullet = new CircleSprite(food.bulletImage,0,0,10);
        //setting up the cannon's details
        food.cannonFired = false;
        food.cannonSpeed = 6;
        food.cannonStill = tk.createImage("Cannon.png");
        food.cannonMove = tk.createImage("CannonMove.gif");
        food.cannon = new Sprite(food.cannonStill,647,650,51,70);
        //Setting up the pointer's details
        food.pointerSpeed = 6;
        food.pointerImage = tk.createImage("Pointer.png");
        food.pointer = new Sprite(food.pointerImage,0,0,25,25);
        food.setPointerCoordinates(521,1344);
        //Setting up the meat's details
        food.meatSpeed = 4;
        food.collectedMeat = tk.createImage("Meat.png");
        food.meat = new Sprite(food.collectedMeat,0,0,29,31);
        //The background which will be used in this situation
        food.background = tk.createImage("FieldBackground.png");
        //resetting the sheep's position on the screen where it will appear
        food.sheepReset(1334,180);
        //The button to exit the mode
        food.btnBack = new Button("EXIT");
        food.btnBack.setBounds(5,694,45,23);
        food.btnBack.addActionListener(this);
        food.btnBack.setVisible(false);
        add(food.btnBack);
        //Setting the speeds for the bombs and the relics/coins
        gold.bombSpeed = 3;
        gold.relicSpeed = 4;
        //Setting the images used by the bomb sprites
        gold.bombLeft = tk.createImage("BombSpriteLeft.png");
        gold.bombRight = tk.createImage("BombSpriteRight.png");
        //Setting the images used by the coin/relic sprites
        gold.bronzeBlock = tk.createImage("BronzeCoin.png");
        gold.silverBlock = tk.createImage("SilverCoin.png");
        gold.goldBlock = tk.createImage("GoldCoin.png");
        gold.diamondBlock = tk.createImage("DiamondCoin.png");
        gold.platinumBlock = tk.createImage("PlatinumCoin.png");
        gold.rubyBlock = tk.createImage("RubyCoin.png");
        gold.immortalityRelic = tk.createImage("ImmortalityRelic.png");
        gold.speedRelic = tk.createImage("SpeedRelic.png");
        //The animation displayed when the character touches a bomb and it explodes
        gold.bombExplode = tk.createImage("BombExplode.gif");
        //Setting the number of relics at a time
        gold.drop = new CircleSprite[2];
        gold.drop[0] = new CircleSprite(gold.goldBlock, 20, 20, 20);
        gold.drop[1] = new CircleSprite(gold.bronzeBlock, 20, 20, 20);
        gold.dropDirection = new int[2];
        gold.dropState = new int[2];
        //Setting the number of bombs hich will be on the screen
        gold.bomb = new CircleSprite[4];
        for(int i = 0; i<4; i++){
            gold.bomb[i] = new CircleSprite(gold.bombLeft, 50);
        }
        //The direction which the bombs are going
        gold.r = new int[gold.bomb.length];
        //The amount of gold each coin/relic gives
        gold.dropGain = new int[]{
            50,75,150,300,500,1000,0,0
        };
        //The button used to exit the room
        gold.btnBack = new Button("EXIT");
        gold.btnBack.setBounds(5,694,45,23);
        gold.btnBack.addActionListener(this);
        gold.btnBack.setVisible(false);
        add(gold.btnBack);
        //The background for the room
        gold.background = tk.createImage("MineshaftBackGround.jpg");
        //The melee enemy's statistics
        me.defaultEnemySpeed = 3;
        me.hp = me.maxhp = 500;
        me.damage = 20;
        me.name = "Shadow Heartless";
        //The images for the enemy
        me.moveUpLeft = tk.createImage("HeartlessUpLeftMove.gif");
        me.moveDownLeft = tk.createImage("HeartlessDownLeftMove.gif");
        me.moveUpRight = tk.createImage("HeartlessUpRightMove.gif");
        me.moveDownRight = tk.createImage("HeartlessDownRightMove.gif");
        me.attackLeft = tk.createImage("HeartlessAttackLeft.gif");
        me.attackRight = tk.createImage("HeartlessAttackRight.gif");
        me.sinkUpLeft = tk.createImage("HeartlessUpLeftSink.gif");
        me.sinkDownLeft = tk.createImage("HeartlessDownLeftSink.gif");
        me.sinkUpRight = tk.createImage("HeartlessUpRightSink.gif");
        me.sinkDownRight = tk.createImage("HeartlessDownRightSink.gif");
        //The images for the enemy bounds(used for testing purposes)
        me.boundsMiddleImage = tk.createImage("HeartlessBoundsMiddle.png");
        me.boundsLeftImage = tk.createImage("HeartlessBoundsLeft.png");
        me.boundsRightImage = tk.createImage("HeartlessBoundsRight.png");
        //The enemy Sprite
        me.enemy = new Sprite(me.moveUpLeft,200,200,70,120);
        //The enemy bounds Sprites
        me.boundsMiddle = new Sprite(me.boundsMiddleImage,0,0,64,61);
        me.boundsLeft = new Sprite(me.boundsLeftImage,0,0,35,120);
        me.boundsRight = new Sprite(me.boundsRightImage,0,0,35,120);
        //When the enemy is damaged, a little animation appears to show he got hit
        me.damagedStars = new Sprite(tk.createImage("DamageStars.gif"));
        //resetting the bounds to their positions
        me.resetBounds();
        //The background for the room
        me.background = tk.createImage("BattleArena.jpg");
        
        //Setting up the buttons for this state
        cw.btnFight = new Button("Fight");
        cw.btnFight.setBounds(250,400,380,230);
        cw.btnFight.addActionListener(this);
        cw.btnFight.setVisible(false);
        add(cw.btnFight);
        
        cw.btnFlight = new Button("Flight");
        cw.btnFlight.setBounds(730,400,380,230);
        cw.btnFlight.addActionListener(this);
        cw.btnFlight.setVisible(false);
        add(cw.btnFlight);
        
        cw.btnBack = new Button("BACK");
        cw.btnBack.setBounds(5,694,45,23);
        cw.btnBack.addActionListener(this);
        cw.btnBack.setVisible(false);
        add(cw.btnBack);
        
        cw.btnExit = new Button("EXIT");
        cw.btnExit.setBounds(50,694,45,23);
        cw.btnExit.addActionListener(this);
        cw.btnExit.setVisible(false);
        add(cw.btnExit);
        
        cw.btnContinue = new Button("Continue");
        cw.btnContinue.setBounds(622,670,100,50);
        cw.btnContinue.addActionListener(this);
        cw.btnContinue.setVisible(false);
        add(cw.btnContinue);
        
        cw.btnPick = new Button[10];
        for(int x = 0;x<2;x++){
            for(int i=0;i<5;i++){
                cw.btnPick[i+x*5] = new Button("PICK");
                cw.btnPick[i+x*5].setBounds(1087,200 + i*73,90,50);
                cw.btnPick[i+x*5].setVisible(false);
                add(cw.btnPick[i+x*5]);
                cw.btnPick[i+x*5].addActionListener(this);
            }
        }
        //Creating the picked booleans and setting them all to false
        cw.picked = new boolean[10];
        cw.setPickedFalse(0,cw.picked.length);
        //The background for this state
        cw.background = tk.createImage("StarBorder.jpg");
        
        //The ranged enemy's statistics
        re.hp = re.maxhp = 300;
        re.speed = 3;
        re.bulletSpeed = 8;
        re.noOfBullets = 1;
        re.bulletDamage = 17;
        //The images for the ranged enemy
        re.leftMove = tk.createImage("BlueRhapsodyLeftMove.gif");
        re.rightMove = tk.createImage("BlueRhapsodyRightMove.gif");
        re.leftAttack = tk.createImage("BlueRhapsodyLeftAttack.gif");
        re.rightAttack = tk.createImage("BlueRhapsodyRightAttack.gif");
        re.bulletImage = tk.createImage("BlizzardBullet.png");
        //The sprites for the ranged enemy's state
        re.enemy = new Sprite(re.rightMove,200,200,20,36);
        re.bullet = new CircleSprite[3];
        re.bullet[0] = new CircleSprite(re.bulletImage,-10,-10,10);
        re.bullet[1] = new CircleSprite(re.bulletImage,-10,-10,10);
        re.bullet[2] = new CircleSprite(re.bulletImage,-10,-10,10);
        //setting up the arrays to 3(the maximum number of bulles at the end of the game
        //number of bullets starts at 1 in the beginning and ends up 3 at the end.
        re.bulletFired = new boolean[3];
        re.shootBulletTimer = new long[3];
        re.bulletSpeedX = new int[3];
        re.bulletSpeedY = new int[3];
        re.bulletFired = new boolean[3];
        //set them all to not-fired
        re.setBulletFiredFalse();
        //The bounds image and sprite
        re.boundsMiddleImage = tk.createImage("RangedEnemyBoundsMiddle.png");
        re.boundsMiddle = new Sprite(re.boundsMiddleImage,100,100,20,35);
        //When the enemy is damaged, a little animation appears to show he got hit
        re.damagedStars = new Sprite(tk.createImage("DamageStars.gif"));
        //for this state, the same background for the melee enemy was used
        
        //The final boss' statistics
        fb.hp = fb.maxHP = 10000;
        fb.enemySpeed = 7;
        fb.bulletSpeed = 12;
        fb.bulletDamage = 30;
        fb.meleeDamage = 70;
        fb.direction = 3;
        fb.enemySpeedX = 0;
        fb.enemySpeedY = 0;
        //The Images for the enemy when he is not moving
        fb.upStill = tk.createImage("RikuUpStill.png");
        fb.downStill = tk.createImage("RikuDownStill.png");
        fb.leftStill = tk.createImage("RikuLeftStill.png");
        fb.rightStill = tk.createImage("RikuRightStill.png");
        fb.upLeftStill = tk.createImage("RikuUpLeftStill.png");
        fb.upRightStill = tk.createImage("RikuUpRightStill.png");
        fb.downLeftStill = tk.createImage("RikuDownLeftStill.png");
        fb.downRightStill = tk.createImage("RikuDownRightStill.png");
        //The Images for the enemy when he is moving
        fb.upMove = tk.createImage("RikuUpMove.gif");
        fb.downMove = tk.createImage("RikuDownMove.gif");
        fb.leftMove = tk.createImage("RikuLeftMove.gif");
        fb.rightMove = tk.createImage("RikuRightMove.gif");
        fb.upLeftMove = tk.createImage("RikuUpLeftMove.gif");
        fb.upRightMove = tk.createImage("RikuUpRightMove.gif");
        fb.downLeftMove = tk.createImage("RikuDownLeftMove.gif");
        fb.downRightMove = tk.createImage("RikuDownRightMove.gif");
        //The Images for the enemy when he is attacking
        fb.upLeftAttack = tk.createImage("RikuUpLeftAttack.gif");
        fb.upRightAttack = tk.createImage("RikuUpRightAttack.gif");
        fb.downLeftAttack = tk.createImage("RikuDownLeftAttack.gif");
        fb.downRightAttack = tk.createImage("RikuDownRightAttack.gif");
        //Images for when special states the enemy is in
        fb.DMImage = tk.createImage("RikuDMSprite.gif");
        fb.tired = tk.createImage("RikuTired.png");
        fb.teleportImage = tk.createImage("Teleport.gif");
        fb.teleport = new Sprite(fb.teleportImage,0,0,23,44);
        //The enemy bounds Sprites
        fb.boundsUpLeft = new Sprite (tk.createImage("RikuUpLeftBounds.png"),0,0,22,20);
        fb.boundsUp = new Sprite (tk.createImage("RikuUpBounds.png"),0,0,29,20);
        fb.boundsUpRight = new Sprite (tk.createImage("RikuUpRightBounds.png"),0,0,23,20);
        fb.boundsLeft = new Sprite (tk.createImage("RikuLeftBounds.png"),0,0,22,54);
        fb.boundsMiddle = new Sprite (tk.createImage("RikuMiddleBounds.png"),0,0,29,54);
        fb.boundsRight = new Sprite (tk.createImage("RikuRightBounds.png"),0,0,23,54);
        fb.boundsDownLeft = new Sprite (tk.createImage("RikuDownLeftBounds.png"),0,0,22,20);
        fb.boundsDown = new Sprite (tk.createImage("RikuDownBounds.png"),0,0,29,20);
        fb.boundsDownRight = new Sprite (tk.createImage("RikuDownRightBounds.png"),0,0,23,20);
        //The main enemy Sprite
        fb.enemy = new Sprite(fb.downStill,200,200,61,127);
        //The statistics and Images and Sprites for the bullets of the final boss
        fb.bulletImage = tk.createImage("DarkAura.gif");
        fb.bullet = new CircleSprite[11];
        for(int i = 0;i<11;i++){
            fb.bullet[i] = new CircleSprite(fb.bulletImage,1000,1000,10);
        }
        fb.bulletFired = new boolean[11];
        for(int i = 0;i<11;i++){
            fb.bulletFired[i] = false;
        }
        fb.bulletSpeedX = new int[11];
        fb.bulletSpeedY = new int[11];
        for(int i = 0;i<11;i++){
            fb.bulletSpeedX[i] = 0;
            fb.bulletSpeedY[i] = 0;
        }
        //The backgrounds for the enemy.
        //The normalBackground is the one displayed most of the time.
        fb.normalBackground = tk.createImage("FinalBossNormalBackground.jpg");
        /*The DMBackground is the one displayed when the enemy is using
          his special ability or DM(Desperation Move)
          Note: Desperation Move is wording used in Kingdom Hearts whose
          Sprites belong to and this is essentially a 'fan game' by me.*/
        fb.DMBackground = tk.createImage("RikuDMBackground2.jpg");
        
        //The images for the credits slideshow
        credits.img = new Image[21];
        credits.img[0] = tk.createImage("KHMoMLogo.jpg");
        credits.img[1] = base.background;
        credits.img[2] = food.background;
        credits.img[3] = gold.background;
        credits.img[4] = me.background;
        credits.img[5] = tk.createImage("StarBorder.jpg");
        credits.img[6] = tk.createImage("CrownBorder.jpg");
        credits.img[7] = tk.createImage("HeartsBorder.jpg");
        credits.img[8] = fb.normalBackground;
        credits.img[9] = fb.DMBackground;
        credits.img[10] = tk.createImage("HeartlessSprites.png");
        credits.img[11] = tk.createImage("SheepSprites.png");
        credits.img[12] = tk.createImage("BlueRhapsodySprites.png");
        credits.img[13] = tk.createImage("RikuSprites.png");
        credits.img[14] = tk.createImage("SoraSprites.png");
        credits.img[15] = tk.createImage("ArmorItems.png");
        credits.img[16] = tk.createImage("AlbumCover1.jpg");
        credits.img[17] = tk.createImage("AlbumCover2.jpg");
        credits.img[18] = tk.createImage("DancingMadCover.jpg");
        credits.img[19] = tk.createImage("OtherCredits.png");
        credits.img[20] = tk.createImage("WelcomeFinal.png");
        
        //The Button to Exit the slideshow
        credits.btnExit = new Button("EXIT");
        credits.btnExit.setBounds(2,694,45,23);
        credits.btnExit.addActionListener(this);
        credits.btnExit.setVisible(false);
        add(credits.btnExit);
        //The next button to go to next slide/image
        credits.btnNext = new Button("Next");
        credits.btnNext.setBounds(1289,375,55,25);
        credits.btnNext.addActionListener(this);
        credits.btnNext.setVisible(false);
        credits.btnNext.setVisible(false);
        add(credits.btnNext);
        //The previous button to go to previous slide/image
        credits.btnPrevious = new Button("Previous");
        credits.btnPrevious.setBounds(3,375,55,25);
        credits.btnPrevious.addActionListener(this);
        credits.btnPrevious.setVisible(false);
        credits.btnPrevious.setVisible(false);
        add(credits.btnPrevious);
        //The captions for each slide
        credits.caption = new String[credits.img.length];
        credits.caption[0] = "Title Screen by Cory Joseph Wood";
        credits.caption[1] = "Both Main Rooms edited by Daniel Cauchi";
        credits.caption[2] = "Field by Noah Fabri";
        credits.caption[3] = "Mineshaft Background by Stephanie Mullet";
        credits.caption[4] = "Battle Arenas by Benjamin Darmanin";
        credits.caption[5] = "Background Border by Susana Herrera";
        credits.caption[6] = "Background Border by Susana Herrera";
        credits.caption[7] = "Background Border by Susana Herrera";
        credits.caption[8] = "Final Boss Battle Arena by Tamara Lewallen";
        credits.caption[9] = "Final Boss desperation move background edited by Tamara Lewallen and Daniel Cauchi";
        credits.caption[10] = "";
        credits.caption[11] = "Sheep Sprites can be found at: http://www.rpgmakervxace.net/topic/2399-grannys-lists-animal-sprites/";
        credits.caption[12] = "";
        credits.caption[13] = "";
        credits.caption[14] = "";
        credits.caption[15] = "Armor Items by Sam Shartzer";
        credits.caption[16] = "";
        credits.caption[17] = "";
        credits.caption[18] = "Elegaic Nocturne for a Dying Existence - Dancing Mad(Final Boss Music) by Harpistry on Youtube";
        credits.caption[19] = "";
        credits.caption[20] = "Concept Title Screen by Benjamin Abela";
        
        //Setting up the labels which show what theuser has gained
        lus.lblHPIncrease = new Label[hpIncrease.length];
        for(int i = 0;i<lus.lblHPIncrease.length;i++){
            lus.lblHPIncrease[i] = new Label("HP Increase: "+ hpIncrease[i]);
            lus.lblHPIncrease[i].setBounds(525,290,130,50);
            lus.lblHPIncrease[i].setVisible(false);
            add(lus.lblHPIncrease[i]);
        }
        lus.lblSpeedIncrease = new Label[speedIncrease.length];
        for(int i = 0;i<lus.lblSpeedIncrease.length;i++){
            lus.lblSpeedIncrease[i] = new Label("Speed Increase: "+ speedIncrease[i]);
            lus.lblSpeedIncrease[i].setBounds(525,370,130,50);
            lus.lblSpeedIncrease[i].setVisible(false);
            add(lus.lblSpeedIncrease[i]);
        }
        lus.lblAbilityUnlock = new Label[abilityName.length];
        for(int i = 0;i<lus.lblSpeedIncrease.length;i++){
            lus.lblAbilityUnlock[i] = new Label("Special: "+ abilityName[i]);
            lus.lblAbilityUnlock[i].setBounds(525,440,300,50);
            lus.lblAbilityUnlock[i].setVisible(false);
            add(lus.lblAbilityUnlock[i]);
        }
        //The continue button to switch states
        lus.btnContinue = new Button("Continue");
        lus.btnContinue.setBounds(622,667,100,50);
        lus.btnContinue.addActionListener(this);
        lus.btnContinue.setVisible(false);
        add(lus.btnContinue);
        //The background to be used in this state
        lus.background = tk.createImage("CrownBorder.jpg");
        
        //The Save and Load button to write and read the items on the text file
        sl.btnSave = new Button("Save");
        sl.btnSave.setBounds(250,280,380,230);
        sl.btnSave.addActionListener(this);
        sl.btnSave.setVisible(false);
        add(sl.btnSave);
        
        sl.btnLoad = new Button("Load");
        sl.btnLoad.setBounds(730,280,380,230);
        sl.btnLoad.addActionListener(this);
        sl.btnLoad.setVisible(false);
        add(sl.btnLoad);
        //The Exit button
        sl.btnExit = new Button("Exit");
        sl.btnExit.setBounds(101,612,60,40);
        sl.btnExit.addActionListener(this);
        sl.btnExit.setVisible(false);
        add(sl.btnExit);
        //the background to be used in this state
        sl.background = tk.createImage("HeartsBorder.jpg");
        
        //The labels to show the user what h has gained
        eds.lblXpGain = new Label[10];
        for(int i = 0;i<eds.lblXpGain.length;i++){
            eds.lblXpGain[i] = new Label("XP Gained: "+ enemyXP[i]);
            eds.lblXpGain[i].setBounds(570,250,130,50);
            eds.lblXpGain[i].setVisible(false);
            add(eds.lblXpGain[i]);
        }
        eds.lblGoldGain = new Label[10];
        for(int i = 0;i<eds.lblGoldGain.length;i++){
            eds.lblGoldGain[i] = new Label("Gold Gained: "+ enemyGold[i]);
            eds.lblGoldGain[i].setBounds(570,320,130,50);
            eds.lblGoldGain[i].setVisible(false);
            add(eds.lblGoldGain[i]);
        }
        //The continue button to switch states
        eds.btnContinue = new Button("Continue");
        eds.btnContinue.setBounds(622,667,100,50);
        eds.btnContinue.addActionListener(this);
        eds.btnContinue.setVisible(false);
        add(eds.btnContinue);
        //The background to be used in this stat
        eds.background = tk.createImage("CrownBorder.jpg");
        
        //The Continue button to continue from before when the user died
        //with he exception that now a death has been added
        ds.btnContinue = new Button("Continue");
        ds.btnContinue.setBounds(607,450,130,40);
        ds.btnContinue.addActionListener(this);
        ds.btnContinue.setVisible(false);
        add(ds.btnContinue);
        //The Load button to load the state where the user last saved
        //with he exception that now a death has been added
        ds.btnLoad = new Button("Load");
        ds.btnLoad.setBounds(607,510,130,40);
        ds.btnLoad.addActionListener(this);
        ds.btnLoad.setVisible(false);
        add(ds.btnLoad);
        //The background for this state
        ds.background = tk.createImage("HeartsBorder.jpg");
        
        //The button to continue with the game
        ps.btnContinue = new Button("Continue");
        ps.btnContinue.setBounds(607,510,130,40);
        ps.btnContinue.addActionListener(this);
        ps.btnContinue.setVisible(false);
        add(ps.btnContinue);
        //The background for the pause screen
        ps.background = tk.createImage("BlankScreen.png");
        
        //The images for the instructions slideshow
        instructions.img = new Image[11];
        instructions.img[0] = tk.createImage("Instruction1.png");//Show main room
        instructions.img[1] = tk.createImage("Instruction2.png");//Food Room 1
        instructions.img[2] = tk.createImage("Instruction3.png");//Food Room 2
        instructions.img[3] = tk.createImage("Instruction4.png");//Main Room
        instructions.img[4] = tk.createImage("Instruction5.png");//Mineshaft
        instructions.img[5] = tk.createImage("Instruction6.png");//Coin Gains
        instructions.img[6] = tk.createImage("Instruction7.png");//Main Room
        instructions.img[7] = tk.createImage("Instruction8.png");//Battlefield 1
        instructions.img[8] = tk.createImage("Instruction9.png");//Battlefield 2
        instructions.img[9] = tk.createImage("Instruction10.png");//Battlefield 3
        instructions.img[10] = tk.createImage("Instruction11.png");//Abilities
        
        //The Button to Exit the slideshow
        instructions.btnExit = new Button("EXIT");
        instructions.btnExit.setBounds(2,694,45,23);
        instructions.btnExit.addActionListener(this);
        instructions.btnExit.setVisible(false);
        add(instructions.btnExit);
        //The next button to go to next slide/image
        instructions.btnNext = new Button("Next");
        instructions.btnNext.setBounds(1289,375,55,25);
        instructions.btnNext.addActionListener(this);
        instructions.btnNext.setVisible(false);
        instructions.btnNext.setVisible(false);
        add(instructions.btnNext);
        //The next button to go to previous slide/image
        instructions.btnPrevious = new Button("Previous");
        instructions.btnPrevious.setBounds(3,375,55,25);
        instructions.btnPrevious.addActionListener(this);
        instructions.btnPrevious.setVisible(false);
        instructions.btnPrevious.setVisible(false);
        add(instructions.btnPrevious);
        
        //The character images(backgrounds in this case)
        begin.characterImage = new Image[2];
        begin.characterImage[0] = tk.createImage("SoraSpeaking.png");
        begin.characterImage[1] = tk.createImage("YenSidSpeaking.png");
        //The sentences to be displayed in each label
        begin.sentence = new String[8];
        begin.sentence[0] = "Welcome Sora to your second Mark of Mastery exam.";
        begin.sentence[1] = "As you failed to pass the first one, you will be given a different trial.";
        begin.sentence[2] = "You will be placed in a realm in which you must survive until the end.";
        begin.sentence[3] = "Throughout the test you will gain levels, and at the maximum level which is 10, you will be given your final task.";
        begin.sentence[4] = "Complete this, and you will be a Keyblade Master.";
        begin.sentence[5] = "Understood?";
        begin.sentence[6] = "Yes, master.";
        begin.sentence[7] = "Very well, we shall proceed with the full instructions of the trial then...";
        //The labels to be displayed as speech quotes
        begin.lblSentence = new Label[begin.sentence.length];
        for(int i = 0;i<begin.lblSentence.length;i++){
            begin.lblSentence[i] = new Label("");
            begin.lblSentence[i].setBounds(360,600,624,50);
            begin.lblSentence[i].setVisible(false);
            add(begin.lblSentence[i]);
        }
        //The boolean to determine whether the sentence has finished loading to the screen
        begin.sentenceRolled = new int[begin.sentence.length];
        begin.setEverySentenceToRolled();
        //The continue button
        begin.btnContinue = new Button("Continue");
        begin.btnContinue.setBounds(622,667,100,50);
        begin.btnContinue.addActionListener(this);
        begin.btnContinue.setVisible(false);
        add(begin.btnContinue);
        
        //The character images(backgrounds in this case)
        end.characterImage = new Image[3];
        end.characterImage[0] = tk.createImage("SoraSpeaking2.png");
        end.characterImage[1] = tk.createImage("YenSidSpeaking.png");
        end.characterImage[2] = tk.createImage("Crew Congratulations.png");
        
        //The sentences to be displayed in each label
        end.sentence = new String[7];
        end.sentence[0] = "I DID IT!";
        end.sentence[1] = "Congratulations Sora!";
        end.sentence[2] = "Only now, Sora, have you truly become a Keyblade Master.";
        end.sentence[3] = "However, there is no time to lose.";
        end.sentence[4] = "Xehanort is harming the worlds as we speak.";
        end.sentence[5] = "You must go on your final journey to save the worlds and bring an end to Xehanort.";
        end.sentence[6] = "Good luck to you all.";
        //The labels to be displayed as speech quotes
        end.lblSentence = new Label[begin.sentence.length];
        for(int i = 0;i<end.lblSentence.length;i++){
            end.lblSentence[i] = new Label("");
            end.lblSentence[i].setBounds(360,600,624,50);
            end.lblSentence[i].setVisible(false);
            add(end.lblSentence[i]);
        }
        //The boolean to determine whether the sentence has finished loading to the screen
        end.sentenceRolled = new int[begin.sentence.length];
        end.setEverySentenceToRolled();
        //The continue button
        end.btnContinue = new Button("Continue");
        end.btnContinue.setBounds(622,667,100,50);
        end.btnContinue.addActionListener(this);
        end.btnContinue.setVisible(false);
        add(end.btnContinue);
        
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        addMouseListener(this);
    }
    
    //set the cannon to the middle of the screen
    void resetCannonPositions(){
        food.cannon.moveTo(647,650);
    }
    
    //The method to write in the "SaveFile.txt."
    void save(){
        try{
            FileWriter f= new FileWriter("Save File.txt");
            
            f.write(cs.maxhp+"\r\n");
            f.write(cs.defaultSpeed+"\r\n");
            f.write(cs.level+"\r\n");
            f.write(cs.xp+"\r\n");
            f.write(cs.gold+"\r\n");
            f.write(cs.food+"\r\n");
            f.write(cs.armor+"\r\n");
            f.write(cs.deathCounter+"\r\n");
            
            f.write(meleeWeaponChoice+"\r\n");
            f.write(rangedWeaponChoice+"\r\n");
            
            for(int k = 0; k<shop.booleanOwned.length;k++){
                f.write(shop.booleanOwned[k]+"\r\n");
            }
            
            f.write(me.maxhp+"\r\n");
            f.write(me.damage+"\r\n");
            f.write(me.enemySpeed+"\r\n");
            
            f.write(re.noOfBullets+"\r\n");
            f.write(re.bulletDamage+"\r\n");
            f.write(re.maxhp+"\r\n");
            
            f.close();
        }catch(Exception r){
        }
    }
    
    //The method to read from the "SaveFile.txt"
    void load(){
        try{
            BufferedReader f = new BufferedReader(new FileReader("Save File.txt"));
            
            String line = f.readLine();
            cs.maxhp = Integer.parseInt(line);
            cs.hp = cs.maxhp;
            line = f.readLine();
            cs.defaultSpeed = Integer.parseInt(line);
            line = f.readLine();
            cs.level = Integer.parseInt(line);
            line = f.readLine();
            cs.xp = Integer.parseInt(line);
            line = f.readLine();
            cs.gold = Integer.parseInt(line);
            line = f.readLine();
            cs.food = Integer.parseInt(line);
            line = f.readLine();
            cs.armor = Integer.parseInt(line);
            line = f.readLine();
            cs.deathCounter = Integer.parseInt(line);
            
            line = f.readLine();
            meleeWeaponChoice = Integer.parseInt(line);
            line = f.readLine();
            rangedWeaponChoice = Integer.parseInt(line);
            
            for(int k = 0; k<booleanOwned.length;k++){
                line = f.readLine();
                shop.booleanOwned[k] = Boolean.parseBoolean(line);
                booleanOwned[k] = Boolean.parseBoolean(line);
            }
            
            line = f.readLine();
            me.maxhp = Integer.parseInt(line);
            line = f.readLine();
            me.damage = Integer.parseInt(line);
            line = f.readLine();
            me.enemySpeed = Integer.parseInt(line);
            line = f.readLine();
            re.noOfBullets = Integer.parseInt(line);
            line = f.readLine();
            re.bulletDamage = Integer.parseInt(line);
            line = f.readLine();
            re.maxhp = Integer.parseInt(line);
            
            f.close();
            ds.hideButtons();
        }catch(Exception r){
            cannotLoadTimer = System.currentTimeMillis();
        }
    }
    //Stop all the players currently playing music
    void stopMusic(){
        try{
            AudioPlayer.player.stop(mineshaftMusic);
            AudioPlayer.player.stop(mainRoomMusic);
            AudioPlayer.player.stop(finalBossMusic);
            AudioPlayer.player.stop(foodRoomMusic);
            AudioPlayer.player.stop(battleRoomMusic);
        }catch (Exception e){
            System.err.println(e);
        }
    }
    //stop all the players currently playing voices and SFX
    void stopSFX(){
        try{
            //don't try and do things with a null object!
            if (sfx != null){
                AudioPlayer.player.stop(sfx);
            }
        }catch (Exception e){
            System.err.println(e);
        }
    }
    //Play the music played during the mineshaft state
    void playMineshaft(){
        if(canPlayMusic == true){
            mineshaftTheme = System.currentTimeMillis();
            try{
                music = new FileInputStream("vouchers.wav");
                mineshaftMusic = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(mineshaftMusic);
        }
    }
    //Play the music played during most states
    void playMainRoom(){
        if(canPlayMusic == true){
            mainRoomTheme = System.currentTimeMillis();
            try{
                music = new FileInputStream("Free.wav");
                mainRoomMusic = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(mainRoomMusic);
        }
    }
    //Play the music played during the final boss battle
    void playFinalBoss(){
        if(canPlayMusic == true){
            try{
                finalBossTheme = System.currentTimeMillis();
                music = new FileInputStream("Elegaic Nocturne for a Dying Existence - Dancing Mad.wav");
                finalBossMusic = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(finalBossMusic);
        }
    }
    //Play the music played during the battle against normal monsters
    void playBattleMusic(){
        if(canPlayMusic == true){
            battleTheme = System.currentTimeMillis();
            try{
                music = new FileInputStream("03 The Doors of Perception (Battle Music).wav");
                battleRoomMusic = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(battleRoomMusic);
        }
    }
    //Play the music played during the food collection/field state
    void playFoodMusic(){
        if(canPlayMusic == true){
            foodTheme = System.currentTimeMillis();
            try{
                music = new FileInputStream("05 A Cup of Tea on a Wednesday (ii).wav");
                foodRoomMusic = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(foodRoomMusic);
        }
    }
    //Play one of the following battle quotes randomly
    //There is a timer in order not to stack these together
    void playBattleQuote(){
        if(canPlayVoices == true){
            try{
                int i = (int)(Math.random()*6);
                switch(i){
                    case 0:
                        music = new FileInputStream("BattleQuote1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("BattleQuote2.wav");
                    break;
                    case 2:
                        music = new FileInputStream("BattleQuote3.wav");
                    break;
                    case 3:
                        music = new FileInputStream("BattleQuote4.wav");
                    break;
                    case 4:
                        music = new FileInputStream("BattleQuote5.wav");
                    break;
                    case 5:
                        music = new FileInputStream("BattleQuote6.wav");
                    break;
                    case 6:
                        music = new FileInputStream("BattleQuote7.wav");
                    break;
                }
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play one of the following heal ability quotes randomly
    void playHealQuote(){
        if(canPlayVoices == true){
            try{
                int i = (int)(Math.random()*1);
                switch(i){
                    case 0:
                        music = new FileInputStream("HealQuote1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("HealQuote2.wav");
                    break;
                }
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play one of the following special ability quotes
    void playLightSeekerQuote(){
        if(canPlayVoices == true){
            try{
                music = new FileInputStream("LightSeekerSound.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
            try{
                int i = (int)(Math.random()*4);
                switch(i){
                    case 0:
                        music = new FileInputStream("LightSeekerQuote1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("LightSeekerQuote2.wav");
                    break;
                    case 2:
                        music = new FileInputStream("LightSeekerQuote3.wav");
                    break;
                    case 3:
                        music = new FileInputStream("LightSeekerQuote4.wav");
                    break;
                    case 4:
                        music = new FileInputStream("LightSeekerQuote5.wav");
                    break;
                }
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play the death quote
    void playDeathQuote(){
        if(canPlayVoices == true){
            try{
                music = new FileInputStream("DeathQuote.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play one of the following quotes when geting hit by an enemy
    void playDamagedQuote(){
        if(canPlayVoices == true){
            try{
                int i = (int)(Math.random()*4);
                switch(i){
                    case 0:
                        music = new FileInputStream("DamagedQuote1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("DamagedQuote2.wav");
                    break;
                    case 2:
                        music = new FileInputStream("DamagedQuote3.wav");
                    break;
                    case 3:
                        music = new FileInputStream("DamagedQuote4.wav");
                    break;
                    case 4:
                        music = new FileInputStream("DamagedQuote5.wav");
                    break;
                }
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this sound effect when a coin is collected
    void playCoinCollected(){
        if(canPlaySFX == true){
            try{
                music = new FileInputStream("MeatCollected.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this sound effect when the user hits a bomb
    void playExplosion(){
        if(canPlaySFX == true){
            try{
                int i = (int)(Math.random()*3);
                switch(i){
                    case 0:
                        music = new FileInputStream("Explosion1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("Explosion2.wav");
                    break;
                    case 2:
                        music = new FileInputStream("Explosion3.wav");
                    break;
                    case 3:
                        music = new FileInputStream("Explosion4.wav");
                    break;
                }
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this sound effect after the user defeats an enemy
    //and the items that he gained are displayed(like xp and gold)
    void playItemGain(){
        if(canPlaySFX == true){
            try{
                music = new FileInputStream("ItemGet.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this effect when a button is pressed and the input is accepted
    void playButtonSound(){
        if(canPlaySFX == true){
            try{
                music = new FileInputStream("ButtonClick.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this effect when a button is pressed and the input is rejected
    //Example: Trying to buy something you don't have the money for
    void playErrorButtonSound(){
        if(canPlaySFX == true){
            try{
                music = new FileInputStream("ErrorButton.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this effect when a bullet si fired
    void playCannonExplosion(){
        if(canPlaySFX == true){
            try{
                music = new FileInputStream("CannonExplosion.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this effect when a bullet hits a sheep
    void playBulletHit(){
        if(canPlaySFX == true){
            try{
                music = new FileInputStream("BulletHit.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //play this effect when a piece of meat is collected by the player
    void playMeatGain(){
        if(canPlaySFX == true){
            try{
                music = new FileInputStream("MeatCollected.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this effect when the user levels up and the level up screen is displayed
    void playLevelUp(){
        if(canPlaySFX == true){
            try{
                music = new FileInputStream("LevelUp.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this effect when the enmy takes damage
    //There is a timer for this in order for them not to stack up
    void playRikuDamagedQuote(){
        if(canPlayVoices == true){
            try{
                int i = (int)(Math.random()*4);
                switch(i){
                    case 0:
                        music = new FileInputStream("RikuDamagedQuote1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("RikuDamagedQuote2.wav");
                    break;
                    case 2:
                        music = new FileInputStream("RikuDamagedQuote3.wav");
                    break;
                    case 3:
                        music = new FileInputStream("RikuDamagedQuote4.wav");
                    break;
                    case 4:
                        music = new FileInputStream("RikuDamagedQuote5.wav");
                    break;
                }
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this when the final boss is defeated
    void playRikuDeathQuote(){
        if(canPlayVoices == true){
            try{
                int i = (int)(Math.random()*1);
                switch(i){
                    case 0:
                        music = new FileInputStream("RikuDeathQuote1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("RikuDeathQuote2.wav");
                    break;
                }
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this quote when the player defeats a normal enemy
    void playEnemyDefeated(){
        if(canPlayVoices == true){
            try{
                music = new FileInputStream("EnemyDefeated.wav");
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    //Play this effect when an enemy is hit
    void playKeybladeHit(){
        if(canPlaySFX == true){
            try{
                int i = (int)(Math.random()*3);
                switch(i){
                    case 0:
                        music = new FileInputStream("KeybladeHit1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("KeybladeHit2.wav");
                    break;
                    case 2:
                        music = new FileInputStream("KeybladeHit3.wav");
                    break;
                    case 3:
                        music = new FileInputStream("KeybladeHit4.wav");
                    break;
                }
                sfx = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(sfx);
        }
    }
    
    //When the user levels up, increase his and the enemy's stats
    void applyLevelUp(){
        cs.defaultSpeed = cs.speed += speedIncrease[cs.level-1];
        cs.maxhp += hpIncrease[cs.level-1];
        me.enemySpeed += MeleeEnemySpeedIncrease[cs.level-1];
        me.damage += MeleeEnemyDamageIncrease[cs.level-1];
        re.noOfBullets += noOfBulletsIncrease[cs.level-1];
        re.bulletDamage += rangedEnemyDamageIncrease[cs.level-1];
        me.maxhp += enemiesHealthIncrease[cs.level-1];
        re.maxhp += enemiesHealthIncrease[cs.level-1];
        cs.level++;
    }
    //Set the labels position for when the user wants to buy in the shop state
    void resetLabelsPositions(){
        for(int x = 0;x<3;x++){
            for(int i=0;i<5;i++){
                shop.lblName[i+x*5].setBounds(190,200 + i*73,110,50);
                shop.lblPrice[i+x*5].setBounds(379,(200 + i*73),110,50);
                shop.lblMindmg[i+x*5].setBounds(568,200 + i*73,110,50);
                shop.lblMaxdmg[i+x*5].setBounds(757,200 + i*73,110,50);
                shop.lblArmor[i+x*5].setBounds(946,200 + i*73,110,50);
            }
        }
    }
    //Set the labels position for when the user wants to sell in the shop state
    void sellingLabelsPositions(){
        for(int i = 0,counter = 0;i<15;i++){
            if(shop.booleanOwned[i] == true){
                shop.lblName[i].setBounds(190,200 + counter*35,110,30);
                shop.lblPrice[i].setBounds(379,(200 + counter*35),110,30);
                shop.lblMindmg[i].setBounds(568,200 + counter*35,110,30);
                shop.lblMaxdmg[i].setBounds(757,200 + counter*35,110,30);
                shop.lblArmor[i].setBounds(946,200 + counter*35,110,30);
                counter++;
            }
        }
    }
    //Set the labels position for when the user wants to pick one of his melee items
    //in the weapon selection screen before fighting an enemy
    void meleePickingLabelsPositions(){
        for(int i = 0,counter = 0;i<15;i++){
            if(shop.booleanOwned[i] == true && shop.booleanMelee[i] == true){
                shop.lblName[i].setBounds(200,235 + counter*70,110,30);
                shop.lblMindmg[i].setBounds(389,235 + counter*70,110,30);
                shop.lblMaxdmg[i].setBounds(578,235 + counter*70,110,30);
                shop.lblArmor[i].setBounds(767,235 + counter*70,110,30);
                cw.btnPick[i].setBounds(1087,225 + counter*70,90,50);
                counter++;
            }
        }
    }
    //Set the labels position for when the user wants to pick one of his ranged items
    //in the weapon selection screen before fighting an enemy
    void rangedPickingLabelsPositions(){
        for(int i = 0,counter = 0;i<15;i++){
            if(shop.booleanOwned[i] == true && shop.booleanRanged[i] == true){
                shop.lblName[i].setBounds(200,235 + counter*70,130,30);
                shop.lblMindmg[i].setBounds(389,235 + counter*70,130,30);
                shop.lblMaxdmg[i].setBounds(578,235 + counter*70,130,30);
                shop.lblArmor[i].setBounds(767,235 + counter*70,130,30);
                cw.btnPick[i].setBounds(1087,225 + counter*70,90,50);
                counter++;
            }
        }
    }
    //Stop everything for a set amount of time(the parameter)
    void delay(int ms){
        long time = System.currentTimeMillis();
        while((System.currentTimeMillis()-time)<ms){
            repaint();
            try{
                Thread.sleep(17);
            }catch(Exception e){
            }
        }
    }
    //reset the positions of the bombs
    void setForGoldState(){
        for(int i=0;i<gold.bomb.length;i++){
            gold.bomb[i].setImage(gold.bombLeft);
        }
        gold.bomb[0].moveTo(0,0);
        gold.bomb[1].moveTo(1234,0);
        gold.bomb[2].moveTo(0,593);
        gold.bomb[3].moveTo(1234,593);
    }
    //reset the character's(Sora) default position, speed, get him to max health
    //and reset his image.
    void resetSoraPositions(){
        cs.charSora = new Sprite (cs.downStill, 630, 320, 85, 93);
        cs.bullet.moveTo((cs.s_boundsMiddle.getX()+(cs.s_boundsMiddle.getWidth()/2)-cs.bullet.radius),cs.s_boundsMiddle.getY()+cs.s_boundsMiddle.getHeight()/2-cs.bullet.radius);
        cs.resetBounds();
        cs.speed = cs.defaultSpeed;
        cs.hp = cs.maxhp;
        cs.bulletFired = false;
    }
    //rset the melee enemy's positions and get him to max hp
    void resetMeleeEnemyPositions(){
        me.enemy = new Sprite(me.moveDownRight,200,200,70,120);
        me.resetBounds();
        me.hp = me.maxhp;
    }
    //reset the ranged enemy's positions and get him to max hp
    void resetRangedEnemyPositions(){
        re.enemy = new Sprite(re.leftMove,200,200,20,36);
        re.hp = re.maxhp;
    }
    //reset he final boss' position, get him to max hp
    //and reset his AI
    void resetFinalBossPositions(){
        fb.hp = fb.maxHP;
        fb.enemy = new Sprite(fb.downRightStill,200,200);
        fb.resetBounds();
        fb.firstDM = true;
        fb.firstTime = true;
    }
    //The opening image(Welcome) is displayed for 2 seconds
    //Then the buttons concerning sound are displayed 
    //and the player is set into the main room
    void Welcome(){
        //setting the 1st image to be the background
        background = tk.createImage("KHMoMLogo.jpg");
        delay(4000);
        playMainRoom();
        btnVoices.setVisible(true);
        btnSFX.setVisible(true);
        btnMusic.setVisible(true);
        begin.btnContinue.setVisible(true);
        begin.lblSentence[0].setVisible(true);
        state = 17;
    }
    
    //the runnable method
    //The main method where everything plays out
    public void run(){
        //Continuously loop this method
        while(true){
            //The thread runs every 17 milli seconds
            try{
                Thread.sleep(17);
            }catch(Exception e){
            }
            
            switch(state){
                //Welcoming state
                case 1:
                    //Do the welcoming screen and then move on with the game
                    Welcome();
                break;
                //Main Room state
                case 2:
                    //If the player runs out of food, he dies
                    if(cs.food<0){
                        stopMusic();
                        cs.charSora.moveTo(650, 340);
                        cs.charSora.setImage(cs.dead);
                        ds.displayButtons();
                        cs.deathCounter++;
                        //Set the screen to the death screen
                        state = 15;
                    }
                    //Loop the music that plays in this state
                    if(System.currentTimeMillis()- mainRoomTheme > 154000){
                        playMainRoom();
                    }
                    //setting the background image of this room
                    if(cs.level == 10){
                        background = base.levelTenBackground;
                    }else{
                        background = base.background;
                    }
                    //Show the flash animation
                    cs.flashVisible();
                    //Move the bullet
                    cs.moveBullet(1354,730);
                    //If the player owns a ranged item, display it's image
                    boolean allow = false;
                    for(int i = 5;i<10;i++){
                        if(shop.booleanOwned[i] == true){
                            allow = true;
                        }
                    }
                    if(allow){
                        if(cs.stunBullet == false){
                            cs.bullet.setImage(cs.bulletImage[rangedWeaponChoice-5]);
                        }
                    }
                break;
                //Shop state
                case 3:
                    //Set the background to this room's and loop the music
                    background = shop.background;
                    if(System.currentTimeMillis()- mainRoomTheme > 154000){
                        playMainRoom();
                    }
                break;
                //Field/food collection state
                case 4:
                    //Loop the music for this room
                    if(System.currentTimeMillis()- foodTheme > 235000){
                        playFoodMusic();
                    }
                    //Stay on this state unless the player runs out of bullets
                    if(food.bulletsLeft > 0 || food.meat.visible == true || (food.cannonFired == true && food.meat.visible == false)){
                        //Set the background to this room's and loop the music
                        background = food.background;
                        if(food.bullet.hasCollidedWith(food.sheep)){
                            playBulletHit();
                        }
                        //Make the animation for when the cannon explodes
                        food.explodeCannon();
                        //Move the sheep
                        food.sheepMove(1334,205);
                        //Move the pointer
                        food.movePointer(1344);
                        //Make the bullet move and play the special effect
                        food.shootBullet(1330,205, canPlaySFX);
                        //Make the meat move
                        food.dropMeat(720);
                        //If the meat is collected, increase the food and xp of the player
                        //The amount varies on the amount of sheep hit
                        if(food.meat.hasCollidedWith(food.cannon)){
                            if(food.sheepHit >=0 && food.sheepHit<6){
                                cs.food ++;
                                cs.xp += sheepXP[cs.level-1];
                            }else if(food.sheepHit >=6 && food.sheepHit<11){
                                cs.food += 2;
                                cs.xp += (int)sheepXP[(cs.level-1)]*1.5;
                            }else{
                                cs.food += 3;
                                cs.xp += sheepXP[cs.level-1]*2;
                            }
                            playMeatGain();
                        }
                        //The maximum foos count is still respected
                        if(cs.food > cs.maxFood){
                            cs.food = cs.maxFood;
                        }
                    }else{
                        //If the player runs out of bullets he exits the state
                        food.btnBack(40, 1344, 180);
                        //The character is teleported to the middle of the screen
                        resetSoraPositions();
                        //The music stops
                        stopMusic();
                        //Since the sheep gice xp, the player may gain a level from this state
                        //therefore we must account for this
                        if(cs.level<10){
                            if(cs.xp>=cs.xpForNextLevel[cs.level-1]){
                                playLevelUp();
                                lus.showLabels(cs.level-1);
                                state = 11;
                            }else{
                                playMainRoom();
                                state = 2;
                            }
                        }else{
                            playMainRoom();
                            state = 2;
                        }
                    }
                break;
                //Mineshaft/gold collecting state
                case 5:
                    //Loop the music
                    if(System.currentTimeMillis()- mineshaftTheme > 179000){
                        playMineshaft();
                    }
                    //This mode can be paused
                    if(paused == false){
                        //Untill the character touches a bomb, this mode keeps on going
                        if(gold.exploding == false){
                            //Show the flash animation
                            cs.flashVisible();
                            //The background for this state
                            background = gold.background;
                            //Moving the bombs
                            gold.bombMove(1244,620);
                            //Moving the coins
                            gold.moveDrop(1344, 720);
                            //If the character has collected a coin, he gets gold for it, or a powerup
                            for(int i = 0; i<gold.drop.length;i++){
                                if(gold.drop[i].hasCollidedWith(cs.s_boundsMiddle)){
                                    //drop 8 gives the character a speed boost
                                    if(gold.dropState[i] == 8){
                                        gold.speedTime = System.currentTimeMillis();
                                    }
                                    //drop 7 gives the character immortality
                                    if(gold.dropState[i] == 7){
                                        gold.immortalityTime = System.currentTimeMillis();
                                    }
                                    //The sound of the coin collected is played
                                    playCoinCollected();
                                    //The gold is increased
                                    cs.gold+= gold.dropGain[gold.dropState[i]-1];
                                    //A new coin is revealed
                                    gold.resetGoldDrop(1244,620, gold.drop[i], i);
                                }
                            }
                            //After 5 seconds the speed returns to normal
                            if(System.currentTimeMillis()-gold.speedTime<5000){
                                cs.speed = cs.defaultSpeed*130/100;
                            }else{
                                cs.speed = cs.defaultSpeed;
                            }
                            //Unless the character is immortal, when he hits a bomb whe character loses
                            if(System.currentTimeMillis()-gold.immortalityTime>5000){
                                if(gold.exploding == false){
                                    //Collission detection
                                    for(int i=0;i<gold.bomb.length;i++){
                                        if(gold.bomb[i].hasCollidedWith(cs.s_boundsMiddle)){
                                            playExplosion();
                                            gold.exploding = true;
                                            gold.animeTime = System.currentTimeMillis();
                                            gold.bomb[i].moveTo(gold.bomb[i].getX()-627,gold.bomb[i].getY()-280);
                                            gold.bomb[i].setImage(gold.bombExplode);
                                        }
                                    }
                                }
                            }
                        }
                        //When the character touches a bomb, the state keeps going for a bit
                        //to show the bomb animations, then ends.
                        if(gold.exploding == true && System.currentTimeMillis()-gold.animeTime > 600){
                            //The mode is exited
                            gold.btnBack();
                            //The character's positions are reset
                            resetSoraPositions();
                            gold.exploding = false;
                            //The music is stopped as new music starts playing
                            stopMusic();
                            playMainRoom();
                            //Return to the main room state
                            state = 2;
                        }
                    }else{
                        
                        //When paused, show the pause background instead of the usual one
                        background = ps.background;
                    }
                break;
                //Fighting the melee enemy
                case 6:
                    //loop the music
                    if(System.currentTimeMillis()- battleTheme > 306000){
                        playBattleMusic();
                    }
                    //This mode can be pasued
                    if(paused == false){
                        //Stop if any of the character or enemyy's health goes down to zero
                        if(me.hp>0 && cs.hp>0){
                            //Show the damaged stars animation
                            me.showDamageStars();
                            //Show the flash animation
                            cs.flashVisible();
                            //If the bullet hits the enemy while he is vulnerable, he takes damage
                            if(cs.bullet.hasCollidedWith(me.boundsMiddle) && System.currentTimeMillis()-me.normalTimer<5000){
                                if(cs.bulletFired == true){
                                    //If the bullet is a stun bullet, the enemy stops moving and attacking
                                    //Here, only the timer is reset
                                    if(cs.stunBullet == true){
                                        cs.stunTimer = System.currentTimeMillis();
                                    }else{
                                        me.hp -= shop.getDamage(rangedWeaponChoice);
                                    }
                                }
                            }
                            //When the enemy is movig towards the character, his speed is increased,
                            //here the speed is returning to it's normal speed
                            me.enemySpeed = me.defaultEnemySpeed;
                            //The background of the room
                            background = me.background;
                            //Unless the monster is stun, he moves
                            if(System.currentTimeMillis() - cs.stunTimer > 2250){
                                me.moveEnemy(cs.s_boundsMiddle,1334,710, 3, cs.bulletSpeedX, cs.bulletSpeedY);
                            }
                            //If the player owns a ranged item
                            allow = false;
                            for(int i = 5;i<10;i++){
                                if(shop.booleanOwned[i] == true){
                                    allow = true;
                                }
                            }
                            //if he dos and stun bullet is not active, make the bullet image of the current selected item
                            if(allow){
                                if(cs.stunBullet == false){
                                    cs.bullet.setImage(cs.bulletImage[rangedWeaponChoice-5]);
                                }
                            }
                            //move the character bullet
                            cs.moveBullet(1354,730,me.boundsMiddle);
                            //If the two sprites are colliding
                            if(cs.s_boundsMiddle.hasCollidedWith(me.boundsMiddle)){
                                //After some time of the sprites colliding, the enemy attacks
                                if((System.currentTimeMillis() - me.hitTimer)>1000){
                                        me.hitTimer = System.currentTimeMillis();
                                        //The damage is reduced by armor
                                        double xx = me.damage*(100-cs.armor)/100;
                                        if((int)xx+0.5>xx){
                                            cs.hp -= (int)xx;
                                        }else{
                                            cs.hp -= (int)xx++;
                                        }
                                        //Stop any character quote and play the damaged quotw
                                        stopSFX();
                                        playDamagedQuote();
                                        battleQuoteTimer = System.currentTimeMillis();
                                }
                                //Start the animation before the enemy hits the character
                                if(((System.currentTimeMillis() - me.hitTimer)+200)%1000>=0 && ((System.currentTimeMillis() - me.hitTimer)+200)%1000<=16){
                                    me.enemyHitting = true;
                                    canBeDamagedTimer = System.currentTimeMillis();
                                }
                            }else{
                                me.enemyHitting = false;
                                me.hitTimer = System.currentTimeMillis();
                            }
                            //Stop the animation after the enemy hits the character
                            if(((System.currentTimeMillis() - me.hitTimer)-300)%1000>=0 && ((System.currentTimeMillis() - me.hitTimer)-300)%1000<=16){
                                me.enemyHitting = false;
                            }
                            //Show the light seeker if it is active
                            cs.lightSeeker(me.boundsMiddle);
                            //Don't let the enemy hit if lightSeeker is active
                            if(cs.lightSeeker == true){
                                me.hitTimer = System.currentTimeMillis();
                            }
                            //Deal damage to the enemy after finishing the animation
                            if(cs.lightSeekerHit == true){
                                me.hp -= shop.getDamage(meleeWeaponChoice)*10;
                                cs.lightSeekerHit = false;
                            }
                        }else{
                            //When the battle ends, stop all music
                            stopMusic();
                            //Reset the ability coldowns
                            cs.resetCooldowns();
                            //If the character loses
                            if(cs.hp <= 0){
                                //Stop all sounds
                                stopSFX();
                                //Play the dying quote
                                playDeathQuote();
                                //Show the death screen
                                cs.charSora.moveTo(650, 340);
                                cs.charSora.setImage(cs.dead);
                                ds.displayButtons();
                                //reduce the food as a penalty
                                cs.food -= 3;
                                //Increase the death counter
                                cs.deathCounter++;
                                state = 15;
                            }else{
                                //If you beat the enemy, the enemy defeated sound is played
                                playEnemyDefeated();
                                //Gold and xp are increased
                                cs.gold += enemyGold[cs.level-1];
                                cs.xp += enemyXP[cs.level-1];
                                //These are all displayed on screen
                                eds.displayLabels(cs.level - 1);
                                //The character is repositioned to the middle of the scren
                                resetSoraPositions();
                                state = 14;
                            }
                        }
                    }else{
                        //If the game is paused, the background is changed
                        background = ps.background;
                    }
                break;
                //The weapon selection screen
                case 7:
                    //The background is changed
                    background = cw.background;
                    //The music is looped
                    if(System.currentTimeMillis()- mainRoomTheme > 154000){
                        playMainRoom();
                    }
                break;
                //Fighting the ranged enemy
                case 8:
                    //loop the music
                    if(System.currentTimeMillis()- battleTheme > 306000){
                        playBattleMusic();
                    }
                    //This mode can be paused
                    if(paused == false){
                        //Keep looping untill either character or enemy has above zero health
                        if(re.hp>0 && cs.hp>0){
                            //Show the damaged stars animation
                            re.showDamageStars();
                            //Show the flash animation
                            cs.flashVisible();
                            //Take damage from bullets if hit
                            for(int i = 0;i<re.noOfBullets;i++){
                                if(re.bulletFired[i] == true && cs.lightSeeker == false){
                                    if(re.bullet[i].hasCollidedWith(cs.s_boundsMiddle) && (System.currentTimeMillis() - canBeDamagedTimer)>50){
                                        double xx = re.bulletDamage*(100-cs.armor)/100;
                                        if((int)xx+0.5>xx){
                                            cs.hp -= (int)xx;
                                        }else{
                                            cs.hp -= (int)xx++;
                                        }
                                        //Play the damaged quote
                                        stopSFX();
                                        playDamagedQuote();
                                        battleQuoteTimer = System.currentTimeMillis();
                                        canBeDamagedTimer = System.currentTimeMillis();
                                    }
                                }
                            }
                            //make the enemy take damage from the character's bullet
                            if(cs.bullet.hasCollidedWith(re.boundsMiddle)){
                                if(cs.bulletFired == true){
                                    if(cs.stunBullet == true){
                                        //If it's the stun bullet, stun instead
                                        cs.stunTimer = System.currentTimeMillis();
                                    }else{
                                        re.hp -= shop.getDamage(rangedWeaponChoice);
                                    }
                                }
                            }
                            //If the player owns a ranged item, set the bullet image to his choice
                            allow = false;
                            for(int i = 5;i<10;i++){
                                if(shop.booleanOwned[i] == true){
                                    allow = true;
                                }
                            }
                            if(allow){
                                if(cs.stunBullet == false){
                                    cs.bullet.setImage(cs.bulletImage[rangedWeaponChoice-5]);
                                }
                            }
                            //move the enemy bullet
                            re.moveBullet(1364,735,cs.s_boundsMiddle, 19, 15);
                            //Unless stunned, move the enemy
                            if(System.currentTimeMillis() - cs.stunTimer > 2250){
                                re.move(1324,700,cs.s_boundsMiddle,19,5);
                            }
                            //move the character
                            cs.moveBullet(1354,730,re.boundsMiddle);
                            //Display the light seeker animation
                            cs.lightSeeker(re.boundsMiddle);
                            //Make the enemy take damage after light seeker
                            if(cs.lightSeekerHit == true){
                                re.hp -= shop.getDamage(meleeWeaponChoice)*10;
                                cs.lightSeekerHit = false;
                            }
                            //The background for this state
                            background = me.background;
                        }else{
                            //When it ends the music stops
                            stopMusic();
                            //Reset the ability coldowns
                            cs.resetCooldowns();
                            for(int i = 0;i<re.noOfBullets;i++){
                                re.bulletFired[i] = false;
                            }
                            if(cs.hp <= 0){
                                //Stop all sounds
                                stopSFX();
                                //Play the dying quote
                                playDeathQuote();
                                //Show the death screen
                                cs.charSora.moveTo(650, 340);
                                cs.charSora.setImage(cs.dead);
                                ds.displayButtons();
                                //reduce the food as a penalty
                                cs.food -= 3;
                                //Increase the death counter
                                cs.deathCounter++;
                                state = 15;
                            }else{
                                //If you beat the enemy, the enemy defeated sound is played
                                playEnemyDefeated();
                                //Gold and xp are increased
                                cs.gold += enemyGold[cs.level-1];
                                cs.xp += enemyXP[cs.level-1];
                                //These are all displayed on screen
                                eds.displayLabels(cs.level - 1);
                                //The character is repositioned to the middle of the scren
                                resetSoraPositions();
                                state = 14;
                            }
                        }
                    }else{
                        //IF paused, change the background
                        background = ps.background;
                    }
                break;
                case 9:
                    //loop the music
                    if(System.currentTimeMillis()- finalBossTheme > 646000){
                        playFinalBoss();
                    }
                    //This mode is pausable
                    if(paused == false){
                        //Stop when any of the characters hp falls to zero
                        if(fb.hp>0 && cs.hp>0){
                            //Hit after light seeker ends
                            if(cs.lightSeekerHit == true){
                                fb.hp -= shop.getDamage(meleeWeaponChoice)*5;
                                cs.lightSeekerHit = false;
                            }
                            //This is whn the enemy is doing his special move
                            if(fb.state == 6){
                                //The background is changed for this special state
                                background = fb.DMBackground;
                                //get the midpoint for both sprites
                                int charMidPointX = cs.charSora.getX()+cs.charSora.getWidth()/2;
                                int charMidPointY = cs.charSora.getY()+cs.charSora.getHeight()/2;
                                int enemyMidPointX = fb.boundsMiddle.getX()+fb.boundsMiddle.getWidth()/2;
                                int enemyMidPointY = fb.boundsMiddle.getY()+fb.boundsMiddle.getHeight()/2;
                                double hyp = Math.sqrt(Math.pow(charMidPointX-enemyMidPointX,2)+Math.pow(charMidPointY-enemyMidPointY,2));
                                //get the speed and multiply it
                                double dSpeed = hyp/cs.speed*1.1;
                                //The speed is multiplied so here when the division is made, the number is smaller than the actual value
                                double xx = Math.round(((Math.abs(charMidPointX-enemyMidPointX))/dSpeed));
                                double yy = Math.round((Math.abs(charMidPointY-enemyMidPointY)/dSpeed));
                                int xxx, yyy;
                                //round the speeds
                                if((int)xx+0.5>xx){
                                    xxx = (int)xx;
                                }else{
                                    xxx = (int)xx++;
                                }
                                if((int)yy+0.5>yy){
                                    yyy = (int)yy;
                                }else{
                                    yyy = (int)yy++;
                                }
                                //Move the character towards the enemy
                                if(charMidPointX<=enemyMidPointX && charMidPointY<=enemyMidPointY){
                                    cs.charSora.moveRight(xxx);
                                    cs.charSora.moveDown(yyy);
                                }else if(charMidPointX<=enemyMidPointX && charMidPointY>=enemyMidPointY){
                                    cs.charSora.moveRight(xxx);
                                    cs.charSora.moveUp(yyy);
                                }else if(charMidPointX>=enemyMidPointX && charMidPointY<=enemyMidPointY){
                                    cs.charSora.moveLeft(xxx);
                                    cs.charSora.moveDown(yyy);
                                }else if(charMidPointX>=enemyMidPointX && charMidPointY>=enemyMidPointY){
                                    cs.charSora.moveLeft(xxx);
                                    cs.charSora.moveUp(yyy);
                                }
                            }else{
                                //If enemy state isn't 6, set the background to the normal one
                                background = fb.normalBackground;
                            }
                            //If the character bullet hits the enemy, the enemy takes damage, 
                            //unless he is in state 6 where he is invulnerable
                            if(cs.bullet.hasCollidedWith(fb.boundsMiddle)){
                                if(cs.bulletFired == true){
                                    if(fb.state != 6){
                                        fb.hp -= shop.getDamage(rangedWeaponChoice);
                                        if(System.currentTimeMillis() - rikuDamagedQuoteTimer>500){
                                            //The enemy says a damaged quote
                                            playRikuDamagedQuote();
                                            rikuDamagedQuoteTimer = System.currentTimeMillis();
                                        }
                                    }
                                }
                            }
                            //if the enemy bullets are fired and the player is hit by them, he takes damage
                            //unless the character is doing light seeker where he is invulnerable
                            for(int i = 0;i<11;i++){
                                if(fb.bulletFired[i] == true && cs.lightSeeker == false){
                                    if(fb.bullet[i].hasCollidedWith(cs.s_boundsMiddle) && (System.currentTimeMillis() - canBeDamagedTimer)>50){
                                        double xx = fb.bulletDamage*(100-cs.armor)/100;
                                        if((int)xx+0.5>xx){
                                            cs.hp -= (int)xx;
                                        }else{
                                            cs.hp -= (int)xx++;
                                        }
                                        //The damaged quote is heard
                                        stopSFX();
                                        playDamagedQuote();
                                        canBeDamagedTimer = System.currentTimeMillis();
                                    }
                                }
                            }
                            //Move the character bullet
                            cs.moveBullet(1354,730,fb.boundsMiddle);
                            //Display the light seeker animation
                            cs.lightSeeker(fb.boundsMiddle);
                            //Move the enemy
                            fb.move(cs.s_boundsMiddle, 1334, 700, canPlayVoices);
                            //Make the flash animation visible
                            cs.flashVisible();
                            //Make the enemy teleport animation visible
                            fb.teleportVisible();
                            //If the character is coliding with the enemy's sprites, the counter for the enemy to attack starts
                            //And every multiple of 1.5 seconds, he attacks
                            if(cs.s_boundsMiddle.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsMiddle.hasCollidedWith(fb.boundsUpLeft) || cs.s_boundsMiddle.hasCollidedWith(fb.boundsUp) || cs.s_boundsMiddle.hasCollidedWith(fb.boundsUpRight) || cs.s_boundsMiddle.hasCollidedWith(fb.boundsLeft) || cs.s_boundsMiddle.hasCollidedWith(fb.boundsRight) || cs.s_boundsMiddle.hasCollidedWith(fb.boundsDownLeft) || cs.s_boundsMiddle.hasCollidedWith(fb.boundsDown) || cs.s_boundsMiddle.hasCollidedWith(fb.boundsDownRight)){
                                if((System.currentTimeMillis() - fb.hitTimer)%1500>=0 && (System.currentTimeMillis() - fb.hitTimer)%1500<=16){
                                    //If the state is the enemy's special move, the enemy sends the character
                                    //to the edge of the screen and deals triple damage
                                    if(fb.state == 6){
                                        if(cs.lightSeeker == false){
                                            //The tiple damage is reduced by armor
                                            double xx = (fb.meleeDamage*(100-cs.armor)/100)*3;
                                        
                                            if((int)xx+0.5>xx){
                                                cs.hp -= (int)xx;
                                            }else{
                                                cs.hp -= (int)xx++;
                                            }
                                            //The state is changed after hitting
                                            fb.canChangeState = true;
                                            int charMidPointX = cs.s_boundsMiddle.getX()+cs.s_boundsMiddle.getWidth()/2;
                                            int charMidPointY = cs.s_boundsMiddle.getY()+cs.s_boundsMiddle.getHeight()/2;
                                            int enemyMidPointX = fb.boundsMiddle.getX()+fb.boundsMiddle.getWidth()/2;
                                            int enemyMidPointY = fb.boundsMiddle.getY()+fb.boundsMiddle.getHeight()/2;
                                            //The character is sent to the edge of the screen depending on the direction of the enemy tot he character
                                            if(charMidPointX == enemyMidPointX && charMidPointY > enemyMidPointY){
                                                cs.charSora.setY(744-cs.charSora.getHeight());
                                            }else if(charMidPointX == enemyMidPointX && charMidPointY < enemyMidPointY){
                                                cs.charSora.setY(3);
                                            }else if(charMidPointX < enemyMidPointX && charMidPointY == enemyMidPointY){
                                                cs.charSora.setX(-15);
                                            }else if(charMidPointX > enemyMidPointX && charMidPointY == enemyMidPointY){
                                                cs.charSora.setX(1374-cs.charSora.getWidth());
                                            }else if(charMidPointX<=enemyMidPointX && charMidPointY<=enemyMidPointY){
                                                cs.charSora.moveTo(-15,3);
                                            }else if(charMidPointX<=enemyMidPointX && charMidPointY>=enemyMidPointY){
                                                cs.charSora.moveTo(-15, 744-cs.charSora.getHeight());
                                            }else if(charMidPointX>=enemyMidPointX && charMidPointY<=enemyMidPointY){
                                                cs.charSora.moveTo(1374-cs.charSora.getWidth(), 3);
                                            }else if(charMidPointX>=enemyMidPointX && charMidPointY>=enemyMidPointY){
                                                cs.charSora.moveTo(1374-cs.charSora.getWidth(), 744-cs.charSora.getHeight());
                                            }
                                            cs.resetBounds();
                                            playDamagedQuote();
                                        }
                                    }
                                    //If the enemy is not in the special ability state, it is just a normal hit
                                    else{
                                        if(cs.lightSeeker == false){
                                            //reduced by armor
                                            double xx = fb.meleeDamage*(100-cs.armor)/100;
                                            if((int)xx+0.5>xx){
                                                cs.hp -= (int)xx;
                                            }else{
                                                cs.hp -= (int)xx++;
                                            }
                                            //Play the character damaged quote
                                            stopSFX();
                                            playDamagedQuote();
                                            battleQuoteTimer = System.currentTimeMillis();
                                        }
                                    }
                                }
                                //The attacking animation starts
                                if(((System.currentTimeMillis() - fb.hitTimer)+200)%1500>=0 && ((System.currentTimeMillis() - fb.hitTimer)+200)%1500<=16){
                                    fb.enemyHitting = true;
                                    canBeDamagedTimer = System.currentTimeMillis();
                                }
                            }else{
                                //If the sprites are not colliding, reset the timer
                                fb.enemyHitting = false;
                                fb.hitTimer = System.currentTimeMillis();
                            }
                            //The attacking animation ends
                            if(((System.currentTimeMillis() - fb.hitTimer)-300)%1500>=0 && ((System.currentTimeMillis() - fb.hitTimer)-300)%1500<=16){
                                fb.enemyHitting = false;
                            }
                        }else{
                            //Reset the ability coldowns
                            cs.resetCooldowns();
                            if(cs.hp <= 0){
                                //When it ends the music stops
                                stopMusic();
                                //Stop all sounds
                                stopSFX();
                                //Play the dying quote
                                playDeathQuote();
                                //Show the death screen
                                cs.charSora.moveTo(650, 340);
                                cs.charSora.setImage(cs.dead);
                                ds.displayButtons();
                                //reduce the food as a penalty
                                cs.food -= 3;
                                //Increase the death counter
                                cs.deathCounter++;
                                state = 15;
                            }else{
                                //If you beat the final boss, his death quoteis heard
                                playRikuDeathQuote();
                                //Go to the ending cutscene
                                end.btnContinue.setVisible(true);
                                end.lblSentence[0].setVisible(true);
                                state = 18;
                                //reposition the buttons
                                btnSFX.setVisible(false);
                                btnVoices.setVisible(false);
                                btnMusic.setBounds(1253,25,90,25);
                            }
                        }
                    }else{
                        background = ps.background;
                    }
                break;
                case 10:
                    //set the background for this state
                    background = credits.background;
                    //loop music
                    if(System.currentTimeMillis()- mainRoomTheme > 154000){
                        playMainRoom();
                    }
                break;
                case 11:
                    //set the background for this state
                    background = lus.background;
                    //loop music
                    if(System.currentTimeMillis()- mainRoomTheme > 154000){
                        playMainRoom();
                    }
                break;
                case 12:
                    //loop music
                    if(System.currentTimeMillis()- mainRoomTheme > 154000){
                        playMainRoom();
                    }
                break;
                case 13:
                    //set the background for this state
                    background = sl.background;
                break;
                case 14:
                    //set the background for this state
                    background = eds.background;
                break;
                case 15:
                    //set the background for this state
                    background = ds.background;
                break;
                //Instructions
                case 16:
                    //set the background for this state
                    background = instructions.background;
                    //loop music
                    if(System.currentTimeMillis()- mainRoomTheme > 154000){
                        playMainRoom();
                    }
                break;
                //Story, beginning part
                case 17:
                    //loop music
                    if(System.currentTimeMillis()- mainRoomTheme > 154000){
                        playMainRoom();
                    }
                    //Set the background for this state
                    if(begin.labelNumber == 6){
                        background = begin.characterImage[0];
                    }else{
                        background = begin.characterImage[1];
                    }
                    //Draw the sentences on the labels with an effect
                    if(System.currentTimeMillis() - begin.effectTimer>20){
                        begin.rollSentence();
                    }
                break;
                //Story, end part
                case 18:
                    //loop the music
                    if(System.currentTimeMillis()- finalBossTheme > 646000){
                        playFinalBoss();
                    }
                    //Set the background for this state
                    if(end.labelNumber == 0){
                        background = end.characterImage[0];
                    }else if(end.labelNumber == 1){
                        background = end.characterImage[2];
                    }else{
                        background = end.characterImage[1];
                    }
                    //Draw the sentences on the labels with an effect
                    if(System.currentTimeMillis() - end.effectTimer>20){
                        end.rollSentence();
                    }
                break;
                //The end screen
                case 19:
                    background = kh3Logo;
                    //loop the music
                    if(System.currentTimeMillis()- finalBossTheme > 646000){
                        playFinalBoss();
                    }
                break;
            }
            //redraw the screen every time the thread runs
            repaint();
        }
    }
    void drawUI(Graphics g){ 
        //Draw the character's hp 
        g.setColor(Color.GREEN);
        double xx = (350.0f/cs.maxhp)*cs.hp;
        if((int)xx+0.5>xx){
            xx = (int)xx;
        }else{
            xx = (int)xx++;
        }
        //This is drawn by a green bar that decays as the player loses health
        g.fillRect(950,699,(int)xx,14);
        if((cs.hp*1.0f)/cs.maxhp < 0.5f){
            if(state == 9){
                g.setColor(Color.PINK);
            }else{
                g.setColor(Color.BLACK);
            }
        }else{
            g.setColor(Color.BLUE);
        }
        //Furthermore, in the bar, there is the actual amount of health displayed as digits
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString(cs.hp + "/" + cs.maxhp,1107,711);
        if(state == 6){
            //An string saying HP is displayed near the hp bar
            g.setColor(Color.PINK);
            g.setFont(new Font("Arial", Font.BOLD, 13));
            g.drawString("HP:",10,45);
            //The hp bar is drawn
            g.setColor(Color.RED);
            g.drawRect(40,30,150,15);
            double xx2 = (150.0f/me.maxhp)*me.hp;
            if((int)xx2+0.5>xx2){
                xx2 = (int)xx2;
            }else{
                xx2 = (int)xx2++;
            }
            g.fillRect(40,30,(int)xx2,15);
            //In the bar, there is the actual amount of health displayed as digits
            g.setColor(Color.ORANGE);
            g.drawString(me.hp + "/" + me.maxhp,100,43);
        }
        if(state == 8){
            //An string saying HP is displayed near the hp bar
            g.setColor(Color.PINK);
            g.setFont(new Font("Arial", Font.BOLD, 13));
            g.drawString("HP:",10,45);
            //The hp bar is drawn
            g.setColor(Color.RED);
            g.drawRect(40,30,150,15);
            double xx2 = (150.0f/re.maxhp)*re.hp;
            if((int)xx2+0.5>xx2){
                xx2 = (int)xx2;
            }else{
                xx2 = (int)xx2++;
            }
            g.fillRect(40,30,(int)xx2,15);
            //In the bar, there is the actual amount of health displayed as digits
            g.setColor(Color.ORANGE);
            g.drawString(re.hp + "/" + re.maxhp,100,43);
        }
        if(state == 9){
            //An string saying HP is displayed near the hp bar
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 13));
            g.drawString("HP:",10,43);
            //The hp bar is drawn
            g.drawRect(40,30,150,15);
            double xx2 = (150.0f/fb.maxHP)*fb.hp;
            if((int)xx2+0.5>xx2){
                xx2 = (int)xx2;
            }else{
                xx2 = (int)xx2++;
            }
            g.fillRect(40,30,(int)xx2,15);
            //In the bar, there is the actual amount of health displayed as digits
            g.setColor(Color.WHITE);
            g.drawString(fb.hp + "/" + fb.maxHP,100,43);
        }
        //The user interface Image is drawn on top of the background
        g.drawImage(ui,0,20,null);
        //Here the abilities icons and timers are drawn
        //If the player has not unlocked an ability yet, it has a question mark icon
        //The timers are only drawn if the ability is not available yet for use due to cooldown
        /*Abilities like stun bullet and homerunblow where they can be activated and deactivated again
          have a new image when they are activated but not yet used, 
          then return to their original picture if they are used/deactivated. */
        if(cs.level<2){
            //Question mark icon
            g.drawImage(questionMarkIcon,569,666, null);//569,666
        }else{
            //Ability icon
            g.drawImage(flashIcon,569,666,null);
            //Timers are drawn
            if(System.currentTimeMillis() - cs.flashCooldownTimer < 4000){
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.drawString(""+ (4-((System.currentTimeMillis() - cs.flashCooldownTimer)/1000)), 579, 708);//579,708
            }
        }
        if(cs.level<5){
            g.drawImage(questionMarkIcon,623,666, null);
        }else{
            //This ability is changed if the player is fighting the final boss
            if(state == 2 || state == 6 || state == 8){
                if(cs.stunBullet == true){
                    g.drawImage(stunBulletIcon2,623,666,null);
                }else{
                    g.drawImage(stunBulletIcon,623,666,null);
                }
                if(System.currentTimeMillis() - cs.stunBulletCooldownTimer < 16000){
                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 48));
                    g.drawString(""+ (16-((System.currentTimeMillis() - cs.stunBulletCooldownTimer)/1000)), 620, 708);
                }
            }else if(state == 9){
                g.drawImage(healIcon,623,666,null);
                if(System.currentTimeMillis() - cs.healCooldownTimer < 16000){
                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 48));
                    g.drawString(""+ (16-((System.currentTimeMillis() - cs.healCooldownTimer)/1000)), 620, 708);
                }
            }
        }
        if(cs.level<7){
            g.drawImage(questionMarkIcon,677,666, null);
        }else{
            g.drawImage(lightSeekerIcon,677,666,null);
            if(System.currentTimeMillis() - cs.lightSeekerCooldownTimer < 30000){
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.drawString(""+ (30-((System.currentTimeMillis() - cs.lightSeekerCooldownTimer)/1000)), 676, 708);
            }
        }
        if(cs.level<9){
            g.drawImage(questionMarkIcon,731,666, null);//569,666
        }else{
            if(cs.homerunBlow == true){
                g.drawImage(homeRunBlowIcon2,731,666,null);
            }else{
                g.drawImage(homeRunBlowIcon,731,666,null);
            }
            if(System.currentTimeMillis() - cs.homerunBlowCooldownTimer < 15000){
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.drawString(""+ (15-((System.currentTimeMillis() - cs.homerunBlowCooldownTimer)/1000)), 727, 708);
            }
        }
    }
    public void paint(Graphics g){
        //The double buffering is made and the paint is renamed to paint component
        dbi = createImage(getWidth(), getHeight());
        dbg = dbi.getGraphics();
        paintComponent(dbg);
        //The double bufering is drawn on screen
        g.drawImage(dbi,0,0,this);
    }
    public void paintComponent(Graphics g){
        //The background is always drawn first thing
        g.drawImage(background,0,25,null);
        //If the program cannot load data from the text file, a timer is started, 
        //and here the timer is used to display an error message for 2 seconds
        if(System.currentTimeMillis() - cannotLoadTimer < 2000){
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.RED);
            g.drawString("Cannot Load Data" , 430, 590);
        }
        //Here we draw the extra things besides the background for each state
        switch(state){
            //Welcoming image
            case 1:
                //what happens in the background state: nothing extra is drawn
            break;
            //Main room
            case 2:
                //In state 1(When the Room of Options(Map)) is displayed, the extra items(The character) are drawn
                if(cs.charSora != null){
                    //When the bullet is fired, draw it
                    if(cs.bulletFired == true){
                        cs.bullet.paint(g);
                    }
                    //The character is drawn
                    cs.charSora.paint(g);
                    //The flash animation is drawn
                    cs.flash.paint(g);
                    //The user interface is drawn
                    drawUI(g);
                    //The character stats are drawn
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 15));
                    g.drawString("Gold: " + cs.gold,400,692);
                    g.drawString("Food: " + cs.food + "/25",400,712);
                    g.drawString("Armor: " + cs.armor,565,650);
                    if(cs.level<10){
                        g.drawString("XP: " + cs.xp,565,630);
                        g.setColor(Color.WHITE);
                        g.drawString("Xp for next level: " + (cs.xpForNextLevel[cs.level-1]-cs.xp),788,690);
                    }
                    g.setColor(Color.WHITE);
                    g.drawString("Speed: " + cs.speed,788,710);
                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.RED);
                    g.drawString("" + cs.level,1237,685);
                    //draw the character bounds(was used in testing)
                    /*cs.s_boundsUpLeft.paint(g);
                    cs.s_boundsUp.paint(g);
                    cs.s_boundsUpRight.paint(g);
                    cs.s_boundsLeft.paint(g);
                    cs.s_boundsMiddle.paint(g);
                    cs.s_boundsRight.paint(g);
                    cs.s_boundsDownLeft.paint(g);
                    cs.s_boundsDown.paint(g);
                    cs.s_boundsDownRight.paint(g);*/
                }
            break;
            //Shop state
            case 3:
                //Draw the weapon images displayed on screen
                if(shop.state == 2){
                    for(int i = 0; i<shop.meleeVisible.length;i++){
                        if(shop.meleeVisible[i] == true){
                            g.drawImage(shop.weaponImage[i],148,(200 + i*70),null);
                        }
                    }
                    for(int i = 0; i<shop.rangedVisible.length;i++){
                        if(shop.rangedVisible[i] == true){
                            g.drawImage(shop.weaponImage[i+5],148,(216 + i*70),null);
                        }
                    }
                    for(int i = 0; i<shop.armorVisible.length;i++){
                        if(shop.armorVisible[i] == true){
                            g.drawImage(shop.weaponImage[i+10],120,(200 + i*72),null);
                        }
                    }
                }
                if(shop.state == 3){
                    for(int i = 0, counter = 0; i<shop.weaponImage.length;i++){
                        if(shop.booleanOwned[i] == true){
                            g.drawImage(shop.weaponImage[i],148,(190 + counter*36),null);
                            counter++;
                        }
                    }
                }
                //Draw the question
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.drawString(shop.question,120,155);
                //Draw the gold string and amount
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.YELLOW);
                g.drawString("Gold: "+cs.gold,1045,165);
                //The item statistics titles are drawn and underlined
                if(shop.state == 3 || shop.state == 2){
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 15));
                    g.drawString("Name",190,190);
                    g.drawString("Price",379,190);
                    g.drawString("Minimum Damage",568,190);
                    g.drawString("Maximum Damage",757,190);
                    g.drawString("Armor",946,190);
                    g.drawLine(190,195,1070,195);
                }
                //When the player tries to set all his melee items, a timer starts
                //and here it is used to draw an error message for 2 seconds
                g.setColor(Color.BLACK);
                if(System.currentTimeMillis()-meleeItemTimer<2000){
                    g.setFont(new Font("Arial", Font.PLAIN, 20));
                    g.setColor(Color.RED);
                    g.drawString("You cannot sell all your melee items!" , 430, 590);
                }
                //When the player tries to buy more then 10 items, a timer starts
                //and here it is used to draw an error message for 2 seconds
                if(System.currentTimeMillis()-itemLimitTimer<2000){
                    g.setFont(new Font("Arial", Font.PLAIN, 20));
                    g.setColor(Color.RED);
                    g.drawString("You cannot buy more than 10 items!" , 430, 590);
                }
                //When the player tries to buy something that costs more than he has money for, a timer starts
                //and here it is used to draw an error message for 2 seconds
                if(System.currentTimeMillis()-notEnoughGoldTimer<2000){
                    g.setFont(new Font("Arial", Font.PLAIN, 20));
                    g.setColor(Color.RED);
                    g.drawString("Not enough gold!" , 430, 590);
                }
            break;
            //Food state
            case 4:
                //Here the food, bullets left and xp are drawn
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.RED);
                g.drawString("Food: " + cs.food + "/25",894,649);
                g.drawString("Bullets Left: " + food.bulletsLeft,1107,649);
                g.drawString("XP:  " + cs.xp,681,649);
                //The meat is drawn
                food.meat.paint(g);
                //The sheep is drawn
                food.sheep.paint(g);
                //The cannon is drawn
                food.cannon.paint(g);
                //The pointer is drawn
                food.pointer.paint(g);
                //The bullet is drawn
                food.bullet.paint(g);
            break;
            //The mineshaft/gold collection state
            case 5:
                //Show the duration of both the immortality and speed boost timer
                if(System.currentTimeMillis() - gold.immortalityTime < 5000){
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    g.setColor(Color.WHITE);
                    g.drawString("Immortal: " + (5 - (System.currentTimeMillis() - gold.immortalityTime)/1000),650,45);
                }
                if(System.currentTimeMillis() - gold.speedTime < 5000){
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    g.setColor(Color.WHITE);
                    g.drawString("Speed Boost: " + (5 - (System.currentTimeMillis() - gold.speedTime)/1000),400,45);
                }
                //Draw the current gold
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.YELLOW);
                g.drawString("Speed: " + cs.speed,1150,75);
                g.drawString("Gold: "+cs.gold,1150,110);
                //Draw the character
                cs.charSora.paint(g);
                //Draw the bombs
                for(int i = 0; i<4; i++){
                    gold.bomb[i].paint(g,50,70);
                }
                //draw the coins
                gold.drop[0].paint(g);
                gold.drop[1].paint(g);
                //Draw the flash animation
                cs.flash.paint(g);
            break;
            //Fighting the melee enemy
            case 6:
                if(me.enemy != null){
                    //Draw the light seeker animtion
                    cs.starExplosion.paint(g);
                    //draw the bullet if it is fired
                    if(cs.bulletFired == true){
                        cs.bullet.paint(g);
                    }
                    //draw the enemy
                    me.enemy.paint(g);
                    //draw the damaged stars animation
                    me.damagedStars.paint(g);
                    //draw the character
                    cs.charSora.paint(g);
                    //draw the flash animation
                    cs.flash.paint(g);
                    //draw the health bars and ui image
                    drawUI(g);
                    //Draw the character level
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.drawString("" + cs.level,1237,685);
                    //draw the enemy bounds, was used in testing
                    /*me.boundsMiddle.paint(g);
                     * me.boundsLeft.paint(g);
                     * me.boundsright.paint(g);*/
                }
            break;
            //The item selection screen
            case 7:
                //Draw the enemy image
                if(cw.enemyImageVisible == true){
                    g.drawImage(cw.enemyImage,800,95,null);
                }
                //Draw the question
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.drawString(cw.question,305,170);
                //When the user tries to continue without picking an item first, a timer starts
                //and here that timer is used to display an error message foe 2 seconds
                if(System.currentTimeMillis()-cw.noItemPickedTimer<2000){
                    g.setFont(new Font("Arial", Font.PLAIN, 20));
                    g.setColor(Color.RED);
                    g.drawString("Pick an item!" , 430, 590);
                }
                //The item stats are displayed
                if(cw.state == 2 || cw.state == 3 || cw.state == 4){
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 15));
                    g.drawString("Name",200,190);
                    g.drawString("Minimum Damage",389,190);
                    g.drawString("Maximum Damage",578,190);
                    g.drawString("Armor",767,190);
                    g.drawLine(200,195,1070,195);
                }
                //Display the melee weapons images
                if(cw.state == 2){
                    for(int i = 0,counter = 0;i<10;i++){
                        if(shop.booleanOwned[i] == true && shop.booleanMelee[i] == true){
                            g.drawImage(shop.weaponImage[i],158,(220 + counter*70),null);
                            counter++;
                        }
                    }
                }
                //Display the ranged weapons images
                if(cw.state == 3){
                    for(int i = 0,counter = 0;i<10;i++){
                        if(shop.booleanOwned[i] == true && shop.booleanRanged[i] == true){
                            g.drawImage(shop.weaponImage[i],158,(235 + counter*72),null);
                            counter++;
                        }
                    }
                }
                //Draw the picked and armor weapons images
                if(cw.state == 4){
                    int counter = 0;
                    for(int i = 0;i<10;i++){
                        if(cw.picked[i] == true){
                            g.drawImage(shop.weaponImage[i],158,(200 + counter*60),null);
                            counter++;
                        }
                    }
                    for(int i = 0;i<15;i++){
                        if(shop.booleanOwned[i] == true && shop.booleanArmor[i] == true){
                            g.drawImage(shop.weaponImage[i],125,(190 + counter*60),null);
                            counter++;
                        }
                    }
                }
            break;
            //Fighting the melee enemy
            case 8:
                if(re.enemy != null){
                    //draw the light seeker animation
                    cs.starExplosion.paint(g);
                    //draw the character's bullet
                    if(cs.bulletFired == true){
                        cs.bullet.paint(g);
                    }
                    //dsraw the damaged stars animation
                    re.damagedStars.paint(g);
                    //drw the enemy's bullets
                    for(int i = 0;i<re.noOfBullets;i++){
                        if(re.bulletFired[i] == true){
                            re.bullet[i].paint(g);
                        }
                    }
                    //draw the ranged enemy
                    re.enemy.paint(g);
                    //draw the flash animation
                    cs.flash.paint(g);
                    //draw the character
                    cs.charSora.paint(g);
                    //draw the user interface(hp bars and abilities)
                    drawUI(g);
                    //Draw the character level
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.drawString("" + cs.level,1237,685);
                    //re.boundsMiddle.paint(g);
                }
            break;
            //Fighting the final boss
            case 9:
                //Draw the light seeker animation
                cs.starExplosion.paint(g);
                //draw the health bar and ability icons
                drawUI(g);
                //draw the enmemy
                fb.enemy.paint(g);
                //draw the character
                cs.charSora.paint(g);
                //draw the flash animation
                cs.flash.paint(g);
                //draw the teleport animation of the enemy
                fb.teleport.paint(g);
                //draw the character's bullet image
                if(cs.bulletFired == true){
                    cs.bullet.paint(g);
                }
                //draw all of the enemy's bullets
                for(int i = 0;i<fb.bullet.length;i++){
                    fb.bullet[i].paint(g);
                }
                //draw the character's level
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.drawString("" + cs.level,1237,685);
                /*fb.boundsUpLeft.paint(g);
                fb.boundsUp.paint(g);
                fb.boundsUpRight.paint(g);
                fb.boundsLeft.paint(g);
                fb.boundsMiddle.paint(g);
                fb.boundsRight.paint(g);
                fb.boundsDownLeft.paint(g);
                fb.boundsDown.paint(g);
                fb.boundsDownRight.paint(g);*/
            break;
            //credits
            case 10:
                //draw the caption for an image
                g.setFont(new Font("Arial", Font.PLAIN, 25));
                g.setColor(Color.RED);
                switch(credits.imgCounter){
                    case 0:
                    case 2:
                        g.setColor(Color.RED);
                        g.drawString(credits.caption[credits.imgCounter], 100, 680);
                    break;
                    case 1:
                        g.setColor(Color.ORANGE);
                        g.drawString(credits.caption[credits.imgCounter], 680, 680);
                    break;
                    case 3:
                        g.setColor(Color.YELLOW);
                        g.drawString(credits.caption[credits.imgCounter], 100, 680);
                    break;
                    case 4:
                        g.setColor(Color.BLACK);
                        g.drawString(credits.caption[credits.imgCounter], 100, 680);
                    break;
                    case 5:
                    case 6:
                    case 7:
                        g.setColor(Color.BLACK);
                        g.drawString(credits.caption[credits.imgCounter], 120, 630);
                    break;
                    case 9:
                        g.setColor(Color.WHITE);
                        g.drawString(credits.caption[credits.imgCounter], 100, 680);
                    break;
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 15:
                        g.setColor(Color.BLACK);
                        g.drawString(credits.caption[credits.imgCounter], 100, 680);
                    break;
                    case 18:
                        g.setColor(Color.BLACK);
                        g.drawString(credits.caption[credits.imgCounter], 100, 680);
                    break;
                    case 20:
                        g.setColor(Color.RED);
                        g.drawString(credits.caption[credits.imgCounter], 100, 680);
                    break;
                    case 8:
                        g.setColor(Color.WHITE);
                        g.drawString(credits.caption[credits.imgCounter], 100, 680);
                    break;
                }
            break;
            //level up state
            case 11:
                //Draw the level up congratulatory message
                g.setFont(new Font("Arial", Font.PLAIN, 45));
                g.setColor(Color.BLACK);
                g.drawString("Level Up: ", 200, 190);
                g.drawString("Level "+ (cs.level+1), 200, 250);
            break;
            //Saving
            case 13:
                //Display a message for the user to already have saved a file before loading
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.RED);
                g.drawString("Please make sure you already have a save file before loading.", 200, 190);
            break;
            //The enemy defeated screen
            case 14:
                //Draw a congratulatory message saying "ENEMY DEFEATED!"
                g.setFont(new Font("Arial", Font.PLAIN, 45));
                g.setColor(Color.BLACK);
                g.drawString("ENEMY DEFEATED!", 250, 190);
            break;
            //Death screen
            case 15:
                //draw a floating heart above the character
                g.drawImage(floatingHeart, 663, 270, null);
                //draw the character
                cs.charSora.paint(g);
                //If he died of hunger, display a different message than if he died from losing to an enemy
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.BLACK);
                if(cs.hp<=0){
                    g.drawString("You have been defeated!", 250, 190);
                }else{
                    g.drawString("You died of hunger!", 250, 190);
                }
            break;
        }
        //If the game is paused, draw a string saying that the game is paused
        if(paused == true){
            g.drawImage(background,0,25,null);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.setColor(Color.BLACK);
            g.drawString("Game Paused", 250, 190);
        }
    }
    @Override
    public void update(Graphics g){
        if(paused == false){
            //These were put here instead of any of the keylistener methods 
            //in order to avoid any delay and move them instantly upon pressing a button
            if(cs.lightSeeker == false){
                moveSora();
            }
            moveRiku();
            moveCannon();
        }
        //The paint is done each time
        paint(g);
    }
    public void keyReleased(KeyEvent e){
        //When a key is released, it is removed from keysDown and added to keysUp
        keysDown.remove(new Integer(e.getKeyCode()));
        if(!keysUp.contains(e.getKeyCode())){
            keysUp.add(new Integer(e.getKeyCode()));
        }
        if(state == 2 || state == 5 || state == 6 || state == 8 || state == 9){
            //The timers are for stopping at diagonal directions
            if(e.getKeyCode() == 68){
                cs.rightTimer = System.currentTimeMillis();
            }
            if(e.getKeyCode() == 65){
                cs.leftTimer = System.currentTimeMillis();
            }
            if(e.getKeyCode() == 87){
                cs.upTimer = System.currentTimeMillis();
            }
            if(e.getKeyCode() == 83){
                cs.downTimer = System.currentTimeMillis();
            }
        }
        
        if(state == 9 && fb.state == 3){
            //The timers are for stopping at diagonal directions
            if(e.getKeyCode() == 68){
                fb.rightTimer = System.currentTimeMillis();
            }
            if(e.getKeyCode() == 65){
                fb.leftTimer = System.currentTimeMillis();
            }
            if(e.getKeyCode() == 87){
                fb.upTimer = System.currentTimeMillis();
            }
            if(e.getKeyCode() == 83){
                fb.downTimer = System.currentTimeMillis();
            }
        }
        if(state == 9 && fb.state == 4){
            //The timers are for stopping at diagonal directions
            if(e.getKeyCode() == 68){
                fb.leftTimer = System.currentTimeMillis();
            }
            if(e.getKeyCode() == 65){
                fb.rightTimer = System.currentTimeMillis();
            }
            if(e.getKeyCode() == 87){
                fb.downTimer = System.currentTimeMillis();
            }
            if(e.getKeyCode() == 83){
                fb.upTimer = System.currentTimeMillis();
            }
        }
    }
    
    public void keyTyped(KeyEvent e){
    }
    
    public void keyPressed(KeyEvent e){
        //When a key is pressed, it is removed from keysUp and added to keysDown
        keysUp.remove(new Integer(e.getKeyCode()));
        if(!keysDown.contains(e.getKeyCode())){
            keysDown.add(new Integer(e.getKeyCode()));
        }
        //At level 2 the user unlocks flash, which can be accessed by pressing F
        if(cs.level>=2){
            if(keysDown.contains(KeyEvent.VK_F)){
                cs.flash(1344,720);
            }
        }
        //At level 5 the user unlocks stun bullet, which can be accessed by pressing Q
        if(cs.level>=5){
            if(keysDown.contains(KeyEvent.VK_Q)){
                if(state == 2 || state == 6 || state == 8){
                    //It is only available if the player has a ranged item owned
                    boolean allow = false;
                    for(int i = 5;i<10;i++){
                        if(shop.booleanOwned[i] == true){
                            allow = true;
                        }
                    }
                    //It can be turned on and off again if the user decides to cancel the attack
                    if(allow){
                        //Cooldown is 16 seconds
                        if(System.currentTimeMillis() - cs.stunBulletCooldownTimer > 16000 && cs.bulletFired == false && cs.stunBullet == false){
                            cs.stunBullet = true;
                        }else{
                            cs.stunBullet = false;
                        }
                    }
                }
                //At the final boss battle, he instead heals himself for 50 health
                else if(state == 9){
                    //It cannot be used if the character is at full health
                    if(cs.hp<cs.maxhp){
                        //Cooldown is 16 seconds
                        if(System.currentTimeMillis() - cs.healCooldownTimer > 16000){
                            stopSFX();
                            playHealQuote();
                            battleQuoteTimer = System.currentTimeMillis();
                            cs.hp += 50;
                            if(cs.hp > cs.maxhp){
                                cs.hp = cs.maxhp;
                            }
                            cs.healCooldownTimer = System.currentTimeMillis();
                            cs.healAnimationTimer = System.currentTimeMillis();
                        }
                    }
                }
            }
        }
        //At level 7 the user unlocks Light Seeker, which can be accessed by pressing Z
            if(cs.level>=7){
                if(keysDown.contains(KeyEvent.VK_Z)){
                    if(state == 6 || state == 8 || state == 9){
                    //Cooldown is 30 seconds
                    if(System.currentTimeMillis() - cs.lightSeekerCooldownTimer > 30000){
                        if(System.currentTimeMillis() - battleQuoteTimer > 500){
                            playLightSeekerQuote();
                            battleQuoteTimer = System.currentTimeMillis();
                        }
                        cs.lightSeeker = true;
                        cs.lightSeekerTimer = System.currentTimeMillis();
                        cs.lightSeekerCooldownTimer = System.currentTimeMillis();
                    }
                }
            }
        }
        //At level 9 the user unlocks Homerun Blow, which can be accessed by pressing X
        if(cs.level>=9){
            if(keysDown.contains(KeyEvent.VK_X)){
                 if(state == 6 || state == 8 || state == 9){
                    //Cooldown is 15 seconds
                    //It can be turned on and off again if the user decides to cancel the attack
                    if(System.currentTimeMillis() - cs.homerunBlowCooldownTimer > 15000 && cs.homerunBlow == false){
                        cs.homerunBlow = true;
                    }else{
                        cs.homerunBlow = false;
                    }
                }
            }
        }
        if(keysDown.contains(KeyEvent.VK_ESCAPE)){
            if((state == 5 || state == 6 || state == 8 || state == 9) && paused == false){
                 paused = true;
                 ps.pause();
                 cs.pause();
                 gold.btnBack.setVisible(false);
            }
        }
    }
    //In certain situatons, the final boss moves the same direction as the character 
    //and at others the opposite direction. They are both coded below
    void moveRiku(){
        //Move the sprite and set the image to the direction needed
        if(state == 9 && fb.state == 3){
            //Up
            if(keysDown.contains(KeyEvent.VK_W)){
                fb.direction = 1;
                fb.enemy.setImage(fb.upMove);
                fb.moveUp();
                if(fb.boundsMiddle.getY() <= 25){
                    fb.moveDown();
                }
            }
            //Left
            if(keysDown.contains(KeyEvent.VK_A)){
                fb.direction = 2;
                fb.enemy.setImage(fb.leftMove);
                fb.moveLeft();
                if(fb.boundsMiddle.getX() <= -3){
                    fb.moveRight();
                }
            }
            //Down
            if(keysDown.contains(KeyEvent.VK_S)){
                fb.direction = 3;
                fb.enemy.setImage(fb.downMove);
                fb.moveDown();
                if(fb.boundsMiddle.getY()  + fb.boundsMiddle.getHeight()>= 720){
                    fb.moveUp();
                }
            }
            //Right
            if(keysDown.contains(KeyEvent.VK_D)){
                fb.direction = 4;
                fb.enemy.setImage(fb.rightMove);
                fb.moveRight();
                if(fb.boundsMiddle.getX() + fb.boundsMiddle.getWidth()>= 1344){
                    fb.moveLeft();
                }
            }
            //Up Left
            if(keysDown.contains(KeyEvent.VK_W) && keysDown.contains(KeyEvent.VK_A)){
                fb.direction = 5;
                fb.enemy.setImage(fb.upLeftMove);
            }
            //Up Right
            if(keysDown.contains(KeyEvent.VK_W) && keysDown.contains(KeyEvent.VK_D)){
                fb.direction = 6;
                fb.enemy.setImage(fb.upRightMove);
            }
            //Down Right
            if(keysDown.contains(KeyEvent.VK_S) && keysDown.contains(KeyEvent.VK_A)){
                fb.direction = 7;
                fb.enemy.setImage(fb.downLeftMove);
            }
            //Down Left
            if(keysDown.contains(KeyEvent.VK_S) && keysDown.contains(KeyEvent.VK_D)){
                fb.direction = 8;
                fb.enemy.setImage(fb.downRightMove);
            }
        }
        //This time, it's the other way around
        if(state == 9 && fb.state == 4){
            //Down
            if(keysDown.contains(KeyEvent.VK_W)){
                fb.direction = 3;
                fb.enemy.setImage(fb.downMove);
                fb.moveDown();
                if(fb.boundsMiddle.getY() + fb.boundsMiddle.getHeight()>= 720){
                    fb.moveUp();
                }
            }
            //Right
            if(keysDown.contains(KeyEvent.VK_A)){
                fb.direction = 4;
                fb.enemy.setImage(fb.rightMove);
                fb.moveRight();
                if(fb.boundsMiddle.getX() + fb.boundsMiddle.getWidth()>= 1344){
                    fb.moveLeft();
                }
            }
            //Up
            if(keysDown.contains(KeyEvent.VK_S)){
                fb.direction = 1;
                fb.enemy.setImage(fb.upMove);
                fb.moveUp();
                if(fb.boundsMiddle.getY() <= 25){
                    fb.moveDown();
                }
            }
            //Left
            if(keysDown.contains(KeyEvent.VK_D)){
                fb.direction = 2;
                fb.enemy.setImage(fb.leftMove);
                fb.moveLeft();
                if(fb.boundsMiddle.getX() <= -3){
                    fb.moveRight();
                }
            }
            //Down Right
            if(keysDown.contains(KeyEvent.VK_W) && keysDown.contains(KeyEvent.VK_A)){
                fb.direction = 8;
                fb.enemy.setImage(fb.downRightMove);
            }
            //Down Left
            if(keysDown.contains(KeyEvent.VK_W) && keysDown.contains(KeyEvent.VK_D)){
                fb.direction = 7;
                fb.enemy.setImage(fb.downLeftMove);
            }
            //Up Right
            if(keysDown.contains(KeyEvent.VK_S) && keysDown.contains(KeyEvent.VK_A)){
                fb.direction = 6;
                fb.enemy.setImage(fb.upRightMove);
            }
            //Up Left
            if(keysDown.contains(KeyEvent.VK_S) && keysDown.contains(KeyEvent.VK_D)){
                fb.direction = 5;
                fb.enemy.setImage(fb.upLeftMove);
            }
        }
        //When the user releases the key/s the enemy not only stops, but also changes the sprite to a stopped one
        if(state == 9 && (fb.state == 3 || fb.state == 4)){
            if(keysUp.contains(KeyEvent.VK_W) && keysUp.contains(KeyEvent.VK_A) && keysUp.contains(KeyEvent.VK_S) && keysUp.contains(KeyEvent.VK_D)){
                //Up
                if(fb.direction == 1){
                    fb.enemy.setImage(fb.upStill);
                } 
                //Left
                if(fb.direction == 2){
                    fb.enemy.setImage(fb.leftStill);
                }
                //Down
                if(fb.direction == 3){
                    fb.enemy.setImage(fb.downStill);
                }
                //Right
                if(fb.direction == 4){
                    fb.enemy.setImage(fb.rightStill);
                }
                //Up Left
                if(System.currentTimeMillis() - fb.upTimer<50 && System.currentTimeMillis() - fb.leftTimer<50){
                    fb.direction = 5;
                    fb.enemy.setImage(fb.upLeftStill);
                } else if(fb.direction == 5){
                    fb.enemy.setImage(fb.upLeftStill);
                }
                //Up Right
                if(System.currentTimeMillis() - fb.upTimer<50 && System.currentTimeMillis() - fb.rightTimer<50){
                    fb.direction = 6;
                    fb.enemy.setImage(fb.upRightStill);
                }else if(fb.direction == 6){
                    fb.enemy.setImage(fb.upRightStill);
                }
                //Down Left
                if(System.currentTimeMillis() - fb.downTimer<50 && System.currentTimeMillis() - fb.leftTimer<50){
                    fb.direction = 7;
                    fb.enemy.setImage(fb.downLeftStill);
                }else if(fb.direction == 7){
                    fb.enemy.setImage(fb.downLeftStill);
                }
                //Down Right
                if(System.currentTimeMillis() - fb.downTimer<50 && System.currentTimeMillis() - fb.rightTimer<50){
                    fb.direction = 8;
                    fb.enemy.setImage(fb.downRightStill);
                }else if(fb.direction == 8){
                    fb.enemy.setImage(fb.downRightStill);
                }
            }
        }
    }
    //The method to move the cannon
    void moveCannon(){
        if(state == 4){
            //Move the cannon to the Left
            if(keysDown.contains(KeyEvent.VK_A)){
                food.cannonMoveLeft();
            }
            //Move the cannon to the Right
            else if(keysDown.contains(KeyEvent.VK_D)){
                food.cannonMoveRight(1344);
            }
            //Shoot the bullet to the direction of the pointer 
            else if(keysDown.contains(KeyEvent.VK_S)){
                if(food.cannonFired == false  && food.bulletsLeft >= 1){
                    //lose 1 bullet
                    food.bulletsLeft --;
                    //Play the cannon exploding special effect
                    playCannonExplosion();
                    food.setBulletSpeed((food.pointer.getX()+food.pointer.getWidth()/2) , (food.pointer.getY()+food.pointer.getHeight()/2));
                }
            }
            //Shoot the bullet straight up
            else if(keysDown.contains(KeyEvent.VK_W)){
                if(food.cannonFired == false && food.bulletsLeft >= 2){
                    //lose 2 bullets
                    food.bulletsLeft -= 2;
                    //Play the cannon exploding special effect
                    playCannonExplosion();
                    food.setBulletSpeed(food.bullet.getRadius() + food.bullet.getX() , food.bullet.getY()-150);
                }
            }
        }
    }
    //The method to move the character
    void moveSora(){
        if(state == 2 || state == 5 || state == 6 || state == 8 || state == 9){
            //When the character is not attacking(As he cannot move and attack at the same time)
            //The image is also changed
            if(keysUp.contains(KeyEvent.VK_SPACE)){
                //Up
                if(keysDown.contains(KeyEvent.VK_W)){
                    cs.direction = 1;
                    cs.charSora.setImage(cs.upMove);
                    cs.moveSoraUp();
                    if(cs.s_boundsMiddle.getY() <= 25){
                        cs.moveSoraDown();
                    }
                }
                //Left
                if(keysDown.contains(KeyEvent.VK_A)){
                    cs.direction = 2;
                    cs.charSora.setImage(cs.leftMove);
                    cs.moveSoraLeft();
                    if(cs.s_boundsMiddle.getX() <= 3){
                        cs.moveSoraRight();
                    }
                }
                //Down
                if(keysDown.contains(KeyEvent.VK_S)){
                    cs.direction = 3;
                    cs.charSora.setImage(cs.downMove);
                    cs.moveSoraDown();
                    if(cs.s_boundsMiddle.getY() + cs.s_boundsMiddle.getHeight()>= 720){
                        cs.moveSoraUp();
                    }
                }
                //Right
                if(keysDown.contains(KeyEvent.VK_D)){
                    cs.direction = 4;
                    cs.charSora.setImage(cs.rightMove);
                    cs.moveSoraRight();
                    if(cs.s_boundsMiddle.getX() + cs.s_boundsMiddle.getWidth()>= 1344){
                        cs.moveSoraLeft();
                    }
                }
                //Up Left
                if(keysDown.contains(KeyEvent.VK_W) && keysDown.contains(KeyEvent.VK_A)){
                    cs.direction = 5;
                    cs.charSora.setImage(cs.upLeftMove);
                }
                //Up Right
                if(keysDown.contains(KeyEvent.VK_W) && keysDown.contains(KeyEvent.VK_D)){
                    cs.direction = 6;
                    cs.charSora.setImage(cs.upRightMove);
                }
                //Down Left
                if(keysDown.contains(KeyEvent.VK_S) && keysDown.contains(KeyEvent.VK_A)){
                    cs.direction = 7;
                    cs.charSora.setImage(cs.downLeftMove);
                }
                //Down Right
                if(keysDown.contains(KeyEvent.VK_S) && keysDown.contains(KeyEvent.VK_D)){
                    cs.direction = 8;
                    cs.charSora.setImage(cs.downRightMove);
                }
            }
            //When the user releases the buttons, he stops and the image is changed into a stopped one
            if(keysUp.contains(KeyEvent.VK_W) && keysUp.contains(KeyEvent.VK_A) && keysUp.contains(KeyEvent.VK_S) && keysUp.contains(KeyEvent.VK_D) && keysUp.contains(KeyEvent.VK_SPACE)){
                //Up
                if(cs.direction == 1){
                    cs.charSora.setImage(cs.upStill);
                } 
                //Left
                if(cs.direction == 2){
                    cs.charSora.setImage(cs.leftStill);
                }
                //Down
                if(cs.direction == 3){
                    cs.charSora.setImage(cs.downStill);
                }
                //Right
                if(cs.direction == 4){
                    cs.charSora.setImage(cs.rightStill);
                }
                //Up Left
                if(System.currentTimeMillis() - cs.upTimer<50 && System.currentTimeMillis() - cs.leftTimer<50){
                    cs.direction = 5;
                    cs.charSora.setImage(cs.upLeftStill);
                } else if(cs.direction == 5){
                    cs.charSora.setImage(cs.upLeftStill);
                }
                //Up Right
                if(System.currentTimeMillis() - cs.upTimer<50 && System.currentTimeMillis() - cs.rightTimer<50){
                    cs.direction = 6;
                    cs.charSora.setImage(cs.upRightStill);
                }else if(cs.direction == 6){
                    cs.charSora.setImage(cs.upRightStill);
                }
                //Down Left
                if(System.currentTimeMillis() - cs.downTimer<50 && System.currentTimeMillis() - cs.leftTimer<50){
                    cs.direction = 7;
                    cs.charSora.setImage(cs.downLeftStill);
                }else if(cs.direction == 7){
                    cs.charSora.setImage(cs.downLeftStill);
                }
                //Down Right
                if(System.currentTimeMillis() - cs.downTimer<50 && System.currentTimeMillis() - cs.rightTimer<50){
                    cs.direction = 8;
                    cs.charSora.setImage(cs.downRightStill);
                }else if(cs.direction == 8){
                    cs.charSora.setImage(cs.downRightStill);
                }
            }
            /*If the player presses space, he attacks.
              In this case, the image turns to an attacking one.
              However the player only attacks once per press so in order to do 
              successive attacks he must press the button over and over.
              In different states the enemy is different, this is why there are
              3 version for each direction. Also the homerun blow is taken into effect in this state.*/
            if(canHit == true){
                if(keysDown.contains(KeyEvent.VK_SPACE)){
                    if(System.currentTimeMillis() - battleQuoteTimer > 500){
                        //Play the battle quote
                        playBattleQuote();
                        battleQuoteTimer = System.currentTimeMillis();
                    }
                    cs.hitTimer = System.currentTimeMillis();
                    //Up
                    if(cs.direction == 1){
                        cs.charSora.setImage(cs.upAttack[meleeWeaponChoice]);
                        if(state == 6 && System.currentTimeMillis()-me.normalTimer<5000){
                            if(cs.s_boundsUpLeft.hasCollidedWith(me.boundsMiddle) || cs.s_boundsUp.hasCollidedWith(me.boundsMiddle) || cs.s_boundsUpRight.hasCollidedWith(me.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(me.enemy, 1344, 720);
                                    me.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    me.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                me.damageStars();
                            }
                        }
                        if(state == 8){
                            if(cs.s_boundsUpLeft.hasCollidedWith(re.boundsMiddle) || cs.s_boundsUp.hasCollidedWith(re.boundsMiddle) || cs.s_boundsUpRight.hasCollidedWith(re.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(re.enemy, 1344, 720);
                                    re.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    re.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                re.damageStars();
                            }
                        }
                        if(state == 9 && fb.state != 6){
                            if(cs.s_boundsUpLeft.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsUp.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsUpRight.hasCollidedWith(fb.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(fb.enemy, 1344, 720);
                                    fb.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    fb.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                if(System.currentTimeMillis() - rikuDamagedQuoteTimer>500){
                                    playRikuDamagedQuote();
                                    rikuDamagedQuoteTimer = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                    //Left
                    else if(cs.direction == 2){
                        cs.charSora.setImage(cs.leftAttack[meleeWeaponChoice]);
                        if(state == 6 && System.currentTimeMillis()-me.normalTimer<5000){
                            if(cs.s_boundsUpLeft.hasCollidedWith(me.boundsMiddle) || cs.s_boundsLeft.hasCollidedWith(me.boundsMiddle) || cs.s_boundsDownLeft.hasCollidedWith(me.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(me.enemy, 1344, 720);
                                    me.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    me.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                me.damageStars();
                            }
                        }
                        if(state == 8){
                            if(cs.s_boundsUpLeft.hasCollidedWith(re.boundsMiddle) || cs.s_boundsLeft.hasCollidedWith(re.boundsMiddle) || cs.s_boundsDownLeft.hasCollidedWith(re.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(re.enemy, 1344, 720);
                                    re.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    re.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                re.damageStars();
                            }
                        }
                        if(state == 9 && fb.state != 6){
                            if(cs.s_boundsUpLeft.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsLeft.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsDownLeft.hasCollidedWith(fb.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(fb.enemy, 1344, 720);
                                    fb.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    fb.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                if(System.currentTimeMillis() - rikuDamagedQuoteTimer>500){
                                    playRikuDamagedQuote();
                                    rikuDamagedQuoteTimer = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                    //Down
                    else if(cs.direction == 3){
                        cs.charSora.setImage(cs.downAttack[meleeWeaponChoice]);
                        if(state == 6 && System.currentTimeMillis()-me.normalTimer<5000){
                            if(cs.s_boundsDownLeft.hasCollidedWith(me.boundsMiddle) || cs.s_boundsDown.hasCollidedWith(me.boundsMiddle) || cs.s_boundsDownRight.hasCollidedWith(me.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(me.enemy, 1344, 720);
                                    me.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    me.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                me.damageStars();
                            }
                        }
                        if(state == 8){
                            if(cs.s_boundsDownLeft.hasCollidedWith(re.boundsMiddle) || cs.s_boundsDown.hasCollidedWith(re.boundsMiddle) || cs.s_boundsDownRight.hasCollidedWith(re.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(re.enemy, 1344, 720);
                                    re.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    re.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                re.damageStars();
                            }
                        }
                        if(state == 9 && fb.state != 6){
                            if(cs.s_boundsDownLeft.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsDown.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsDownRight.hasCollidedWith(fb.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(fb.enemy, 1344, 720);
                                    fb.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    fb.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                if(System.currentTimeMillis() - rikuDamagedQuoteTimer>500){
                                    playRikuDamagedQuote();
                                    rikuDamagedQuoteTimer = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                    //Right
                    else if(cs.direction == 4){
                        cs.charSora.setImage(cs.rightAttack[meleeWeaponChoice]);
                        if(state == 6 && System.currentTimeMillis()-me.normalTimer<5000){
                            if(cs.s_boundsUpRight.hasCollidedWith(me.boundsMiddle) || cs.s_boundsRight.hasCollidedWith(me.boundsMiddle) || cs.s_boundsDownRight.hasCollidedWith(me.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(me.enemy, 1344, 720);
                                    me.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    me.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                me.damageStars();
                            }
                        }
                        if(state == 8){
                            if(cs.s_boundsUpRight.hasCollidedWith(re.boundsMiddle) || cs.s_boundsRight.hasCollidedWith(re.boundsMiddle) || cs.s_boundsDownRight.hasCollidedWith(re.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(re.enemy, 1344, 720);
                                    re.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    re.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                re.damageStars();
                            }
                        }
                        if(state == 9 && fb.state != 6){
                            if(cs.s_boundsUpRight.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsRight.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsDownRight.hasCollidedWith(fb.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(fb.enemy, 1344, 720);
                                    fb.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    fb.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                if(System.currentTimeMillis() - rikuDamagedQuoteTimer>500){
                                    playRikuDamagedQuote();
                                    rikuDamagedQuoteTimer = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                    //Up Left
                    else if(cs.direction == 5){
                        cs.charSora.setImage(cs.upLeftAttack[meleeWeaponChoice]);
                        if(state == 6 && System.currentTimeMillis()-me.normalTimer<5000){
                            if(cs.s_boundsUp.hasCollidedWith(me.boundsMiddle) || cs.s_boundsUpLeft.hasCollidedWith(me.boundsMiddle) || cs.s_boundsLeft.hasCollidedWith(me.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(me.enemy, 1344, 720);
                                    me.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    me.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                me.damageStars();
                            }
                        }
                        if(state == 8){
                            if(cs.s_boundsUp.hasCollidedWith(re.boundsMiddle) || cs.s_boundsUpLeft.hasCollidedWith(re.boundsMiddle) || cs.s_boundsLeft.hasCollidedWith(re.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(re.enemy, 1344, 720);
                                    re.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    re.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                re.damageStars();
                            }
                        }
                        if(state == 9 && fb.state != 6){
                            if(cs.s_boundsUp.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsUpLeft.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsLeft.hasCollidedWith(fb.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(fb.enemy, 1344, 720);
                                    fb.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    fb.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                if(System.currentTimeMillis() - rikuDamagedQuoteTimer>500){
                                    playRikuDamagedQuote();
                                    rikuDamagedQuoteTimer = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                    //Up Right
                    else if(cs.direction == 6){
                        cs.charSora.setImage(cs.upRightAttack[meleeWeaponChoice]);
                        if(state == 6 && System.currentTimeMillis()-me.normalTimer<5000){
                            if(cs.s_boundsUp.hasCollidedWith(me.boundsMiddle) || cs.s_boundsUpRight.hasCollidedWith(me.boundsMiddle) || cs.s_boundsUpRight.hasCollidedWith(me.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(me.enemy, 1344, 720);
                                    me.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    me.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                me.damageStars();
                            }
                        }
                        if(state == 8){
                            if(cs.s_boundsUp.hasCollidedWith(re.boundsMiddle) || cs.s_boundsUpRight.hasCollidedWith(re.boundsMiddle) || cs.s_boundsUpRight.hasCollidedWith(re.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(re.enemy, 1344, 720);
                                    re.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    re.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                re.damageStars();
                            }
                        }
                        if(state == 9 && fb.state != 6){
                            if(cs.s_boundsUp.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsUpRight.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsUpRight.hasCollidedWith(fb.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(fb.enemy, 1344, 720);
                                    fb.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    fb.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                if(System.currentTimeMillis() - rikuDamagedQuoteTimer>500){
                                    playRikuDamagedQuote();
                                    rikuDamagedQuoteTimer = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                    //Down Left
                    else if(cs.direction == 7){
                        cs.charSora.setImage(cs.downLeftAttack[meleeWeaponChoice]);
                        if(state == 6 && System.currentTimeMillis()-me.normalTimer<5000){
                            if(cs.s_boundsDown.hasCollidedWith(me.boundsMiddle) || cs.s_boundsDownLeft.hasCollidedWith(me.boundsMiddle) || cs.s_boundsLeft.hasCollidedWith(me.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(me.enemy, 1344, 720);
                                    me.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    me.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                me.damageStars();
                            }
                        }
                        if(state == 8){
                            if(cs.s_boundsDown.hasCollidedWith(re.boundsMiddle) || cs.s_boundsDownLeft.hasCollidedWith(re.boundsMiddle) || cs.s_boundsLeft.hasCollidedWith(re.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(re.enemy, 1344, 720);
                                    re.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    re.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                re.damageStars();
                            }
                        }
                        if(state == 9 && fb.state != 6){
                            if(cs.s_boundsDown.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsDownLeft.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsLeft.hasCollidedWith(fb.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(fb.enemy, 1344, 720);
                                    fb.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    fb.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                if(System.currentTimeMillis() - rikuDamagedQuoteTimer>500){
                                    playRikuDamagedQuote();
                                    rikuDamagedQuoteTimer = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                    //Down Right
                    else if(cs.direction == 8){
                        cs.charSora.setImage(cs.downRightAttack[meleeWeaponChoice]);
                        if(state == 6 && System.currentTimeMillis()-me.normalTimer<5000){
                            if(cs.s_boundsDown.hasCollidedWith(me.boundsMiddle) || cs.s_boundsDownRight.hasCollidedWith(me.boundsMiddle) || cs.s_boundsRight.hasCollidedWith(me.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(me.enemy, 1344, 720);
                                    me.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    me.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                me.damageStars();
                            }
                        }
                        if(state == 8){
                            if(cs.s_boundsDown.hasCollidedWith(re.boundsMiddle) || cs.s_boundsDownRight.hasCollidedWith(re.boundsMiddle) || cs.s_boundsRight.hasCollidedWith(re.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(re.enemy, 1344, 720);
                                    re.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    re.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                re.damageStars();
                            }
                        }
                        if(state == 9 && fb.state != 6){
                            if(cs.s_boundsDown.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsDownRight.hasCollidedWith(fb.boundsMiddle) || cs.s_boundsRight.hasCollidedWith(fb.boundsMiddle)){
                                if(cs.homerunBlow == true){
                                    cs.homerunBlowKnocback(fb.enemy, 1344, 720);
                                    fb.hp-=shop.getDamage(meleeWeaponChoice)*3;
                                }else{
                                    fb.hp-=shop.getDamage(meleeWeaponChoice);
                                }
                                playKeybladeHit();
                                if(System.currentTimeMillis() - rikuDamagedQuoteTimer>500){
                                    playRikuDamagedQuote();
                                    rikuDamagedQuoteTimer = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                    //reset the can Hit
                    canHit = false;
                    //start the cooldown timer if homerun blow was active
                    if(cs.homerunBlow == true){
                        cs.homerunBlow = false;
                        cs.homerunBlowCooldownTimer = System.currentTimeMillis();
                    }
                }
            }
            //When space is released or a timer passes, the player can deal damage again
            if(keysUp.contains(KeyEvent.VK_SPACE) || (System.currentTimeMillis() - cs.hitTimer > 400)){
                canHit = true;
            }
            //When the character is in state 2, he can go to different rooms/places
            //This is made by stepping on a portal and pressing E while on it.
            if(keysDown.contains(KeyEvent.VK_E)){
                if(state == 2){
                    //Here the user wants to go to the mineshaft
                    if(cs.s_boundsMiddle.hasCollidedWith(base.mineshaft)){
                        //start new music
                        stopMusic();
                        playMineshaft();
                        //Clear the keysDown
                        keysDown.clear();
                        //reset the positions of the bombs
                        setForGoldState();
                        //reset the drop positions
                        gold.resetGoldDrop(1244,620, gold.drop[0], 0);
                        gold.resetGoldDrop(1244,620, gold.drop[1], 1);
                        //Make the back/exit button visible
                        gold.btnBack.setVisible(true);
                        //Make sure the immortality and speed time are reset
                        gold.immortalityTime = System.currentTimeMillis()-10000;
                        gold.speedTime = System.currentTimeMillis()-10000;
                        //place the character in the middle of the screen
                        resetSoraPositions();
                        //Finally, start the state
                        state = 5;
                        //reduce the food by 2
                        cs.food -= 2;
                    }
                    //Whent he user wants to buy
                    else if(cs.s_boundsMiddle.hasCollidedWith(base.shop)){
                        //clear the keysDown
                        keysDown.clear();
                        //set the shop state to the opening screen
                        shop.state = 0;
                        //Display the opening buttons
                        shop.shopInterfaceShopOrSell();
                        //place the character in the middle of the screen
                        resetSoraPositions();
                        //start state 3
                        state = 3;
                    }
                    else if(cs.s_boundsMiddle.hasCollidedWith(base.food)){
                        //clear the keysDown
                        keysDown.clear();
                        //reset the bulletsLeft
                        food.bulletsLeft = 40;
                        //start new music
                        stopMusic();
                        playFoodMusic();
                        //Display the back button
                        food.btnBack.setVisible(true);
                        //reset the number of sheep hit
                        food.sheepHit = 0;
                        //place the character in the middle of the screen
                        resetSoraPositions();
                        //place the cannon in the middle of the screen
                        resetCannonPositions();
                        //Put the bullet in the cannon
                        food.resetBulletPosition();
                        //start state 4
                        state = 4;
                        //decrease the food by 1
                        cs.food --;
                    }
                    //If the user wants to save or load his data
                    else if(cs.s_boundsMiddle.hasCollidedWith(base.save)){
                        //place the character in the middle of the screen
                        resetSoraPositions();
                        //Make the buttons for this mode visible
                        sl.makeButtonsVisible();
                        //Start state 13
                        state = 13;
                    }
                    //User is ready to fight the final boss
                    else if(cs.s_boundsMiddle.hasCollidedWith(base.finalBoss) && cs.level == 10){
                        //Reset the ability coldowns
                        cs.resetCooldowns();
                        //clear the keysDown
                        keysDown.clear();
                        //The image of the enemy
                        cw.enemyImage = tk.createImage("FinalBossLogo.png");
                        //The name of the enemy
                        cw.fightOrFlight("__??__");
                        //reset the positions of the final boss
                        resetFinalBossPositions();
                        //reposition the labels of the items 
                        meleePickingLabelsPositions();
                        rangedPickingLabelsPositions();
                        //start state 7
                        state = 7;
                        //reduce the food count by 2
                        cs.food -= 2;
                    }
                    //The user wants to battle a normal enemy
                    else if(cs.s_boundsMiddle.hasCollidedWith(base.enemy)){
                        //Reset the ability coldowns
                        cs.resetCooldowns();
                        //clear the keysDown
                        keysDown.clear();
                        //The enemy is randomly generated, and to reroll multiple times, the player mus use 2 food each time
                        meleeOrRanged = (int)(1+Math.random()*2);
                        if(meleeOrRanged == 1){
                            //the image of the enemy
                            cw.enemyImage = tk.createImage("ShadowHeartless.png");
                            //The name of the enemy
                            cw.fightOrFlight("Shadow Heartless");
                            //reset the positions of the enemy
                            resetMeleeEnemyPositions();
                        }else if(meleeOrRanged == 2){
                            //Reset the first time boolean
                            re.firstTime = true;
                            //the image of the enemy
                            cw.enemyImage = tk.createImage("BlueRhapsody.png");
                            //The name of the enemy
                            cw.fightOrFlight("Blue Rhapsody");
                            //reset the positions of the enemy
                            resetRangedEnemyPositions();
                        }
                        //reposition the labels of the items 
                        meleePickingLabelsPositions();
                        rangedPickingLabelsPositions();
                        //start state 7
                        state = 7;
                        //reduce the food count by 2
                        cs.food -= 2;
                    }
                    //When the user wants to display the credits
                    else if(cs.s_boundsMiddle.hasCollidedWith(base.credits)){
                        //The credits background is set
                        credits.background = credits.img[credits.imgCounter];
                        //The images will be displayed on screen as the backgrounds
                        background = credits.background;
                        //The buttons for the beginning of this state are displayed
                        credits.btnExit.setVisible(true);
                        credits.btnNext.setVisible(true);
                        //State 10 is started
                        state = 10;
                    }
                    //When the user wants to see the instructions
                    else if(cs.s_boundsMiddle.hasCollidedWith(base.instructions)){
                        //The instructions background is set
                        instructions.background = instructions.img[instructions.imgCounter];
                        //The images will be displayed on screen as the backgrounds
                        background = instructions.background;
                        //The buttons for the beginning of this state are displayed
                        instructions.btnExit.setVisible(true);
                        instructions.btnNext.setVisible(true);
                        //State 10 is started
                        state = 16;
                    }
                }
            }
            //Make the heal animation
            if(System.currentTimeMillis()-cs.healAnimationTimer<600){
                cs.charSora.setImage(cs.healAnimation);
            }
        }
    }
    public void actionPerformed(ActionEvent e){
        //This is the boolean which will turn false if the error sound
        //should be heard instead of the normal button sound
        boolean canPlayNormalSound = true;
        //When the player is shopping is shopping
        if(e.getSource() == shop.btnBack){
            //go back to state 2 if the player is in the shop opening screen
            if(shop.state == 0){
                resetSoraPositions();
                state = 2;
            }
            //Do the methods in the shop class
            shop.btnBack(booleanOwned);
        }else if(e.getSource() == shop.btnShop){
            //Do the method in the shop class
            shop.btnShop();
            //reposition the labels accordingly
            resetLabelsPositions();
        }else if(e.getSource() == shop.btnSell){
            //do the method in the shop class
            sellingLabelsPositions();
            //reposition the labels accordingly
            shop.btnSell();
        }else if(e.getSource() == shop.btnMelee){
            //do the method in the shop class
            shop.btnMelee();
        }else if(e.getSource() == shop.btnRanged){
            //do the method in the shop class
            shop.btnRanged();
        }else if(e.getSource() == shop.btnArmor){
            //do the method in the shop class
            shop.btnArmor();
        }else if(e.getSource() == food.btnBack){
            //Stop th music, check if the user has gained a level, else go directly to state 2
            stopMusic();
            if(cs.level<10){
                //If the user has gained a level, go to the level up screen
                if(cs.xp>=cs.xpForNextLevel[cs.level-1]){
                    lus.showLabels(cs.level-1);
                    playLevelUp();
                    state = 11;
                }else{
                    playMainRoom();
                    state = 2;
                }
            }else{
                playMainRoom();
                state = 2;
            }
            //Reset items
            food.btnBack(40, 1344, 180);
            resetSoraPositions();
        }else if(e.getSource() == gold.btnBack){
            //Play the mian room music
            stopMusic();
            playMainRoom();
            //reset items
            gold.btnBack();
            resetSoraPositions();
            //go to the main room
            state = 2;
        }else if(e.getSource() == cw.btnFlight){
            //Exiting the weapon selection state and going back to state 2
            cw.btnFlight();
            resetSoraPositions();
            state = 2;
        }else if(e.getSource() == cw.btnContinue){
            //The continue button for the weapon select
            if(cw.state == 4){
                //Whent he user is ready, the enemy is reset
                me.enemy.moveTo(100,100);
                //The detection of which enemy is detected by seeing on which portal the player is on
                if(cs.s_boundsMiddle.hasCollidedWith(base.finalBoss)){
                    //Final boss battle
                    resetSoraPositions();
                    stopMusic();
                    state = 9;
                }else{
                    //Normal enemy battle
                    resetSoraPositions();
                    stopMusic();
                    playBattleMusic();
                    //This number(melee or ranged) is determined when the player first enters on the portal
                    if(meleeOrRanged == 1){
                        state = 6;
                    }else{
                        state = 8;
                    }
                }
                //Check if a melee weapon is picked
                for(int i = 0;i<5;i++){
                    if(cw.picked[i] == true){
                        meleeWeaponChoice = i;
                    }
                }
                //Check if a ranged weapon is picked and change the image of the bullet
                for(int i = 5;i<10;i++){
                    if(cw.picked[i] == true){
                        rangedWeaponChoice = i;
                        cs.bullet.setImage(cs.bulletImage[i-5]);
                    }
                }
            }
            //Do the method in the class
            cw.btnContinue(shop.booleanOwned, shop.booleanMelee, shop.booleanRanged, shop.booleanArmor, shop.lblName, shop.lblMindmg, shop.lblMaxdmg, shop.lblArmor, 0, 5, 5, 5, meleeWeaponChoice);
            //If cw.state == 4 AFTER the btn Continue is pressed
            if(cw.state == 4){
                //Show the picked items
                int counter = 0;
                for(int i = 0;i<10;i++){
                    if(cw.picked[i] == true || (shop.booleanOwned[i] == true && shop.booleanArmor[i])){
                        shop.lblName[i].setBounds(200,200 + counter*60,130,30);
                        shop.lblMindmg[i].setBounds(389,200 + counter*60,130,30);
                        shop.lblMaxdmg[i].setBounds(578,200 + counter*60,130,30);
                        shop.lblArmor[i].setBounds(767,200 + counter*60,130,30);
                        counter++;
                    }
                }
                //Show the armor items
                for(int i = 10;i<15;i++){
                    if(shop.booleanOwned[i] == true && shop.booleanArmor[i]){
                        shop.lblName[i].setBounds(200,200 + counter*60,130,30);
                        shop.lblMindmg[i].setBounds(389,200 + counter*60,130,30);
                        shop.lblMaxdmg[i].setBounds(578,200 + counter*60,130,30);
                        shop.lblArmor[i].setBounds(767,200 + counter*60,130,30);
                        counter++;
                    }
                }
            }
        }else if(e.getSource() == cw.btnExit){
            //Exit the weapon selection screen
            cw.btnExit(shop.lblName, shop.lblMindmg, shop.lblMaxdmg, shop.lblArmor);
            state = 2;
            resetSoraPositions();
        }else if(e.getSource() == cw.btnFight){
            //Show the mele items
            cw.btnFight(shop.booleanOwned, shop.booleanMelee, shop.lblName, shop.lblMindmg, shop.lblMaxdmg, shop.lblArmor);
        }else if(e.getSource() == cw.btnBack){
            //Do the method in the class and also set the positions of the labels to their picking positions
            meleePickingLabelsPositions();
            rangedPickingLabelsPositions();
            cw.btnBack(shop.booleanOwned, shop.booleanMelee, shop.booleanRanged, shop.lblName, shop.lblMindmg, shop.lblMaxdmg, shop.lblArmor, 5, 5);
        }else if(e.getSource() == credits.btnNext){
            //do the method in the class(display the next image of the slidshow)
            credits.btnNext();
        }else if(e.getSource() == credits.btnPrevious){
            //do the method in the class(display the previous image of the slidshow)
            credits.btnPrevious();
        }else if(e.getSource() == credits.btnExit){
            //do the method in the class(exit and reset the image counter) and go back to state 2
            resetSoraPositions();
            credits.btnExit();
            state = 2;
        }else if(e.getSource() == instructions.btnNext){
            //do the method in the class(display the next image of the slidshow)
            instructions.btnNext();
        }else if(e.getSource() == instructions.btnPrevious){
            //do the method in the class(display the previous image of the slidshow)
            instructions.btnPrevious();
        }else if(e.getSource() == instructions.btnExit){
            //do the method in the class(exit and reset the image counter) and go back to state 2
            resetSoraPositions();
            instructions.btnExit();
            state = 2;
        }else if(e.getSource() == lus.btnContinue){
            //When the user levels up, give him and the enemy their extra things
            applyLevelUp();
            //Check if there is a seond level up just in case
            if(cs.level<10){
                if(cs.xp>=cs.xpForNextLevel[cs.level-1]){
                    lus.showLabels(cs.level-1);
                    playLevelUp();
                    state = 11;
                }else{
                    //Go back to the main room
                    state = 2;
                    playMainRoom();
                }
            }else{
                //Go back to the main room
                playMainRoom();
                state = 2;
            }
            //Do the method in the class and set the character to the middle of the screen
            resetSoraPositions();
            lus.btnContinue();
        }else if(e.getSource() == sl.btnSave){
            //Save items to the save file
            save();
        }else if(e.getSource() == sl.btnLoad){
            //Load items from the save file
            load();
        }else if(e.getSource() == sl.btnExit){
            //Hide buttons and go back to state 2
            sl.btnExit();
            resetSoraPositions();
            state = 2;
        }else if(e.getSource() == eds.btnContinue){
            //When enemy is defeated, the continue button:
            //hides the current buttons and labels
            eds.btnContinue();
            //Checks if their is a level up
            state = 11;
            if(cs.level<10){
                if(cs.xp>=cs.xpForNextLevel[cs.level-1]){
                    lus.showLabels(cs.level-1);
                    playLevelUp();
                    state = 11;
                }else{
                    playMainRoom();
                    state = 2;
                }
            }else{
                //else, return to the main room
                playMainRoom();
                state = 2;
            }
        }else if(e.getSource() == ds.btnLoad){
            //Play the main room music
            playMainRoom();
            //Load items
            load();
            //Return to the main room
            state= 2;
            resetSoraPositions();
            //If the character had died from hunger, make the hunger to 1
            if(cs.food<=0){
                cs.food = 1;
            }
        }else if(e.getSource() == ds.btnContinue){
            //If the character had died from hunger, make the hunger to 1
            if(cs.food<=0){
                cs.food = 1;
            }
            //Hide the buttns and go to the main room
            resetSoraPositions();
            ds.hideButtons();
            playMainRoom();
            state = 2;
        }else if(e.getSource() == btnSFX){
            //De/activate the SFX
            if(canPlaySFX== true){
                canPlaySFX = false;
            }else{
                canPlaySFX = true;
            }
        }else if(e.getSource() == btnMusic){
            //De/activate the Music
            if(canPlayMusic == true){
                canPlayMusic = false;
                stopMusic();
            }else{
                //Activate the music
                canPlayMusic = true;
                if(state == 2 || state == 3 || state == 7 || state == 10 || state == 11 || state == 12 || state == 13 || state == 16 || state == 17){
                    playMainRoom();
                }else if(state == 4){
                    playFoodMusic();
                }else if(state == 5){
                    playMineshaft();
                }else if(state == 6 || state == 8){
                    playBattleMusic();
                }else if(state == 9 || state == 18 || state == 19){
                    playFinalBoss();
                }
            }
        }else if(e.getSource() == btnVoices){
            //De/activate the Voices
            if(canPlayVoices == true){
                canPlayVoices = false;
            }else{
                canPlayVoices = true;
            }
        }else if(e.getSource() == ps.btnContinue){
            //Remove the button and stop being paused
            ps.btnContinue();
            paused = false;
            cs.unPause();
            //If it is in the gold state, make the button visible again
            if(state == 5){
                gold.btnBack.setVisible(true);
            }
        }else if(e.getSource() == begin.btnContinue){
            if(begin.labelNumber == begin.sentence.length-1 && begin.sentenceFinished == true){
                //The instructions background is set
                instructions.background = instructions.img[instructions.imgCounter];
                //The images will be displayed on screen as the backgrounds
                background = instructions.background;
                //The buttons for the beginning of this state are displayed
                instructions.btnExit.setVisible(true);
                instructions.btnNext.setVisible(true);
                //State 10 is started
                state = 16;
                //Hide the button
                begin.btnContinue.setVisible(false);
                //Hide the label
                begin.lblSentence[begin.lblSentence.length-1].setVisible(false);
            }else{
                //do the method in the class
                begin.btnContinue();
            }
        }else if(e.getSource() == end.btnContinue){
            if(end.labelNumber == end.sentence.length-1 && end.sentenceFinished == true){
                //set it to the end state
                state = 19;
                //Hide the button
                end.btnContinue.setVisible(false);
                //Hide the label
                end.lblSentence[end.sentence.length-1].setVisible(false);
            }else{
                //do the method in the class
                end.btnContinue();
            }
        }
        //The pick buttons
        for(int i = 0;i<10;i++){
            if(e.getSource() == cw.btnPick[i]){
                if(cw.state == 2){
                    //Picing a melee item
                    cw.pickThis(i,0,5);
                }else if(cw.state == 3){
                    //Picking a ranged item
                    cw.pickThis(i,5,5);
                }
            }
        }
        //The buttons to buy the items
        for(int i = 0;i<15;i++){
            if(e.getSource() == shop.btnBuyItem[i]){
                //The player cannot buy more than 10 items
                if(cs.ownedItems<10){
                    //Checks that the player has more gold than the item's price
                    if(cs.gold >= shop.price[i]){
                        //If the command is accepted:
                        //Disable the button
                        shop.btnBuyItem[i].setEnabled(false);
                        //Make the item owned
                        shop.booleanOwned[i] = true;
                        booleanOwned[i] = true;
                        //Increase the number of owned items
                        cs.ownedItems++;
                        //The gold is decreased
                        cs.gold-=shop.price[i];
                        //Make the melee weapon bought as the meleeWeaponChoice
                        if(i>=0 && i<5){
                            meleeWeaponChoice = i;
                        }
                        //OR make the ranged weapon bought as the meleeWeaponChoice 
                        if(i>=5 && i<10){
                            rangedWeaponChoice = i;
                            cs.bullet.setImage(cs.bulletImage[i-5]);
                        }
                        //Increase the armor by that of the item's
                        cs.armor += shop.armor[i];
                    }else{
                        //Else if the user doesn't have enough gold, start the error message timer
                        notEnoughGoldTimer = System.currentTimeMillis();
                        //Play the error sound
                        playErrorButtonSound();
                        //Don't let the program play the normal button sound
                        canPlayNormalSound = false;
                    }
                }else{
                    //Else if the user has 10 items already, start the error message timer
                    itemLimitTimer = System.currentTimeMillis();
                    //Play the error sound
                    playErrorButtonSound();
                    //Don't let the program play the normal button sound
                    canPlayNormalSound = false;
                }
            }
        }
        //The buttons to sell the items
        for(int i = 0;i<10;i++){
            if(e.getSource() == shop.btnSellItem[i]){
                for(int x=0,counter=0;x<15;x++){
                    if(shop.booleanOwned[x] == true){
                        if(counter == i){
                            int meleeCounter = 0;
                            //The player may not sell all of his melee items
                            for(int i2 = 0;i2<shop.booleanMelee.length;i2++){
                                if(booleanOwned[i2] == true && shop.booleanMelee[i2] == true){
                                    meleeCounter++;
                                }
                            }
                            //If the player tries to sell all his melee items
                            if(shop.booleanMelee[x] == true && meleeCounter<=1){
                                //The timer for the error message starts
                                meleeItemTimer = System.currentTimeMillis();
                                //Play the error sound
                                playErrorButtonSound();
                                //Don't let the program play the normal button sound
                                canPlayNormalSound = false;
                            }else{
                                //Else: Disable the sell button
                                shop.btnSellItem[i].setEnabled(false);
                                //Make the item not owned
                                booleanOwned[x] = false;
                                //Decrease the owned items
                                cs.ownedItems--;
                                //Increase the gold by half the price
                                cs.gold+=(shop.price[x]/2);
                            }
                            //reduce the armor
                            cs.armor -= shop.armor[x];
                        }
                        counter++;
                    }
                }
            }
        }
        //This is done here because the timer is set in  nother class
        if(System.currentTimeMillis()-cw.noItemPickedTimer<20){
            playErrorButtonSound();
            canPlayNormalSound = false;
        }
        //Unless otherwise, play the normal button pressed sound
        if(canPlayNormalSound == true){
            playButtonSound();
        }
        //After pressing a button, request the focus once again
        requestFocus();
    }
    
    public void mousePressed(java.awt.event.MouseEvent m) {} 
    public void mouseReleased(java.awt.event.MouseEvent m){
        //get the X and y coordinates of the click
        int px = m.getX();
        int py = m.getY();
        //Make p a new point
        Point p = new Point(px,py);
        //Shoot the bullet in the food state
        /*if(food.cannonFired == false && food.bulletsLeft >= 2){
            food.bulletsLeft -= 2;
            playCannonExplosion();
            food.setBulletSpeed(px,py);
        }*/
        //The methods to shoot the character bullet in the main room and other battle fields
        if(state == 6 || state == 2 || state == 8 || state == 9){
            //This only works if the player owns a ranged item
            boolean allow = false;
            for(int i = 5;i<10;i++){
                if(shop.booleanOwned[i] == true){
                    allow = true;
                }
            }
            //Checks that the bullet is not already fired
            if(cs.bulletFired == false){
                if(allow){
                    if(System.currentTimeMillis() - battleQuoteTimer > 500){
                        //Play the battle quote
                        playBattleQuote();
                        battleQuoteTimer = System.currentTimeMillis();
                    }
                    //Make the melee enemey dodge the bullet
                    me.enemyState3Timer = System.currentTimeMillis();
                    //Set the bullet directions
                    cs.setBulletDirections(px,py,5,5,shop.booleanOwned);
                }
                //If it was the stun bullet, start it's cooldown timer
                if(cs.stunBullet == true && System.currentTimeMillis() - cs.stunBulletCooldownTimer > 5000){
                     cs.stunBulletCooldownTimer = System.currentTimeMillis();
                }
            }
        }
    } 
    public void mouseEntered(java.awt.event.MouseEvent m) {
        //This is made to avoid keeping the focus on a button if you click on it then drag the cursor away from it.
        requestFocus();
    } 
    public void mouseExited(java.awt.event.MouseEvent m) {}
    public void mouseClicked(MouseEvent m) {}
    //The main method to run the class
    public static void main(String args[]){
        KHMoM kh = new KHMoM();
        kh.setLocation(0,0);
        kh.setSize(1344,720);
        kh.setResizable(false);
        kh.setVisible(true);
        //This part of the code makes the X button in the top right of the screen exit the program
        kh.addWindowListener(
           new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                     System.exit(0);
                }
           }
        );
    }
}