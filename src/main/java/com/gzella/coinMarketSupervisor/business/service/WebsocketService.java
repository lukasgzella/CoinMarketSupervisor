package com.gzella.coinMarketSupervisor.business.service;

import com.binance.connector.client.impl.WebsocketStreamClientImpl;
import com.google.gson.Gson;
import com.gzella.coinMarketSupervisor.business.dto.charts.BinanceKlineDTO;
import com.gzella.coinMarketSupervisor.business.dto.charts.ChartDetailsDTO;
import io.socket.emitter.Emitter;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoServerOptions;
import io.socket.engineio.server.JettyWebSocketHandler;
import io.socket.socketio.server.SocketIoNamespace;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;
import org.eclipse.jetty.http.pathmap.ServletPathSpec;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketUpgradeFilter;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class WebsocketService {

    private final EngineIoServerOptions engineIoServerOptions = EngineIoServerOptions.newFromDefault();
    private final EngineIoServer engineIoServer = new EngineIoServer(engineIoServerOptions);
    private final SocketIoServer socketIoServer = new SocketIoServer(engineIoServer);
    private final Server server = new Server(new InetSocketAddress("localhost", 4000));
    private final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    private final WebsocketStreamClientImpl client = new WebsocketStreamClientImpl();
    private BinanceKlineDTO bKline;
    private final Gson gson = new Gson();
    private int currentConnectionId = -1;
    private boolean ioServerStarted = false;

    public void startBinanceWSStream(ChartDetailsDTO chartDetails) {
        String symbol = chartDetails.getCoinSymbol().toLowerCase() + "usdt";
        String interval = chartDetails.getInterval();
        System.out.println(symbol);
        System.out.println(interval);
        currentConnectionId = client.klineStream(symbol, interval, ((event) -> {
            System.out.println(event);
            bKline = gson.fromJson(event, BinanceKlineDTO.class);
        }));

    }

    public void stopBinanceWSStream() {
        client.closeConnection(currentConnectionId);
        bKline = null;
    }

    public void init() throws ServletException {
        servletContextHandler.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
                engineIoServer.handleRequest(request, response);
            }
        }), "/socket.io/*");

        WebSocketUpgradeFilter webSocketUpgradeFilter = WebSocketUpgradeFilter.configureContext(servletContextHandler);
        webSocketUpgradeFilter.addMapping(new ServletPathSpec("/socket.io/*"), (servletUpgradeRequest,
                                                                                servletUpgradeResponse) -> new JettyWebSocketHandler(engineIoServer));

        server.setHandler(servletContextHandler);
    }

    public void startIOServer() throws Exception {
        if (ioServerStarted) {
            return;
        }
        init();
        server.start();
        ioServerStarted = true;
        SocketIoNamespace ns = socketIoServer.namespace("/");
        ns.on("connection", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                SocketIoSocket socket = (SocketIoSocket) args[0];
                System.out.println("Client " + socket.getId() + " has connected.");

                socket.on("message", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("[Client " + socket.getId() + "] " + args[0]);
                    }
                });
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (bKline != null) {
                            socket.send("KLINE", bKline.toString());
                        }
                    }
                }, 0, 1000);
            }
        });
    }

    public void stopIOServer() throws Exception {
        server.stop();
    }
}
