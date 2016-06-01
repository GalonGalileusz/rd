/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rd;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Dendelion
 */
public class Board extends JPanel implements Runnable{
    
    private int size = 8;
    private int free = size*size;
    private int id = 0;
    private int empty = size*size;
    
    /*-----rekrystalization------*/
    private double A = 86710969050178.5;
    private double B = 9.41268203527779;
    private double t = 0.0;                 //time
    private int free_r = size*size;
    private int rek = 0;                    //ilosc komorek ktore zrekrystalizowaly
    private double q_critic = 4215840142323.42;
    private double q = 0.0;
    private double q_mean = 0.0;
    /*---------------------------*/
    
    

    private Cell [][]tab = new Cell[size][size];
    private final Cell [][]temp = new Cell[size][size];
    
    public Conditions cond;
        
        public Board(){
                   
        cond = new Conditions();
        
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                
                tab[i][j] = new Cell();
                tab[i][j].setID(0);
                          
                temp[i][j] = new Cell();
                temp[i][j].setID(0);
            }
        }
             
        MouseListener mouseHandler = new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e){
                
                int w = getWidth();
                int h = getHeight();
                              
                int cellW = w/size;
                int cellH = h/size;
                
                if(e.getX()>=0 && e.getY()>=0){
                    
                    int c = e.getX() / cellW;
                    int r = e.getY() / cellH;
                    
                    if(c>=0 && r>=0 && c<size && r<size){
                    
                    /*-------Generating structures-----------*/
                        switch (cond.getLocation()){
                            case 0: //own
                                if(tab[r][c].getID()==0 && tab[r][c].getInRay()==0){
                                    id++;
                                    tab[r][c].setID(id);
                                    tab[r][c].drawColor();
                                    empty--;
                                    tab[r][c].checked();
                                }else{
                                    tab[r][c].setID(0);
                                    tab[r][c].resetColor();
                                }
                                break;
                            case 1: //random
                                
                                for(int i=0; i<cond.getAmount(); ){
                                    
                                    if(cond.getAmount()>=free)
                                        break;
                                    
                                    Random ri = new Random();
                                    
                                    int a=ri.nextInt(size);
                                    int b=ri.nextInt(size);
                                    
                                    if(tab[a][b].getID()==0){
                                        id++;
                                        tab[a][b].setID(id);
                                        tab[a][b].drawColor();
                                        i++;
                                        free--;
                                        empty--;
                                        tab[a][b].checked();
                                    }
                                }
                                break;
                            case 2: //evenly
                                int grains = cond.getAmount();
                                int maxInRow = (size-1)/2;          //60
                                int max = maxInRow * maxInRow;      //3600
                                
                                System.out.println(" "+maxInRow);
                                
                                if(grains<=max){
                                    double element = Math.sqrt(grains);
                                    
                                    System.out.println("ele "+element);
                                    
                                    //if(element%1 == 0){
                                                                       
                                        int position = (int) ((size-1) / (2*element));
                                        //int position = (int) (Math.ceil((size-1) / (2*element)));
                                        
                                        //System.out.println("position "+position);
                                        
                                        int rest = (int)(((size-1) / (2*element) - position) * 10);
                                        
                                        //System.out.println("rest "+rest);
                                        
                                        if(rest>=5)
                                            position++;
                                        
                                        //System.out.println("2position "+position);
                                        //System.out.println("2rest "+rest);
                                        
                                        for(int i=0; i<grains; ){
                                                for(int p=position; p<size && i<grains; p+=2*position){
                                                    for(int q=position; q<size && i<grains; q+=2*position){
                                                        id++;
                                                        tab[p][q].setID(id);
                                                        tab[p][q].drawColor();
                                                        i++;  
                                                        empty--;
                                                        tab[p][q].checked();
                                                    }
                                                }

                                        }
                                        
//                                    }else{
//                                        
//                                    }                                    
                                }
                                
                                break;
                            case 3: //with ray
                                
                                int ray = cond.getRay();
                                
                                for(int i=0; i<cond.getAmount(); ){
                                    
                                    System.out.println(i);
                                    
                                    if(cond.getAmount()>=free)
                                        break;
                                    
                                    Random ri = new Random();
                                    
                                    int a=ri.nextInt(size);
                                    int b=ri.nextInt(size);
                                    
                                    if(tab[a][b].getID()==0 && tab[a][b].getInRay()==0){
                                        id++;
                                        tab[a][b].setID(id);
                                        tab[a][b].drawColor();
                                        empty--;
                                        tab[a][b].checked();
                                        
                                        i++;
                                        
                                        for(int m=a-ray; m<=a+ray; m++){
                                            for(int n=b-ray; n<=b+ray; n++){
                                                if(m<0 || m>=size)
                                                    continue;
                                            
                                                if(n<0 || n>=size)
                                                    continue;
                                            
                                                if(tab[m][n].getInRay()==0){
                                                    tab[m][n].setInRay();
                                                    free--;
                                                }
                                            }
                                        }
                                    }  
                                }
                                break;
                        }            
                    }
                }                
                repaint();      
            }
        };
        addMouseListener(mouseHandler);
    }
   
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        int w = getWidth();
        int h = getHeight();
                              
        int cellW = w/size;
        int cellH = h/size;
        
        int x = (w - size * cellW)/2;
        int y = (h - size * cellH)/2;
                
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(tab[i][j].getID()==0){
                    g2d.setColor(tab[i][j].getColor());
                }else{
                    g2d.setColor(tab[i][j].getColor());
                }
                Rectangle cell = new Rectangle(x + j*cellW,
                            y + i*cellH,
                            cellW-1,
                            cellH-1);
                g2d.fill(cell);
            }
        }     
    }
    
    @Override
    public void run() {
        
        System.out.println("Running ...");
        while(true && free_r>0)
        {
            if(empty==0){
                System.out.println("freeR "+free_r);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                for(int i=0; i<size; i++){
                    for(int j=0; j<size; j++){
                        this.checkPosition(i, j);
                        }
                    }
                
                
                System.out.println("t "+t);
                //this.countDensity(t);
                this.recrystallization(t);
                repaint();
                t+=0.001;
            }
            else{
                System.out.println("empty "+empty);
                if(cond.getStatus()==1){
                this.action();
                 repaint();
                }
            }           
            if(cond.getStatus()==2){
                this.clean();
                repaint();
            }
            try{
                Thread.sleep(1000);
            }catch (Exception ex){
            }
        }
    }

    public void clean(){
        
        empty = size*size;
        free = size*size;
        id=0;
        
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){

                tab[i][j] = new Cell();
                tab[i][j].setID(0);
                tab[i][j].resetColor();
                
                temp[i][j] = new Cell();
                temp[i][j].setID(0);
                temp[i][j].resetColor();

            }
        }
        cond.setStatus(0);
    }
    
    public void action(){
            
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){       
                int nbhdIndex = 0; 
                if(tab[i][j].getID()==0){
                            switch (cond.getBC()){
                                case 0: //----------------NON PERIODIC BC
                                    if((i==0 || i==size-1) && (j==0 || j==size-1)){
                                        tab[i][j].setNBHD(0, 0);
                                        tab[i][j].setNBHD(1, 0);
                                        tab[i][j].setNBHD(2, 0); 
                                        tab[i][j].setNBHD(3, 0);
                                        tab[i][j].setNBHD(4, 0);//corner cell
                                        nbhdIndex = 5;  
                                    }else if(i==0 || i==size-1 || j==0 || j==size-1){
                                        tab[i][j].setNBHD(0, 0);
                                        tab[i][j].setNBHD(1, 0);
                                        tab[i][j].setNBHD(2, 0);   //on the edge
                                        nbhdIndex = 3;
                                    }
                                    /*-------CHECKING NEIGHBOURHOOD--------------*/           
                                    for(int k=i-1; k<i+2; k++){ //-----------------from 1 up to 1 low (3 iterations) in rows
                                        for(int l=j-1; l<j+2; l++){ //-------------from 1 up to 1 low (3 iterations) in columns
                                            if((k==i && l==j) || k<0 || k>=size || l<0 || l>=size)
                                                ;//System.out.println("go ");
                                            else{
                                                tab[i][j].setNBHD(nbhdIndex, tab[k][l].getID());
                                                nbhdIndex++;
                                            }
                                        }
                                    } 
                                    temp[i][j].setID(tab[i][j].chooseSeed());
                                    break;
        
                                case 1: //----------------PERIODIC BC
                                    for(int k=i-1; k<i+2; k++){
                                        for(int l=j-1; l<j+2; l++){
                                            int kk, ll;
                                            
                                            if(k<0)
                                                kk=size-1;
                                            else if(k>=size)
                                                kk=0;
                                            else
                                                kk=k;
                                            
                                            if(l<0)
                                                ll=size-1;
                                            else if(l>=size)
                                                ll=0;
                                            else 
                                                ll=l;
                                            
                                            if(k==i && l==j)
                                                ;//System.out.println("don't check itself");
                                            else{
                                                tab[i][j].setNBHD(nbhdIndex, tab[kk][ll].getID());
                                                nbhdIndex++;
                                            }
                                        }
                                    }
                                    temp[i][j].setID(tab[i][j].chooseSeed());
                                    break;
                            }
                            
                    }
                
            }
        }
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                for(int k=0; k<size; k++){
                    for(int l=0; l<size; l++){
                        if(tab[i][j].getID()==0 && temp[i][j].getID()==tab[k][l].getID()){
                            tab[i][j].setID(temp[i][j].getID());
                            tab[i][j].setColor(tab[k][l].getColor());
                            tab[i][j].cleanNBHD();
                            temp[i][j].setID(0);
                            
                        }
                        else if(tab[i][j].getID()!=0 && !tab[i][j].isChecked()){
                            empty--;
                            tab[i][j].checked();
                        }
                        
                    }
                }
            }
        }
    }
    
    /*------------------------RECRYSTALLIZATION-----------*/
    
    public double countDensity(double time){
        double density=A/B+(1-A/B) * Math.exp(-B*time);
        //System.out.println("q "+q);
        return density;
    }
    
    public void checkPosition(int i, int j){
        for(int k=i-1; k<i+2; k++){ //-----------------from 1 up to 1 low (3 iterations) in rows
            for(int l=j-1; l<j+2; l++){ //-------------from 1 up to 1 low (3 iterations) in columns
                if((k==i && l==j) || k<0 || k>=size || l<0 || l>=size)
                    ;//System.out.println("go ");
                else{
                    if(tab[k][l].getID()!=tab[i][j].getID()){
                     tab[i][j].onEdge();
                     break;
                    }                   
                }
            }
        }
    }
    
    public double packages(boolean edge){
        Random rand = new Random();
        double pack = 0;
        
        if(edge){
            pack = ((rand.nextInt(61)+120)/100)*q_mean;
        }else{
            pack = (rand.nextInt(31)/100)*q_mean;
        }
        
        return pack;
    }
    
    public void recrystallization(double time){
        q = this.countDensity(time) - this.countDensity(time-1);
//        System.out.println("q "+q);
        q_mean = q/free_r;
//        System.out.println("q-mean "+q_mean);
        
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                //this.checkPosition(i, j);
                tab[i][j].setQ(this.packages(tab[i][j].isOnEdge()));
                
//                System.out.println("i j "+i+" "+j);
//                System.out.println("q kom "+tab[i][j].getQ());
//                System.out.println("q cri "+q_critic);
            }
        }
        
        for(int m=0; m<size; m++){
            for(int n=0; n<size; n++){
                if(tab[m][n].getQ()>q_critic && tab[m][n].isOnEdge() && !tab[m][n].isRecrystalized()){
                    //rekrystalizuj
                    free_r--; //zmniejsz pzrestrzen
                    id++;
                    tab[m][n].setID(id);
                    tab[m][n].drawColor();
                    tab[m][n].recrastilezed();
                    tab[m][n].setQ(0);                   
                }
            }
        }
    }
    
}
