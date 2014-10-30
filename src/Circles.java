import java.awt.*;
import java.util.*;
import java.awt.geom.*;

class Circles
{
	Vector pos, oldPos, acc, vel;
	double radius, mass;
	private static final MainApplet app = new MainApplet();
	private static final float[] dist = {0.0f, 1.0f};
	private Color [] colors = {Color.BLACK, Color.BLACK};
	private Color [] color = {Color.WHITE, Color.RED};
    int xMax = app.appletWidth;
    int yMax = app.appletHeight;
	
	public Circles(Vector pos, Vector acc, double radius)
	{
		this.pos = pos;
		this.acc = acc;
		this.oldPos = pos;
		this.radius = radius;
		this.vel = new Vector();
		mass = this.radius*this.radius;
	}

	
	public void updateMass()
	{
		mass = this.radius*radius;
		intToCol((int)this.mass*2);
	}

	public void update()
	{
		vel = pos.sub(oldPos);
		//Vector a = (acc.mult(new Vector(.5,.5)));
		oldPos = pos;
		pos = pos.add(vel.add(acc));
	}


	public static boolean isInCir(ArrayList<Circles> a, int x, int y)
	{
		for(Circles v:a)
		{
			double db = Math.abs(v.pos.findDistanceBetween(new Vector(x,y))); //distance between
			if(db <= v.radius)
			{
				return true;
			}
		}
		return false;
	}
	
	public void setMagnitudeVelocity(double m)
	{
		Vector vel = pos.sub(oldPos);
		vel.setLength(m);
		this.oldPos = pos.sub(vel);
		//this.oldPos = new Vector((oldPos.x/Math.sqrt(oldPos.x*oldPos.x+oldPos.y*oldPos.y)*m),(oldPos.y/Math.sqrt(oldPos.x*oldPos.x+oldPos.y*oldPos.y)*m)).add(oldPos);
	}

	public void wallColl(double fac)
	 {
      int xMin = 0;
      int yMin = 0;
	  double vX = pos.x - oldPos.x;
	  double vY = pos.y - oldPos.y;
	  vX *= fac;
	  vY *= fac;
	  if (this.pos.x - radius < xMin)
	  {
		  this.pos.x = Math.max(xMin, this.pos.x - this.radius) + this.radius;
		  this.oldPos.x = this.pos.x + vX;
	  }
	  if (this.pos.x + radius > xMax)
	  {
		  this.pos.x = Math.min(xMax, this.pos.x + this.radius) - this.radius;
		  this.oldPos.x = this.pos.x + vX;
	  }
	  if (this.pos.y - radius < yMin)
	  {
		  this.pos.y = Math.max(yMin, this.pos.y - this.radius) + this.radius;
		  this.oldPos.y = this.pos.y + vY;
	  }
	  if (this.pos.y + radius > yMax)
	  {
		  this.pos.y = Math.min(yMax, this.pos.y + this.radius) - this.radius;
		  this.oldPos.y = this.pos.y + vY;
	  }
	 }
	

	public static void checkColl(ArrayList<Circles> a)	//Checking for Circle Collisions
	{
		for(int x = 0; x < a.size(); x++)
		{
			for(int y = 0; y < a.size(); y++)
			{
				if(x != y)
				{
					double db = a.get(x).pos.findDistanceBetweenSq(a.get(y).pos); //distance between
					double er = a.get(x).radius + a.get(y).radius;
					
					if(db < er*er)
					{
						//db = a.get(x).pos.findDistanceBetween(a.get(y).pos); //distance between
					    //er = a.get(x).radius + a.get(y).radius;
						//if(db == 0)
					  	//	db =1;
						double m1m2 = a.get(x).mass + a.get(y).mass;
						Vector d =  a.get(x).pos.sub(a.get(y).pos);
						d.setLength(er - Math.sqrt(db));
						a.get(x).pos = a.get(x).pos.add(d.mult((a.get(y).mass/m1m2)));
						a.get(y).pos = a.get(y).pos.sub(d.mult((a.get(x).mass/m1m2)));
						if(db == 0)
					  		System.out.println(db);

					}
				}
			}
			a.get(x).update();
			a.get(x).wallColl(.3);
		}
			
	}
	
	
	public void singleCheckColl(ArrayList<Circles> a)	//Checking for Circle Collisions
	{
		for(int x = 0; x < a.size(); x++)
		{
			double db = a.get(x).pos.findDistanceBetweenSq(this.pos); //distance between
			double er = a.get(x).radius + this.radius;
			if(db < er*er)
			{
				//db = a.get(x).pos.findDistanceBetween(this.pos); //distance between
				if(db == 0)
			  		db =1;
				double m1m2 = a.get(x).mass + this.mass;
				Vector d =  a.get(x).pos.sub(this.pos);
				d.setLength(er - Math.sqrt(db));
				a.get(x).pos = a.get(x).pos.add(d.mult((this.mass/m1m2)));
				this.pos = this.pos.sub(d.mult((a.get(x).mass/m1m2)));
				if(db == 0)
			  		System.out.println(db);
			}
		}
	}
	
			

	public boolean isColl(ArrayList<Circles> a)
	{
		for(int v = 0; v < a.size(); v++)
		{
			if(this == a.get(v))
			{
				continue;
			}
					double db = this.pos.findDistanceBetweenSq(a.get(v).pos); //distance between
					double er = this.radius + a.get(v).radius;
					if(db < er*er)
					{
						return true;
					}
		}
		return false;
	}

	public boolean isColl(Circles a)
	{
					double db = this.pos.findDistanceBetweenSq(a.pos); //distance between
					double er = this.radius + a.radius;
					if(db < er*er)
					{
						return true;
					}
		
		return false;
	}
	RadialGradientPaint paint= new RadialGradientPaint(new Point2D.Float(0,0), (float)1, dist, colors); //new RadialGradientPaint(center, radius, dist, colors);
	public void drawCircle(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		g2D.translate(this.pos.x, this.pos.y);
		g2D.setPaint(paint);
		g.fillOval((int)(-radius),(int)(-radius),(int)(radius*2),(int)(radius*2));
		g2D.translate(-this.pos.x, -this.pos.y);
	}
	RadialGradientPaint redPaint = new RadialGradientPaint(new Point2D.Float(0,0), MainApplet.redBallRadius/2, dist, color); //new RadialGradientPaint(center, radius, dist, colors);
	public void drawRedCircle(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		g2D.translate(this.pos.x, this.pos.y);
		g2D.setPaint(redPaint);
		g.fillOval((int)(-radius),(int)(-radius),(int)(radius*2),(int)(radius*2));
		g2D.translate(-this.pos.x, -this.pos.y);
	}
	
	public void addRad(double rad)
	{
		this.radius +=rad;
		mass = this.radius * this.radius;
		this.paint = new RadialGradientPaint(new Point2D.Float(0,0), (float)this.radius, dist, colors); //new RadialGradientPaint(center, radius, dist, colors);
	}

	public void intToCol(int a)
	{
		int red  = (a & 0xFF0000) >> 16;
		int green= (a & 0x00FF00) >> 8;
		int blue = a & 0x0000FF;
		colors[0] = new Color(red, green, blue);
	}


}
