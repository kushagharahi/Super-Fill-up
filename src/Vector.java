
class Vector {

	public double x,y;

	public Vector()
	{
	}

	public Vector(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public double findDistanceBetween(Vector v)
	{
		double a = this.y - v.y;
		double b = this.x - v.x;
		double c = Math.sqrt((a*a)+(b*b));
		return c;
	}
	
	public double findDistanceBetweenSq(Vector v)
	{
		double a = this.y - v.y;
		double b = this.x - v.x;
		double c = ((a*a)+(b*b));
		return c;
	}
	
	public double getLength()
	{
		return Math.sqrt((this.x*this.x)+(this.y*this.y));
	}

	public void setLength(double length)
	{
		double l = this.getLength();
		this.x = (this.x/l)*length;
		this.y = (this.y/l)*length;
	}

	public Vector sub(Vector b)
	{
		return new Vector(this.x-b.x, this.y-b.y);
	}

	public Vector add(Vector b)
	{
		return new Vector(this.x+b.x, this.y+b.y);
	}

	public Vector mult(Vector b)
	{
		return new Vector(this.x*b.x, this.y*b.y);
	}

	public Vector mult(Double b)
	{
		return new Vector(this.x*b, this.y*b);
	}

	public Vector div(Vector b)
	{
		return new Vector(this.x/b.x, this.y/b.y);
	}
	public String toString()
	{
		return this.x + " " + this.y;
	}
}
