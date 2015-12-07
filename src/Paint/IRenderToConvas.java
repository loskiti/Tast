package Paint;
/**
 * гарантия реализации метода обрисовки на холсте
 */

import java.awt.Graphics;

public interface IRenderToConvas {

	void render(Graphics g);

	/**
	 * глубина позиционирования
	 */

	int getDeep();

}
