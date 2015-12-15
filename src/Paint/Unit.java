package Paint;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import Base.Game;
import Map.MapWay;

public abstract class Unit implements IRenderToConvas {

	/**
	 * картинка
	 */
	protected Image imageUnit;
	/**
	 * реальные координаты персонажа
	 */

	private int X;
	private int Y;
	protected int layerDeep;
	/**
	 * координаты, видимые на экране
	 */
	protected int posUnitY;
	protected int posUnitX;
	protected int speed = 5;
	private int width = 32;
	private int height = 32;
	protected int directX = 0;
	protected int directY = 0;
	public MapWay mapway;

	/**
	 * Отступ при рисовании объекта на карте, что бы он занял середину клетки
	 */
	public int offsetRenderX = 0;
	/**
	 * Отступ при рисовании объекта на карте, что бы он занял середину клетки
	 */
	public int offsetRenderY = 0;

	public void initiation(String name) {
		imageUnit = new ImageIcon(getClass().getResource(name)).getImage();
	}

	@Override
	public void render(Graphics g) {

		update();

		int isoX = posUnitX, isoY = posUnitY;
		if (Game.USE_ISO) {
			isoX = (posUnitX - posUnitY);
			isoY = (posUnitX + posUnitY) / 2;
		}
		g.drawImage(imageUnit, isoX + offsetRenderX + Game.OFFSET_MAP_X, isoY + offsetRenderY, width, height, null);
	}

	/**
	 * определение направления следущий точки (для мышки, на клаве авто, тут в
	 * зависимости от следущий точки пути до цели)
	 */

	public void update() {
		if (mapway != null && mapway.isNextPoint()) {
			if (mapway.getP() == null) {
				mapway.startPoint(X, Y);
			} else {
				mapway.nextPoint(speed);
				if (X != mapway.getP().x) {
					directX = (X > mapway.getP().x) ? -1 : 1;
				} else {
					directX = 0;
				}
				if (Y != mapway.getP().y) {
					directY = (Y > mapway.getP().y) ? -1 : 1;
				} else {
					directY = 0;
				}
			}
		} else {
			directX = 0;
			directY = 0;
		}
	}

	public int getDeep() {

		return layerDeep;

	}

	public int getX() {
		return X;
	}

	public void setX(int X) {
		this.X = X;
	}

	public int getY() {
		return Y;
	}

	public void setY(int Y) {
		this.Y = Y;
	}

	public void setPosUnitY(int posUnitY) {
		this.posUnitY = posUnitY;
	}

	public int getPosUnitY() {
		return posUnitY;
	}
	public void setPosUnitX(int posUnitX) {
		this.posUnitX = posUnitX;
	}

	public int getPosUnitX() {
		return posUnitX;
	}
	public int getSpeed() {
		return speed;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getDirectX() {
		return directX;
	}
	public void setDirectX(int directX ) {
		this.directX= directX;
	}
	public int getDirectY() {
		return directY;
	}
	public void setDirectY(int directY ) {
		this.directY= directY;
	}
}
