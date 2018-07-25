This is a sample reproducer for issue where Vertx WebClient is encoding query parameter and appending extra "=" at end.

e.g. If we are requesting remote server at following path :

        /remote-server?jREJBBB5x2AaiSSDO0/OskoCztDZBAAAAAADV1A4
        

If using HttpClient then remote server is receiving following:

        jREJBBB5x2AaiSSDO0/OskoCztDZBAAAAAADV1A4

If using WebClient then remote server is receiving following:

        jREJBBB5x2AaiSSDO0%2FOskoCztDZBAAAAAADV1A4=
        
        
To see the issue, just run AppStarter. It will start a sample remote server, and a client impl which will use HttpClient and WebClient to make remote call. Wait 5 seconds after starting AppStarter and look at console logs.