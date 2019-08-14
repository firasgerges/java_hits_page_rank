# Hyperlink-Induced Topic Search (HITS) and Page Rank Algorithm: Java Implementation
For any search engine to display properly the returned web pages, a proper ranking algorithm must be used to sort such pages based on their relevancy.
These algorithms are used for link analysis in order to rate web pages. In this project, we show a working Java implementation of Kleinberg’s HITS Algorithm, and Google’s PageRank algorithm.
# Hyperlink-Induced Topic Search (HITS)
HITS algorithm is introduced by Jon Kleinberg in 1998 with the aim of assigning and authoritative rank (Auth) and a hub rank (Hub) for each web page resulting from a search query.
## Auth
The Authoritative value of a page (P) is a representation of how many hub pages pointing to it.
## Hub
The hub value of a page (P) is a representation of the pages this page is pointing to.

The Hits algorithm uses both the Auth value and the Hub value when ranking pages.
## Root Set and Base Set
The first step is to start  building the root set of a query, RS(q). This set contains all the pages returned as a result for the search query q. 

RS(q) is then expanded to be a base set for the query ,BS(q),  by including all pages pointing to a page in RS(q), and all pages pointed by a page in RS(q), therefore:

BS(q) = RS(q) U {All pages linking to RS(q)} U {all pages linked by RS(q)}

The base set can be represented as a directed graph where nodes representing web pages, and an edge (u,v) shows that page u is pointing to page p.
## Hits Algorithm
The main input of the core function in Hits is the before mentioned graph. For a certain number of iterations (until convergence) and at each iteration t, Hits updates the auth and hub values of each node Pi (webpage) based on pre-defined equations. E is the set of edges.
### Auth Update
At time t, the Auth value of a page Pi, is the sum of all the hubs values (at time t-1) of pages linking to Pi

<pre>
Auth(Pi) = ∑_((j,i)∈E)▒〖Hub(Pj)〗
</pre>

### Hub Update
After updating the Auth value of each page at time t, the new hub value of a page Pi, is the sum of all the new Auth values of pages pointed to by Pi.
<pre>
Hub(Pi) = ∑_((i,j)∈E)▒〖Auth(Pj)〗
</pre>

### Normalization
At each time step, and to ensure convergence, all the new computed hub and auth values are normalized.
  <pre>
Auth(Pi)=(Auth(Pi))/(∑_(for every page)▒〖Auth(Pj)〗^2 )  </pre>
<pre>
Hub(Pi)=(Hub(Pi))/(∑_(for every page)▒〖Hub(Pj)〗^2 )</pre>
### Convergence
After each time step t, two error values are computed for each web page, Auth_error(Pi) and Hub_error(Pi).
<pre>
Auth_error(Pi) = | Auth(Pi)time t – Auth(Pi)time t-1 |</pre>
<pre>
Hub_error(Pi) = | Hub(Pi)time t – Hub(Pi)time t-1 |</pre>
In order to reach convergence, every error value for all the web pages must be less than a given threshold (e.g. 0.0001)
### Input
Inputs to the algorithm are fed as command line arguments.
#### Iterations
The algorithm can be either ran for a certain number of iterations, or until the error rate is below a certain threshold. The iteration input represents the number of iterations. However, if the value was 0 or a negative number, the error rate will be used. A value of zero indicates a default error rate of 10-5. Values -1, -2, -3, … represent error values of 10-1, 10-2, 10-3, …
#### Initial Values
In order for the algorithm to run successfully, the auth and hub values must be initialized. If it is 0 they are initialized to 0, if it is 1 they are initialized to 1. A -1 value  indicates that the auth and hub values are to be initialized to 1/N, where N is the number of web pages. If it is -2 they are initialized 1/√N.
#### Graph
The main input of Hits is the pages graph. In this implementation, the graph is given as text file. This file contains two columns, space separated. The first row contains the number of vertices followed by the number of edges. For all the remaining lines, each row contains two values ‘u’ and ‘v’, and it represents a directed edge between these two nodes. Refer to graph.txt to see the format.
### Running the program
In order run hits.java, compile the file:
  <pre>
javac hits.java
</pre>
And run the algorithm with the specified arguments:
  <pre>
java hits “iterations” “initial value” “file name (graph)”
</pre>
