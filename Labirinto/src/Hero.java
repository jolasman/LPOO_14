
public class Hero extends MazeObject {
	boolean armed;
	
	public Hero(int posx, int posy) {super(posx, posy); this.armed = false;}
	
	public void setArmed(boolean armed) {
		this.armed = armed;
	}
	
	public boolean getArmed() {
		return armed;
	}
}
