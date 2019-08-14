//Firas Gerges CS 610 4548 prp
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

public class hits4548 {

    public static int n; //number of vertices
    public static int m; //number of edges
    public static double[] a_0; // authorities at time t-1
    public static double[] h_0; // hubs at time t-1
    public static double[] a_1; // authorities at time t
    public static double[] h_1; // hubs at time t
    public static int[][] graph; ///the graph here is represented by an adjacency matrix

    public static void main(String[] args) throws IOException {
        if(args.length<3){
            System.out.println("Missing Params");
            return;
        }


        //read file and build the adjacency matrix representation of the graph
        getGraphfromFile(args[2]);

        //fetch initial values
        double initial_value=fetchInitialValue(args[1]);

        //fetch iterations and error rate
        int iterations=fetchIterations(args[0]);
        double error_rate=0;
        if(iterations<0){
            error_rate=Math.pow(10.0,iterations);
            iterations=-1;
        }
        //initialize authority and hub lists
        a_0= new double[n];
        h_0= new double[n];

        a_1= new double[n];
        h_1= new double[n];


        Arrays.fill(a_0,initial_value);
        Arrays.fill(h_0,initial_value);
        //print base values, only if n not greater than 10;
        if(n<=10)
            printValues(0,a_0,h_0);

        //run hits algorithm
        double max_error;
        int iter=1;

        boolean is_convereged=false;
        while(!is_convereged){
            //update a_1 from previous h_0
            stepA();
            //update h_1 from the newly computed a_1
            stepH();
            //scaling step
            scale_A_H();

            //get max_error
            double a_max_error= getMaxError(a_0,a_1);
            double h_max_error= getMaxError(h_0,h_1);
            max_error=Math.max(a_max_error,h_max_error);
            //print values
            if(n<=10)
                printValues(iter,a_1,h_1);

            //we just need the values of the direct previous iteration
            a_0=a_1.clone();
            h_0=h_1.clone();


//            System.out.println("ERROR: " +max_error+"  ---- RATE: "+error_rate);
            iter++;

            //check convergence
            if(iterations>0){ // conv based on number of iterations
                if(iter>iterations)
                    is_convereged=true;
            }
            else{ // conv based on max error
                if(max_error<=error_rate)
                    is_convereged=true;
            }

        }

        //print values if n>10, print only final iteration
        if(n>10)
            printValues(iter-1,a_0,h_0);


    }

    public static void getGraphfromFile(String arg) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(arg));
        String line = reader.readLine();
        n = Integer.parseInt(line.split(" ")[0]);
        m = Integer.parseInt(line.split(" ")[1]);
        graph= new int[n][n];
        for(int i=0; i<n;i++){
            for(int j=0;j<n;j++)
                graph[i][j]=0;
        }
        while ((line = reader.readLine())!= null) {
            int v1=Integer.parseInt(line.split(" ")[0]);
            int v2=Integer.parseInt(line.split(" ")[1]);
            graph[v1][v2]=1;
        }
        reader.close();
    }

    public static double fetchInitialValue(String arg){
        int initial_values_arg=Integer.parseInt(arg);
        if(n>10)
            initial_values_arg=-1;
        double initial_value=0.0;
        if(initial_values_arg>=0)
            initial_value=initial_values_arg;
        if(initial_values_arg==-1)
            initial_value=1.0/n;
        if(initial_values_arg==-2)
            initial_value=1.0/Math.sqrt((double)n);

        return initial_value;
    }

    public static int fetchIterations(String arg){
        int iteration=Integer.parseInt(arg);
        if(n>10)
            iteration=0;
        if(iteration==0)
            iteration=-5;
        return iteration;
    }

    public static void stepA(){
        for(int j=0;j<n;j++){ // for each vertex j
            double sum_hubs_i=0;
            for(int i=0;i<n;i++){ //for each vertex i
                if(graph[i][j]==1) { // if an edge point from i to j
                    sum_hubs_i += h_0[i];
                }
            }
            //update auth of j
            a_1[j]=sum_hubs_i;
        }
    }

    public static void stepH(){
        for(int j=0;j<n;j++){ // for each vertex j
            double sum_auth_k=0;
            for(int k=0;k<n;k++){ //for each vertex k
                if(graph[j][k]==1){ // if an edge point from j to k
                    sum_auth_k+=a_1[k];
                }
            }
            //update hub of j
            h_1[j]=sum_auth_k;
        }
    }

    public static void scale_A_H(){
        double A_squared_sum=0;
        double H_squared_sum=0;
        for (int i=0; i< n; i++){
            A_squared_sum+=Math.pow(a_1[i],2);
            H_squared_sum+=Math.pow(h_1[i],2);
        }
        for (int j=0;j<n;j++){
            a_1[j] = a_1[j]/Math.sqrt(A_squared_sum);
            h_1[j] = h_1[j]/Math.sqrt(H_squared_sum);
        }
    }

    public static double getMaxError(double[] arr_0,double[] arr_1){
        double max_error=0;
        for (int i=0;i<n;i++){
            double err = Math.abs(arr_0[i]-arr_1[i]);
            if(err>max_error) {
                max_error = err;
            }
        }
        return max_error;
    }


    public static void printValues(int iter, double[] a, double[] h){
        DecimalFormat formatter = new DecimalFormat("#0.0000000");
        String line="";
        if(iter==0)
            line="Base : 0";
        else
            line="Iter : "+iter;
        if(n<=10)
            line=line+" : ";
        else
            line=line+"\n";
        for(int i=0; i<n;i++){
            line=line+"A/H[ "+i+"]="+formatter.format(a[i])+"/"+formatter.format(h[i])+" ";
            if(n>10)
                line=line+"\n";
        }
        System.out.println(line);
    }



}
