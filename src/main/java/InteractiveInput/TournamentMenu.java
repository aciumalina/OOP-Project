package InteractiveInput;

import DomainModels.DtoPlayer;
import DomainModels.Game;
import DomainModels.Tournaments.KickOut;
import DomainModels.Tournaments.RoundRobin;
import DomainModels.Tournaments.Tournament;
import DomainModels.Tournaments.TournamentRequest;
import Enums.Option;
import Enums.Result;
import Enums.TournamentOption;
import Service.RoundRobinService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class TournamentMenu {
    Tournament tournament;

    public void showOptions(){
        System.out.println("0. Incheie simularea turneului");
        System.out.println("1. Incepeti runda");
        System.out.println("2. Arata clasamentul actual");
    }

    public TournamentMenu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introdu tipul turneului (Round Robin/Kick Out Format)");
        System.out.println("Round Robin - optiunea 1");
        System.out.println("Kick Out - optiunea 2");
        int optiune = scanner.nextInt();
        while (true) {
            if (optiune == 1)
            {
                System.out.println("Ati ales turneu de tip Round Robin");
                TournamentRequest tournamentRequest = getTournamentDetails();
                this.tournament = new RoundRobin(tournamentRequest.getName(),tournamentRequest.getStartDate(),tournamentRequest.getEndDate(),tournamentRequest.getCity());
                break;
            }
                

            if (optiune == 2) {
                System.out.println("Ati ales turneu de tip Kick Out");
                TournamentRequest tournamentRequest2 = getTournamentDetails();
                this.tournament = new KickOut(tournamentRequest2.getName(),tournamentRequest2.getStartDate(),tournamentRequest2.getEndDate(),tournamentRequest2.getCity());
                break;
            } else {
                System.out.println("Optiune invalida!");
            }
        }
    }
    public void chooseOption(TournamentOption option){
        Scanner scanner = new Scanner(System.in);
        switch (option){
            case PAIR_PLAYERS:
                ArrayList<Game> games = tournament.pairPlayers().getGames();
                if ( games.size() == 0)
                {
                    System.out.println("Turneul s-a incheiat! Iata rezultatul final: \n \n");
                    chooseOption(TournamentOption.SHOW_STANDINGS);
                    break;
                }
                int i;
                for (i=0;i<games.size();i++)
                {
                    Result result = null;
                    Game currentGame = games.get(i);
                    currentGame.showPlayingPlayers();
                    System.out.println("\n1 -- pentru victorie alb");
                    System.out.println("2 -- pentru remiza");
                    System.out.println("3 -- pentru victorie negru");
                    int option2 = scanner.nextInt();
                    switch (option2){
                        case 1:
                            result = Result.WHITE;
                            break;
                        case 2:
                            result = Result.DRAW;
                            break;
                        case 3:
                            result = Result.BLACK;
                            break;
                    }
                    tournament.inputResult(i, result);
                }
                tournament.updatePlayersStats();

                break;
            case SHOW_STANDINGS:
                ArrayList<DtoPlayer> playersToShow = tournament.showStandings();
                for ( int j=0; j<playersToShow.size();j++)
                {
                    System.out.println("Locul " + (j+1) + ". " + playersToShow.get(j));
                }


                break;




        }
    }
    private TournamentRequest getTournamentDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introdu numele turneului: ");
        String name = scanner.nextLine();
        System.out.println("Introdu data finalizarii: ");
        String dataString = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate data = LocalDate.parse(dataString, formatter);
        System.out.println("Introdu orasul care gazduieste turneul: ");
        String city = scanner.nextLine();
        return new TournamentRequest(name, LocalDate.now(), data, city);
    }


}
