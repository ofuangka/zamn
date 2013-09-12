package zamn.creation;

import java.util.Map;

public class SpriteMapDefinition {
	private Map<String, int[]> spriteMap;
	private String spriteSheetClassPath;

	public Map<String, int[]> getSpriteMap() {
		return spriteMap;
	}

	public String getSpriteSheetClassPath() {
		return spriteSheetClassPath;
	}

	public void setSpriteMap(Map<String, int[]> spriteMap) {
		this.spriteMap = spriteMap;
	}

	public void setSpriteSheetClassPath(String spriteSheetClassPath) {
		this.spriteSheetClassPath = spriteSheetClassPath;
	}

}
