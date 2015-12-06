package Base;

import java.awt.Graphics;

// запуск/ остановка игры
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import Map.IMapCheckPoint;
import Map.Map;
import Map.Map1;
import Map.Map2;
import Map.MapWay;
import Map.MapWayFind;
import Paint.Canvas;
import Paint.Tile;
import Paint.Unit;

public class Game {

	protected Canvas canvas;
	protected Player player;
	protected Map map;
	static PlayAudio audio;

	protected ArrayList<Bot> bots;

	static Timer mTimer;

	// использовать изометрическую проекцию
	final public static boolean USE_ISO = true;
	// Отступ при рисовании справа
	final public static int OFFSET_MAP_X = 320;// 10 клеток 32 * 10
	// для таймера пола
	private int timerTile = -1;
	private int deadTileX = 0, deadTileY = 0;
	private int flag = 0;
	private int flag1 = 0;
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

	public void stop(String name) {

		canvas.addRenders(new Unit() {
			@Override
			public void render(Graphics g) {
				initiation(name);
				width = 640;
				height = 480;
				g.drawImage(imageUnit, 0, 0, width, height, null);

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

	public void Listener() {
		canvas.setFocusable(true);
		canvas.addKeyListener(keyListener);
		canvas.addMouseListener(mouseListener);
	}

	public void unListener() {
		canvas.setFocusable(false);
		canvas.removeKeyListener(keyListener);
		canvas.removeMouseListener(mouseListener);
	}

	// после нажатия на кнопку
	protected KeyAdapter keyListener = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {

			switch (e.getKeyCode()) {
			case KeyEvent.VK_DOWN: {
				player.directY = 1;
				player.turnBoth();
				x = 0;
				y = 1;
				break;
			}
			case KeyEvent.VK_UP: {
				player.directY = -1;
				player.turnTop();
				x = 0;
				y = -1;
				break;
			}
			case KeyEvent.VK_RIGHT: {
				player.directX = 1;
				player.turnRight();
				x = 1;
				y = 0;
				break;
			}
			case KeyEvent.VK_LEFT: {
				player.directX = -1;
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
				player.directX = 0;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
				player.directY = 0;
			}
		}

	};
	protected MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			flag = 0;

			int startTileX, startTileY;
			if (USE_ISO) {
				// обратная функция рассчета изометрический координат
				int _offX = (e.getX() - Game.OFFSET_MAP_X), _y = (2 * e.getY() - _offX) / 2,
						isoX = Math.round((_offX + _y) / Tile.SIZE) + 0 - 0 - 0,
						isoY = Math.round((_y + Tile.SIZE / 2) / Tile.SIZE) + 0 - 0;
				startTileX = isoX + Math.round((Math.abs(player.X - player.posUnitX)) / Tile.SIZE);
				startTileY = isoY + Math.round((Math.abs(player.Y - player.posUnitY)) / Tile.SIZE);
			} else {
				startTileX = (int) Math.floor((Math.abs(player.X - player.posUnitX) + e.getX()) / Tile.SIZE);
				startTileY = (int) Math.floor((Math.abs(player.Y - player.posUnitY) + e.getY()) / Tile.SIZE);
			}
			// создаем путь
			if (tileIsPossible(startTileX, startTileY)) {
				MapWay mapPath = makePath(player.X / Tile.SIZE, player.Y / Tile.SIZE, startTileX, startTileY);
				player.mapway = mapPath;
			}
		}
	};

	// создать путь
	public MapWay makePath(int startX, int startY, int endX, int endY) {
		MapWayFind mfp = new MapWayFind();
		mfp.setcheckPoint(new IMapCheckPoint() {
			@Override
			public boolean check(int x, int y) {
				if (tileIsPossible(x, y)) {
					// запрещаем проходить между зомби
					for (Bot bot : bots) {
						if (bot.X / Tile.SIZE == x && bot.Y / Tile.SIZE == y) {
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
			map = new Map2();
			player.mapway = null;
			player.X = 3 * Tile.SIZE;
			player.Y = 3 * Tile.SIZE;
			// устанавливаем статическое позиционирование
			player.posUnitX = 3 * Tile.SIZE;
			player.posUnitY = 3 * Tile.SIZE;
			player.turnRight();
			timerTile = 0;
			bots.clear();
		} else {
			map = new Map1();

			Bot bot;
			/*
			 * // проверять на tileisPossible!!!! bd разные месте кинуть for
			 * (int i = 0; i < 3; i++) { bot = new Bot(); bot.X = bot.posUnitX =
			 * (6 + i) * Tile.SIZE; bot.Y = bot.posUnitY = 7 * Tile.SIZE;
			 * bots.add(bot); }
			 */
			Random random = new Random();
			int a = 0, b = 0;
			int i = 0;
			while (i < 4) {
				bot = new Bot();
				if (i == 2)
					a = 1;
				if (i == 1 || i == 3)
					b = 1;
				int x = random.nextInt(13) + 3 + a * 13;
				int y = random.nextInt(13) + 1 + a * 13;
				if (tileIsPossible(x, y)) {
					bot.X = bot.posUnitX = x * Tile.SIZE;
					bot.Y = bot.posUnitY = y * Tile.SIZE;
					bots.add(bot);
					i++;
					b = 0;
				}

			}
			player.mapway = null;
			player.X = Tile.SIZE;
			player.Y = Tile.SIZE;
			// устанавливаем статическое позиционирование
			player.posUnitX = Tile.SIZE;
			player.posUnitY = Tile.SIZE;

		}

	}

	public void changeMap() {
		int tileX = player.X / Tile.SIZE, tileY = player.Y / Tile.SIZE;
		int tileId = map.getTile(tileX, tileY);
		Tile tile = Tile.getTileId(tileId);
		if (tile.door > 0) {
			setMap(tile.door);
		}
	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			changeMap();
			canvas.cleaneRenders();
			int W = map.getWidth(), H = map.getHeight();
			int tileId;
			// всего клеток на экране
			int widthTile = 13, heightTile = 15;
			// центр
			int centreTileWigdth = widthTile / 2 - 1, centreTileHeight = heightTile / 2 - 1;
			// какие ячейки надо прорисовывать
			int startTileX = (int) Math.floor(Math.abs(player.X - player.posUnitX) / Tile.SIZE);
			int startTileY = (int) Math.floor(Math.abs(player.Y - player.posUnitY) / Tile.SIZE);
			int endTileX = widthTile + startTileX > W ? W : widthTile + startTileX;
			int endTileY = heightTile + startTileY > H ? H : heightTile + startTileY;
			// cдвиг карты
			int offsetX = (player.X - player.posUnitX) % Tile.SIZE;
			int offsetY = (player.Y - player.posUnitY) % Tile.SIZE;
			// определяем когда двигать карту - а когда должен двигаться
			// персонаж
			boolean movePlayerX = (player.X / Tile.SIZE < centreTileWigdth
					|| W - player.X / Tile.SIZE < centreTileWigdth + 1),
					movePlayerY = (player.Y / Tile.SIZE < centreTileHeight
							|| H - player.Y / Tile.SIZE < centreTileHeight + 2);

			if (timerTile == 1200) {
				stop("/data/win.jpg");
				return;
			}

			if ((timerTile % 120 == 0) && (timerTile > 4)) {

				if ((deadTileX == player.X / Tile.SIZE || deadTileX - 1 == player.X / Tile.SIZE)
						&& (deadTileY == player.Y / Tile.SIZE || deadTileY - 1 == player.Y / Tile.SIZE)) {
					stop("/data/game-over.png");
					return;
				} else {

					deadTileX = player.X / Tile.SIZE + 1;
					deadTileY = player.Y / Tile.SIZE + 1;
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
			Tile tile;
			for (int y = startTileY; y < endTileY; y++) {
				for (int x = startTileX; x < endTileX; x++) {
					tileId = map.getTile(x, y);

					if (tileId == 14) {

						if (((x == deadTileX) || (x == deadTileX - 1)) && ((y == deadTileY) || (y == deadTileY - 1))) {
							tileId = 15;

							if (timerTile % 8 < 4)
								tileId = 15;

							else
								tileId = 16;
							timerTile = timerTile + 1;

						}
					}
					tile = Tile.getTileId(tileId);

					tile.X = (x - startTileX) * Tile.SIZE;
					tile.Y = (y - startTileY) * Tile.SIZE;
					tile.X -= offsetX;
					tile.Y -= offsetY;
					canvas.addRenders(tile);
				}
			}

			// добавляем ботов
			for (Bot bot : bots) {

				// если бот достиг игрока, игра закончена
				if (bot.X / Tile.SIZE == player.X / Tile.SIZE && bot.Y / Tile.SIZE == player.Y / Tile.SIZE) {
					stop("/data/game-over.png");
					return;
				}

				// рисуем бота, когда он в зоне видемости
				if (bot.X >= startTileX * Tile.SIZE && bot.X <= endTileX * Tile.SIZE && bot.Y >= startTileY * Tile.SIZE
						&& bot.Y <= endTileY * Tile.SIZE) {
					// обновляем точку преследования
					if (bot.posPlayer(player.X / Tile.SIZE, player.Y / Tile.SIZE)) {
						bot.mapway = makePath(bot.X / Tile.SIZE, bot.Y / Tile.SIZE, bot.posX, bot.posY);

					} else
						// изменяем положение
						if (bot.directX != 0 || bot.directY != 0) {
						bot.X += bot.directX * bot.speed;
						bot.Y += bot.directY * bot.speed;
					}

					bot.posUnitX = (bot.X - startTileX * Tile.SIZE) - offsetX;
					bot.posUnitY = (bot.Y - startTileY * Tile.SIZE) - offsetY;
					canvas.addRenders(bot);
				}
			}

			if (flag == 0)
				canvas.addRenders(player);

			if ((player.directX != 0 || player.directY != 0) && possibleMove()) {

				player.X += player.directX * player.speed;
				player.Y += player.directY * player.speed;

				if (movePlayerX) {
					player.posUnitX += player.directX * player.speed;
				}
				if (movePlayerY) {
					player.posUnitY += player.directY * player.speed;
				}
			}
			if (flag != 0) {
				if (possibleMove()) {

					if (x == 0 && y == 1) {
						if (flag % 24 < 7) {
							paintPlayer("/data/hero/warr.png", 1304, 39, 80, 60, -10, -35, player.posUnitX,
									player.posUnitY, 10000);
						} else if ((flag % 24 < 12 && flag % 24 > 6) || (flag % 24 > 17)) {
							paintPlayer("/data/hero/warr.png", 1174, 40, 74, 59, -10, -35, player.posUnitX,
									player.posUnitY, 10000);
						}

						else if (flag % 24 < 18 && flag % 24 > 11) {
							paintPlayer("/data/hero/warr.png", 931, 40, 64, 56, 0, -25, player.posUnitX,
									player.posUnitY, 10000);
						}

					} else if (x == 0 && y == -1) {

						if (flag % 24 < 7) {
							paintPlayer("/data/hero/warr.png", 1318, 411, 69, 79, 0, -30, player.posUnitX,
									player.posUnitY, 10000);
						} else if ((flag % 24 < 12 && flag % 24 > 6) || (flag % 24 > 17)) {
							paintPlayer("/data/hero/warr.png", 1193, 421, 66, 69, 10, -30, player.posUnitX,
									player.posUnitY, 10000);
						}

						else if (flag % 24 < 18 && flag % 24 > 11) {
							paintPlayer("/data/hero/warr.png", 1067, 421, 57, 66, 10, -30, player.posUnitX,
									player.posUnitY, 10000);
						}

					} else if (x == 1 && y == 0) {
						if (flag % 24 < 7) {
							paintPlayer("/data/hero/warr.png", 807, 684, 56, 60, 10, -35, player.posUnitX,
									player.posUnitY, 10000);
						} else if ((flag % 24 < 12 && flag % 24 > 6) || (flag % 24 > 17)) {
							paintPlayer("/data/hero/warr.png", 686, 682, 47, 62, 10, -35, player.posUnitX,
									player.posUnitY, 10000);
						}

						else if (flag % 24 < 18 && flag % 24 > 11) {
							paintPlayer("/data/hero/warr.png", 555, 681, 53, 60, 10, -35, player.posUnitX,
									player.posUnitY, 10000);
						}

					} else if (x == -1 && y == 0) {

						if (flag % 24 < 7) {
							paintPlayer("/data/hero/warr.png", 939, 165, 48, 66, 25, -30, player.posUnitX,
									player.posUnitY, 10000);
						} else if ((flag % 24 < 12 && flag % 24 > 6) || (flag % 24 > 17)) {
							paintPlayer("/data/hero/warr.png", 1066, 164, 45, 64, 25, -30, player.posUnitX,
									player.posUnitY, 10000);
						}

						else if (flag % 24 < 18 && flag % 24 > 11) {
							paintPlayer("/data/hero/warr.png", 1448, 158, 55, 70, 25, -40, player.posUnitX,
									player.posUnitY, 10000);
						}

					} else if (x == 0 && y == 0) {

						if (flag1 % 6 < 3) {
							paintPlayer("/data/hero/warr.png", 42, 686, 48, 57, 20, -35, player.posUnitX,
									player.posUnitY, 10000);
						}

						else {
							paintPlayer("/data/hero/warr.png", 170, 686, 47, 57, 20, -35, player.posUnitX,
									player.posUnitY, 10000);
						}

						flag1 = flag1 + 1;
					}
				} else {

					paintPlayer("/data/hero/warr.png", 42, 686, 48, 57, 20, -35, player.posUnitX, player.posUnitY,
							10000);

				}
				x = 0;
				y = 0;
			}

			canvas.repaint();

		}

	}

	public void paintPlayer(String name, int offsetX, int offsetY, int width, int hight, int standoffX, int standoffY,
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

	public boolean possibleMove() {
		int left, right, up, down;
		boolean isPossible = true;

		// верх и низ
		left = (int) Math.ceil((player.X) / Tile.SIZE);
		right = (int) Math.floor((player.X + player.width - 1) / Tile.SIZE);
		up = (int) Math.ceil((player.Y + player.speed * player.directY) / Tile.SIZE);
		down = (int) Math.floor((player.Y + player.height + player.speed * player.directY - 1) / Tile.SIZE);
		if (player.directY == -1 && !(tileIsPossible(left, up) && tileIsPossible(right, up))) {
			isPossible = false;
		} else if (player.directY == 1 && !(tileIsPossible(left, down) && tileIsPossible(right, down))) {
			isPossible = false;
		}
		// право и лево
		left = (int) Math.ceil((player.X + player.speed * player.directX) / Tile.SIZE);
		right = (int) Math.floor((player.X + player.width + player.speed * player.directX - 1) / Tile.SIZE);
		up = (int) Math.ceil((player.Y) / Tile.SIZE);
		down = (int) Math.floor((player.Y + player.height - 1) / Tile.SIZE);
		if (player.directX == -1 && !(tileIsPossible(left, up) && tileIsPossible(left, down))) {
			isPossible = false;
		} else if (player.directX == 1 && !(tileIsPossible(right, up) && tileIsPossible(right, down))) {
			isPossible = false;
		}

		return isPossible;
	}

	public boolean tileIsPossible(int x, int y) {
		Tile tile = Tile.getTileId(map.getTile(x, y));
		return (tile != null && tile.isPossible);
	}
}
