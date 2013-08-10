package zamn.creation;

import java.util.Map;

public class SpriteMapDefinition {
	private Map<String, Integer[]> spriteMap;
	private String spriteSheetClassPath;

	public Map<String, Integer[]> getSpriteMap() {
		return spriteMap;
	}

	public String getSpriteSheetClassPath() {
		return spriteSheetClassPath;

	}

	public void setSpriteMap(Map<String, Integer[]> spriteMap) {
		this.spriteMap = spriteMap;
	}

	public void setSpriteSheetClassPath(String spriteSheetClassPath) {
		this.spriteSheetClassPath = spriteSheetClassPath;
	}

}
