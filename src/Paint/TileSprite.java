package Paint;
/**
 * работа со спрайтом изображений
 */

import java.awt.Graphics;

import Base.Game;

public class TileSprite extends Tile {

	/**
	 * размеры картинки на спрайте!
	 */
	public int width;
	public int height;
	/**
	 * Отступ в изображении (координаты изображения на спрайте)
	 */

	public int offsetX;
	public int offsetY;

	public int layerDeep;

	public TileSprite(String image, int posX, int posY, int width, int height, int offsetX, int offsetY,
			boolean isWalkable, int door, int layerDeep) {
		super(image, posX, posY, isWalkable, door, layerDeep);
		this.width = width;
		this.height = height;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.layerDeep = layerDeep;
	}

	@Override
	public void render(Graphics g) {
		int isoX = X, isoY = Y;
		if (Game.USE_ISO) {
			// Расчет координат в изометрической проекции
			isoX = (X - Y);
			isoY = (X + Y) / 2;
		}

		g.drawImage(getImage(image), isoX + Game.OFFSET_MAP_X, isoY, isoX + width + Game.OFFSET_MAP_X, isoY + height,
				offsetX, offsetY, offsetX + width, offsetY + height, null);
	}
}
