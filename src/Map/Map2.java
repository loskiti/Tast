package Map;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Map2 extends Map {
	public Map2() {

		int n = 10, m = 8;
		map = new int[n][m];
		try {
			URL url1 = this.getClass().getResource("/data/in.txt");
			Scanner sc = new Scanner(url1.openStream());
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (sc.hasNextInt()) {
						map[i][j] = sc.nextInt();
					}
				}
			}

			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	};

}
