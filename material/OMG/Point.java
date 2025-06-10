package OMG;
public class Point
{
	public int x;
	public int y;
	public Point(int _x, int _y) {
		this.x = _x;
		this.y = _y;
	}
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public String toString() { return "x:" + x + ",y:" + y; }
}