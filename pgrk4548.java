//Firas Gerges CS 610 4548 prp
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;

public class pgrk4548 {

    public static int n; //number of vertices
    public static int m; //number of edges
    public static double[] PR_0; // page rank at time t-1
    public static double[] PR_1; // page rank at time t-1

    public static double[][] graph; //the graph
    public static double[] C; // save outdegrees
    public static double d=0.85;
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
        PR_0= new double[n];
        PR_1= new double[n];

        Arrays.fill(PR_0,initial_value);
        //print base values, only if n not greater than 10;
        if(n<=10)
            printValues(0,PR_0);

        //run hits algorithm
        double max_error;
        int iter=1;

        boolean is_convereged=false;
        while(!is_convereged){

            //compute page rank values
            for(int i=0;i<n;i++){//for each vertex i
                PR_1[i]=(1.0-d)/n;
                double prev_pr_sum=0;
                for(int j=0;j<n;j++){
                    if(graph[j][i]==1){//for each vertex pointing to i
                        prev_pr_sum+=(PR_0[j]/C[j]); //get the sum of the page rank values from the previous iteration
                                                    // of all vertices pointing to i devided but the outdegree of that vertex
                    }
                }
                //update this iteration value of page rank of i
                PR_1[i]+=d*prev_pr_sum;

            }
            //get max_error
             max_error= getMaxError(PR_0,PR_1);

            //print values
            if(n<=10)
                printValues(iter,PR_1);

            //we just need the values of the direct previous iteration
            PR_0=PR_1.clone();

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
            printValues(iter-1,PR_0);



    }

    public static void getGraphfromFile(String arg) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(arg));
        String line = reader.readLine();
        n = Integer.parseInt(line.split(" ")[0]);
        m = Integer.parseInt(line.split(" ")[1]);
        graph= new double[n][n];
        C=new double[n];
        for(int i=0; i<n;i++){
            for(int j=0;j<n;j++)
                graph[i][j]=0;
            C[i]=0;
        }
        while ((line = reader.readLine())!= null) {
            int v1=Integer.parseInt(line.split(" ")[0]);
            int v2=Integer.parseInt(line.split(" ")[1]);
            graph[v1][v2]=1;
            C[v1]++;
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


    public static void printValues(int iter, double[] a){
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
            line=line+"P[ "+i+"]="+formatter.format(a[i])+" ";
            if(n>10)
                line=line+"\n";
        }
        System.out.println(line);
    }



}
