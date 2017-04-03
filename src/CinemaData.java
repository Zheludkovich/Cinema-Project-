import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

// ��������� �������
public class CinemaData {
	private String reservationsFileName = "RESERVATIONS.TXT"; // ������������
	// id/<id>/id_cinema/<id_cinema>/row/<row>/place/<place>/
	private String timeTableFileName = "TIMETABLE.TXT"; // ����������
	//id/<id>/cinema/<name>/date/<date>/start_time/<start_time>/end_time/<end_time>

	private StringBuilder reservationsData = null;
	private StringBuilder timeTableData = null;

	private int lastIdReservations = 0;
	private int lastIdTimeTable = 0;

	public void printInfo(){
		System.out.println("INFO");
		System.out.println("����������� ����������: GET /TIMETABLE/"
				+ "\n������������� ���� ��� ��������� ���� �� ������: PUT /RESERVATIONS/"
				+ "\n�������� �����: DELETE /RESERVATIONS/?/"
				+ "\n�������� ���������� �� ������ ������ ������: GET /RESERVATIONS/?/\n");
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
			System.out.println("������ ��������� ������ ������������");
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
			System.out.println("������ ��������� ���������� �������");
			return;
		}
	}
	public void refresh(){
		try {
			PrintWriter out = new PrintWriter(reservationsFileName);
			out.write(String.valueOf(reservationsData));
			out.close();
		} catch (IOException e) {
			System.out.println("������ ���������� ������ ������������");
			return;
		}
		// ���������� �� �������������, ������� ���������� �� �����������
	}
	public void getTimeTable(){
		int c1 = 0, c2 = 0;
		while( (c2 = timeTableData.substring(c1).indexOf('\n')) != -1){
			String[] str = timeTableData.substring(c1, c2+c1).split("/");
			System.out.println(str[1] + ". " + str[3] + ".");
			System.out.println("   ���� ������: " + str[5]);
			System.out.println("   ������: " + str[7] + "   �����: " + str[9]);
			c1 += c2+1;
		}
	}
	public void putReservation(){
		System.out.print("������� �� ������������� ��������� ���� (��/���):");
		String inputData = new String();
		int numberPlaces = 1;
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
		while(!inputData.equals("��") && !inputData.equals("���") ){
			try {
				inputData = reader.readLine();
			} catch (IOException e) {
				System.out.println("������ ���������");
			}
			if(!inputData.equals("��") && !inputData.equals("���"))
				System.out.print("������� �������� �����: ");
			if(inputData.equals("��")){
				System.out.print("������� ������� ������������� ����:");
				while(numberPlaces == 1){
					try {
						numberPlaces = Integer.valueOf(reader.readLine());
					} catch (NumberFormatException e) {
						System.out.print("������� �����: ");
					} catch (IOException e) {
						System.out.println("������ ���������");
						System.out.print("������� �����: ");
					}
				}
			}
		}
		lastIdReservations++;
		int idCinema = 0, row, place;
		System.out.print("�������� ����: ");
		while(idCinema == 0){
			try {
				idCinema = Integer.valueOf(reader.readLine());
			} catch (NumberFormatException e) {
				System.out.print("������� �����: ");
			} catch (IOException e) {
				System.out.println("������ ���������");
				System.out.print("������� �����: ");
			}
			if(idCinema > lastIdTimeTable){
				System.out.print("������� ����� �� ��������� id �����������: ");
				idCinema = 0;
			}
		}
		for(int i = 0; i < numberPlaces; i++)
		{
			row = 0;
			place = 0;
			System.out.print("�������� ���: ");
			while(row == 0){
				try {
					row = Integer.valueOf(reader.readLine());
				} catch (NumberFormatException e) {
					System.out.print("������� �����: ");
				} catch (IOException e) {
					System.out.println("������ ���������");
					System.out.print("������� �����: ");
				}
			}
			System.out.print("�������� �����: ");
			while(place == 0){
				try {
					place = Integer.valueOf(reader.readLine());
				} catch (NumberFormatException e) {
					System.out.print("������� �����: ");
				} catch (IOException e) {
					System.out.println("������ ���������");
					System.out.print("������� �����: ");
				}
			}
			// ������� ������
			String puttedString = "id/" + lastIdReservations + "/id_cinema/" + idCinema + 
					"/row/" + row + "/place/" + place + "/\n";
			if(reservationsData.indexOf(puttedString) != -1){
				System.out.println("������ ����� �������������");
				i--;
			}
			else
				reservationsData.append(puttedString);
		}
		refresh();
	}
	public void getReservation(){
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
		System.out.print("������� ����� ������: ");
		int num = 0;
		while(num == 0){
			try {
				num = Integer.valueOf(reader.readLine());
			} catch (NumberFormatException e) {
				System.out.print("������� �����: ");
			} catch (IOException e) {
				System.out.println("������ ���������");
				System.out.print("������� �����: ");
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
				//System.out.println("����� ������: " + str[1]);
				//System.out.println("ID Seesion cinema: " + str[3]);
				int b1 = timeTableData.indexOf("id/" + str[3]);
				int b2 = timeTableData.substring(b1).indexOf('\n');
				String[] strCinema = timeTableData.substring(b1, b2+b1).split("/");
				System.out.println("   "+strCinema[3]);
				System.out.println("   ���� ������: " + strCinema[5]);
				System.out.println("   ������: " + strCinema[7] + "   �����: " + strCinema[9]);
			}
			System.out.println("   ���: " + str[5] + "   �����: " + str[7]);
			c1 += c2+1;
			numberPlaces++;
		}
		if(numberPlaces == 0){
			System.out.println("������ � ����� ������� ���");
			return;
		}
	}
	public void deleteReservation(){
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
		System.out.print("������� ����� ������: ");
		int num = 0;
		while(num == 0){
			try {
				num = Integer.valueOf(reader.readLine());
			} catch (NumberFormatException e) {
				System.out.print("������� �����: ");
			} catch (IOException e) {
				System.out.println("������ ���������");
				System.out.print("������� �����: ");
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
			System.out.println("������ � ����� ������� ���");
			return;
		}
		if(numberPlaces > 1){
			System.out.println("�� ������ ����� ���������������� ��������� ����");
			System.out.print("������� �� �������� ������ ���� ����� ��� ���� ����� (��/���):");
			String inputVariant = new String();
			while(!inputVariant.equals("��") && !inputVariant.equals("���") ){
				try {
					inputVariant = reader.readLine();
				} catch (IOException e) {
					System.out.println("������ ���������");
				}
				if(!inputVariant.equals("��") && !inputVariant.equals("���"))
					System.out.print("������� �������� �����: ");
			}
			if(inputVariant.equals("��")){
				int row = 0;
				int place = 0;
				System.out.print("�������� ���: ");
				while(row == 0){
					try {
						row = Integer.valueOf(reader.readLine());
					} catch (NumberFormatException e) {
						System.out.print("������� �����: ");
					} catch (IOException e) {
						System.out.println("������ ���������");
						System.out.print("������� �����: ");
					}
				}
				System.out.print("�������� �����: ");
				while(place == 0){
					try {
						place = Integer.valueOf(reader.readLine());
					} catch (NumberFormatException e) {
						System.out.print("������� �����: ");
					} catch (IOException e) {
						System.out.println("������ ���������");
						System.out.print("������� �����: ");
					}
				}
				// ������ ������
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
			System.out.println("����� ������: " + str1[1]);
			System.out.println("ID Seesion cinema: " + str1[3]);
			System.out.println("   ���: " + str1[5] + "   �����: " + str1[7]);
			reservationsData.delete(c1, c1+c2+1);
			//refresh();
		}
		refresh();
	}
}
