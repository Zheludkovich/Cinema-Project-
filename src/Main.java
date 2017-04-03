import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	// основной класс
	public static void main(String[] args) {
		CinemaData cinema = new CinemaData();
		
		/**
		cinema.getTimeTable();
		*/
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
		String inputString = new String();
		while(!inputString.equals("EXIT")){
			try {
				inputString = reader.readLine();
			} catch (IOException e) {
				System.out.println("Ошибка прочтения");
				continue;
			}
			if(inputString.equals("EXIT")){
				continue;
			}
			if(inputString.equals("GET /TIMETABLE/")){
				cinema.getTimeTable();
				continue;
			}
			if(inputString.equals("PUT /RESERVATIONS/")){
				cinema.putReservation();
				continue;
			}
			if(inputString.equals("DELETE /RESERVATIONS/")){
				cinema.deleteReservation();
				continue;
			}
			if(inputString.equals("GET /RESERVATIONS")){
				cinema.getReservation();
				continue;
			}
			System.out.println("Данный запрос отсутствует");
		}		
	}

}
