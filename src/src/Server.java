package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server {

    ServerSocket server;
    Socket client;
    PrintWriter output;
    int port = 12345;
    
    public double g = 9.8;    
    double v0, startAngle;    
    public int howMany = 7;   
    public double tobesent[][] = null;
    public boolean wasToBeSentset = false;
    
    public Server() throws IOException {

        server = new ServerSocket(port);
    }

    
    public void calculate(){
    
        double x,y;
      
        double vy0 = v0*Math.sin(convertDegreeToRadians(startAngle));
        double tRISE = vy0/g;
        
        double allTime = tRISE*2;
        //System.out.println("Alltime is" +allTime);
        
        double timeIncrementer = (allTime/(howMany-1));
        
        double t=0;
        
        for(int j= 0; j<howMany; j++){

            double theCOS = Math.cos(convertDegreeToRadians(startAngle));
            double theSIN = Math.sin(convertDegreeToRadians(startAngle));

            x = v0*t*theCOS;
            double vyONE = v0*t*theSIN;       
            double vyTWO = -(0.5*9.8*Math.pow(t, 2));

            y = vyONE + vyTWO;

            tobesent[j][0] = x;
            tobesent[j][1] = y;

            //System.out.println("t: "+t+"  x = "+x+" , y = "+y);

            t += timeIncrementer;
    
        }
    
    }

    public void execute() {

        while (true) {
            try {
                client = server.accept();
                
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                
                howMany = Integer.valueOf(in.readLine());
                
               
                
                tobesent = new double[howMany][2];   
                
                                
                 v0 = Double.valueOf(in.readLine());
                 startAngle = Double.valueOf(in.readLine());
                
                //System.out.println("INCOMING: v0 = "+v0);
                //System.out.println("INCOMING:  angle = " + startAngle);
                
                 calculate();
                
                output = new PrintWriter(client.getOutputStream(), true);
              
                for(int i =0; i<howMany ;i++){
                    
                    try {
                            Thread.sleep(1000);
                            output.write(String.valueOf(tobesent[i][0])+"\n");
                            output.flush();
                            output.write(String.valueOf(tobesent[i][1])+"\n");
                            output.flush();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            
                        }
                }
         
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public static void main(String[] args) {

        try {

            Server server = new Server();
            server.execute();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public double convertDegreeToRadians(double deg){
    
    
    return deg*(Math.PI/180);
    
    }

}
