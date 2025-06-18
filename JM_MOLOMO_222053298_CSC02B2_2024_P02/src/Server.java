import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * 
 * @author JM Molomo
 * @version P02
 */
public class Server {
   
    private ServerSocket serverSocket; 	  //The server socket that listens for connections.
    private Socket clientSocket;		  //The client socket that is connected to the user.
    private int numOfQuery = 4;           //The number of queries that the user is allowed to make. 

    /**
     * Server listens on port 8888
     * Connects on 8888
     */
    public Server() {
        try {
            serverSocket = new ServerSocket(8888);
            System.out.println("You may initiate your Session...");
            clientSocket = serverSocket.accept(); // accept the connection
            System.out.println("Connection successful");
        } catch (IOException e) {
            System.err.println("Access Denied");
            e.printStackTrace();
        }
    }
    
    /**
     * Handle the incoming connection
     */
    public void weather() {
        if (clientSocket.isConnected()) {
            try (
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                writer.println("HELLO - you may make 4 requests and I'll provide weather information.");
                String ready = reader.readLine().toUpperCase(); //convert to uppercase

                if (ready.equals("START")) {
                    int responseNumber = 0;
                    Random rand;
                    boolean proceed = true;
                    
                    
                  
                    
                    String[] randResponse = {"No data available","Please try again later"," data outdated"};
          
                    while(numOfQuery >0 && proceed == true) {
						writer.println("REQUEST OR DONE");
						String userInput = reader.readLine().toUpperCase();
						
						
                        if (userInput.contains("JOHANNESBURG")  && userInput.startsWith("REQUEST")) {
                            numOfQuery--;
                            responseNumber++;
                            writer.println("0" + responseNumber + "[Clear Skies in Joburg]");
                        }
                      
                        else if (userInput.contains("DURBAN") && userInput.startsWith("REQUEST")) {
                            numOfQuery--;
                            responseNumber++;
                            rand = new Random();
                            writer.println("0" + responseNumber + "[Sunny and Warm in Durban]");
                        }
                       
                        else if (userInput.contains("CAPE TOWN") && userInput.startsWith("REQUEST")) {
                            numOfQuery--;
                            responseNumber++;
                            writer.println("0" + responseNumber + "[Cool and Cloudy in Cape Town]");
                        }
                        // If the user's input is "DONE", then the connection is terminated.
                        else if (userInput.contains("DONE")) {
                            writer.println("0" + responseNumber + "GOOD BYE-[" + responseNumber + "] queries answered");
                            clientSocket.close();
                            serverSocket.close();
                            proceed = false;
                        }
                      
                        else if (userInput.startsWith("REQUEST")) {
                            numOfQuery--;
                            responseNumber++;
							rand = new Random();
							writer.println("0"+responseNumber + userInput +"["+randResponse[rand.nextInt(3)]+"]");
							
						}else {
							writer.println("Invalid input! start with either:");
						}
                    } 
                    writer.println("GOOD BYE-["+responseNumber+"] queries answered");
                }
                   
            }catch (IOException e) {
                System.err.println("Could not read or write to a client socket");
                e.printStackTrace();
            }finally {
                try {
                    clientSocket.close();
                    serverSocket.close();
                    System.out.println("Connection Terminated");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
