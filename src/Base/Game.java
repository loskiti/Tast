package Base;

/**
 *  запуск/ остановка игры
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import Map.IMapCheckPoint;
import Map.ActionMap;
import Map.LabirintMap;
import Map.BossMap;
import Map.MapWay;
import Map.MapWayFind;
import Paint.Canvas;
import Paint.Tile;
import Paint.Unit;

public class Game {

	private Canvas canvas;
	private Player player;
	private ActionMap map;
	private PlayAudio audio;
	private List<Bot> bots;
	private static Timer mTimer;
	/**
	 * использовать изометрическую проекцию
	 */
	final public static boolean USE_ISO = true;

	/**
	 * Отступ при рисовании справа
	 */
	final public static int OFFSET_MAP_X = 320;

	/**
	 * для таймера пола
	 */
	private int timerTile = -1;
	/**
	 * координаты ячейки с огнем
	 */
	private int deadTileX = 0, deadTileY = 0;
	/**
	 * был ли keyEvent 1-true
	 */
	private int flag = 0;
	private int flag1 = 0;
	/**
	 * направление keyEvent
	 */
	private int x = 0, y = 0;

	public void start() {

		canvas = new Canvas();
		player = new Player();
		javax.swing.JFrame f = new javax.swing.JFrame();
		f.setLayout(null);
		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		f.setResizable(false);
		canvas.setBounds(0, 0, 640, 480);
		f.add(canvas);
		f.setTitle("Игра");
		f.setBounds(300, 300, 640, 480);
		f.setVisible(true);
		Listener();
		bots = new ArrayList<Bot>();
		setMap(1);
		audio = new PlayAudio();
		URL url1 = this.getClass().getResource("/sound/3.wav");
		audio.play(url1);

		Timer mTimer = new Timer();
		MyTimerTask mMyTimerTask = new MyTimerTask();
		mTimer.schedule(mMyTimerTask, 0, 100);

	}

	/**
	 * конец игры
	 */
	public void stop(String name) {

		canvas.addRenders(new Unit() {
			@Override
			public void render(Graphics g) {
				Image image = new ImageIcon(getClass().getResource(name)).getImage();
				// width = 640;
				// height = 480;
				g.drawImage(image, 0, 0, 640, 480, null);

			}

			@Override
			public int getDeep() {

				return Integer.MAX_VALUE;

			}
		});
		canvas.repaint();
		unListener();
		mTimer.cancel();
	}

	/**
	 * подключение клавиатуры и мыши
	 */
	public void Listener() {
		canvas.setFocusable(true);
		canvas.addKeyListener(keyListener);
		canvas.addMouseListener(mouseListener);
	}

	/**
	 * отключение клавиатуры и мыши
	 */
	public void unListener() {
		canvas.setFocusable(false);
		canvas.removeKeyListener(keyListener);
		canvas.removeMouseListener(mouseListener);
	}

	/**
	 * нажатие клавиатуры
	 */
	public KeyAdapter keyListener = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {

			switch (e.getKeyCode()) {
			case KeyEvent.VK_DOWN: {
				player.setDirectY(1);
				player.turnBoth();
				x = 0;
				y = 1;
				break;
			}
			case KeyEvent.VK_UP: {
				player.setDirectY(-1);
				player.turnTop();
				x = 0;
				y = -1;
				break;
			}
			case KeyEvent.VK_RIGHT: {
				player.setDirectX(1);
				player.turnRight();
				x = 1;
				y = 0;
				break;
			}
			case KeyEvent.VK_LEFT: {
				player.setDirectX(-1);
				player.turnLeft();
				x = -1;
				y = 0;
				break;
			}
			}
			flag = flag + 1;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
				player.setDirectX(0);
			}
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
				player.setDirectY(0);
			}
		}

	};
	/**
	 * клик мыши
	 */
	public MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			flag = 0;

			int startTileX, startTileY;
			if (USE_ISO) {
				// обратная функция рассчета изометрический координат
				int _offX = (e.getX() - Game.OFFSET_MAP_X), _y = (2 * e.getY() - _offX) / 2,
						isoX = Math.round((_offX + _y) / Tile.SIZE) + 0 - 0 - 0,
						isoY = Math.round((_y + Tile.SIZE / 2) / Tile.SIZE) + 0 - 0;
				startTileX = isoX + Math.round((Math.abs(player.getX() - player.getPosUnitX())) / Tile.SIZE);
				startTileY = isoY + Math.round((Math.abs(player.getY() - player.getPosUnitY())) / Tile.SIZE);
			} else {
				startTileX = (int) Math.floor((Math.abs(player.getX() - player.getPosUnitX()) + e.getX()) / Tile.SIZE);
				startTileY = (int) Math.floor((Math.abs(player.getY() - player.getPosUnitY()) + e.getY()) / Tile.SIZE);
			}
			// создаем путь
			if (tileIsPossible(startTileX, startTileY)) {
				MapWay mapPath = makePath(player.getX() / Tile.SIZE, player.getY() / Tile.SIZE, startTileX, startTileY);
				player.mapway = mapPath;
			}
		}
	};

	/**
	 * создать путь
	 */

	public MapWay makePath(int startX, int startY, int endX, int endY) {
		MapWayFind mfp = new MapWayFind();
		mfp.setcheckPoint(new IMapCheckPoint() {
			@Override
			public boolean check(int x, int y) {
				if (tileIsPossible(x, y)) {
					// запрещаем проходить между зомби
					for (Bot bot : bots) {
						if (bot.getX() / Tile.SIZE == x && bot.getY() / Tile.SIZE == y) {
							return false;
						}
					}
					return true;
				}
				return false;
			}

		});

		if (mfp.findWay(startX, startY, endX, endY)) {
			return mfp.getWay();
		}

		return null;
	}

	public void setMap(int mapId) {
		if (mapId == 2) {
			map = new BossMap();
			player.mapway = null;
			player.setX(3 * Tile.SIZE);
			player.setY(3 * Tile.SIZE);
			/**
			 * статическое позиционирование
			 */

			player.setPosUnitX(3 * Tile.SIZE);
			player.setPosUnitY(3 * Tile.SIZE);
			player.turnRight();
			timerTile = 0;
			bots.clear();
		} else {
			map = new LabirintMap();

			Bot bot;
			Random random = new Random();
			int a = 0, b = 0;
			int i = 0;
			// расстановка мобов с 4 секторах карты
			while (i < 4) {
				bot = new Bot();
				if (i == 2)
					a = 1;
				if (i == 1 || i == 3)
					b = 1;
				int x = random.nextInt(13) + 3 + a * 13;
				int y = random.nextInt(13) + 1 + a * 13;
				if (tileIsPossible(x, y)) {
					bot.setX(x * Tile.SIZE);
					bot.setPosUnitX(x * Tile.SIZE);
					bot.setY(y * Tile.SIZE);
					bot.setPosUnitY(y * Tile.SIZE);
					bots.add(bot);
					i++;
					b = 0;
				}

			}
			player.mapway = null;
			player.setX(Tile.SIZE);
			player.setY(Tile.SIZE);
			// устанавливаем статическое позиционирование
			player.setPosUnitX(Tile.SIZE);
			player.setPosUnitY(Tile.SIZE);

		}

	}

	/**
	 * телепорт на другую карту
	 */
	public void changeMap() {
		int tileX = player.getX() / Tile.SIZE, tileY = player.getY() / Tile.SIZE;
		int tileId = map.getTile(tileX, tileY);
		//String tileId = map.getTile(tileX, tileY);
		Tile tile = Tile.getTileId(tileId);
		if (tile.getDoor() > 0) {
			setMap(tile.getDoor());
		}
	}

	/**
	 * обработка событий
	 */
	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			changeMap();
			canvas.cleaneRenders();
			int W = map.getWidth(), H = map.getHeight();
			int tileId;
			//String tileId;
			/**
			 * всего клеток на экране
			 */

			int widthTile = 13, heightTile = 15;
			/**
			 * центр
			 */
			int centreTileWigdth = widthTile / 2 - 1, centreTileHeight = heightTile / 2 - 1;
			/**
			 * какие ячейки надо прорисовывать
			 */

			int startTileX = (int) Math.floor(Math.abs(player.getX() - player.getPosUnitX()) / Tile.SIZE);
			int startTileY = (int) Math.floor(Math.abs(player.getY() - player.getPosUnitY()) / Tile.SIZE);
			int endTileX = widthTile + startTileX > W ? W : widthTile + startTileX;
			int endTileY = heightTile + startTileY > H ? H : heightTile + startTileY;

			/**
			 * cдвиг карты
			 */
			int offsetX = (player.getX() - player.getPosUnitX()) % Tile.SIZE;
			int offsetY = (player.getY() - player.getPosUnitY()) % Tile.SIZE;
			/**
			 * определяем когда двигать карту - а когда должен двигаться
			 * персонаж
			 */

			boolean movePlayerX = (player.getX() / Tile.SIZE < centreTileWigdth
					|| W - player.getX() / Tile.SIZE < centreTileWigdth + 1),
					movePlayerY = (player.getY() / Tile.SIZE < centreTileHeight
							|| H - player.getY() / Tile.SIZE < centreTileHeight + 2);
			// ячейки с огнем на 2 уровне
			if (timerTile == 1200) {
				stop("/data/win.jpg");
				return;
			}

			if ((timerTile % 120 == 0) && (timerTile > 4)) {

				if ((deadTileX == player.getX() / Tile.SIZE || deadTileX - 1 == player.getX() / Tile.SIZE)
						&& (deadTileY == player.getY() / Tile.SIZE || deadTileY - 1 == player.getY() / Tile.SIZE)) {
					stop("/data/game-over.png");
					return;
				} else {

					deadTileX = player.getX() / Tile.SIZE + 1;
					deadTileY = player.getY() / Tile.SIZE + 1;
				}
			}
			if (timerTile == 0) {

				deadTileX = 5;
				deadTileY = 5;

			}
			if (timerTile > 0) {
				if (timerTile % 8 == 0)
					paintPlayer("/data/fenix.png", 1569, 301, 40, 58, 20, -35, 270, 100, 150);
				else
					paintPlayer("/data/fenix.png", 1634, 301, 41, 58, 20, -35, 270, 100, 150);
			}
			// прорисовка карты
			Tile tile;
			for (int y = startTileY; y < endTileY; y++) {
				for (int x = startTileX; x < endTileX; x++) {
					tileId = map.getTile(x, y);

					if (tileId == 14/*tileId.equals("Way")*/) {

						if (((x == deadTileX) || (x == deadTileX - 1)) && ((y == deadTileY) || (y == deadTileY - 1))) {
							tileId = 15;
							//tileId="Fire1";

							if (timerTile % 8 < 4)
								tileId = 15;
							//tileId="Fire1";

							else
								tileId = 16;
							//	tileId="Fire2";
							timerTile = timerTile + 1;

						}
					}
					tile = Tile.getTileId(tileId);
					tile.setX((x - startTileX) * Tile.SIZE - offsetX);
					tile.setY((y - startTileY) * Tile.SIZE - offsetY);
					canvas.addRenders(tile);
				}
			}

			// добавляем ботов
			for (Bot bot : bots) {

				// если бот достиг игрока, игра закончена
				if (bot.getX() / Tile.SIZE == player.getX() / Tile.SIZE
						&& bot.getY() / Tile.SIZE == player.getY() / Tile.SIZE) {
					stop("/data/game-over.png");
					return;
				}

				// рисуем бота, когда он в зоне видемости
				if (bot.getX() >= startTileX * Tile.SIZE && bot.getX() <= endTileX * Tile.SIZE
						&& bot.getY() >= startTileY * Tile.SIZE && bot.getY() <= endTileY * Tile.SIZE) {
					// обновляем точку преследования
					if (bot.posPlayer(player.getX() / Tile.SIZE, player.getY() / Tile.SIZE)) {
						bot.mapway = makePath(bot.getX() / Tile.SIZE, bot.getY() / Tile.SIZE, bot.posX, bot.posY);

					} else
						// изменяем положение
						if (bot.getDirectX() != 0 || bot.getDirectY() != 0) {
						bot.setX(bot.getX() + bot.getDirectX() * bot.getSpeed());
						bot.setY(bot.getY() + bot.getDirectY() * bot.getSpeed());
					}

					bot.setPosUnitX((bot.getX() - startTileX * Tile.SIZE) - offsetX);
					bot.setPosUnitY((bot.getY() - startTileY * Tile.SIZE) - offsetY);
					canvas.addRenders(bot);
				}
			}

			if (flag == 0)
				canvas.addRenders(player);

			if ((player.getDirectX() != 0 || player.getDirectY() != 0) && possibleMove()) {

				player.setX(player.getX() + player.getDirectX() * player.getSpeed());
				player.setY(player.getY() + player.getDirectY() * player.getSpeed());

				if (movePlayerX) {
					player.setPosUnitX(player.getPosUnitX() + player.getDirectX() * player.getSpeed());
				}
				if (movePlayerY) {
					player.setPosUnitY(player.getPosUnitY() + player.getDirectY() * player.getSpeed());
				}
			}
			// движение при нажаии клавиатуры
			if (flag != 0) {
				if (possibleMove()) {

					if (x == 0 && y == 1) {
						if (flag % 24 < 7) {
							paintPlayer("/data/hero/warr.png", 1304, 39, 80, 60, -10, -35, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						} else if ((flag % 24 < 12 && flag % 24 > 6) || (flag % 24 > 17)) {
							paintPlayer("/data/hero/warr.png", 1174, 40, 74, 59, -10, -35, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						}

						else if (flag % 24 < 18 && flag % 24 > 11) {
							paintPlayer("/data/hero/warr.png", 931, 40, 64, 56, 0, -25, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						}

					} else if (x == 0 && y == -1) {

						if (flag % 24 < 7) {
							paintPlayer("/data/hero/warr.png", 1318, 411, 69, 79, 0, -30, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						} else if ((flag % 24 < 12 && flag % 24 > 6) || (flag % 24 > 17)) {
							paintPlayer("/data/hero/warr.png", 1193, 421, 66, 69, 10, -30, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						}

						else if (flag % 24 < 18 && flag % 24 > 11) {
							paintPlayer("/data/hero/warr.png", 1067, 421, 57, 66, 10, -30, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						}

					} else if (x == 1 && y == 0) {
						if (flag % 24 < 7) {
							paintPlayer("/data/hero/warr.png", 807, 684, 56, 60, 10, -35, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						} else if ((flag % 24 < 12 && flag % 24 > 6) || (flag % 24 > 17)) {
							paintPlayer("/data/hero/warr.png", 686, 682, 47, 62, 10, -35, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						}

						else if (flag % 24 < 18 && flag % 24 > 11) {
							paintPlayer("/data/hero/warr.png", 555, 681, 53, 60, 10, -35, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						}

					} else if (x == -1 && y == 0) {

						if (flag % 24 < 7) {
							paintPlayer("/data/hero/warr.png", 939, 165, 48, 66, 25, -30, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						} else if ((flag % 24 < 12 && flag % 24 > 6) || (flag % 24 > 17)) {
							paintPlayer("/data/hero/warr.png", 1066, 164, 45, 64, 25, -30, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						}

						else if (flag % 24 < 18 && flag % 24 > 11) {
							paintPlayer("/data/hero/warr.png", 1448, 158, 55, 70, 25, -40, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						}

					} else if (x == 0 && y == 0) {

						if (flag1 % 6 < 3) {
							paintPlayer("/data/hero/warr.png", 42, 686, 48, 57, 20, -35, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						}

						else {
							paintPlayer("/data/hero/warr.png", 170, 686, 47, 57, 20, -35, player.getPosUnitX(),
									player.getPosUnitY(), 10000);
						}

						flag1 = flag1 + 1;
					}
				} else {

					paintPlayer("/data/hero/warr.png", 42, 686, 48, 57, 20, -35, player.getPosUnitX(),
							player.getPosUnitY(), 10000);

				}
				x = 0;
				y = 0;
			}

			canvas.repaint();

		}

	}

	/**
	 * вывод на экран
	 */
	private void paintPlayer(String name, int offsetX, int offsetY, int width, int hight, int standoffX, int standoffY,
			int X, int Y, int layerdoor) {
		canvas.addRenders(new Tile(name, X, Y, true, 0, layerdoor) {
			@Override
			public void render(Graphics g) {

				int isoX = X, isoY = Y;
				if (Game.USE_ISO) {
					isoX = (X - Y);
					isoY = (X + Y) / 2;

				}

				g.drawImage(getImage(name), isoX + Game.OFFSET_MAP_X + standoffX, isoY + standoffY,
						isoX + width + Game.OFFSET_MAP_X + standoffX, isoY + hight + standoffY, offsetX, offsetY,
						offsetX + width, offsetY + hight, null);
			}
		});
	}

	/**
	 * возможно ли движение
	 */
	public boolean possibleMove() {
		int left, right, up, down;
		boolean isPossible = true;

		// верх и низ
		left = (int) Math.ceil((player.getX()) / Tile.SIZE);
		right = (int) Math.floor((player.getX() + player.getWidth() - 1) / Tile.SIZE);
		up = (int) Math.ceil((player.getY() + player.getSpeed() * player.getDirectY()) / Tile.SIZE);
		down = (int) Math
				.floor((player.getY() + player.getHeight() + player.getSpeed() * player.getDirectY() - 1) / Tile.SIZE);
		if (player.getDirectY() == -1 && !(tileIsPossible(left, up) && tileIsPossible(right, up))) {
			isPossible = false;
		} else if (player.getDirectY() == 1 && !(tileIsPossible(left, down) && tileIsPossible(right, down))) {
			isPossible = false;
		}
		// право и лево
		left = (int) Math.ceil((player.getX() + player.getSpeed() * player.getDirectY()) / Tile.SIZE);
		right = (int) Math
				.floor((player.getX() + player.getWidth() + player.getSpeed() * player.getDirectX() - 1) / Tile.SIZE);
		up = (int) Math.ceil((player.getY()) / Tile.SIZE);
		down = (int) Math.floor((player.getY() + player.getHeight() - 1) / Tile.SIZE);
		if (player.getDirectX() == -1 && !(tileIsPossible(left, up) && tileIsPossible(left, down))) {
			isPossible = false;
		} else if (player.getDirectX() == 1 && !(tileIsPossible(right, up) && tileIsPossible(right, down))) {
			isPossible = false;
		}

		return isPossible;
	}

	/**
	 * возможно ли движение по определенной ячейке карты
	 */
	public boolean tileIsPossible(int x, int y) {
		Tile tile = Tile.getTileId(map.getTile(x, y));
		return (tile != null && tile.getIsPossible());
	}
}
