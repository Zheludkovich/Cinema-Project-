import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

// иммитация сервера
public class CinemaData {
	private String reservationsFileName = "RESERVATIONS.TXT"; // бронирование
	// id/<id>/id_cinema/<id_cinema>/row/<row>/place/<place>/
	private String timeTableFileName = "TIMETABLE.TXT"; // расписание
	//id/<id>/cinema/<name>/date/<date>/start_time/<start_time>/end_time/<end_time>

	private StringBuilder reservationsData = null;
	private StringBuilder timeTableData = null;

	private int lastIdReservations = 0;
	private int lastIdTimeTable = 0;

	public void printInfo(){
		System.out.println("INFO");
		System.out.println("просмотреть расписание: GET /TIMETABLE/"
				+ "\nзабронировать одно или несколько мест на сеансе: PUT /RESERVATIONS/"
				+ "\nотменить бронь: DELETE /RESERVATIONS/?/"
				+ "\nполучить информацию по номеру своего заказа: GET /RESERVATIONS/?/\n");
	}

	public CinemaData(){
		printInfo();
		reservationsData = new StringBuilder();
		timeTableData = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(reservationsFileName));
			String s;
			String sLast = null;
			while ((s = in.readLine()) != null) {
				reservationsData.append(s);
				reservationsData.append("\n");
				sLast = s;
			}
			in.close();
			if(sLast != null){
				String[] s1 = sLast.split("/"); 
				lastIdReservations = Integer.valueOf(s1[1]);
			}
		} catch (IOException e) {
			System.out.println("Ошибка прочтения данных бронирования");
			return;
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader(timeTableFileName));
			String s;
			String sLast = null;
			while ((s = in.readLine()) != null) {
				timeTableData.append(s);
				timeTableData.append("\n");
				sLast = s;
			}
			in.close();
			if(sLast != null){
				String[] s1 = sLast.split("/"); 
				lastIdTimeTable = Integer.valueOf(s1[1]);
			}
		} catch (IOException e) {
			System.out.println("Ошибка прочтения расписания сеансов");
			return;
		}
	}
	public void refresh(){
		try {
			PrintWriter out = new PrintWriter(reservationsFileName);
			out.write(String.valueOf(reservationsData));
			out.close();
		} catch (IOException e) {
			System.out.println("Ошибка сохранения данных бронирования");
			return;
		}
		// Расписание не редактируется, поэтому перезапись не потребуется
	}
	public void getTimeTable(){
		int c1 = 0, c2 = 0;
		while( (c2 = timeTableData.substring(c1).indexOf('\n')) != -1){
			String[] str = timeTableData.substring(c1, c2+c1).split("/");
			System.out.println(str[1] + ". " + str[3] + ".");
			System.out.println("   Дата сеанса: " + str[5]);
			System.out.println("   Начало: " + str[7] + "   Конец: " + str[9]);
			c1 += c2+1;
		}
	}
	public void putReservation(){
		System.out.print("Желаете ли забронировать несколько мест (да/нет):");
		String inputData = new String();
		int numberPlaces = 1;
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
		while(!inputData.equals("да") && !inputData.equals("нет") ){
			try {
				inputData = reader.readLine();
			} catch (IOException e) {
				System.out.println("Ошибка прочтения");
			}
			if(!inputData.equals("да") && !inputData.equals("нет"))
				System.out.print("Введите повторно ответ: ");
			if(inputData.equals("да")){
				System.out.print("Сколько желаете забронировать мест:");
				while(numberPlaces == 1){
					try {
						numberPlaces = Integer.valueOf(reader.readLine());
					} catch (NumberFormatException e) {
						System.out.print("Введите число: ");
					} catch (IOException e) {
						System.out.println("Ошибка прочтения");
						System.out.print("Введите число: ");
					}
				}
			}
		}
		lastIdReservations++;
		int idCinema = 0, row, place;
		System.out.print("Выберите кино: ");
		while(idCinema == 0){
			try {
				idCinema = Integer.valueOf(reader.readLine());
			} catch (NumberFormatException e) {
				System.out.print("Введите число: ");
			} catch (IOException e) {
				System.out.println("Ошибка прочтения");
				System.out.print("Введите число: ");
			}
			if(idCinema > lastIdTimeTable){
				System.out.print("Введите число из имеющихся id киносеансов: ");
				idCinema = 0;
			}
		}
		for(int i = 0; i < numberPlaces; i++)
		{
			row = 0;
			place = 0;
			System.out.print("Выберите ряд: ");
			while(row == 0){
				try {
					row = Integer.valueOf(reader.readLine());
				} catch (NumberFormatException e) {
					System.out.print("Введите число: ");
				} catch (IOException e) {
					System.out.println("Ошибка прочтения");
					System.out.print("Введите число: ");
				}
			}
			System.out.print("Выберите место: ");
			while(place == 0){
				try {
					place = Integer.valueOf(reader.readLine());
				} catch (NumberFormatException e) {
					System.out.print("Введите число: ");
				} catch (IOException e) {
					System.out.println("Ошибка прочтения");
					System.out.print("Введите число: ");
				}
			}
			// добавим запись
			String puttedString = "id/" + lastIdReservations + "/id_cinema/" + idCinema + 
					"/row/" + row + "/place/" + place + "/\n";
			if(reservationsData.indexOf(puttedString) != -1){
				System.out.println("Данное место забронировано");
				i--;
			}
			else
				reservationsData.append(puttedString);
		}
		refresh();
	}
	public void getReservation(){
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
		System.out.print("Введите номер заказа: ");
		int num = 0;
		while(num == 0){
			try {
				num = Integer.valueOf(reader.readLine());
			} catch (NumberFormatException e) {
				System.out.print("Введите число: ");
			} catch (IOException e) {
				System.out.println("Ошибка прочтения");
				System.out.print("Введите число: ");
			}
		}
		String findString = "id/" + num;
		int c = 0,c1 = 0, c2 = 0;
		int numberPlaces = 0;

		while( (c = reservationsData.substring(c1).indexOf(findString)) != -1){
			c1 += c;
			c2 = reservationsData.substring(c1).indexOf('\n');
			String[] str = reservationsData.substring(c1, c2+c1).split("/");
			if(numberPlaces == 0){
				//System.out.println("Номер заказа: " + str[1]);
				//System.out.println("ID Seesion cinema: " + str[3]);
				int b1 = timeTableData.indexOf("id/" + str[3]);
				int b2 = timeTableData.substring(b1).indexOf('\n');
				String[] strCinema = timeTableData.substring(b1, b2+b1).split("/");
				System.out.println("   "+strCinema[3]);
				System.out.println("   Дата сеанса: " + strCinema[5]);
				System.out.println("   Начало: " + strCinema[7] + "   Конец: " + strCinema[9]);
			}
			System.out.println("   Ряд: " + str[5] + "   Место: " + str[7]);
			c1 += c2+1;
			numberPlaces++;
		}
		if(numberPlaces == 0){
			System.out.println("Заказа с таким номером нет");
			return;
		}
	}
	public void deleteReservation(){
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
		System.out.print("Введите номер заказа: ");
		int num = 0;
		while(num == 0){
			try {
				num = Integer.valueOf(reader.readLine());
			} catch (NumberFormatException e) {
				System.out.print("Введите число: ");
			} catch (IOException e) {
				System.out.println("Ошибка прочтения");
				System.out.print("Введите число: ");
			}
		}
		String findString = "id/" + num;
		int c = 0,c1 = 0, c2 = 0;
		int numberPlaces = 0;

		while( (c = reservationsData.substring(c1).indexOf(findString)) != -1){
			c1 += c;
			c2 = reservationsData.substring(c1).indexOf('\n');
			c1 += c2+1;
			numberPlaces++;
		}
		if(numberPlaces == 0){
			System.out.println("Заказа с таким номером нет");
			return;
		}
		if(numberPlaces > 1){
			System.out.println("На данный заказ зарегистрировано несколько мест");
			System.out.print("Желаете ли отменить только одно место или весь заказ (да/нет):");
			String inputVariant = new String();
			while(!inputVariant.equals("да") && !inputVariant.equals("нет") ){
				try {
					inputVariant = reader.readLine();
				} catch (IOException e) {
					System.out.println("Ошибка прочтения");
				}
				if(!inputVariant.equals("да") && !inputVariant.equals("нет"))
					System.out.print("Введите повторно ответ: ");
			}
			if(inputVariant.equals("да")){
				int row = 0;
				int place = 0;
				System.out.print("Выберите ряд: ");
				while(row == 0){
					try {
						row = Integer.valueOf(reader.readLine());
					} catch (NumberFormatException e) {
						System.out.print("Введите число: ");
					} catch (IOException e) {
						System.out.println("Ошибка прочтения");
						System.out.print("Введите число: ");
					}
				}
				System.out.print("Выберите место: ");
				while(place == 0){
					try {
						place = Integer.valueOf(reader.readLine());
					} catch (NumberFormatException e) {
						System.out.print("Введите число: ");
					} catch (IOException e) {
						System.out.println("Ошибка прочтения");
						System.out.print("Введите число: ");
					}
				}
				// найдем запись
				String findedString = "/row/" + row + "/place/" + place + "/";
				c = 0;
				c1 = 0;
				c2 = 0;
				while( (c = reservationsData.substring(c1).indexOf(findString)) != -1){
					c1 += c;
					c2 = reservationsData.substring(c1).indexOf('\n');
					String str = reservationsData.substring(c1, c2+c1);
					if(str.indexOf(findedString) == -1){
						c1 += c2+1;
						continue;
					}
					reservationsData.delete(c1, c1+c2+1);
					refresh();
					return;
				}
				return;
			}
		}
		c = 0;
		c1 = 0;
		c2 = 0;
		while( (c = reservationsData.substring(c1).indexOf(findString)) != -1){
			c1 += c;
			c2 = reservationsData.substring(c1).indexOf('\n');
			String str = reservationsData.substring(c1, c2+c1);
			String[] str1 = str.split("/");
			System.out.println("Номер заказа: " + str1[1]);
			System.out.println("ID Seesion cinema: " + str1[3]);
			System.out.println("   Ряд: " + str1[5] + "   Место: " + str1[7]);
			reservationsData.delete(c1, c1+c2+1);
			//refresh();
		}
		refresh();
	}
}
