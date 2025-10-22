import java.util.Scanner;
final String QUIT_MESS = "EXIT";
final String BYE_MESS = "Bye";//TODO trocar isto
final String ERROR = "Invalid command";
final String RACE_NOT_OVER ="The race is not over yet!";
final String ONGOING = " (ongoing)";
final String RACE_ENDED =" (ended)";
final char HUMAN_PLAYER ='P';
final char START ='S';
final char[] POSSIBLE_OPPS_IDS ={'a','b','c','d','e','f','g','h','i'};
final int MIN_SPEED = 0;
final int SPACES_SEEN_BY_BOTS = 3;
final char BOOST = '+';
final char OIL = '!';
final char DRAG = '-';
char [] playersIds;
int [] velocity;
int [] playerPositions;
int [] oldPositions;
int[] playerLaps;
boolean[] hasCompletedFormationLap;
char []track;
int raceLaps;
int maxSpeed;
int numbOfOpponents;
boolean raceEnded = false;
char winnerId = ' ';


void startLapCounter(){
    int initialLapValue = 0;
    for(int i=0;i<playerLaps.length;i++)
        playerLaps[i] = initialLapValue;
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
    oldPositions = new int[numbOfOpponents+1];
    hasCompletedFormationLap = new boolean[numbOfOpponents+1];
    int posS = startingLineFounder();
    playersPositions(posS);
    ids();
    firstVelocity();
    startLapCounter();
    for(int i = 0; i < oldPositions.length; i++){
        oldPositions[i] = playerPositions[i];
    }
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
    if(raceEnded){
        System.out.println("Race ended: "+winnerId+" won the race!");
    }else
         System.out.println(RACE_NOT_OVER);
}

int knowingWhichPlayerIs(char Id){
    for(int i=0;i<playersIds.length;i++){
        if(Id == playersIds[i]){
            return i;
        }
    }
    return  -1;
}


void execStatus(Scanner inp){
    char chossenId = inp.next().charAt(0);
    int i=knowingWhichPlayerIs(chossenId);
    if(raceEnded && chossenId == winnerId){
        System.out.println("Race ended: " + chossenId + " won the race!");
    } else if (i ==-1) {
        System.out.println("Player " + chossenId + " does not exist!");
    }else
        System.out.println("Player " + chossenId + ": cell " + playerPositions[i] + ", laps " + playerLaps[i] + "!");
}

void messagesOfAccel(boolean raceIsConcluded) {
        if (raceIsConcluded) {
            System.out.println("Race ended:" + winnerId + " won the race!");
        } else if (raceEnded) {
            System.out.println("Player " + winnerId + " won the race!");
        } else
            System.out.println("Player " + HUMAN_PLAYER + ": cell " + playerPositions[0] + ", laps " + playerLaps[0]+'!');

    }



String raceStatus(){
    String status = ONGOING;
    for(int i = 0; i < playerLaps.length; i++){
        if(playerLaps[i] >= raceLaps) {
            status = RACE_ENDED;
            if (!raceEnded) {
                raceEnded = true;
                winnerId = playersIds[i];
            }
        }
    }
    return status;
}


char[] copyTrack(){
    char[] currentTrack = new char[track.length];
    for(int i=0;i< track.length;i++){
        currentTrack[i]=track[i];
    }
    return currentTrack;
}

void execShow(){
    char [] currentTrack = copyTrack();
    for(int i=0;i<playersIds.length;i++){
        int pos= playerPositions[i];
        currentTrack[pos] = playersIds[i];
    }
    System.out.print(currentTrack);
    System.out.println(raceStatus());
}

boolean hasModification(int player, char modification) {
    for (int i = 1; i <= SPACES_SEEN_BY_BOTS; i++) {
        int posChecked = (playerPositions[player] + i + track.length) % track.length;
        if (track[posChecked] == modification) {
            return true;
        }
    }
    return false;
}


int accelModifications(int player) {
    if (hasModification(player, OIL) && velocity[player] > MIN_SPEED) {
        velocity[player] = 0;
        return 0;
    }
    int modfication = 0;
    if (hasModification(player, BOOST) && velocity[player] < maxSpeed) {
        modfication += 1;
    }
    if (hasModification(player, DRAG) && velocity[player] > 0) {
        modfication -= 1;
    }
    return modfication;
}

boolean hasCrossedStartLine(int oldPos, int newPos, int startLinePos) {
    int trackLength = track.length;
    if (oldPos < startLinePos && newPos >= startLinePos) {
        return true;
    }
    if (oldPos > newPos) {
        if (startLinePos >= 0 && startLinePos <= newPos) {
            return true;
        }
        if (startLinePos >= oldPos && startLinePos < trackLength) {
            return true;
        }
    }

    return false;
}

void updateLapCounters() {
    int startLinePos = startingLineFounder();
    for (int i = 0; i < playerPositions.length; i++) {
        if (hasCrossedStartLine(oldPositions[i], playerPositions[i], startLinePos)) {
            if (!hasCompletedFormationLap[i]) {
                hasCompletedFormationLap[i] = true;
            } else {
                playerLaps[i]++;
            }
        }
    }
}

void play(int addAccel, int player) {
    velocity[player] += addAccel;
    if (velocity[player] > maxSpeed) {
        velocity[player] = maxSpeed;
    } else if (velocity[player] < 0) {
        velocity[player] = 0;
    }

    oldPositions[player] = playerPositions[player];

    int nextPosition = (playerPositions[player] + velocity[player] + track.length) % track.length;
    playerPositions[player] = nextPosition;
}

void execAccel(Scanner inp) {
    boolean raceIsConcluded = raceEnded;
    int addAccel = inp.nextInt();
    inp.nextLine();
    int humanMod = accelModifications(0);
    play(addAccel + humanMod, 0);
    for (int i = 1; i < playersIds.length; i++) {
        int botMod = accelModifications(i);
        play(botMod, i);
    }
    updateLapCounters();
    raceStatus();
    messagesOfAccel(raceIsConcluded);
}


void execCommands(Scanner inp) {
    String option;
    do {
        option = inp.next();
        switch (option){
            case"accel"->execAccel(inp);
            case"show" ->execShow();
            case"status" ->execStatus(inp);
            case"quit" ->{
                execQuit();
                return;
            }
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

