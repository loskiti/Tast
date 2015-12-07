package Base;

/**
 * Зомби
 */
import Paint.BaseActionUnitSprite;

public class Bot extends BaseActionUnitSprite {
	/**
	 * позиция игрока
	 */
	int posX;
	int posY;

	public Bot() {

		speed = 2;
		offsetRenderY = -74;
		offsetRenderX = -27;
		paintWidth = 128;
		paintHeight = 128;
		layerDeep = 1000;
		String name = "/data/bot/zombie.png";
		initiation(name);
		actionStand();
		turnRight();

	}

	/**
	 * изменилось ли положение игрока
	 */
	public boolean posPlayer(int posX, int posY) {
		if (posX != this.posX || posY != this.posY) {
			this.posX = posX;
			this.posY = posY;
			return true;
		}
		return false;
	}
}
