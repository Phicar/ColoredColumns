import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
public class FitchPresen extends JFrame{ // implements KeyListener{
public static int tipoGrafo = 0;//0-> grid, 1-> cycle, 2-> complete
public static int h = 0; //this is the size of layer
public static int w = 0; // this is the amount of layers
public static int t = 0; //time per sample 
public static boolean expImage = false;
public static int col = 0;
public static int longX = 1500;
public static int longY = 800;
public static int widthRec = 50;
public static int widthCirc = 20;
public static int sizeSample = 1000;
public static int tSize = 0;
public static int numComb[];
public static int grid[][];
public static int contAct =0;
public static int sumCont =0;
public static int maxComp = 0;
public static int actComp = 0;
public static int sumMaxCont = 0;
public static double valEspMax = 0.0;
public static int perc = 0;
public static int vis[][];
public static Color Colores[] = {new Color(2,77,54),new Color(251,191,22),Color.black,Color.red};
public static int cuantCol = 2;//Colores.length;
public static int dx[] = {1,0,0,-1};
public static int dy[] = {0,-1,1,0};
public static double valEsp = 0.0;
public static Random ran = new Random();
public static grafo gra;
public static double form(int m,int n,int k){//m is size of each layer and n number of layers
	if(tipoGrafo==0){
		if(m==1)
			return ((k-1)*n+1)/((double)k);
		if(m==2){
			int kkk = k*k*k;
			return (2*kkk*n+(k*k)*(2-3*n)+n-1)/((double)kkk);
		}
		if(m==3){
			return (1183*n+1945.0+1.0/Math.pow(8,n-2))/1568.0;
		}
	}else if(tipoGrafo==1){
		return 1;
	}else if(tipoGrafo==2){
		double num = Math.pow(k,2*m)-Math.pow(k*k-1,m)+n*Math.pow(k-1,m)*(Math.pow(k+1,m)-Math.pow(k,m));
		double den = Math.pow(k,2*m-1);
		return num/den;
	}
	return -1;
}
public static void dfs(int a,int c){
	//vis[a][b]=c;
	gra.vis[a] = c;
	actComp++;
	int acCol = gra.randColor[a];//grid[a][b];
	for(int k = 0;k<gra.listAdy.get(a).size();k++){
		//int na = a+dy[k];
		//int nb = b+dx[k];
		//if(na<0 || na>=h)
		//	continue;
		//if(nb<0 || nb>=w)
		//	continue;
		int codk = gra.listAdy.get(a).get(k);
		//if(gra.vis[na*w+nb]==0 && gra.randColor[na*w+nb]==acCol) //if(vis[na][nb]==0 && grid[na][nb]==acCol)
		if(gra.vis[codk]==0 && gra.randColor[codk]==acCol)
			dfs(codk,c);
	}
}
public static void main(String args[]){
	tipoGrafo = Integer.parseInt(args[0]);
	h = Integer.parseInt(args[1]);
	w = Integer.parseInt(args[2]);
	gra = new grafo(h*w);
	t = Integer.parseInt(args[3]);
	if(args.length>4 && Integer.parseInt(args[4])==1)
		expImage = true;
	grid = new int[h][w];
	for(int i = 0;i<h;i++){
		for(int j = 0;j<w;j++){
			int codv = i*w+j;
			for(int ii = 0;ii<h;ii++){
				for(int jj = 0;jj<w;jj++){
					int codij = ii*w+jj;
					if(i==ii && (jj==j+1 || jj==j-1))
						gra.listAdy.get(codv).add(codij);
					if(jj==j){
						if(tipoGrafo==0 && (ii==i+1 || ii==i-1)){
							gra.listAdy.get(codv).add(codij);
						}else if(tipoGrafo==1 && (ii==(i+1)%h || ii==((i-1)%h+h)%h)){
							gra.listAdy.get(codv).add(codij);
						}else if(tipoGrafo==2 && (ii!=i)){
							gra.listAdy.get(codv).add(codij);
						}
					}
				}

			}
		}
	}
	System.out.println(gra);
	FitchPresen fp = new FitchPresen();
	}
public FitchPresen(){
	super("Percolation Presentation");
	setSize(longX,longY);
	setVisible(true);
	//addKeyListener(this);
	//repaint();
	tSize = 0;
	boolean tal = true;
	numComb = new int[w*h+1];
	while(tal){
		//if(tSize<=sizeSample)
		//	repaint();
		//else
		//	break;
		try{
			Thread.sleep(t);
		}catch(Exception tiempoE){
			System.err.println("Problema en el while Thread");
		}
		if(tSize<sizeSample){
			tSize+=1;
			//numComp = new HashMap<Integer,Integer>();
			for(int i=0;i<h;i++)
				for(int j = 0;j<w;j++){
					//grid[i][j] = ran.nextInt(cuantCol);
					gra.randColor[i*w+j]=ran.nextInt(cuantCol);
				}
			vis = new int[h][w];
			gra.vis = new int[h*w+1];
			contAct =0;
			maxComp = 0;
			for(int i = 0;i<h;i++)
				for(int j= 0;j<w;j++)
					if(gra.vis[i*w+j]==0){
							contAct++;
							actComp = 0;
							dfs(i*w+j,contAct);
							maxComp = Math.max(maxComp,actComp);
						}
			boolean perco = false;
			for(int i=0;i<h;i++){
				for(int ii=0;ii<h;ii++){
					int cod1 = i*w;
					int cod2 = ii*w+w-1;
					if(gra.vis[cod1]==gra.vis[cod2]){
						perc+=1;
						perco = true;
						System.out.println("Percolo! "+tSize);
						break;
					}
				}
				if(perco)break;
			}
			numComb[contAct]++;
			sumCont+=contAct;
			sumMaxCont+=maxComp;
			//System.out.println(contAct);
			valEsp = sumCont/((double)tSize);
			valEspMax = sumMaxCont/((double)tSize);
			//System.out.println(valEsp+"--"+valEspMax);

		}
		//System.out.println("-<"+tSize);
		if(tSize>sizeSample)break;
		if(tSize%1==0){//if(tSize<=sizeSample){
			//System.out.println("-->"+tSize);
			repaint();
			if(!expImage)
				continue;
			// I should put like a flag if I want to print out the images. I will also have to change the color
			try{
				BufferedImage image = new BufferedImage(longX,longY,BufferedImage.TYPE_INT_RGB);
				Graphics g = image.getGraphics();
				Graphics2D graphics = (Graphics2D) g;
				paint(graphics);
				FileOutputStream fos = new FileOutputStream(new File("demo"+tSize+".png"));
				ImageIO.write(image,"png",fos);
			}catch(Exception Err){
				System.err.println(Err);
			}
		}
	}
	}
	public void paint(Graphics g){
		g.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		g.setColor(Color.white);
		g.fillRect(0,0,longX,longY);
		if(tipoGrafo==0){
			for(int i = 0;i<h;i++){
				for(int j = 0;j<w;j++){
					g.setColor(Colores[gra.randColor[i*w+j]]);//Colores
					g.fillRect(50+widthRec*i+5*i,50+widthRec*j+5*j,widthRec,widthRec);
					g.setColor(Color.white);
					if(tSize>1)
						g.drawString(""+gra.vis[i*w+j],50+widthRec*i+5*i+widthRec/2-3,50+widthRec*j+5*j+widthRec/2+3);
				}
			}
		}else{
			for(int i = 0;i<h;i++){
				double ang = (2*Math.PI*i)/((double)h);
				double xRe = 130*Math.cos(ang);
				//if(ang>Math.PI/2.0 && ang<3.0*Math.PI/2.0)
				//	xRe*=-1.0;
				double yRe = 40*Math.sin(ang);
				//if(ang>Math.PI)
				//	yRe*=(-1.0);
				//System.out.println("Level "+(ang)+"--"+xRe+","+yRe);
                                for(int j = 0;j<w;j++){
                                        g.setColor(Colores[gra.randColor[i*w+j]]);//Colores
                                        //g.fillOval(50+widthRec*i+5*i,50+widthRec*j+5*j,widthRec,widthRec);
					g.fillOval(150+50*j+(int)xRe,100+150*j+(int)yRe,widthCirc,widthCirc);
                                        //g.setColor(Color.white);
                                        //g.drawString(""+gra.vis[i*w+j],50+widthRec*i+5*i+widthRec/2-3,50+widthRec*j+5*j+widthRec/2+3);
					//g.drawString(""+gra.vis[i*w+j],50+(int)xRe+widthRec/2-3,50+150*j+(int)yRe+widthRec/2+3);
                                }
                        }
			for(int i= 0;i<h;i++){
				for(int j = 0;j<w;j++){
					int codA = i*w+j;
					double ang = (2*Math.PI*i)/((double)h);
					int xCo1 = 150+50*j+(int)(130*Math.cos(ang));
					int yCo1 = 100+150*j+(int)(40*Math.sin(ang));
					for(int ii = 0;ii<h;ii++){
						for(int jj = 0;jj<w;jj++){
							int codB =ii*w+jj;
                                        		double ang2 = (2*Math.PI*ii)/((double)h);
                                        		int xCo2 = 150+50*jj+(int)(130*Math.cos(ang2));
                                        		int yCo2 = 100+150*jj+(int)(40*Math.sin(ang2));
							if(gra.randColor[codA]==gra.randColor[codB]){
								if((i==ii && (j==jj-1 || j==jj+1))){
									g.setColor(Colores[gra.randColor[codA]]);
									g.drawLine(xCo1+widthCirc/2,yCo1+widthCirc/2,
									xCo2+widthCirc/2,yCo2+widthCirc/2);
								}else if(tipoGrafo==1 && j==jj && (ii==(i+1)%h || ii==((i-1)%h+h)%h)){
									g.setColor(Colores[gra.randColor[codA]]);
                                                                        g.drawLine(xCo1+widthCirc/2,yCo1+widthCirc/2,
                                                                        xCo2+widthCirc/2,yCo2+widthCirc/2);
								}else if(tipoGrafo==2 && j==jj && (i!=ii)){
									g.setColor(Colores[gra.randColor[codA]]);
                                                                        g.drawLine(xCo1+widthCirc/2,yCo1+widthCirc/2,
                                                                        xCo2+widthCirc/2,yCo2+widthCirc/2);
								}
							}
						}
					}
				}
			}
		}
		g.setColor(Color.black);
		g.drawString("Muestra #"+tSize+"/"+sizeSample,longX-500,80);
		g.drawString("Numero de bloques "+contAct,longX-500,100);
		g.drawString("Media # bloques "+valEsp,longX-500,120);
		g.drawString("La formula a discutir: "+form(h,w,cuantCol),longX-500,140);
		g.drawString("Bloque Maximo "+maxComp,longX-500,160);
		g.drawString("Media max Bloque "+valEspMax,longX-500,180);
		double percProb = (double)perc;
		if(tSize>0)percProb/=(double)tSize;
		g.drawString("Prob Perc "+percProb,longX-500,200);
		g.setColor(Color.gray);
		int maxComb = 0;
		int maxi = -1;
		int jj = 0;
		int prNonZero = -1;
		int laNonZero = -1;
		for(int i = 0;i<numComb.length;i++){
			//maxComb = Math.max(maxComb,numComb[i]);
			if(numComb[i]>maxi){
				maxComb = i;
				maxi = numComb[i];
			}
			if(prNonZero==-1 && numComb[i]>0)
				prNonZero = i;
			if(numComb[i]>0)
				laNonZero = i;
			}
		if(prNonZero==-1){
			prNonZero = 0;
			laNonZero = numComb.length-1;
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 13));
		for(int i = prNonZero;i<laNonZero+1;i++){
			g.fillRect(longX-800+jj*20,longY-30-2*numComb[i],15,2*numComb[i]);
			g.drawString(""+(i),longX-800+jj*20+10,longY-5);
			jj+=1;
		}
	}
}
class grafo{
	public int vis[];
	public int randColor[];
	public Vector<Vector<Integer>> listAdy;
	public grafo(int v){
		randColor = new int[v+1];
		listAdy = new Vector<Vector<Integer>>();
		for(int n = 0;n<v;n++)listAdy.add(new Vector<Integer>());
	}
	public String toString(){
		return "->"+listAdy; //System.out.println(listAdy);
	}
}
