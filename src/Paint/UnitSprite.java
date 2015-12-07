package Paint;

import java.awt.Graphics;

import Base.Game;

public abstract class UnitSprite extends Unit {

	public int imageWidth = 128;
	public int imageHeight = 128;
	public int imageX = 0;
	public int imageY = 0;
	/**
	 * позиции при зацикливании
	 */
	public int imageMinX = 0;
	public int imageMinY = 0;
	public int imageMaxX = 0;
	public int imageMaxY = 0;
	/**
	 * для рисования размеры (для расчета другие)
	 */

	public int paintWidth = 100;
	public int paintHeight = 100;

	@Override
	public void render(Graphics g) {
		update();
		// статическое позиционирование
		int isoX = posUnitX, isoY = posUnitY;
		if (Game.USE_ISO) {
			isoX = (posUnitX - posUnitY);
			isoY = (posUnitX + posUnitY) / 2;
		}

		g.drawImage(imageUnit, isoX + Game.OFFSET_MAP_X + offsetRenderX, isoY + offsetRenderY,
				isoX + paintWidth + Game.OFFSET_MAP_X + offsetRenderX, isoY + paintHeight + offsetRenderY,
				imageX * imageWidth, imageY * imageHeight, imageX * imageWidth + imageWidth,
				imageY * imageHeight + imageHeight, null);
	}

	@Override
	public void update() {
		// зацикливаем переход по кадрам
		imageX++;
		if (imageX > imageMaxX) {
			imageX = imageMinX;
		}
		imageY++;
		if (imageY > imageMaxY) {
			imageY = imageMinY;
		}
		super.update();
	}
}
