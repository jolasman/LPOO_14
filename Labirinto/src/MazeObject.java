
public class MazeObject {
	int posx;
	int posy;
	
	public MazeObject(int posx, int posy) {this.posx = posx; this.posy = posy;}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MazeObject other = (MazeObject) obj;
		if (posx != other.posx)
			return false;
		if (posy != other.posy)
			return false;
		return true;
	}

	public int getX() {
		return this.posx;
	}
	
	public int getY() {
		return this.posy;
	}
	
	public void setX(int posx) {
		this.posx = posx;
	}
	
	public void setY(int posy) {
		this.posy = posy;
	}
}
