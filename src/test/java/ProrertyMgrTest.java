import com.dhb.tank.comms.ProrertyMgr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProrertyMgrTest {

	@Test
	void testGetInt() {
		int gameWidth = ProrertyMgr.getInt("gameWidth");
		int gameHight = ProrertyMgr.getInt("gameHeight");
		Assertions.assertEquals(gameWidth,1000);
		Assertions.assertEquals(gameHight,800);
	}
}
