import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Dates {
	// Choix de la langue francaise
	static Locale locale = Locale.getDefault();
	static Date actuelle = new Date();

	// Definition du format utilise pour les dates
	static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	// Donne la date
	public static String date() {
		String dat = dateFormat.format(actuelle);
		return dat;
	}
}