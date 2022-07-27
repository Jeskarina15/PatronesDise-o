package oscarblancarte.ipd.state.states;

import oscarblancarte.ipd.state.Server;

public class SafeShutdownState extends AbstractServerState{
	private Thread monitoringThread;
	public SafeShutdownState(final Server server) {
        server.getMessageProcess().OffSafe();
        monitoringThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (true) {
                        if (server.getMessageProcess().countMessage() 
                                ==0) {
                            server.setState(
                                    new StopServerState(server));
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        monitoringThread.start();
        
	}

	@Override
	public void handleMessage(Server server, String message) {
		 boolean isPending = 
	                server.getMessageProcess().pendinQueueMessage(message);
		 if (!isPending) {
			 System.out.println("Please wait for all processes to finish");
		 }
	}

}
