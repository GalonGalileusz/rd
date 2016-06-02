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
    
    private double suma = 0.0;
    
    private int size = 300;
    private int free = size*size;
    private int id = 0;
    private int empty = size*size;
    
    /*-----rekrystalization------*/
    private double A = 86710969050178.5;
    private double B = 9.41268203527779;
    private double t = 0.0;                 //time
    private int free_r = size*size;
    private int rek = 0;                    //ilosc komorek ktore zrekrystalizowaly
    private double q_critic = 46842668.248038;
    private double q = 0.0;
    private double q_mean = 0.0;
    private double delta_q = 0.0;
    /*---------------------------*/
    
    

    private Cell [][]tab = new Cell[size][size];
    private final Cell [][]temp = new Cell[size][size];
    private final Cell [][]temp_r = new Cell[size][size];
    
    public Conditions cond;
        
        public Board(){
                   
        cond = new Conditions();
        
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                
                tab[i][j] = new Cell();
                tab[i][j].setID(0);
                          
                temp[i][j] = new Cell();
                temp[i][j].setID(0);
                
                temp_r[i][j] = new Cell();
                temp_r[i][j].setID(0);
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
                            cellW,
                            cellH);
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
                    Thread.sleep(4000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("t "+t);
                this.recrystallization2();
                //repaint();
                
//                for(int i=0; i<size; i++){
//                    for(int j=0; j<size; j++){
//                        this.checkPosition(i, j);
//                        }
//                    }
//                
//                
//                System.out.println("t "+t);
//                //this.countDensity(t);
//                this.recrystallization(t);
//                repaint();
//                t+=0.001;
            }
            else{
                //System.out.println("empty "+empty);
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
                Thread.sleep(100);
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
                for(int k=i-1; k<i+2; k++){
                    for(int l=j-1; l<j+2; l++){
                        
                        if(cond.getBC()==0){
                             if((k==i && l==j) || k<0 || k>=size || l<0 || l>=size)
                    ;//System.out.println("go ");
                        else{
                        if(tab[i][j].getID()==0 && temp[i][j].getID()==tab[k][l].getID()){
                            tab[i][j].setID(temp[i][j].getID());
                            tab[i][j].setColor(tab[k][l].getColor());
                            tab[i][j].cleanNBHD();
                            temp[i][j].setID(0);
                            
                        }
                        else if(tab[i][j].getID()!=0 && !tab[i][j].isChecked()){
                            empty--;
                            tab[i][j].checked();
                        }}
                        }else{
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
                                                if(tab[i][j].getID()==0 && temp[i][j].getID()==tab[kk][ll].getID()){
                            tab[i][j].setID(temp[i][j].getID());
                            tab[i][j].setColor(tab[kk][ll].getColor());
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
        }
    }
    
    /*------------------------RECRYSTALLIZATION-----------*/

    public void checkPosition(int i, int j){       //-----sprawdz pozycje komorki (granica/ srodek) TYLKO 1 RAZ
        
        for(int k=i-1; k<i+2; k++){ //-----------------from 1 up to 1 low (3 iterations) in rows
            for(int l=j-1; l<j+2; l++){ //-------------from 1 up to 1 low (3 iterations) in columns
                if((k==i && l==j) || k<0 || k>=size || l<0 || l>=size)
                    ;//System.out.println("go ");
                else{
                    if(tab[k][l].getID()!=tab[i][j].getID()){
                     tab[i][j].onEdge();
                     //tab[i][j].setColor(Color.black);
                     
                     break;
                    }                   
                }
            }
        }
    }
    
    public double countDensity(double time){        // oblicz gęstość dyslokacji
        double density=(A/B)+(1-A/B)*Math.exp(-B*time);
        return density;
    }
    
    public double packages(boolean edge){       
        Random rand = new Random();             
        double pack = 0;
        
        if(edge){
            pack = ((rand.nextInt(61)+120.0)/100.0)*q_mean;
        }else{
            pack = (rand.nextInt(31)/100.0)*q_mean;
        }
        
        return pack;
    }
    
    public boolean firstRec(int m, int n){
        boolean firstRec = false;
        
        if(!tab[m][n].isRecrystalized() && tab[m][n].getQ()>q_critic && tab[m][n].isOnEdge() ){
            free_r--; //zmniejsz pzrestrzen
            id++;
            tab[m][n].setID(id);
            tab[m][n].drawColor();
            tab[m][n].recrastilezed();  
            tab[m][n].setQ(0.0);
            
            //System.out.println("QQQQQQQQQQ "+m+" "+n+" "+tab[m][n].getQ());
            
            firstRec = true;
            repaint();
        }      
        return firstRec;
    }
    
    public double suma (){
        suma=0.0;
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(!tab[i][j].isRecrystalized())
                    suma+=tab[i][j].getQ();
            }
        }
        return suma;
    }
        
    
    public void recrystallization2 (){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                this.checkPosition(i, j);
            }
        }
        
        //repaint();
        
        //free_r=0;
               
