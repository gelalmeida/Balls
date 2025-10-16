import java.util.Scanner;

final String QUIT_MESS = "EXIT";
final String BYE_MESS = "Bye";
final String ERROR = "Invalid command";
final char HUMAN_PLAYER ='P';
final char START ='S';
final char[] POSSIBLE_OPPS_IDS ={'a','b','c','d','e','f','g','h','i'};
char []track;
int raceLaps;
int maxSpeed;
int numbOfOpponents;




void PlayersPositions(int positionS) {   // refazer isto

void ids(){
    char[] PlayersIds = new char [numbOfOpponents+HUMAN_PLAYER];
    PlayersIds[0]=HUMAN_PLAYER;
    for(int n=1;n<numbOfOpponents;n++){
        PlayersIds[n]= POSSIBLE_OPPS_IDS[n];
}




void PlayersPositions(int posS) {// method that creates a new array where the positions of the players are defined
    int trackLength = track.length;
    int positionP = (positionS - 1 + trackLength) % trackLength;
    track[positionP] = HUMAN_PLAYER;
    for (int i = 0; i < numbOfOpponents; i++) {
        int posOpp = (positionP - (i + 1) + trackLength) % trackLength;
        track[posOpp] = POSSIBLE_OPPS_IDS[i];
    }
}



int startingLineFounder() {    //method that founds where the Starting Line 'S' is
    int i = 0;
    while (track[i] != START) {
       i++;
    }
        return i;

}



void initState(Scanner inp){
    int positionS = startingLineFounder();
    PlayersPositions(positionS);
}


void launchRace(Scanner inp){
    //read inputs to initialize state
    track=inp.next().toCharArray();//Length off the track and its composition
    raceLaps = inp.nextInt();// number of laps in the race
    maxSpeed = inp.nextInt();//The maximum speed a player can have
    numbOfOpponents = inp.nextInt();//number of  players in the race
    initState(inp);

}

void execQuit(){

}


void execStatus(){

}


void execShow(){

}


void execAccel(Scanner inp){
    int moreAccel=inp.nextInt();inp.nextLine();


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