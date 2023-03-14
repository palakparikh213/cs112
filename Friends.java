package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {
    public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
        //new queue
        Queue <Person> q = new Queue<>();
        //previously visited int array
        int[] prev= new int[g.members.length];
        //visited boolean array 
        boolean[] visited = new boolean [g.members.length];
        //first person and target person
        Person starter=g.members[g.map.get(p1)];
        Person target=g.members[g.map.get(p2)];
        //has the target been found yet 
        boolean found=false; 
        //set all to -1
        for(int i=0; i<prev.length;i++){
            prev[i]=-1;
        }
        //first is enqueued
        q.enqueue(starter);
        //if queue is empty, target not found
        while(q.isEmpty()==false){
            //dequeued becomes current person
            Person current= q.dequeue();
            //if current is target, boolean is found, break
            if(current==target){
                found=true; 
                break;
            }
            int name=g.map.get(current.name);
            visited[name]=true; 
            //new ptr head
            Friend ptr=g.members[name].first;
            //while node is not empty
            while(ptr!=null){
                //if ptr.fnum is false, add to queue and array, visited
                if(visited[ptr.fnum]==false){
                    q.enqueue(g.members[ptr.fnum]);
                    prev[ptr.fnum]=name;
                    visited[ptr.fnum]=true; 
                    }
                    //traverse
                    ptr=ptr.next;
                }
            }
        //if not found, return null
        if(!found) {
            return null;
        }
        //create new stack
        Stack <String> s = new Stack<>();
        int prevNum=prev[g.map.get(target.name)];
        s.push(target.name);
        //while not empty
        while(prevNum!=-1){
            s.push(g.members[prevNum].name);
            prevNum=prev[prevNum];
        }
        //new arrayList
        ArrayList<String> array = new ArrayList<>();
            //fill array, empty stack
            while(!s.isEmpty()){
                array.add(s.pop());
            }
        return array; 
    }
    public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
        //new array list of array list
        ArrayList<ArrayList<String>> arrayCliques = new ArrayList<ArrayList<String>>();
        //visited boolean array
        boolean[] visited = new boolean[g.members.length];
        for(int i=0; i<g.members.length; i++){
            //new array list of clique
            ArrayList<String> arr= new ArrayList<String>(); 
            Person current=g.members[i];
            int name=g.map.get(current.name);
            if(current.school==null){
                continue;
            }
            if(current.school.equals(school) && visited[name]==false) {
                Queue<Person> q=new Queue<>();
                q.enqueue(current);
                while(q.isEmpty()==false){
                    Person x=q.dequeue();
                    arr.add(x.name);
                    visited[g.map.get(x.name)]=true; 
                    Friend ptr= g.members[g.map.get(x.name)].first;
                    while(ptr!=null){
                        String schoolString= g.members[ptr.fnum].school;
                        if(schoolString==null){
                            ptr=ptr.next;
                            continue; 
                        }
                        if(visited[ptr.fnum]==false && school.equals(schoolString)){
                        q.enqueue(g.members[ptr.fnum]);
                        visited[ptr.fnum]=true; 
                        }
                        ptr=ptr.next;
                    }
                }
                arrayCliques.add(arr);
            }
        }
        return arrayCliques;
    }

    public static ArrayList<String> connectors(Graph g) {

        ArrayList <String> connectorList = new ArrayList<>();
        int length=g.members.length;
        //visited boolean array
        boolean[] visited=new boolean[length];
        int[] x = new int[length];
        int[] y = new int[length];
        boolean[] boolarr=new boolean[length];
        for(int i=0;i<length;i++){
            if(!visited[i]){
                con(g, connectorList, boolarr, i, visited, x, y, i, i);
            }
        }
            return connectorList;
    }
    //recursive private function
    private static void con(Graph g, ArrayList<String> connectorList, boolean[] boolarr, int prev, boolean[] visited, int[] x, int y[], int current, int first){
        if(visited[current]){
            return;
        }
        visited[current]=true;
        x[current]=x[prev]+1;
        y[current]=x[current];
        Friend ptr=g.members[current].first;
        while(ptr!=null){
            if(visited[ptr.fnum]){
                y[current]=Math.min(y[current],x[ptr.fnum]);
            }    
            else{
                con(g, connectorList, boolarr, ptr.fnum,visited,x, y, current, first);    
                if(x[current]<=y[ptr.fnum]&&!connectorList.contains(g.members[current].name)){
                    if(current!=first||boolarr[current]==true){
                        connectorList.add(g.members[current].name);
                    }
                }
                if(x[current]>y[ptr.fnum]){
                        y[current]=Math.min(y[current], y[ptr.fnum]);
                }
                boolarr[current]=true;
                }
                ptr=ptr.next;    
            }
    }
}