//        boolean f = false;
//        while(!f){
//            
//            if(t==0)
//               q = this.countDensity(t);
//            else
//                q = this.countDensity(t)-this.countDensity(t-1);
//            
//            q_mean = q/(size*size);
////
//            for(int i=0; i<size; i++){
//                for(int j=0; j<size; j++){
//                    tab[i][j].setQ(this.packages(tab[i][j].isOnEdge()));
//                }
//            }
//            
//            for(int i=0; i<size; i++){
//                for(int j=0; j<size; j++){
//                    if(firstRec(i, j)){
//                        f=true;
//                    }
//                }
//            }
//           
//            t+=0.001;
//            System.out.println("t "+t);
//        }
//        
// //       free_r=0;
//        
//        System.out.println("-------------------------------------------");
        

        while(free_r>0){
            
            
            
/*---------------------------------------------------------------------------*/            
            if(t==0)
                q = countDensity(t);
            else
                q = countDensity(t)-countDensity(t-0.001);
            
            //System.out.println(countDensity(t));
            
            q_mean = q/(size*size);

            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    tab[i][j].addDeltaQ(packages(tab[i][j].isOnEdge()));
                }
            }
/*---------------------------------------------------------------------------*/            
            boolean [][]av = new boolean[size][size];
            
            
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    av[i][j]=false;
                    if(!tab[i][j].isRecrystalized()){
                        //System.out.println(i+" "+j+" "+this.canRecrystalize(i, j));
                        av[i][j]=canRecrystalize(i, j);

                    }
                }
            }
            
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    if(av[i][j]){
                        tab[i][j].setID(temp_r[i][j].getID());
                        tab[i][j].setColor(temp_r[i][j].getColor());
                        tab[i][j].recrastilezed();
                        tab[i][j].setQ(0.0);
                        
                        free_r--;
                        
                        temp_r[i][j].setID(0);
                        temp_r[i][j].resetColor();
                        av[i][j]=false;
                        repaint();
                    }
                    else
                        firstRec(i,j);
                    

                }
            }
            
            System.out.println(suma());
            
            t+=0.001;
            //System.out.println("t "+t);
            //free_r=0;
        }
    }
    
    

    
    public boolean canRecrystalize(int i, int j){      //sprawdzanie czy sąsiad zrekrystalizował i czy gęstość dyslokacji w sąsiednich komorkach jest mniejsza
        double sum_q = 0.0;
        boolean can_r = false;
        for(int k=i-1; k<i+2; k++){ //-----------------from 1 up to 1 low (3 iterations) in rows
            for(int l=j-1; l<j+2; l++){ //-------------from 1 up to 1 low (3 iterations) in columns
                if((k==i && l==j) || k<0 || k>=size || l<0 || l>=size)
                    ;//System.out.println("go ");
                else{
                    sum_q += tab[k][l].getQ();
                    if(tab[k][l].isRecrystalized()){
                     can_r = true;
                     temp_r[i][j].setID(tab[k][l].getID());
                     temp_r[i][j].setColor(tab[k][l].getColor());
                    }                   
                }
            }
        }
        return can_r && sum_q>tab[i][j].getQ();//            tab[i][j].setID(temp_r[i][j].getID());

    }
    

    

    
}
