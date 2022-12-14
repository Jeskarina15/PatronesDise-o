package oscarblancarte.ipd.state.states;

import oscarblancarte.ipd.state.Server;

public class StopServerState extends AbstractServerState {
    public StopServerState(final Server server){
    	 server.getMessageProcess().stop();
    }
    
    @Override
    public void handleMessage(Server server, String message) {
    	System.out.println("Server is stopped");
    }
}
