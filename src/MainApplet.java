import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.text.*;

public class MainApplet extends Frame implements MouseListener, MouseMotionListener, Runnable
{
	private static final long serialVersionUID = 956428199263150993L;
	private static final Vector gravity = new Vector(0,.98);
	private Vector mousePos = new Vector(0,0);
	private Circles actualCircle;
	private static Circles redBall;
	static ArrayList<Circles> circle = new ArrayList<Circles>();
	private Image dbImage;
	private Graphics dbg;
	static final int appletWidth = 800;
	static final int appletHeight = 500;
	private final double screenArea = (appletWidth * appletHeight);
	//private static final int monitorRefreshRate = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
	private static final int monitorRefreshRate = 60;
	private static final long rateNs = (long)1000000000/monitorRefreshRate;
	private double areaFilled = 0;
	boolean expandingCircle = false;
	final static int redBallRadius = 20;
	private DecimalFormat df = new DecimalFormat("#.##");
	int lives = 5;
	boolean gameOver = false;
	int level = 1;
	boolean clicked = false;
	boolean wat = false;
	public MainApplet()
	{
		
	}

	public static void main(String args[])
	{
		MainApplet mainApplet = new MainApplet();
		mainApplet.setSize(appletWidth, appletHeight+100);
		mainApplet.addMouseListener(mainApplet);
		mainApplet.addMouseMotionListener(mainApplet);
		System.out.println("Monitor Refresh Rate: " + monitorRefreshRate + " -- Refresh Rate in Nano Seconds: " + rateNs);
		

			redBall = new Circles(new Vector(((int)(Math.random()*appletHeight)), ((int)(Math.random()*appletWidth))), new Vector(0,0), redBallRadius);
			redBall.oldPos = redBall.pos.sub(new Vector(Math.random()>0.5?2:-2,Math.random()>0.5?2:-2));
			redBall.mass = 100;
			Thread thread = new Thread(mainApplet);
			thread.setDaemon(true);
			thread.start();
		mainApplet.setVisible(true);
	}
	
	@Override
	public void run()
	{
		while(true)
		{

		}
		
	}

	public void paint(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
			if((expandingCircle == true) && ((actualCircle.radius*2) <= Math.min(appletWidth, appletHeight))) //&& (!(actualCircle.isColl(circle))))
			{
							actualCircle.addRad(1);
							actualCircle.pos = mousePos;
							actualCircle.wallColl(15);
							actualCircle.updateMass();
							actualCircle.drawCircle(g);
						    
					if(actualCircle.isColl(circle))
					{
							actualCircle.pos = mousePos;
							circle.add(actualCircle);
							actualCircle.radius--;
					 		actualCircle.acc = gravity;
					 		areaFilled 	+= actualCircle.mass * 3.14;
					 		expandingCircle = false;
					}
					if(redBall.isColl(actualCircle))
					{
						if(lives != 0)
							lives--;
						else
							gameOver = true;
						expandingCircle = false;
						actualCircle = null;	
					}
			}
			Circles.checkColl(circle);
				for(int x = 0; x < circle.size(); x++)
				{
					circle.get(x).drawCircle(g);		
				}
			
			redBall.singleCheckColl(circle);
			redBall.wallColl(1);
			redBall.setMagnitudeVelocity(1.41*2);
			redBall.update();
			redBall.drawRedCircle(g);
			FontMetrics fm = g.getFontMetrics();
		
		
		g.setFont(new Font("SansSerif", Font.BOLD, 30));
		double area = areaFilled/screenArea*100;
		if(gameOver == true)
		{
			g.setColor(Color.GREEN);
			int widthString = fm.stringWidth("GAME OVER :(, Click anywhere to restart");
			g.drawString("GAME OVER :(, Click anywhere to restart", appletWidth/2 - widthString/2, (appletHeight+100)/2 - 15);
			wat = true;
			while(clicked != false)
			if(clicked == true)
			{
				
				circle.clear();
				g.setColor(Color.white);
				g.drawRect(0, 0, appletWidth, appletHeight);
				lives = 5;
				areaFilled = 0;
				level = 1;
				clicked = false;
				gameOver = false;
				wat = false;
			}
		}
		
		if(area > level*10)
		{
			g.setColor(Color.GREEN);
			int widthString = fm.stringWidth("Winner! Level++ Click anywhere to continue");
			g.drawString("Winner! Level++ Click anywhere to continue", appletWidth/2 - widthString/2, (appletHeight+100)/2 - 15);
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(clicked != false)
			if(clicked = true)
			{
				level++;
				circle.clear();
				g.setColor(Color.white);
				g.drawRect(0, 0, appletWidth, appletHeight);
				lives = 5;
				areaFilled = 0;
				clicked = false;

			}
		}
		g.setColor(Color.BLACK);
		int livesLeft = fm.stringWidth("Area Filled (Pel^2)                ");
		int live = fm.stringWidth("Lives Left:    )");
	    g.drawString("Area Filled (Pel^2):" + df.format(area)+ "%" , 0, appletHeight + 30);
	    g.drawString("Lives Left: " + lives  , livesLeft, appletHeight + 30);
	    g.drawString("Level: " + level  , live+livesLeft, appletHeight + 30);
	    g.drawString("Level: " + level  , live+livesLeft, appletHeight + 30);
		repaint();
		}
	

		public void update (Graphics g)
		{
			if (dbImage == null)
			{
				dbImage = createImage (this.getSize().width, this.getSize().height);
				dbg = dbImage.getGraphics();
				}

				dbg.setColor(getBackground());
				dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

				dbg.setColor(getForeground());
				paint(dbg);

				g.drawImage(dbImage, 0, 0, this);
			}
		
		public void clear()
		{
			
		}
		
		
	public void mousePressed(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			mousePos.x = e.getX();
      		mousePos.y = e.getY();
      		if(Circles.isInCir(circle, (int)mousePos.x, (int)mousePos.y))
      			return;
			actualCircle = new Circles(mousePos, new Vector(0,0), 1);
			expandingCircle = true;
		}
    }

    public void mouseReleased(MouseEvent e)
    {
    	if(e.getButton() == MouseEvent.BUTTON1)
		{
    	if(expandingCircle == false)
    	{
    		return;
    	}
    		mousePos.x = e.getX();
    	    mousePos.y = e.getY();
    	    actualCircle.pos = mousePos;
    		actualCircle.acc = gravity;
    		circle.add(actualCircle);
    		areaFilled 	+= actualCircle.mass * 3.14;
			expandingCircle = false;
			
		}
    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }

    public void mouseClicked(MouseEvent e)
    {
    	if(e.getButton() == MouseEvent.BUTTON1 && expandingCircle == false && gameOver == true || wat == true)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		clicked = true;
    }

	public void mouseMoved(MouseEvent e)
	{
		if(expandingCircle == false)
    	{
    		return;
    	}
		actualCircle.pos.x = e.getX();
      	actualCircle.pos.y = e.getY();
      	mousePos.x = e.getX();
        mousePos.y = e.getY();
    }

    public void mouseDragged(MouseEvent e)
    {
    	if(expandingCircle == false)
    	{
    		return;
    	}
		actualCircle.pos.x = e.getX();
      	actualCircle.pos.y = e.getY();
	    mousePos.x = e.getX();
    	mousePos.y = e.getY();
    }
}