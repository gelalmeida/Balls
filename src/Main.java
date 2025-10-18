import java.util.Scanner;

final String QUIT_MESS = "EXIT";
final String BYE_MESS = "Bye";//TODO trocar isto
final String ERROR = "Invalid command";
final char HUMAN_PLAYER ='P';
final char START ='S';
final char[] POSSIBLE_OPPS_IDS ={'a','b','c','d','e','f','g','h','i'};
final int MIN_SPEED = 0;
final int SPACES_SEEN_BY_BOTS = 3;
final char BOOST = '+';
final char OIL = '!';
char [] playersIds;
int [] velocity;
int [] playerPositions;
int[] playerLaps;
char []track;
int raceLaps;
int maxSpeed;
int numbOfOpponents;



void play(int addAccel,int player){
    velocity[player] += addAccel;
    if(velocity[player]>maxSpeed){
        velocity[player] = maxSpeed;
    } else if (velocity[player]<0) {
        velocity[player] = 0;
    }

    int nextPosition = (playerPositions[player]+velocity[player] + addAccel+ track.length)% track.length;
    playerPositions[player] = nextPosition;

}


void firstVelocity(){
    int inicialVelocity = 1;
    for(int i=0;i<velocity.length;i++){
        velocity[i]= inicialVelocity;
    }


}

void ids(){
    playersIds = new char[numbOfOpponents+1];
    playersIds[0]= HUMAN_PLAYER;
    for(int i=0;i<numbOfOpponents;i++){
        playersIds[i+1] = POSSIBLE_OPPS_IDS[i];
    }

}

// method that creates a new array where the positions of the players are defined
void playersPositions(int posS) {
    int trackLength = track.length;
    int positionP = (posS - 1 + trackLength) % trackLength;
    playerPositions[0] = positionP;
    for (int i = 0; i < numbOfOpponents; i++) {
        int posOpp = (positionP - (i + 1) + trackLength) % trackLength;
        playerPositions[i+1] = posOpp;
    }
}



int startingLineFounder() {    //method that founds where the Starting Line 'S' is
    int i = 0;
    while (track[i] != START) {
        i++;
    }
    return i;

}



void initState(){
    playerPositions = new int[numbOfOpponents+1];
    velocity = new int[numbOfOpponents + 1];
    playerLaps = new int[numbOfOpponents+1];
    int posS = startingLineFounder();
    playersPositions(posS);
    ids();
    firstVelocity();


}


void launchRace(Scanner inp){
    //read inputs to initialize state
    track=inp.next().toCharArray();//Length off the track and its composition
    raceLaps = inp.nextInt();// number of laps in the race
    maxSpeed = inp.nextInt();//The maximum speed a player can have
    numbOfOpponents = inp.nextInt();//number of  players in the race
    initState();

}

void execQuit(){

}

void execStatus(){

}


void execShow(){
    char [] currentTrack = track.clone();//TODO adicionar o raceStatus
    for(int i=0;i<playersIds.length;i++){
        int pos= playerPositions[i];
        currentTrack[pos] = playersIds[i];
    }
    System.out.println(currentTrack);
}


boolean hasModification(int player, char modification){
    boolean ret = false;
    for(int i=1;i<=SPACES_SEEN_BY_BOTS;i++){
        int posChecked =(playerPositions[player]+i+ track.length)% track.length;
        ret = track[posChecked] == modification;
        if (ret)
            break;

    }
return ret;

}

int accelBots(int i){
    int accelBot = 0;
    if(hasModification(i, BOOST)&& velocity[i]<maxSpeed){
        accelBot = 1;
    } else if (hasModification(i, OIL)&&velocity[i]> MIN_SPEED) {
        accelBot = -1;
    }
    return accelBot;
}



void execAccel(Scanner inp){
    int addAccel=inp.nextInt();inp.nextLine();
    play(addAccel,0);//Human player plays
    for(int i =1;i<=numbOfOpponents;i++){
        play(accelBots(i),i);
    }


}

void execCommands(Scanner inp) {
    String option;
    do {
        option = inp.next().toUpperCase();
        switch (option){
            case"ACCEL"->execAccel(inp);
            case"SHOW" ->execShow();
            case"STATUS" ->execStatus();
            case"QUIT" ->execQuit();
            default ->System.out.println(ERROR);
        }
    } while (!(option.equals(QUIT_MESS)));
    System.out.println(BYE_MESS);
}


void main(){
    Scanner inp = new Scanner(System.in);
    launchRace(inp);
    execCommands(inp);
    inp.close();
}
